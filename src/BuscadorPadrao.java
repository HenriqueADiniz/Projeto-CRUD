public class BuscadorPadrao {
    BuscadorPadrao() {
    }

    // monta o array de falhas
    static int[] prefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];
        for (int i = 1; i < n; ++i) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = pi[j - 1];
            if (s.charAt(i) == s.charAt(j))
                ++j;
            pi[i] = j;
        }
        return pi;
    }

    // executa o algoritmo de kmp
    static int executaKMP(String s, String t) {
        int n = s.length();
        int m = t.length();
        int[] pi = prefixFunction(t);
        int j = 0;
        int operacoes = 0;
        for (int i = 0; i < n; ++i) {
            operacoes = operacoes + 2;
            while (j > 0 && s.charAt(i) != t.charAt(j)) {
                j = pi[j - 1];
                operacoes = operacoes + 3;
            }
            if (s.charAt(i) == t.charAt(j)) {
                ++j;
                operacoes = operacoes + 2;
            }
            if (j == m) {
                operacoes = operacoes + 2;
                System.out.println("Numero de operacoes: " + operacoes);
                System.out.println("Padrao encontrado na posicao: " + (i - m + 1));
                return i - m + 1;
            }
        }
        System.out.println("Numero de operacoes: " + operacoes);
        return -1;
    }

    public static int forcaBruta(String pat, String txt) {
        int j, M = pat.length();
        int i, N = txt.length();
        int operacoes = 0;

        for (i = 0; i <= N - M; i++) {
            operacoes = operacoes+2;
            for (j = 0; j < M; j++) {
                operacoes = operacoes+2;
                if (txt.charAt(i + j) != pat.charAt(j)) {
                    operacoes=operacoes+2;
                    break;
                }
            }
            if (j == M) {
                operacoes++;
                System.out.println("Padrao encontrado na posicao: "+i);
                System.out.println("Numero de operacoes:"+operacoes);
                return i;
            }
        }
        System.out.println("Numero de operacoes:"+operacoes);
        return -1;
    }
}
