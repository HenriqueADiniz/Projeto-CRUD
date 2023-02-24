import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import com.opencsv.CSVReader;

public class Reader {
    // caminho do arquivo .csv
    private static final String CSV_PATH = "/tmp/pokemons.csv";

    /*
     * MAIN
     * -
     * realiza carga inicial criando a lista
     * converte cada linha da lista para objeto
     */
    public static void main(String[] args) {
        List<List<String>> lista = cargaInicial();
        listaParaObjeto(lista);
    }

    /* 
     * CARGA INICIAL
     * -
     * inicializa LISTA de LISTA de strings, ou seja, LINHAS de COLUNAS (linha[y].coluna[x])
     * inicializa o leitor, pula a primeira linha, adiciona as colunas de cada linha
     * retorna a lista
     */
    public static List<List<String>> cargaInicial(){
        List<List<String>> linha = new ArrayList<List<String>>();
        String[] coluna = null;

        try {
            CSVReader reader = new CSVReader(new FileReader(CSV_PATH));

            coluna = reader.readNext();
            while ((coluna = reader.readNext()) != null){
                linha.add(Arrays.asList(coluna));
            }
        } catch (Exception e) {
            // tratamento de erro
        }

        return linha;
    }

    /* 
     * LISTA PARA OBJETOS
     * -
     * cria lista de objetos
     * para cada linha, cria um objeto
     */
    public static void listaParaObjeto(List<List<String>> lista){
        int tam = lista.size();
        List<Pokemon> listaDePokemons = new ArrayList<>();

        for (int i=0; i<tam; i++){
            int number = Integer.parseInt(lista.get(i).get(0));
            String name = lista.get(i).get(1);
            String type1 = lista.get(i).get(2);
            String type2 = lista.get(i).get(3);
            String[] abilities = trataMultivalorados(lista.get(i).get(4));
            int hp = Integer.parseInt(lista.get(i).get(5));
            int att = Integer.parseInt(lista.get(i).get(6));
            int def = Integer.parseInt(lista.get(i).get(7));
         // date = 
         // TRATAR DATA AQUI

            listaDePokemons.add(new Pokemon(number, name, type1, type2, abilities, hp, att, def, date));
        }
    }
    /*
     * TRATA MULTIVALORADOS
     * -
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
}