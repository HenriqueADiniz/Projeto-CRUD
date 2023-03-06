import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
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
    public static void main(String[] args) {
        List<List<String>> lista = cargaInicial();
        
        listaParaObjeto(lista);
       
    
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
    public static void listaParaObjeto(List<List<String>> lista){
        List<Pokemon> listaDePokemons = new ArrayList<>();

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

            listaDePokemons.add(new Pokemon(number, name, type1, type2, abilities, hp, att, def, date));

        }
    }

    /*Após colocar todos os objetos em uma lista, acrescentar elemento por elemento da lista em um arquivo binario
      public static void iniciarBdPeloCSV(String arquivo,objesto pokemon) throws Exception {

     *  // Deletar o arquivo para recomeçar
        File arq = new File(arquivo);
        if(arq.delete()) System.out.println("deletado"); else System.out.println("nao foi possivel deletar");
          // Objeto que permite ler e escrever aleatoriamente no arquivo
        RandomAccessFile ras = new RandomAccessFile(arquivo, "rw");
            int qntReg = 0;
             ras.writeInt(qntReg);
        laço de repetição , seleciona os pokemons 

         // escrever registro no arquivo
            byte [] barr = pokemon.toByteArray();
            ras.writeBoolean(true);    // escrever lapide
            ras.writeInt(barr.length); // escrever tamanho
            ras.write(barr);           // escrever registro

         // aumento da quantidade de registros
         qntReg++
        
        } // end laço 

     // acessa o comeco do arquivo e escreve a quantidade de registros(necessario para o Create, e para intercalação balanceada)
        ras.seek(0);
        ras.writeInt(qntReg);
        
     // fechamento dos manipuladores de arquivo
        ras.close();
        freader.close();
     */
}