import java.util.Scanner;

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
}