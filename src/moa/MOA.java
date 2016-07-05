package moa;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author guest-GOTfqn
 */
class No {

    private int peso;

    No() {
        this.peso = 0;
    }

    int getPeso() {
        return peso;
    }

    void setPeso(int peso) {
        this.peso = peso;
    }

}

public class MOA {

    /**
     * @param args the command line arguments
     */
    public int[][] criarMatriz(int entrada) {
        int matrizInicial[][] = new int[4][4];
        
        int total = 0;
        for (int i = 0; i < matrizInicial.length; i++) {
            for (int j = 0; j < matrizInicial[0].length; j++) {
                matrizInicial[i][j] = Integer.parseInt(entradaSplit[total++]);
            }
        }
        return matrizInicial;
    }

    public int caclularPrimeiraHeuristicaMatriz(int[] configInicial, int[] configFim) {
        matrizInicial = this.criarMatriz(configInicial);
        matrizFinal = this.criarMatriz(configFim);
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4.length; j++) {
                matrizInicial[i][j] = matrizFinal[i][j];
            }
        }
    }

    public int calcularPrimeiraHeuristica(int[] configuracao_i, int[] configuracao_f) {
        int peças_fora_lugar = 0;
        for (int i = 0; i < configuracao_f.length; i++) {
            if (configuracao_i[i] == configuracao_i[i]) {
                peças_fora_lugar++;
            }
        }
        return peças_fora_lugar;
    }

    public int[] converterMatrizLinha(int[] vet) {
        int[] novoFormato = new int[vet.length];
        return novoFormato;
    }

    public int calcularSegundaHeuristica(int[] configuracao_f) {
        int peças_fora_sequencia = 0;
        for (int i = 0; i < configuracao_f.length - 1; i++) {
            if (configuracao_f[i] != 15 && configuracao_f[i] != 0) {
                if (configuracao_f[i + 1] != configuracao_f[i] + 1) {
                    peças_fora_sequencia++;
                }
            }
            if (configuracao_f[i] == 15 && configuracao_f[i + 1] != 0) {
                peças_fora_sequencia++;
            }
        }
        return peças_fora_sequencia;
    }

    protected void getPossibilidadesPermuta(int[][] matriz) {

    }

    public void algoritmoAEstrela() {

    }

    protected int getZeroPosition(int[] conf) {
        int i = 0;
        for (i = 0; i < conf.length; i++) {
            if (conf[i] == 0) {
                return i;
            }
        }
        return - 1;
    }

    public int[] getArrayPosTrocada(int zeroPos, int posTroca, int[] config) {
        int[] newConfig = config.clone();
        int aux = newConfig[posTroca];
        newConfig[posTroca] = newConfig[zeroPos];
        newConfig[zeroPos] = aux;

        return newConfig;
    }

    /**
     * Suponha Arry as Matrix[4][4]
     *
     * @param confInicial
     */
    private ArrayList getPossibilidadesPermuta(int[] confInicial) {
        int posTroca;
        int zeroPosition = this.getZeroPosition(confInicial);
        int maxPositionMatriz = confInicial.length;
        ArrayList<Integer> lastColRight = new ArrayList<Integer>();
        ArrayList lstPossibilidades = new ArrayList<>();
        lastColRight.addAll(Arrays.asList(3, 7, 11));

        //Pega o Elemento a direita
        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {
            lstPossibilidades.add(getArrayPosTrocada(zeroPosition,
                    posTroca, confInicial));
        }

        //Pega o Elemento a esquerda
        posTroca = zeroPosition - 1;
        if ((lastColRight.indexOf(posTroca) == -1)
                && (zeroPosition - 1 > 0)) {
            lstPossibilidades.add(getArrayPosTrocada(zeroPosition,
                    posTroca, confInicial));
        }

        //Pega o Elemento a cima
        posTroca = zeroPosition - 4;
        if (posTroca > 0) {
            lstPossibilidades.add(getArrayPosTrocada(zeroPosition,
                    posTroca, confInicial));
        }

        //Pega o Elemento a baixo
        posTroca = zeroPosition + 4;
        if (posTroca < maxPositionMatriz) {
            lstPossibilidades.add(getArrayPosTrocada(zeroPosition,
                    posTroca, confInicial));
        }
        return lstPossibilidades;
    }

    public static void main(String[] args) {

        MOA aEstrela = new MOA();

        int[] confFinal = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};
        int[] confInicial = {1, 2, 3, 4, 5, 13, 7, 8, 9, 10, 11, 12, 6, 14, 15, 0};
        //String entrada = "2 3 4 5 13 14 8 9 10 11 12 1 15 0 7 6";
        //int[][] matriz = aEstrela.criarMatriz(entrada);
        aEstrela.getPossibilidadesPermuta(confInicial);

        //System.out.println(a_estrela.calcularPrimeiraHeuristica(conf_inicial, conf_final));
        //System.out.println(a_estrela.calcularSegundaHeuristica(conf_inicial));

        /*Scanner input = new Scanner(System.in);
         System.out.println("Digite sua string de entrada: ");
         String entrada = input.nextLine();
         System.out.println("\n" + entrada);*/
    }

}
