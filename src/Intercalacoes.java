
import java.io.EOFException;
import java.io.File;

import java.io.RandomAccessFile;

public class Intercalacoes {



//• Estratégia geral dos métodos de ordenação externa:
//1. Quebre o arquivo em blocos do tamanho da memória interna disponível.
//2. Ordene cada bloco na memória interna.
//3. Intercale os blocos ordenados, fazendo várias passadas sobre o arquivo.
//4. A cada passada são criados blocos ordenados cada vez maiores, até que todo o arquivo esteja ordenado.



    //Método de ordenação de um Array de pokemons, metodo de Seleção;
    public static Pokemon[] ordenar(Pokemon[] ordenados) {
        int menor = 0;
    
        for (int a = 1; a < ordenados.length - 1; a++) {
            menor = a;
            for (int b = a + 1; b < ordenados.length; b++) {
                if (ordenados[b] != null && ordenados[menor] != null) {
                    if (ordenados[b].getNumber() < ordenados[menor].getNumber()) {
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

public static void distribuicao(int QntReg, int QntCam, RandomAccessFile rasDB, RandomAccessFile[] ras) throws Exception {
    // Saltar o número de registros no banco de dados (não é usado)
    rasDB.readInt();
    // Variável para controlar o fim do arquivo
    long fim = 4;
    // Variável para controlar a quantidade real de registros escritos
    int qntRegistrosReal = 0;
    // Continuar enquanto não alcançar o final do arquivo
    while (fim < rasDB.length()) {
    // Variável para armazenar o tamanho de cada registro
    int tam = 0;
    // Array de Pokémon com tamanho igual à quantidade de registros a serem lidos
    Pokemon[] pokemons = new Pokemon[QntReg];
    // Loop para alternar o arquivo temporário de gravação
    for (int x = 0; x < QntCam; x++) {
    qntRegistrosReal = 0;
    // Loop para gravar Pokémon no array
    for (int i = 0; fim < rasDB.length() && i < QntReg; i++, fim += tam + 5) {
    // Verificar se o registro existe ou não
    if (!rasDB.readBoolean()) {
    tam = rasDB.readInt();
    rasDB.seek(rasDB.getFilePointer() + tam);
    i--;
    } else {
    tam = rasDB.readInt();
    byte[] ba = new byte[tam];
    rasDB.read(ba);
    // Salvar o número de registros do banco de dados no array Pokémon
    pokemons[i] = new Pokemon(ba);
    qntRegistrosReal++;
    }
    }
    // 2ª etapa
    // Ordenar o array de Pokémon
    ordenar(pokemons);
    // Loop para gravar registros em arquivo
    for (int j = 0; j < qntRegistrosReal; j++) {
    byte[] ba = pokemons[j].toByteArray();
    ras[x].writeInt(ba.length);
    ras[x].write(ba);
    }
    }
    }
    }
    public static int getMenor(Pokemon[] pokemons) {
        int menorIndex = -1;
        for (int i = 0; i < pokemons.length; i++) {
            if (pokemons[i] != null && (menorIndex == -1 || pokemons[i].getNumber() < pokemons[menorIndex].getNumber())) {
                menorIndex = i;
            }
        }
        return menorIndex;
    }
    
// 3 Passo
public static void intercalacoes(int QntReg, int QntCam, RandomAccessFile[] ras) throws Exception {

    // Posiciona o ponteiro de leitura de cada arquivo no início
    for (int i = 0; i < ras.length; i++) {
        ras[i].seek(0);
    }

    int tam = -1;
    Pokemon[] pokemons = new Pokemon[QntCam];

    // Executa a intercalação para cada bloco de registros
    for (int y = 0; y < QntReg / QntCam; y++) {
        // Lê um registro de cada arquivo e armazena no array de Pokemons
        for (int x = 0; x < QntCam; x++) {
            tam = ras[x].readInt();
            byte[] ba = new byte[tam];
            ras[x].read(ba);
            pokemons[x] = new Pokemon(ba);
        }

        // Escreve no arquivo temporário os registros em ordem crescente de chave
        for (int i = 0; i < pokemons.length; i++) {
            int arquivoComMenor = getMenor(pokemons);
            byte[] ba = pokemons[arquivoComMenor].toByteArray();
            ras[5].writeInt(ba.length);
            ras[5].write(ba);
            pokemons[arquivoComMenor] = null;
        }
    }
}



public static void iBComum ( int QntReg, int QntCam ) throws Exception {
  RandomAccessFile rasDB = new RandomAccessFile("tmp/pokemons.db", "rw");
  RandomAccessFile [] ras = new RandomAccessFile [QntCam * 2];
  
 // criação dos arquivos temporários
 File[] tempF = new File[QntCam * 2];
 for (int i = 0; i < QntCam * 2; i++) {
     String fileName = "Arqtemp" + i + ".tmp";
     ras[i] = new RandomAccessFile(fileName, "rw");
     tempF[i] = new File(fileName);
 }

  distribuicao(QntReg, QntCam, rasDB, ras);
  intercalacoes(QntReg, QntCam, ras);

  // 4 Passo
for (int i = 0; i < ras.length; i++) {
    ras[i].seek(0);
    System.out.println("\nArqtemp" + i);
    while (true) {
    int tam = 0;
    try {
    tam = ras[i].readInt();
    byte[] ba = new byte[tam];
    ras[i].read(ba);
    Pokemon temp = new Pokemon(ba);
    System.out.println(temp.getNumber() + " " + temp.getName());
    } catch (EOFException e) {
    break;
    }
    }
    }

  for (int i = 0; i < ras.length; i++)  ras[i].close();
  for (int i = 0; i < tempF.length; i++) tempF[i].delete(); 
  rasDB.close();
} 

}