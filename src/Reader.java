import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.RandomAccessFile;
import com.opencsv.CSVReader;
import java.io.File;

public class Reader {
    // caminho do arquivo .csv
    private static final String CSV_PATH = "tmp/pokemons.csv";
    // caminho do arquivo .db
    private static final String DB_PATH = "tmp/pokemons.db";

    /* ----
     * MAIN
     * ----
     * .csv para lista de strings
     * lista de strings para lista de objetos
     * lista de objetos para .db
     */
    public static void main(String[] args) throws Exception {
        List<List<String>> lista = csvPraLista();
        List<Pokemon> listaDePokemons = listaPraObjetos(lista);
        inicializarBD(listaDePokemons);
    }
    
    /* -------------
     * CSV PRA LISTA
     * -------------
     * inicializa LISTA de LISTA de strings, ou seja, LINHAS de COLUNAS (linha[y].coluna[x])
     * pula a primeira linha, inicializa o leitor, adiciona as colunas de cada linha
     * retorna a lista
     */
    public static List<List<String>> csvPraLista(){
        System.out.println("Realizando carga inicial...");
        List<List<String>> linha = new ArrayList<List<String>>();

        String[] coluna = null;
        try {
            CSVReader reader = new CSVReader(new FileReader(CSV_PATH));

            coluna = reader.readNext();
            while ((coluna = reader.readNext()) != null){
                linha.add(Arrays.asList(coluna));
            }
            
            System.out.println("Dados carregados!");
        } catch (Exception e) {
            System.out.println("Erro ao carregar os dados. Confira o caminho do arquivo.");
            System.exit(1);
        }

        return linha;
    }

    /* -----------------
     * LISTA PRA OBJETOS
     * -----------------
     * cria lista de objetos
     * para cada linha lida, adiciona um objeto
     */
    public static List<Pokemon> listaPraObjetos(List<List<String>> lista){
        List<Pokemon> listaDePokemonsTEMP = new ArrayList<>();
        
        for (int i=0; i<lista.size(); i++){
            int number = Integer.parseInt(lista.get(i).get(0));
            String name = lista.get(i).get(1);
            String type1 = lista.get(i).get(2);
            String type2 = lista.get(i).get(3);
            String[] abilities = Tratamentos.trataMultivalorados(lista.get(i).get(4));
            int hp = Integer.parseInt(lista.get(i).get(5));
            int att = Integer.parseInt(lista.get(i).get(6));
            int def = Integer.parseInt(lista.get(i).get(7));
            Date date = Tratamentos.trataDatas(lista.get(i).get(8));

            listaDePokemonsTEMP.add(new Pokemon(number, name, type1, type2, abilities, hp, att, def, date));
        }
        return listaDePokemonsTEMP;
    }

    /* --------------------------
     * INICIALIZAR BANCO DE DADOS
     * --------------------------
     * cria o arquivo
     * inicializa e grava a variavel que conta o numero de registros
     * percorre TODA a lista de pokemons, os convertendo pra binario, gravando a lapide, o tamanho do registro e o objeto
     * volta para o inicio do arquivo e atualiza a quantidade de registros
     */
    public static void inicializarBD(List<Pokemon> listaDePokemons) throws Exception {
        File arq = new File(DB_PATH);
        if(arq.delete()) System.out.println("deletado"); else System.out.println("nao foi possivel deletar");
        RandomAccessFile ras = new RandomAccessFile(DB_PATH, "rw");
        int qntReg = 0;
        ras.writeInt(qntReg);

        for (int i = 0; i < listaDePokemons.size(); i++){
            byte [] barr = listaDePokemons.get(i).toByteArray();
            ras.writeBoolean(true);
            ras.writeInt(barr.length);
            ras.write(barr);

            qntReg++;
        }

        ras.seek(0);
        ras.writeInt(qntReg);
        ras.close();
    }
}