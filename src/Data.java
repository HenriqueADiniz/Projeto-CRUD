public class Data {
    private int dia;
    private int mes;
    private int ano;

    public Data(String str) {
        String[] array = str.split("/");
        this.dia = Integer.parseInt(array[0]);
        this.mes = Integer.parseInt(array[1]);
        this.ano = Integer.parseInt(array[2]);
    }

    //=====GETTERS & SETTERS=====//
    // dia
    public int getDia(){return dia;}
    public void setDia(int dia){this.dia = dia;}
    // mes
    public int getMes(){return mes;}
    public void setMes(int mes){this.mes = mes;}
    // ano
    public int getAno(){return ano;}
    public void setAno(int ano){this.ano = ano;}

    /* ----------------
     * DATA PARA STRING
     * ----------------
     * retorna a data completa como string
     */
    public String dataToString() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}