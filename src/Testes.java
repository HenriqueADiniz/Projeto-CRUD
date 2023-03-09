import java.io.File;
import java.io.RandomAccessFile;

public class Testes {
    public static void main(String[] args) throws Exception {
        File arq = new File("tmp/pokemons.db");
        testes();
    }

    public static void testes () throws Exception {
        RandomAccessFile ras = new RandomAccessFile("tmp/pokemons.db", "rw");
        ras.writeInt(0);
        ras.close();
    
        CRUD crud = new CRUD("tmp/pokemons.db");
        Pokemon temp = new Pokemon();
    //Create Ok
    //Read Ok
    //update Ok
    //Delete OK
    //CRUD COMPLETO
    

        // criacao de 100 pokemons
        for (int i = 0; i < 100; i++) {
            temp.setName("Pokemon " + i + 1);
            crud.create(temp);
        } // end for

        for (int i = 1; i < 200; i++){
            temp.setNumber(i);
            crud.read(temp.getNumber());
            
        }
    
        // // update de 20 pokemons
        // for (int i = 0; i < 20; i+=2) {
            // temp.setName("Atualizado Filme " + i);
           //  crud.update(temp);
        // } // end for
    } // end testes ()
}