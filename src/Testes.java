import java.io.File;
import java.io.RandomAccessFile;

public class Testes {
    public static void main(String[] args) throws Exception {
        File arq = new File("tmp/pokemons.bin");
        testes(); 
    }

    public static void testes () throws Exception {
        RandomAccessFile ras = new RandomAccessFile("tmp/pokemons.bin", "rw");
        ras.writeInt(0);
        ras.close();
    
        CRUD crud = new CRUD("tmp/pokemons.bin");
        Pokemon temp = new Pokemon();
    
        // criacao de 100 pokemons
        for (int i = 0; i < 100; i++) {
            temp.setName("Pokemon " + i);
            crud.create(temp);
        } // end for
    
        // // update de 20 pokemons
        // for (int i = 0; i < 20; i+=2) {
        //     temp.setName("Atualizado Filme " + i);
        //     crud.update(temp);
        // } // end for
    } // end testes ()
}