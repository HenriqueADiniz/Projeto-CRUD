import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import com.opencsv.CSVReader;

public class Reader {
    // caminho do arquivo .csv
    private static final String CSV_PATH = "tmp/pokemons.csv";

    /* ----
     * MAIN
     * ----
     * realiza carga inicial criando uma lista
     * converte cada linha da lista criada para objeto
     */
    public static void main(String[] args) throws Exception {
        List<List<String>> lista = cargaInicial();
        List<Pokemon> listaDePokemons = listaParaObjeto(lista);
        inicializarBD(listaDePokemons);
    }
    
    /* -------------
     * CARGA INICIAL
     * -------------
     * inicializa LISTA de LISTA de strings, ou seja, LINHAS de COLUNAS (linha[y].coluna[x])
     * pula a primeira linha, inicializa o leitor, adiciona as colunas de cada linha
     * retorna a lista
     */
    public static List<List<String>> cargaInicial(){
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

    /* ------------------
     * LISTA PARA OBJETOS
     * ------------------
     * cria lista de objetos
     * para cada linha lida, adiciona um objeto
     */
    public static List<Pokemon> listaParaObjeto(List<List<String>> lista){
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
     * cria arquivo e inicializa o RAS
     * inicializa e grava a variavel que conta o numero de registros
     * percorre a lista de pokemons, os convertendo pra binario, gravando a lapide, o tamanho do registro e o objeto
     * volta para o inicio do arquivo e atualiza a quantidade de registros
     */
    public static void inicializarBD(List<Pokemon> listaDePokemons) throws Exception {
        String arquivo = "banco";
        File arq = new File(arquivo);
        RandomAccessFile ras = new RandomAccessFile(arquivo, "rw");

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