import java.util.Scanner;
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

    // delay
    public static void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
        // --------------- Atributos ---------------
       static int ultimoId;
        // --------------- Create ---------------
        public static boolean create(RandomAccessFile raf, Pokemon pokemon) { // Cria um pokemon

            try {
                raf.seek(raf.length()); // Posiciona o ponteiro no final do arquivo
                raf.writeByte(0); // Escreve a lapide (0 = ativo)
                raf.writeInt(pokemon.toByteArray().length); // Escreve o tamanho do registro
                raf.writeInt(pokemon.getNumber()); // Escreve o numero do pokemon
                raf.writeInt(pokemon.getHP()); // Escreve o Hp do pokemon
                raf.writeInt(pokemon.getAtt()); // Escreve o Att do pokemon
                raf.writeInt(pokemon.getDef()); // Escreve a Def do pokemon
                raf.writeUTF(pokemon.getName()); // Escreve o nome do pokemon
                raf.writeUTF(pokemon.getType1()); // Escreve o Type1 do pokemon
                raf.writeUTF(pokemon.getType2()); // Escreve o Type 2 do pokemon
                // Escreve as abilities do pokemon
                // Escreve a data do pokemon
             
    
                raf.seek(0); // Posiciona o ponteiro no inicio do arquivo
                raf.writeInt(ultimoId); // Atualiza o ultimo id
    
                return true;
            } catch (Exception e) {
                System.out.println("-> Erro ao criar registro!");
                return false;
            }
        }
        // --------------- Delete ---------------

    public static boolean delete(RandomAccessFile raf, Pokemon pokemon) { // Exclui um Pokemon
        try {
            raf.seek(4); // Posiciona o ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()) { // Enquanto o ponteiro nao chegar no final do arquivo
                if(raf.readByte() == 0) { // Se a lapide for 0 (ativo)
                    int tam = raf.readInt();
                    int id = raf.readInt();

                    if(id == pokemon.getNumber()) { // Se o numero do pokemon for igual ao numero do  a ser excluido
                        raf.seek(raf.getFilePointer() - 9); // Volta o ponteiro para o inicio do registro
                        raf.writeByte(1); // Escreve a lapide 1 (excluido)
                        return true;
                    }else {
                        raf.skipBytes(tam - 4); // Pula o resto do registro
                    }
                }else {
                    raf.skipBytes(raf.readInt()); // Pula o registro
                }
            }
            return false;
        }catch (Exception e) {
            System.out.println("-> Erro ao deletar registro!");
            return false;
        }
    }

}