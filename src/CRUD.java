import java.util.Scanner;
import java.io.RandomAccessFile;

public class CRUD {
    //=====ATRIBUTOS=====//
    static int ultimoId;

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
     * posiciona o ponteiro no final do arquivo
     * escreve a lapide
     * escreve o tamanho do registro
     * escreve os atributos do pokemon
     * posiciona o ponteiro no inicio do arquivo
     * atualiza o ultimo id
     */
    public static boolean create(RandomAccessFile raf, Pokemon pokemon){
        try {
            raf.seek(raf.length());
            raf.writeByte(0);
            raf.writeInt(pokemon.toByteArray().length);
            
            raf.writeInt(pokemon.getNumber());
            raf.writeInt(pokemon.getHP());
            raf.writeInt(pokemon.getAtt());
            raf.writeInt(pokemon.getDef());
            raf.writeUTF(pokemon.getName());
            raf.writeUTF(pokemon.getType1());
            raf.writeUTF(pokemon.getType2());
            // escreve as abilities do pokemon
            // escreve a data do pokemon

            raf.seek(0);
            raf.writeInt(ultimoId);

            return true;
        } catch (Exception e) {
            System.out.println("Erro ao criar registro!");
            return false;
        }
    }

    /* ------
     * DELETE
     * ------
     * posiciona o ponteiro no inicio do arquivo
     * varre o arquivo, somente de lapide ativa em lapide ativa (0)
     * quando encontrado o registro a ser excluido,
     * volta o ponteiro para o inicio do registro
     * e demarca a lapide como excluida (1)
     */
    public static boolean delete(RandomAccessFile raf, Pokemon pokemon){
        try {
            raf.seek(4);

            while(raf.getFilePointer() < raf.length()){
                if(raf.readByte() == 0){
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == pokemon.getNumber()){
                        raf.seek(raf.getFilePointer()-9);
                        raf.writeByte(1);
                        return true;
                    } else {
                        raf.skipBytes(tam-4);
                    }
                } else {
                    raf.skipBytes(raf.readInt());
                }
            }

            return false;
        } catch (Exception e) {
            System.out.println("Erro ao deletar registro!");
            return false;
        }
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