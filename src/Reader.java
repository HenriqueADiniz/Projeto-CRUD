import java.io.File;
import java.io.FileReader;
import com.opencsv.CSVReader;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Reader {
    private static final String CSV_PATH = "../tmp/pokemons.csv";

    public static void main(String[] args) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(new File(CSV_PATH)));

            List<List<String>> linhas = new ArrayList<List<String>>();
            String[] colunas = null;

            while ((colunas = csvReader.readNext()) != null){
                linhas.add(Arrays.asList(colunas));
            }

            linhas.forEach(cols -> {
                System.out.println(cols);
            });
        } catch (Exception e) {

        }
    }
}