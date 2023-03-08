import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;

public class CRUD {
    // caminho do arquivo .db
    private static final String DB_PATH = "tmp/pokemons.db";

    // inicializa o RAS
    private  RandomAccessFile ras;

    // construtor do CRUD com o arquivo .db
    public CRUD (String arquivo) throws FileNotFoundException {
        this.ras = new RandomAccessFile(arquivo, "rw");
    }

    /* ----
     * MAIN
     * ----
     * inicializa instancia do CRUD com o arquivo .db
     * aciona o arquivo reader e seus metodos
     * entra em loop com as opcoes do CRUD
     */
    public static void main(String[] args) throws Exception {
        System.out.print("\033[H\033[2J");

        CRUD crud = new CRUD(DB_PATH);
        Reader.main(args);
        
        delay(1250);
        Scanner scan = new Scanner (System.in);
        Scanner scanLinha = new Scanner (System.in).useDelimiter("\\n");
        while (true){
            System.out.print("\033[H\033[2J");
            System.out.print("*--------------------------*\n");
            System.out.print("#    OPCOES DE REGISTRO    #\n");
            System.out.print("*--------------------------*\n");
            System.out.print("| 1) Criar                 |\n");
            System.out.print("| 2) Ler                   |\n");
            System.out.print("| 3) Atualizar             |\n");
            System.out.print("| 4) Deletar               |\n");
            System.out.print("|                          |\n");
            System.out.print("| 0) Sair                  |\n");
            System.out.print("*--------------------------*\n");
            System.out.print("Digite uma opcao: ");
            String out = "";
            String opt = scan.next();

            switch (opt){
                case "1":
                    int hp, att, def;
                    String name, type1, type2, abilitiesTemp, dateTemp;

                    System.out.print("Digite o nome: ");
                    name = scan.next();
                    System.out.print("Digite o tipo 1: ");
                    type1 = scan.next();
                    System.out.print("Digite o tipo 2: ");
                    type2 = scan.next();
                    System.out.print("Digite as habilidades, separadas por virgulas: ");
                    abilitiesTemp = scanLinha.next();
                    System.out.print("Digite o HP: ");
                    hp = scan.nextInt();
                    System.out.print("Digite o ataque: ");
                    att = scan.nextInt();
                    System.out.print("Digite a defesa: ");
                    def = scan.nextInt();
                    System.out.print("Digite a data, no formato (dd/mm/aaaa): ");
                    dateTemp = scan.next();

                    Date date = Tratamentos.trataDatas(dateTemp);
                    String[] abilities = abilitiesTemp.split(",");

                    Pokemon criado = new Pokemon(name, type1, type2, abilities, hp, att, def, date);
                    crud.create(crud.ras, criado);
                    break;

                case "2":
                    System.out.print("Digite o ID do Pokemon a ser lido: ");
                    int idRead = scan.nextInt();
                    
                    Pokemon lido = crud.read(idRead);
                    out = (lido != null) ? "Pokemon encontrado!\n" + lido.toString() : "Erro: Pokemon nao-existente.";
                    
                    break;

                case "3":
                    System.out.println();
                    // chamar metodo atualizar
                    System.out.println();
                    break;
                    
                case "4":
                    System.out.print("Digite o ID do Pokemon a ser deletado: ");
                    int idDel = scan.nextInt();

                    Boolean ctrl = crud.delete(idDel);
                    out = (ctrl) ? "Pokemon deletado com sucesso." : "Erro: Pokemon nao-existente.";
                    System.out.println(out);
                    break;

                case "0":
                    System.out.println();
                    scan.close();
                    scanLinha.close();
                    System.exit(1);
                
                default:
                    break;
            }
        }
    }

    /* ------
     * CREATE
     * ------
     * move o ponteiro pro inicio do arquivo
     * le a quantidade de ids
     * cria a id do pokemon novo (qntd + 1)
     *
     * move o ponteiro pro inicio do arquivo
     * altera a quantidade de id's nova para (qntd + 1)
     *
     * move o ponteiro pro fim do arquivo
     * converte o objeto para bytes
     * escreve a lapide
     * escreve o tamanho do registro
     * escreve o registro
     *
     * retorna o id do registro gerado
     */
   
    public int create(RandomAccessFile ras, Pokemon pokemon) throws IOException{
        if (pokemon == null) pokemon = new Pokemon();
        
        ras.seek(0);
        int qntReg = ras.readInt();
        pokemon.setNumber(qntReg+1);

        ras.seek(0);
        ras.writeInt(qntReg+1);
        
        ras.seek(ras.length()); 
        byte[] ba = pokemon.toByteArray();
        ras.writeBoolean(true);
        ras.writeInt(ba.length);
        ras.write(ba);
       // System.out.println(pokemon.toString());
        return pokemon.getNumber();
    }
    public int create(Pokemon pokemon) throws IOException{
       return create(ras, pokemon);
    }

    /* ----
     * READ
     * ----
     * move o ponteiro pro inicio do arquivo (APOS O CABECALHO)
     * varre o arquivo (tamReg = tamReg + 1 da lapide (booleano) + 4 do tamanho (int))
     * 
     * em cada registro,
     * le a lapide do registro
     * le o tamanho do registro
     * cria o byte a ser lido
     * le e aloca o byte
     * cria o pokemon com as informacoes do byte lido
     * 
     * se a lapide for verdadeira,
     * se encontrado o registro a ser lido,
     * retorna o objeto pokemon
     */
    public Pokemon read(int id) throws Exception{
        int tamReg = 0;
        boolean lapide = true;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            lapide = ras.readBoolean();
            System.out.println(lapide);
            tamReg = ras.readInt();

            byte [] ba = new byte [tamReg];
            ras.read(ba);
            Pokemon pokemon = new Pokemon(ba);


            if(lapide){
                System.out.println(pokemon.getNumber() +  " " + id);
                if(pokemon.getNumber() == id) {
                    System.out.println("Chegou");
                    return pokemon;
                    
                }
            }
        }
    System.out.println("NÃO CHEGOU");
        return null;
    }
   
    /* ------
     * UPDATE
     * ------
     * move o ponteiro pro inicio do arquivo (APOS O CABECALHO)
     * varre o arquivo (tamReg = tamanho do registro + 1 da lapide (booleano) + 4 do tamanho (int))
     * 
     * em cada registro,
     * salva a posicao inicial
     * le a lapide do registro
     * le o tamanho do registro
     * cria o byte a ser lido
     * le e aloca o byte
     * cria o pokemon com as informacoes do byte lido
     * 
     * se a lapide for verdadeira,
     * se o pokemon tiver o mesmo id do novo pokemon,
     * cria o novo registro em bytes
     * 
     * se o tamanho do novo regitro for igual ao antigo,
     * ?????????????????????????
     * 
     * senao,
     * ?????????????????????????
     * move o ponteiro para a lapide do registro
     * e a demarca como excluida (false)
     */
    public boolean update(RandomAccessFile ras, Pokemon novo) throws Exception {
        boolean lapide = true;
        int tamReg = 0;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            long posInicial = ras.getFilePointer();
            lapide = ras.readBoolean();
            tamReg = ras.readInt();

            byte [] bytes = new byte[tamReg];
            ras.read(bytes);
            Pokemon pokemon = new Pokemon(bytes);

            if(lapide){
                if(pokemon.getNumber() == novo.getNumber()) {
                    byte [] bytesNovo = novo.toByteArray();

                    if(bytesNovo.length <= tamReg) {
                        ras.seek(posInicial + 5);
                        ras.write(bytesNovo);
                        return true;
                    } else {
                        ras.seek(posInicial);
                        ras.writeBoolean(true);
                        ras.seek(ras.length()); 
                        ras.writeBoolean(false);
                        ras.writeInt(novo.toByteArray().length);
                        ras.write(novo.toByteArray());
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public boolean update(Pokemon pokemon) throws Exception{
        return update(ras, pokemon);
    }

    /* ------
     * DELETE
     * ------
     * move o ponteiro pro inicio do arquivo (APOS O CABECALHO)
     * varre o arquivo (tamReg = tamanho do registro + 1 da lapide (booleano) + 4 do tamanho (int))
     * 
     * em cada registro,
     * salva a posicao inicial
     * le a lapide do registro
     * le o tamanho do registro
     * cria o byte a ser lido
     * le e aloca o byte
     * cria o pokemon com as informacoes do byte lido
     * 
     * se a lapide for verdadeira,
     * se encontrado o registro a ser excluido,
     * move o ponteiro para a lapide do registro
     * e a demarca como excluida (false)
     */
    public boolean delete(RandomAccessFile ras, int id) throws Exception{
        boolean lapide = true;
        int tamReg = 0;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            long posInicial = ras.getFilePointer();
            lapide = ras.readBoolean();
            tamReg = ras.readInt();

            byte [] bytes = new byte [tamReg];
            ras.read(bytes);
            Pokemon pokemon = new Pokemon(bytes);
            
            if(!lapide){
                if(pokemon.getNumber() == id) {
                    ras.seek(posInicial);
                    ras.writeBoolean(true);
                    return true;
                }
            }
        }

        return false;
    }
    public boolean delete(int id) throws Exception{
        return delete(ras, id);
    }
   
    //=====DELAY=====//
    public static void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}