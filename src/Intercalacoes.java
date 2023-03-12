
import java.io.File;

import java.io.RandomAccessFile;

public class Intercalacoes {



//• Estratégia geral dos métodos de ordenação externa:
//1. Quebre o arquivo em blocos do tamanho da memória interna disponível.
//2. Ordene cada bloco na memória interna.
//3. Intercale os blocos ordenados, fazendo várias passadas sobre o arquivo.
//4. A cada passada são criados blocos ordenados cada vez maiores, até que todo o arquivo esteja ordenado.



    //Método de ordenação de um Array de pokemons, metodo de Seleção;
    public static Pokemon[] ordenar(Pokemon[] ordenados){
        int menor = 0;

        for(int a = 1; a<ordenados.length - 1; a++){
            menor = a;
            for(int b = a+1; b<ordenados.length; b++){
                if(ordenados[b] != null && ordenados[menor] != null){
                    if(ordenados[b].getNumber() < ordenados[menor].getNumber()){
                        menor = b;
                    }
                }
            }
            
            Pokemon temp = ordenados[menor];
            ordenados[menor] = ordenados[a];
            ordenados[a] = temp;
            
        }

        return ordenados;
    }
    
// 1 Passo:

        public static void distribuicao (int QntReg, int QntCam, RandomAccessFile rasDB, RandomAccessFile [] ras) throws Exception {

          //Numero de Registros do banco de dados(pulamos ele)
             rasDB.readInt();
          // Variavel para definir o fim do final do arquivo
             long fim = 4;
          // controlar a escrita do arquivo,(definição do numero de arquivos que serão lidos em cada arquivo temp)
             int qntRegistrosReal = 0;
          // repetir enquanto nao chegar no fim do arquivo
             while( fim < rasDB.length() ) {
              // para salvar o tamanho dos registros
                 int tam = 0;
              // array de pokemons comd tamanho igual a quantidade de registros que vao ser lidos
                 Pokemon [] pokemons = new Pokemon [QntReg];
              // repeticao para mudar o arquivo temp a ser escrito
                 for (int x = 0; x < QntCam; x++) {
                     qntRegistrosReal = 0;
                  // gravação de pokemons no arranjo de pokemons
                     for (int i = 0; fim < rasDB.length() && i < QntReg; i++, fim += tam + 5) {
                         if (rasDB.readBoolean() == false) { // se lapide == false, registro não existe, portanto pulamos ele
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
                         } 
                     } 
                     //2 Passo
                  // sort do array de pokemons
                     ordenar(pokemons);
 
                  // repeticao para gravar registros em arquivo
                     for (int j = 0; j < qntRegistrosReal; j++) {
                         byte [] ba = pokemons[j].toByteArray();
                         ras[x].writeInt(ba.length);
                         ras[x].write(ba);
                     } 
                 } 
             } 
     } 
     //Método para definir o menor pokemon em um array de pokemon
     public static int getMenor (Pokemon [] pokemons) {
      int temp = 0;
      for (int i = 1; i < pokemons.length; i++){
          if(pokemons[temp] != null){
              if(pokemons[temp].getNumber() > pokemons[i].getNumber()){
                  temp = i;
              } 
          }else{
              temp++;
          } 
      } 
      return temp;
  } 
// 3 Passo
  public static void intercalacoes ( int QntReg, int QntCam, RandomAccessFile [] ras) throws Exception {
   
    for (int i = 0; i < ras.length; i++) {
        ras[i].seek(0);
    } 

    int tam = -1;
    Pokemon [] pokemons = new Pokemon [QntCam];

    for (int y = 0; y < QntReg/QntCam; y++) {
        for (int x = 0; x < QntCam; x++) {
            tam = ras[x].readInt();
            byte [] ba = new byte[tam];
            ras[x].read(ba);
            pokemons[x] = new Pokemon(ba);
        } 

        for (int i = 0; i < pokemons.length; i++) {
            int arquivoComMenor = getMenor(pokemons);
            byte [] ba = pokemons[arquivoComMenor].toByteArray();
            ras[5].writeInt(ba.length);
            ras[5].write(ba);
            pokemons[arquivoComMenor] = null;
        } 
    } 

} 


public static void iBComum ( int QntReg, int QntCam ) throws Exception {
  RandomAccessFile rasDB = new RandomAccessFile("tmp/pokemons.db", "rw");
  RandomAccessFile [] ras = new RandomAccessFile [QntCam * 2];
  
// criacao dos arquivos temporarios
  File [] tempF = new File [QntCam * 2];
  for (int i = 0; i < QntCam * 2; i++) {
      ras[i] = new RandomAccessFile("Arqtemp"+ i + ".tmp", "rw");
      tempF [i] = new File("Arqtemp" + i + ".tmp");
  } 

  distribuicao(QntReg, QntCam, rasDB, ras);
  intercalacoes(QntReg, QntCam, ras);

  //4 Passo
  for (int i = 0; i < ras.length; i++) {
      
      ras[i].seek(0);
      System.out.println("\nArqtemp" + i);
      while (true) {
          int tam = 0;
          try {
              tam = ras[i].readInt();
          } catch (Exception e) {
              break;
          } 
          byte [] ba = new byte [tam];
          ras[i].read(ba);
          Pokemon temp = new Pokemon(ba);
          System.out.println(temp.getNumber() + " " + temp.getName());
      } 
  } 

  for (int i = 0; i < ras.length; i++)  ras[i].close();
  for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
  rasDB.close();
} 

}