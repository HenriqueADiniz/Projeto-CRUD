import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Tratamentos {
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
