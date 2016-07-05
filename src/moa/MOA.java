package moa;

import java.util.ArrayList;
import java.util.Arrays;
import moa.Utils;

/**
 *
 * @author Rafael Altar
 */
class Estado {

    static int custoFilho = 0;
    protected int custo = 0;
    protected int[] elementos;
    protected int hS;
    protected ArrayList<Estado> filhos;
    Estado pai = null;

    public int gethS() {
        return hS;
    }

    public void sethS(int hS) {
        this.hS = hS;
    }

    Estado(int[] elementos) {
        this.elementos = elementos;
        this.filhos = new ArrayList<>();
    }

    void setNewFilho(Estado filho) {
        this.custo += 1;
        filho.setCusto(this.custo);
        this.filhos.add(filho);
    }

    void setPai(Estado pai) {
        this.pai = pai;
    }

    ArrayList<Estado> getFilhos() {
        return this.filhos;
    }

    int[] getElementos() {
        return this.elementos;
    }

    int getCusto() {
        return custo;
    }

    void setCusto(int custo) {
        this.custo = custo;
    }

}

public class MOA {

    /**
     * @param args the command line arguments
     */
    public int[][] criarMatriz(int[] entrada) {
        int matrizInicial[][] = new int[4][4];

        int total = 0;
        for (int i = 0; i < matrizInicial.length; i++) {
            for (int j = 0; j < matrizInicial[0].length; j++) {
                matrizInicial[i][j] = entrada[total++];
            }
        }
        return matrizInicial;
    }

    public int caclularPrimeiraHeuristicaMatriz(int[] configInicial, int[] configFim) {
        int[][] matrizInicial = this.criarMatriz(configInicial);
        int[][] matrizFinal = this.criarMatriz(configFim);

        int pecasFora = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (matrizInicial[i][j] != matrizFinal[i][j]) {
                    pecasFora++;
                }
            }
        }
        return pecasFora;
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

    protected int getZeroPosition(int[] conf) {
        int i = 0;
        for (i = 0; i < conf.length; i++) {
            if (conf[i] == 0) {
                return i;
            }
        }
        return - 1;
    }

    public int[] getArrayPosTroca(int zeroPos, int posTroca, int[] config) {
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
    private Estado getPossibilidadesPermuta(Estado estado) {
        int posTroca;
        int[] confInicial = estado.getElementos();

        int zeroPosition = this.getZeroPosition(confInicial);
        int maxPositionMatriz = confInicial.length;

        ArrayList<Integer> lastColRight = new ArrayList<Integer>();
        lastColRight.addAll(Arrays.asList(3, 7, 11));

        //Pega o Elemento a direita
        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {
            estado.setNewFilho(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial)));
        }

        //Pega o Elemento a esquerda
        posTroca = zeroPosition - 1;
        if ((lastColRight.indexOf(posTroca) == -1)
                && (zeroPosition - 1 > 0)) {
            estado.setNewFilho(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial)));
        }

        //Pega o Elemento a cima
        posTroca = zeroPosition - 4;
        if (posTroca > 0) {
            estado.setNewFilho(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial)));
        }

        //Pega o Elemento a baixo
        posTroca = zeroPosition + 4;
        if (posTroca < maxPositionMatriz) {
            estado.setNewFilho(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial)));
        }
        return estado;
    }

    public void algoritmoAEstrela() {
        ArrayList cnjtA = new ArrayList<>();
        ArrayList cnjtF = new ArrayList<>();
        ArrayList cnjtS = new ArrayList<>();
        ArrayList cnjtT = new ArrayList<>();
        ArrayList cnjtP = new ArrayList<>();

        int[] confFinal = {1, 12, 11, 10, 2, 13, 0, 9, 3, 14, 15, 8, 4, 5, 6, 7};
        int[] confInicial = {1, 2, 3, 4, 5, 13, 7, 8, 9, 10, 11, 12, 6, 14, 15, 0};

        Estado estadoInicial = new Estado(confInicial);
        estadoInicial = this.getPossibilidadesPermuta(estadoInicial);
        cnjtS.add(estadoInicial);

        ArrayList<Integer> vetResults = new ArrayList<>();
        for (Estado filho : estadoInicial.getFilhos()) {
            vetResults.add(this.caclularPrimeiraHeuristicaMatriz(filho.getElementos(), confFinal));
        }

    }

    public static void main(String[] args) {

        MOA moa = new MOA();
        moa.algoritmoAEstrela();

    }

}
