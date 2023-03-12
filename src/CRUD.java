import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;

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
     public int create(RandomAccessFile ras, Pokemon pokemon) throws IOException{
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
     * reescreve o pokemon na mesma posição
     * 
     * senao,
     * deleta o antigo pokemon que possuia o id que esta sendo atualizando
     * escreve o pokemon atualizado sem alterar seu id original ao final do arquivo
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
                        ras.writeBoolean(false);
                        ras.seek(ras.length()); 
                        ras.writeBoolean(true);
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
            
            if(lapide){
                if(pokemon.getNumber() == id) {
                    ras.seek(posInicial);
                    ras.writeBoolean(false);
                    return true;
                }
            }
        }

        return false;
    }
    public boolean delete(int id) throws Exception{
        return delete(ras, id);
    }
}