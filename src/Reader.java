import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import com.opencsv.CSVReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
            String[] abilities = trataMultivalorados(lista.get(i).get(4));
            int hp = Integer.parseInt(lista.get(i).get(5));
            int att = Integer.parseInt(lista.get(i).get(6));
            int def = Integer.parseInt(lista.get(i).get(7));
            Date date = trataDatas(lista.get(i).get(8));

            listaDePokemons.add(new Pokemon(number, name, type1, type2, abilities, hp, att, def, date));
        }
    }
    /* --------------------
     * TRATA MULTIVALORADOS
     * --------------------
     * remove colchetes
     * remove aspas simples
     * realiza split a cada virgula
     * retorna vetor de strings
     */
    public static String[] trataMultivalorados(String temp){
        temp = temp.substring(1, temp.length()-1);
        temp = temp.replaceAll("'", "");
        String[] abilities = temp.split(",");
        return abilities;
    }
    /* -----------
     * TRATA DATAS
     * -----------
     * formata a string pro padrao dd/mm/aaaa
     */
    public static Date trataDatas(String temp){
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            date = format.parse(temp);
        } catch (ParseException e) {
            // tratamento de erro
        }
        return date;
    }
}