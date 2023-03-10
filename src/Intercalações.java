
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

public class Intercalações {
int N = 5000; // size of the file in disk
  int M = 5000; // max items the memory buffer can hold

 // OrdenacaoExterna() {
    //this.N = 0;
   // this.M = 0;

 // }

  //OrdenacaoExterna(int n, int m) {
   // this.N = n;
    //this.M = m;

  //}
//
  
  /**
         * <b>swap</b> - Trocar elementos na lista
         * @param pos1 - poisicao para a troca
         * @param pos2 - posicao para a troca
         * @param pokearr <code>Pokemon []</code> a ser trocado
         */
        private static void swap(int pos1, int pos2, Pokemon [] pokearr) {
            Pokemon aux = pokearr[pos1];
            pokearr[pos1] = pokearr[pos2];
            pokearr[pos2] = aux;
        } // end swap

        /**
         * <b>quicksort</b> - primeira chamada do quicksort
         * @param pokearr - <code>Pokemon []</code> a ser ordenado por id
         */
        public static void quicksort(Pokemon [] pokearr) {
            quicksort( 0, pokearr.length - 1, pokearr);
        } // end quicksort ()

        /**
         * <b>quicksort</b> - ordenacao quicksort
         * @param esq - valor da esquerda
         * @param dir - valor da direita
         * @param pokearr - <code>Pokemon [] </code> a ser ordenado por id
         */
        private static void quicksort(int esq, int dir, Pokemon [] pokearr) {
            int pivo = pokearr[(dir+esq)/2].getNumber();
            int i = esq;
            int j = dir;
         // faz as trocas até que o i passe o j
            while (i <= j) {
             // enquanto o id do pivo for maior do que o elemento da esquerda procura o valor para troca
                while (pivo > pokearr[i].getNumber()) {
                    i++;
                } // end while

             // enquanto o id do pivo for menor do que o elemento da direita procura o valor para troca
                while (pivo < pokearr[j].getNumber()) {
                    j--;
                } // end while

             // se o i estiver em j ou não chegou em j, trocar esses jogos
                if (i <= j) {
                    swap(i, j, pokearr);
                    i++;
                    j--;
                } // end if
            } // end while

         // se necessario, fazer um novo quicksort com um array de elementos a esquerda do pivo
            if (esq < j) {
                quicksort(esq, j, pokearr);
            } // end if

         // se necessario, fazer um novo quicksort com um array de elementos a direita de do pivo
            if (dir > i) {
                quicksort(i, dir, pokearr);
            } // end if
        } // end quicksort ()

        public void distribuicao(int slices, String fileName, RandomAccessFile arq, String tfile)
      throws IOException, ParseException {
    FileReader fr = new FileReader(fileName);
    BufferedReader br = new BufferedReader(fr);
    Pokemon[] poketemp = new Pokemon[M];
    boolean valido;
    int len = 0;
    long posIni = 0;
    byte ba[], ba2[];

    int i, j;
    i = j = 0;
    // Iterate through the elements in the file
    for (i = 0; i < slices; i++) {
      // Read M-element chunk at a time from the file

      for (j = 0; j < (M < N ? M : N); j++) {

        arq.seek(posIni); // posiciona ponteiro no inicio do arquivo
        valido = arq.readBoolean();// ler lapide -- se TRUE Pokemon existe , caso FALSE Pokemon apagado
        len = arq.readInt(); // ler tamanho do registro
        ba = new byte[len]; // cria um vetor de bytes com o tamanho do registro
        int x = arq.read(ba); // Ler registro
        poketemp[j] = new Pokemon();
        poketemp[j].fromByteArray(ba); // Transforma vetor de bytes lido por instancia de Pokemon
        posIni = arq.getFilePointer();// Marca posição que acabou o registro e será iniciado outro

      }

      quicksort(poketemp);

      // precisa ordenar o poketemp[]

      // Write the sorted numbers to temp file
      // FileWriter fw = new FileWriter(tfile + Integer.toString(i) + ".txt");
      // PrintWriter pw = new PrintWriter(fw);
      RandomAccessFile arqTemp = new RandomAccessFile(tfile + Integer.toString(i) + ".db", "rw");
      for (int k = 0; k < j; k++) {
        ba2 = poketemp[k].toByteArray();
        arqTemp.writeBoolean(true); // Escreve lápide
        arqTemp.writeInt(ba2.length); // Escreve tamanho do vetor de bytes(Registro)
        arqTemp.write(ba2); // Escreve registro
      }

      arqTemp.close();

      // fw.close();
    }

    br.close();
    fr.close();
  }
  public void IntercalaçãoComum(String fileName, RandomAccessFile arq) throws ParseException {
    String tfile = "arquivosTemporarios/temp-file-";
    String[] buffer = new String[M < N ? M : N];
    long posIni = 0;
    boolean valido;
    int len = 0;
    byte ba[], ba2[];
    int slices = (int) Math.ceil((double) N / M);
    int i, j;
    i = j = 0;

    try {
      distribuicao(slices, fileName, arq, tfile);

      // Now open each file and merge them, then write back to disk

      RandomAccessFile[] brs = new RandomAccessFile[slices];
      File file[] = new File[slices];

      Pokemon pokemonTemp2[] = new Pokemon[slices];
      Pokemon pokemonTemp3 = new Pokemon();
      Pokemon pokemonTemp4 = new Pokemon();

      posIni = 0;
      byte ba3[];
      valido = true;
      len = 0;

      for (i = 0; i < slices; i++) {
        pokemonTemp2[i] = new Pokemon();
        brs[i] = new RandomAccessFile(tfile + Integer.toString(i) + ".db", "rw");
        file[i] = new File(tfile + Integer.toString(i) + ".db");
        valido = brs[i].readBoolean();// ler lapide -- se TRUE Pokemon existe , caso FALSE Pokemon apagado
        len = brs[i].readInt(); // ler tamanho do registro
        ba3 = new byte[len]; // cria um vetor de bytes com o tamanho do registro
        int x = brs[i].read(ba3); // Ler registro
        pokemonTemp2[i].fromByteArray(ba3); // Transforma vetor de bytes lido por instancia de Pokemon
        posIni = brs[i].getFilePointer();// Marca posição que acabou o registro e será iniciado outro
      }

      RandomAccessFile fw = new RandomAccessFile("dados/PokemonsOrdenados.db", "rw");
      posIni = 0;
      byte ba4[];

      long arrayDif[] = new long[slices];
      int teste = 0;
      for (i = 0; i < N; i++) {
        Pokemon min = pokemonTemp2[0];
        int minFile = 0;

        for (j = 0; j < slices; j++) {
          if (min.getNumber()>pokemonTemp2[j].getNumber()) {
            min = pokemonTemp2[j];
            minFile = j;
          }
        }

        ba4 = min.toByteArray();
         System.out.println(i + " " + min.getNumber() + " " + min.getName());
        fw.writeBoolean(true); // Escreve lápide
        fw.writeInt(ba4.length); // Escreve tamanho do vetor de bytes(Registro)
        fw.write(ba4); // Escreve registro

        long dif = brs[minFile].length() - brs[minFile].getFilePointer();
        arrayDif[minFile] = dif;


        if (arrayDif[minFile] == 0) {
          for (int k = 0; k < slices; k++) {
            dif = brs[k].length() - brs[k].getFilePointer();
            if (arrayDif[k] != 0) {
              System.out.println("ENTREI NO IF");
              brs[k].seek(brs[k].length() - arrayDif[k]);
              valido = brs[k].readBoolean();//ler lapide -- se TRUE Pokemon existe , caso FALSE Pokemon apagado
              len = brs[k].readInt(); // ler tamanho do registro
              ba3 = new byte[len]; // cria um vetor de bytes com o tamanho do registro
              brs[k].read(ba3); // Ler registro
              pokemonTemp3 = new Pokemon();
              pokemonTemp3.fromByteArray(ba3);

              break;
            }

          }
        } else {
          valido = brs[minFile].readBoolean();// ler lapide -- se TRUE Pokemon existe , caso FALSE Pokemon apagado
          len = brs[minFile].readInt(); // ler tamanho do registro
          ba3 = new byte[len]; // cria um vetor de bytes com o tamanho do registro
          brs[minFile].read(ba3); // Ler registro
          pokemonTemp3 = new Pokemon();
          pokemonTemp3.fromByteArray(ba3); // Transforma vetor de bytes lido por instancia de Pokemon
        }
        pokemonTemp2[minFile] = pokemonTemp3;

      }

      fw.close();
      for (i = 0; i < slices; i++) {
        file[i].deleteOnExit();
        brs[i].close();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Dados ordenados com sucesso - Intercalação comum");
  }





    
}
