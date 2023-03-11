import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;

public class Intercalações {
  
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

        public static void distribuicao (int qntRegistros, int qntCaminhos, RandomAccessFile rasDB, RandomAccessFile [] ras) throws Exception {

          // pular o marcador de ultimo id
             rasDB.readInt();
 
          // controlar a chegada do fim do arquivo
             long controle = 4;
 
          // controlar a escrita do arquivo
             int qntRegistrosReal = 0;
 
          // repetir enquanto nao chegar no fim do arquivo
             while( controle < rasDB.length() ) {
              // para salvar o tamanho dos registros
                 int tam = 0;
              // array de pokemons comd tamanho igual a quantidade de registros que vao ser lidos
                 Pokemon [] pokemons = new Pokemon [qntRegistros];
 
              // repeticao para mudar o arquivo temp a ser escrito
                 for (int x = 0; x < qntCaminhos; x++) {
                     qntRegistrosReal = 0;
 
                  // repeticao para gravar pokemons no arranjo de pokemons
                     for (int i = 0; controle < rasDB.length() && i < qntRegistros; i++, controle += tam + 5) {
                         if (rasDB.readBoolean() == false) { // se for lapide pular registro
                             tam = rasDB.readInt();
                             rasDB.seek(rasDB.getFilePointer() + tam);
                             i--;
                         } else {
                             tam = rasDB.readInt();
                             byte [] ba = new byte [tam];
                             rasDB.read(ba);
                             
                          // pegar qntRegistro do banco de dados e salvar no array pokemon
                             pokemons[i] = new Pokemon(ba);
                             qntRegistrosReal++;
                         } // end if
                     } // end for
 
                  // quicksort do array de pokemons
                     quicksort (pokemons);
 
                  // repeticao para gravar registros em arquivo
                     for (int j = 0; j < qntRegistrosReal; j++) {
                         byte [] ba = pokemons[j].toByteArray();
                         // System.out.println("ESCREVENDO NO ARQUIVO " + x + " " + pokemons[j].get_title());
                         ras[x].writeInt(ba.length);
                         ras[x].write(ba);
                     } // end for
                 } // end for
             } // end for
     } // end distribuicao ()
    
     public static int getMenor (Pokemon [] pokemons) {
      int temp = 0;
      for (int i = 1; i < pokemons.length; i++){
          if(pokemons[temp] != null){
              if(pokemons[temp].getNumber() > pokemons[i].getNumber()){
                  temp = i;
              } // end if
          }else{
              temp++;
          } // end if
      } // end for
      return temp;
  } // end getMenor ()

  public static void intercalacoes ( int qntRegistros, int qntCaminhos, RandomAccessFile [] ras) throws Exception {
    /* 
    Tam do arquivo0 = 8
    Tam do arquivo1 = 6
    quantRegistros = 4
    quantCaminhos = 2
    Quantidade de arquivos = 4

    Os registros vao estar escrito nos 2 primeiros arquivos, 
    1passo- vamos ter que pegar o primeiro registro em cada um dos 2 primeiros arquivos
    2passo- pegar um registro do arquivo0, pegar um registro do arquivo1, salvar os 2 num Pokemon [].
    3passo- selecionar o menor e salvar em um arquivo2
    4passo- atualizar Pokemon[arquivoComMenor] com o proximo registro do arquivoComMenor.

    */

    for (int i = 0; i < ras.length; i++) {
        ras[i].seek(0);
    } // end for

    int tam = -1;
    Pokemon [] pokemons = new Pokemon [qntCaminhos];

    for (int y = 0; y < qntRegistros/qntCaminhos; y++) {
        for (int x = 0; x < qntCaminhos; x++) {
            tam = ras[x].readInt();
            byte [] ba = new byte[tam];
            ras[x].read(ba);
            pokemons[x] = new Pokemon(ba);
        } // end for

        for (int i = 0; i < pokemons.length; i++) {
            int arquivoComMenor = getMenor(pokemons);
            byte [] ba = pokemons[arquivoComMenor].toByteArray();
            //System.out.println("ESCREVENDO NO ARQUIVO " +  + " " + pokemons[i].get_title());
            ras[5].writeInt(ba.length);
            ras[5].write(ba);
            pokemons[arquivoComMenor] = null;
        } // end for
    } // end for

} // end intercalacoes ()


public static void iBComum ( int qntRegistros, int qntCaminhos ) throws Exception {
  RandomAccessFile rasDB = new RandomAccessFile("tmp/pokemons.db", "rw");
  RandomAccessFile [] ras = new RandomAccessFile [qntCaminhos * 2];
  
// criacao dos arquivos temporarios
  File [] tempF = new File [qntCaminhos * 2];
  for (int i = 0; i < qntCaminhos * 2; i++) {
      ras[i] = new RandomAccessFile("Arqtemp"+ i + ".tmp", "rw");
      tempF [i] = new File("Arqtemp" + i + ".tmp");
  } // end for

  distribuicao(qntRegistros, qntCaminhos, rasDB, ras);
  intercalacoes(qntRegistros, qntCaminhos, ras);

// debug
  
  for (int i = 0; i < ras.length; i++) {
      
      ras[i].seek(0);
      System.out.println("\nArqtemp" + i);
      while (true) {
          int tam = 0;
          try {
              tam = ras[i].readInt();
          } catch (Exception e) {
              break;
          } // end try
          byte [] ba = new byte [tam];
          ras[i].read(ba);
          Pokemon temp = new Pokemon(ba);
          System.out.println(temp.getNumber() + " " + temp.getName());
      } // end while
  } // end for

  for (int i = 0; i < ras.length; i++)  ras[i].close();
  for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
  rasDB.close();
} // end iBComum ()

}