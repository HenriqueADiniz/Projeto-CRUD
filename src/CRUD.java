import java.util.Scanner;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CRUD {
    public static void main(String[] args) {
        System.out.print("\033[H\033[2J");
        
        Reader.main(args);
        
        delay(1500);
        Scanner scan = new Scanner (System.in);
        while (true){
            System.out.print("\033[H\033[2J");
            System.out.print("*--------------------------*\n");
            System.out.print("#    OPCOES DE REGISTRO    #\n");
            System.out.print("*--------------------------*\n");
            System.out.print("| 1) Ler                   |\n");
            System.out.print("| 2) Atualizar             |\n");
            System.out.print("| 3) Deletar               |\n");
            System.out.print("|                          |\n");
            System.out.print("| 0) Sair                  |\n");
            System.out.print("*--------------------------*\n");
            System.out.print("Digite uma opcao: ");
            String opt = scan.next();

            switch (opt){
                case "1":
                    System.out.println();
                    // chamar metodo ler
                    System.out.println();
                    break;

                case "2":
                    System.out.println();
                    // chamar metodo atualizar
                    System.out.println();
                    break;
                
                case "3":
                    System.out.println();
                    // chamar metodo deletar
                    System.out.println();
                    break;

                case "0":
                    System.out.println();
                    scan.close();
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
     * move o ponteiro pro inicio do arquivo
     * altera a quantidade de id's nova para (qntd + 1)
     * move o ponteiro pro fim do arquivo
     * escreve a lapide
     * escreve o tamanho do byte
     * escreve o registro
     */
    public void create(RandomAccessFile ras, Pokemon pokemon) throws IOException{
        if (pokemon == null) pokemon = new Pokemon(0, null, null, null, null, 0, 0, 0, null);
        
        ras.seek(0);
        int qntIds = ras.readInt();
        pokemon.setNumber(qntIds + 1);

        ras.seek(0);
        ras.writeInt( qntIds + 1 );

        ras.seek(ras.length()); 
        ras.writeBoolean(true);
        ras.writeInt(pokemon.toByteArray().length);
        ras.write(pokemon.toByteArray());
    }

    /* ----
     * READ
     * ----
     * move o ponteiro pro inicio do arquivo (APOS O CABECALHO)
     * varre o arquivo (tamReg = tamanho do registro + 1 da lapide (booleano) + 4 do tamanho (int))
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
    public Pokemon read(RandomAccessFile ras, int id) throws Exception{
        boolean lapide = false;
        int tamReg = 0;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            lapide = ras.readBoolean();
            tamReg = ras.readInt();

            byte [] bytes = new byte [tamReg];
            ras.read(bytes);
            Pokemon pokemon = new Pokemon(bytes);

            if(lapide == true){
                if(pokemon.number == id) {
                    return pokemon;
                }
            }
        }

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
    public boolean update (RandomAccessFile ras, Pokemon novo) throws Exception {
        boolean lapide = false;
        int tamReg = 0;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            long posInicial = ras.getFilePointer();
            lapide = ras.readBoolean();
            tamReg = ras.readInt();

            byte [] bytes = new byte [tamReg];
            ras.read(bytes);
            Pokemon pokemon = new Pokemon(bytes);

            if(lapide == true){
                if(pokemon.getNumber() == novo.getNumber()) {
                    byte [] bytesNovo = novo.toByteArray();

                    if(bytesNovo.length <= tamReg) {
                        ras.seek(posInicial + 5);
                        ras.write(bytesNovo);
                        return true;
                    } else {
                        ras.seek(posInicial);
                        ras.writeBoolean(false);
                        this.create(ras, novo);
                        return true;
                    }
                }
            }
        }

        return false;
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
    public static boolean delete(RandomAccessFile ras, int id) throws Exception{
        boolean lapide = false;
        int tamReg = 0;

        ras.seek(4);
        for (long i = 4; i < ras.length(); i += tamReg + 5) {
            long posInicial = ras.getFilePointer();
            lapide = ras.readBoolean();
            tamReg = ras.readInt();

            byte [] bytes = new byte [tamReg];
            ras.read(bytes);
            Pokemon pokemon = new Pokemon(bytes);
            
            if(lapide == true){
                if(pokemon.getNumber() == id) {
                    ras.seek(posInicial);
                    ras.writeBoolean(false);
                    return true;
                }
            }
        }

        return false;
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