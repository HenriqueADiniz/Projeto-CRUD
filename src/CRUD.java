import java.util.Scanner;
import java.io.RandomAccessFile;
import java.io.IOException;

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
     * posiciona o ponteiro no comeco do arquivo
     * le a quantidade de ids
     * atualiza o id
     * move o ponteiro para o comeco do arquivo
     * escreve a quantidade de ids nova
     * mover o ponteiro para o fim do arquivo
     * escrever o tamnho do byte
     * escreve registro
     */
    public void create(RandomAccessFile ras, Pokemon pokemon) throws IOException{
        if (pokemon == null) pokemon = new Pokemon(0, null, null, null, null, 0, 0, 0, null);
         // mover o ponteiro para o comeco do arquivo
         ras.seek(0);
        // ler quantidade de ids
         int qntIds = ras.readInt();
        // atualizar id
         pokemon.setNumber(qntIds + 1);
         // mover o ponteiro para o comeco do arquivo
        ras.seek(0);
        // escrever quantidade de ids nova
        ras.writeInt( qntIds + 1 );
         // mover o ponteiro para o fim do arquivo
        ras.seek(ras.length()); 
        // escrever lapide
        ras.writeBoolean(true);
        // escrever tamanho do byte
        ras.writeInt(pokemon.toByteArray().length);
        // escrever tamanho do byte
        ras.write(pokemon.toByteArray());
    }

    /* ------
     * DELETE
     * ------
     * posiciona o ponteiro no inicio do arquivo após o cabeçalho
     * varre o arquivo, somente de lapide ativa em lapide ativa (true)
     * quando encontrado o registro a ser excluido,
     * volta o ponteiro para o inicio do registro
     * e demarca a lapide como excluida (false)
     */
    public static boolean delete(RandomAccessFile ras ,int id) throws Exception{
        boolean lapide = false;
        int tamRegistro = 0;
          // posicionar o ponteiro no comeco do arquivo depois do cabecalho
         ras.seek(4); //1 int 
          // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {
             // salvar a posicao inicial
               long posInicial = ras.getFilePointer();
            // leitura da lapide
               lapide = ras.readBoolean();
            // leitura do tamanho
               tamRegistro = ras.readInt();
            // criacao do filme pelo byte array
               byte [] ba = new byte [tamRegistro];
               ras.read(ba);
               Pokemon pokemon = new Pokemon(ba);
               // se lapide verdadeira
               if(lapide == true)
               // se lapide verdadeira
                   if(pokemon.getNumber() == id) {
                      // retorna o ponteiro para a lapide desse registro
                       ras.seek(posInicial);
                         // escreve por cima da lapide, ou seja exclusao logica
                       ras.writeBoolean(false);
                       return true;
                   } //ends if
           } //end for
           return false;
       } //end delete


    
       public Pokemon read(RandomAccessFile ras ,int id) throws Exception{
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(4); // 1 int

     // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {

         // leitura da lapide
            lapide = ras.readBoolean();

         // leitura do tamanho
            tamRegistro = ras.readInt();

         // criacao do filme pelo byte array
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            Pokemon pokemon = new Pokemon(ba);

         // se lapide for verdadeira
            if(lapide == true)
             // se o id for igual ao pesquisado
                if(pokemon.number == id) {
                    return pokemon;
                } // end if
        } // end for
        return null;
    } // end read ()

    public boolean update (RandomAccessFile ras, Pokemon novo) throws Exception {
        boolean lapide = false;
        int tamRegistro = 0;
     // posicionar o ponteiro no comeco do arquivo depois do cabecalho
        ras.seek(4); // 1 int

     // enquanto nao atingir o fim do arquivo // tamRegistro + 5 -> tamanho do registro + 1 booleano (lapide) + 1 int (tamanho)
        for (long i = 4; i < ras.length(); i += tamRegistro + 5) {

         // salvar a posicao inicial
            long posInicial = ras.getFilePointer();

         // leitura da lapide
            lapide = ras.readBoolean();

         // leitura do tamanho
            tamRegistro = ras.readInt();

         // criacao do filme pelo byte array
            byte [] ba = new byte [tamRegistro];
            ras.read(ba);
            Pokemon filme = new Pokemon(ba);

         // se lapide verdadeira
            if(lapide == true)

             // se o filme tiver o mesmo id do novo filme
                if(filme.getNumber() == novo.getNumber()) {

                 // criacao de novo registro
                    byte [] baNovo = novo.toByteArray();

                 // se o tamanho do novo registro for igual ao antigo
                    if(baNovo.length <= tamRegistro) {
                        ras.seek(posInicial + 5);
                        ras.write(baNovo);
                        return true;
                    } else {
                        ras.seek(posInicial);
                        ras.writeBoolean(false);
                        this.create(ras,novo);
                        return true;
                    } // end if
                } // end if
        } // end for
        return false;
    } // end update ()

 

        
    

    //=====DELAY=====//
    public static void delay(int ms){
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}