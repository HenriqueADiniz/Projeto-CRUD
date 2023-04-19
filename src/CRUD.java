import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.EOFException;

public class CRUD {
    // inicializa o RAS
    private  RandomAccessFile ras;
    // construtor do CRUD com o arquivo .db
    public CRUD (String arquivo) throws FileNotFoundException {
        this.ras = new RandomAccessFile(arquivo, "rw");
    }

    /* ------
     * CREATE
     * ------
     * move o ponteiro pro inicio do arquivo
     * le a quantidade de ids
     * cria a id do pokemon novo 
     *
     * move o ponteiro pro inicio do arquivo
     * altera a quantidade de id's nova para (qntdReg)
     *
     * move o ponteiro pro fim do arquivo
     * converte o objeto para bytes
     * escreve a lapide
     * escreve o tamanho do registro
     * escreve o registro
     *
     * retorna o id do registro gerado
     */
     public int create(RandomAccessFile ras, Pokemon pokemon,ArvoreBmais index,Hash index2) throws Exception{
        if (pokemon == null) pokemon = new Pokemon();
        
        ras.seek(0);
        int qntReg = ras.readInt();
        qntReg++;
        pokemon.setNumber(qntReg);
    
        ras.seek(0);
        ras.writeInt(qntReg);
        
        ras.seek(ras.length()); 
        byte[] ba = pokemon.toByteArray();
        ras.writeBoolean(true);
        ras.writeInt(ba.length);
        ras.write(ba);
        index.create(Integer.toString(pokemon.getNumber()),Long.valueOf(ras.length()).intValue());
        index2.create(pokemon.getNumber(),ras.length());
        return pokemon.getNumber();
    }
    public int create(Pokemon pokemon,ArvoreBmais index,Hash index2) throws Exception{
       return create(ras,pokemon,index,index2);
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
            tamReg = ras.readInt();

            byte [] ba = new byte [tamReg];
            ras.read(ba);
            Pokemon pokemon = new Pokemon(ba);

            if(lapide){
                if(pokemon.getNumber() == id) {
                    return pokemon;
                }
            }
        }
        return null;
    }
    public static void ler(RandomAccessFile ras, ArvoreBmais index)throws IOException{
        // Variáveis e Instâncias//
        int len = 0;
        String idProcurado = "";
        long posIni = 0;
        boolean valido;
        Scanner sc = new Scanner(System.in);
        byte ba[];
        Pokemon pokemonTemp = new Pokemon();
        System.out.print("Digite o ID do Pokemon a ser lido: "); // Pede id para usuário
        idProcurado = sc.nextLine();
        System.out.println();
        posIni = index.read(idProcurado);
        try {
            ras.seek(posIni); // posiciona ponteiro no inicio do arquivo
            valido = ras.readBoolean();// ler lapide -- se TRUE filme existe , caso FALSE filme apagado
            len = ras.readInt(); // ler tamanho do registro
            ba = new byte[len]; // cria um vetor de bytes com o tamanho do registro
            ras.read(ba); // Ler registro
            pokemonTemp.fromByteArray(ba); // Transforma vetor de bytes lido por instancia de FIlme
            posIni = ras.getFilePointer();// Marca posição que acabou o registro e será iniciado outro
            if (valido == true) { // caso idProcurado e id do filme lido forem iguais
                                 // e filme não tver sido apagado será escrito as
                                 // informações.
                pokemonTemp.setNumber(pokemonTemp.getNumber()-1);
                System.out.println("+---------------------+");
                System.out.println("| POKEMON ENCONTRADO! |");
                System.out.println("+---------------------+------------");
                System.out.println("POSIÇÃO: " + posIni + "\n");
                System.out.println(pokemonTemp.toString());
                System.out.println("-----------------------------------");
            }
        } catch (java.io.IOException e) {
            System.out.println("*-------------------------------*");
            System.out.println("| ERRO: POKEMON NAO ENCONTRADO. |");
            System.out.println("*-------------------------------*");
        } // Erro fim do arquivo , ou seja , não achou o

    }
    public static void lerHash(RandomAccessFile arq, Hash index)
            throws NumberFormatException, Exception {
        // Variáveis e Instâncias//
        int len = 0;
        String idProcurado = "";
        long posIni = 0;
        boolean valido;
        byte ba[];
        Scanner sc = new Scanner(System.in);
        Pokemon filmeTemp = new Pokemon();
        // Variáveis e Instâncias//

        System.out.println("Digite o id: "); // Pede id para usuário
        idProcurado = sc.nextLine();
        posIni = index.read(Integer.parseInt(idProcurado));

        try {
            arq.seek(posIni); // posiciona ponteiro no inicio do arquivo
            valido = arq.readBoolean();// ler lapide -- se TRUE filme existe , caso FALSE filme apagado
            len = arq.readInt(); // ler tamanho do registro
            ba = new byte[len]; // cria um vetor de bytes com o tamanho do registro
            arq.read(ba); // Ler registro
            filmeTemp.fromByteArray(ba); // Transforma vetor de bytes lido por instancia de FIlme
            posIni = arq.getFilePointer();// Marca posição que acabou o registro e será iniciado outro

            if (valido == true) { // caso idProcurado e id do filme lido forem iguais
                                  // e filme não tver sido apagado será escrito as
                                  // informações.
                                  filmeTemp.setNumber(filmeTemp.getNumber()-1);
                                  System.out.println("+---------------------+");
                                  System.out.println("| POKEMON ENCONTRADO! |");
                                  System.out.println("+---------------------+------------");
                                  System.out.println("POSIÇÃO: " + posIni + "\n");
                                  System.out.println(filmeTemp.toString());
                                  System.out.println("-----------------------------------");
        }
    }catch (java.io.IOException e) {
            System.out.println("Não achei o filme"); // Erro fim do arquivo , ou seja , não achou o
                                                     // filme
        }

        /*
         * Vale ressaltar que foi usado um while(true) tanto no read quanto no update e
         * delete, pois mesmo que o id certo for
         * achado ele podia estar apagado, e se um filme com o mesmo id fosse criado,
         * não seria identificado, dessa forma , esse erro identificado foi corrigido.
         */
    }

    /* ------
     * UPDATE
     * ------
     * move o ponteiro pro inicio do arquivo (apos o cabecalho)
     * varre o arquivo (tamReg = tamanho do registro + 1 da lapide (booleano) + 4 do tamanho (int))
     * 
     * em cada registro,
     * salva a posicao inicial
     * le a lapide do registro
     * le o tamanho do registro
     * cria o byte a ser lido (com o tamanho do registro)
     * le e aloca o byte
     * cria o pokemon com as informacoes do byte lido
     * 
     * se a lapide for verdadeira,
     * se o pokemon lido tiver o mesmo id do novo pokemon,
     * cria o novo registro em bytes
     * 
     * se o tamanho do novo regitro for  menor ou igual ao antigo,
     * reescreve o pokemon na mesma posicao
     * 
     * senao,
     * deleta o antigo pokemon que possuia o id que esta sendo atualizando
     * escreve o pokemon atualizado sem alterar seu id original ao final do arquivo
     */
    public boolean update(RandomAccessFile ras, Pokemon novo,ArvoreBmais index,Hash index2) throws Exception {
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
                        index.create(Integer.toString(novo.getNumber()),Long.valueOf(ras.length()).intValue());
                        index2.create(novo.getNumber(), ras.length());
                        return true;
                    } else {
                        ras.seek(posInicial);
                        ras.writeBoolean(false);
                        ras.seek(ras.length()); 
                        int posNova = Long.valueOf(ras.length()).intValue();
                        ras.writeBoolean(true);
                        ras.writeInt(novo.toByteArray().length);
                        ras.write(novo.toByteArray());
                        index.create(Integer.toString((novo.getNumber())),posNova);
                        index2.create(novo.getNumber(), posNova);
                        System.out.println("Update realizado no final do arquivo!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean update(Pokemon pokemon,ArvoreBmais index,Hash index2) throws Exception{
        return update(ras, pokemon,index,index2);
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
    public boolean delete(RandomAccessFile ras, int id,ArvoreBmais index,Hash index2) throws Exception{
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
            
            if(lapide){
                if(pokemon.getNumber() == id) {
                    ras.seek(posInicial);
                    ras.writeBoolean(false);
                    index.delete(Integer.toString(pokemon.getNumber()));
                    index2.delete(pokemon.getNumber());

                    return true;
                }
            }
        }

        return false;
    }
    public boolean delete(int id,ArvoreBmais index,Hash index2) throws Exception{
        return delete(ras, id,index,index2);
    }
  

    public static void criaIndexArvoreB(RandomAccessFile arq, ArvoreBmais index)
            throws IOException {
        long posIni = 0;
        boolean valido = true;
        int len;
        byte ba[];
        Pokemon PokemonTemp = new Pokemon();
        int posIniInt = 0;
        int i = 0;

        while (true) {
            try {
                arq.seek(posIni); // posiciona ponteiro no inicio do arquivo
                valido = arq.readBoolean();// leitura da lapide -- se TRUE filme existe, caso FALSE filme apagado
                len = arq.readInt(); // leitura tamanho do registro
                ba = new byte[len]; // cria um vetor de bytes com o tamanho do registro
                arq.read(ba); // Leitura registro
                PokemonTemp.fromByteArray(ba); // Transforma vetor de bytes lido por instancia de FIlme
                posIniInt = Long.valueOf(posIni).intValue();
                index.create(Integer.toString(PokemonTemp.getNumber()), posIniInt);
                posIni = arq.getFilePointer();// Marca posição que acabou o registro e será iniciado outro
                i++;

            } catch (EOFException e) {
                break;
            }
        }
        System.out.println("Index Arvore B criado com sucesso");

    }

}