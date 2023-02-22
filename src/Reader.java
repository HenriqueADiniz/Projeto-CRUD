import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.FileReader;
import com.opencsv.CSVReader;

public class Reader {
    // caminho do arquivo .csv
    private static final String CSV_PATH = "/tmp/pokemons.csv";

    /*
     * inicializa o leitor
     * inicializa LISTA de lista de strings (lista de strings = colunas, sao multivaloradas por causa dos campos)
     * pula a primeira coluna com o nome de cada campo
     * adiciona colunas a lista
     */
    public static void main(String[] args) {
        try {
            CSVReader reader = new CSVReader(new FileReader(CSV_PATH));

            List<List<String>> linhas = new ArrayList<List<String>>();
            String[] coluna = null;

            coluna = reader.readNext();
            while ((coluna = reader.readNext()) != null){
                linhas.add(Arrays.asList(coluna));
            }

            linhas.forEach(cols -> {
                System.out.println(cols);
            });
        } catch (Exception e) {

        }
    }
}