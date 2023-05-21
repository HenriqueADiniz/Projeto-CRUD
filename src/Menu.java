// HENRIQUE DE ALMEIDA DINIZ
// SAMUEL LUIZ DA CUNHA VIANA CRUZ

import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Scanner;
import java.util.HashMap;

public class Menu {
    // caminho dos arquivos
    private static final String DB_PATH = "tmp/pokemons.db";
    private static final String BT_PATH = "tmp/Bplus.db";
    private static final String H_PATH = "tmp/Hash.db";
    private static final String HB_PATH = "tmp/HashB.db";

    /* ----
     * MAIN
     * ----
     * inicializa instancia do CRUD com o arquivo .db
     * aciona o arquivo reader e seus metodos
     * entra em loop com as opcoes do CRUD
     */
    public static void main(String[] args) throws Exception {
        System.out.print("\033[H\033[2J");
        
        ArvoreBmais index= new ArvoreBmais(8, BT_PATH);
        Hash index2 =new Hash(45, HB_PATH, H_PATH);

        Reader.main(args);
        CRUD crud = new CRUD(DB_PATH);
        RandomAccessFile ras=new RandomAccessFile(DB_PATH, "rw");
        crud.create(null, index, index2);

        delay(1250);
        Scanner scan = new Scanner (System.in);
        while (true){
            int hp, att, def, id,N,M;
            String name, type1, type2, abilitiesTemp, dateTemp;
            System.out.print("\033[H\033[2J");
            System.out.print("*---------------------------*\n");
            System.out.print("#     OPCOES DE REGISTRO    #\n");
            System.out.print("*---------------------------*\n");
            System.out.print("| 1)  Criar                 |\n");
            System.out.print("| 2)  Ler                   |\n");
            System.out.print("| 3)  Atualizar             |\n");
            System.out.print("| 4)  Deletar               |\n");
            System.out.print("|                           |\n");
            System.out.print("| 5)  Ordenacao Externa     |\n");
            System.out.print("| 6)  Buscar na Arvore      |\n");
            System.out.print("| 7)  Buscar no Hash        |\n");
            System.out.print("|                           |\n");
            System.out.print("| 8)  Comp.: LZW            |\n");
            System.out.print("| 9)  Comp.: Huffman        |\n");
            System.out.print("| 10) Descomp.: LZW         |\n");
            System.out.print("| 11) Descomp.: Huffman     |\n");
            System.out.print("|                           |\n");
            System.out.print("| 0) Sair                   |\n");
            System.out.print("*---------------------------*\n");
            System.out.print("Digite uma opcao: ");
            String opt = scan.next();

            switch (opt){
                case "1":
                    Scanner scanLinhaA = new Scanner (System.in).useDelimiter("\\n");

                    System.out.print("Digite o nome: ");
                    name = scan.next();
                    System.out.print("Digite o tipo 1: ");
                    type1 = scan.next();
                    System.out.print("Digite o tipo 2: ");
                    type2 = scan.next();
                    System.out.print("Digite as habilidades, separadas por virgulas: ");
                    abilitiesTemp = scanLinhaA.next();
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
                    int i = crud.create(criado,index,index2);
                    System.out.println("\nID criado: " + (i-1));

                    waitForEnter();
                    break;
                    
                case "2":
                    System.out.print("Digite o ID do Pokemon a ser lido: ");
                    int idRead = scan.nextInt();
                    
                    System.out.println();
                    Pokemon lido = crud.read(idRead);
                    if (lido != null){
                        System.out.println("+---------------------+");
                        System.out.println("| POKEMON ENCONTRADO! |");
                        System.out.println("+---------------------+------------");
                        System.out.println(lido.toString());
                        System.out.println("-----------------------------------");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;

                case "3":
                    Scanner scanLinhaB = new Scanner (System.in).useDelimiter("\\n");

                    System.out.print("Digite o ID do pokemon que deseja atualizar: ");
                    id = scan.nextInt();
                    System.out.print("Digite o novo nome: ");
                    name = scan.next();
                    System.out.print("Digite o novo tipo 1: ");
                    type1 = scan.next();
                    System.out.print("Digite o novo tipo 2: ");
                    type2 = scan.next();
                    System.out.print("Digite as novas habilidades, separadas por virgulas: ");
                    abilitiesTemp = scanLinhaB.next();
                    System.out.print("Digite o novo HP: ");
                    hp = scan.nextInt();
                    System.out.print("Digite o novo ataque: ");
                    att = scan.nextInt();
                    System.out.print("Digite a nova defesa: ");
                    def = scan.nextInt();
                    System.out.print("Digite a nova data, no formato (dd/mm/aaaa): ");
                    dateTemp = scan.next();

                    date = Tratamentos.trataDatas(dateTemp);
                    abilities = abilitiesTemp.split(",");

                    System.out.println();
                    Pokemon atualizado = new Pokemon((id+1), name, type1, type2, abilities, hp, att, def, date);
                    Boolean upd = crud.update(atualizado,index,index2);
                    if (upd){
                        System.out.println("*---------------------------------*");
                        System.out.println("| POKEMON ATUALIZADO COM SUCESSO! |");
                        System.out.println("*---------------------------------*");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;
                    
                case "4":
                    System.out.print("Digite o ID do Pokemon a ser deletado: ");
                    int idDel = scan.nextInt();

                    System.out.println();
                    Boolean del = crud.delete(idDel,index,index2);
                    if (del){
                        System.out.println("*-------------------------------*");
                        System.out.println("| POKEMON DELETADO COM SUCESSO! |");
                        System.out.println("*-------------------------------*");
                    } else {
                        System.out.println("*-------------------------------*");
                        System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
                        System.out.println("*-------------------------------*");
                    }
                    waitForEnter();
                    break;

                case "5":
                    System.out.print("Digite a quantidade de registros: ");
                    N = scan.nextInt();
                    System.out.print("Digite a quantidade de caminhos: ");
                    M = scan.nextInt();
                    Intercalacoes.iBComum(N, M);
                    waitForEnter();
                    break;

                case "6":
                    CRUD.ler(ras,index);
                    waitForEnter();
                    break;

                case "7":
                    CRUD.lerHash(ras, index2);
                    waitForEnter();
                    break;

                case "8":
                    // COMPRESSÃO LZW
                    System.out.println("> Digite o nome do arquivo de saída: ");
                    String fileName = scan.next();

                    LZW.compress(DB_PATH, fileName);

                    System.out.println("\n>>> Arquivo compactado com sucesso em \"" + fileName + "\"!");
                    break;
            

                case "9":
                    // COMPRESSÃO HUFFMAN
                    break;

                case "10":
                System.out.println("> Digite o nome do arquivo compactado: ");
                String fileName2 = scan.next();

                LZW.decompress(fileName2, DB_PATH);

                System.out.println(
                        "\n>>> Arquivo descompactado com sucesso em \"" + DB_PATH + "\"!");
                break;
                  

                case "11":
                    // DESCOMPRESSÃO HUFFMAN
                    
                case "0":
                    System.exit(1);
                
                default:
                    break;
            
            }
        }
    }

    //=====WAIT=====//
    public static void waitForEnter(){
        Scanner s = new Scanner(System.in);
        System.out.println("Pressione Enter para continuar...");
        s.nextLine();
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