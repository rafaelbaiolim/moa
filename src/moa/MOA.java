package moa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import static jdk.nashorn.internal.objects.NativeArray.map;

/**
 *
 * @author Rafael Altar
 */
class Estado {

    static int custoFilho = 0;
    public int custo = 0;
    public int[] elementos;
    public int hS;
    public int fn;
    Estado pai = null;

    Estado(int[] elementos, Estado pai, int custo) {
        this.custo = custo;
        this.elementos = elementos;
        this.pai = pai;
    }

    Estado(int[] elementos) {
        this.elementos = elementos;
    }

    Estado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public int calcularSegundaHeuristicaMatriz(int[] configInicial, int[] configFim) {
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
    private ArrayList<Estado> getPossibilidadesPermuta(Estado estado) {
        int posTroca;
        int[] confInicial = estado.getElementos();
        int zeroPosition = this.getZeroPosition(confInicial);
        int maxPositionMatriz = confInicial.length;

        ArrayList<Estado> lstFilhos = new ArrayList<Estado>();
        ArrayList<Integer> lastColRight = new ArrayList<Integer>();
        lastColRight.addAll(Arrays.asList(3, 7, 11));

        //Pega o Elemento a direita
        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {

            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado, estado.custo + 1);
            lstFilhos.add(est);

        }

        //Pega o Elemento a esquerda
        posTroca = zeroPosition - 1;
        if ((lastColRight.indexOf(posTroca) == -1)
                && (zeroPosition - 1 > 0)) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado, estado.custo + 1);
            lstFilhos.add(est);

        }

        //Pega o Elemento a cima
        posTroca = zeroPosition - 4;
        if (posTroca > 0) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado, estado.custo + 1);
            lstFilhos.add(est);

        }

        //Pega o Elemento a baixo
        posTroca = zeroPosition + 4;
        if (posTroca < maxPositionMatriz) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado, estado.custo + 1);
            lstFilhos.add(est);
        }
        return lstFilhos;
    }

    // pega o estado de menor fn
    public Estado getMinValue(HashMap<int[], Estado> array) {
        Estado est = new Estado();
        Entry<int[], Estado> min = null;
        for (Entry<int[], Estado> entry : array.entrySet()) {
            if (min == null || min.getValue().fn > entry.getValue().fn) {
                min = entry;
            }
        }

        /*for (int i = 1; i < array.size(); i++) {
            if (array.get(i).fn < minValue.fn) {
                minValue = array.get(i);
            }
        }
        return minValue; */
        return min.getValue();
    }

    public boolean isStateEqual(int[] m, int[] configFinal) {
        HashMap<String, int[]> comparator = new HashMap<>();
        String stringM = Arrays.toString(m);
        comparator.put(Arrays.toString(configFinal), configFinal);

        if (comparator.get(stringM) != null) {
            return true;
        }
        return false;
    }

    public void algoritmoAEstrela() {
        HashMap<int[], Estado> cnjtA = new HashMap<>();
        HashMap<int[], Estado> cnjtF = new HashMap<>();
        //HashMap cnjtS = new HashMap<>();
        HashMap<int[], Estado> cnjtT = new HashMap<>();
        HashMap<int[], Estado> cnjtP = new HashMap<>();
        ArrayList<Estado> cnjtSuss = new ArrayList<>();

        int[] confFinal = {1, 12, 11, 10, 2, 13, 0, 9, 3, 14, 15, 8, 4, 5, 6, 7};

        int[] confInicial = {1, 12, 11, 10, 0, 13, 15, 9, 2, 14, 6, 8, 3, 4, 5, 7};

        Estado estadoInicial = new Estado(confInicial);
        //Segunda , mas é a primeira
        estadoInicial.hS = calcularSegundaHeuristicaMatriz(estadoInicial.getElementos(), confFinal);
        estadoInicial.fn = estadoInicial.getCusto() + estadoInicial.hS;

        //estadoInicial = this.getPossibilidadesPermuta(estadoInicial);
        cnjtA.put(confInicial, estadoInicial);
        Estado m = estadoInicial;
        int breaker = 0;
        while (m != null && !isStateEqual(m.getElementos(), confFinal)) {

            if (isStateEqual(m.getElementos(), confFinal)) {
                break;
            }

            Estado est = cnjtA.get(m.getElementos());
            cnjtA.remove(est.getElementos());
            cnjtF.put(est.getElementos(), est);
            cnjtSuss = this.getPossibilidadesPermuta(m);

            for (Estado filho : cnjtSuss) {
                for (int i = 0; i < cnjtA.size(); i++) {
                    if (isStateEqual(filho.getElementos(), cnjtA.get(i).getElementos()) && filho.custo < cnjtA.get(i).custo) {
                        cnjtA.remove(cnjtA.get(i));
                    }
                }

                if ((cnjtF.get(filho.getElementos()) == null) && (cnjtF.get(filho.getElementos()) == null)) {
                    filho.hS = calcularSegundaHeuristicaMatriz(filho.getElementos(), confFinal);
                    //System.out.println(filho.custo);
                    filho.fn = filho.custo + filho.hS;

                    cnjtA.put(filho.getElementos(), filho);
                }
            }
            m = getMinValue(cnjtA);
            breaker++;
            Utils.printArray(m.getElementos());
            if (breaker == 2) {
                return;

            }
        }
    }

    public static void main(String[] args) {
        MOA moa = new MOA();
        moa.algoritmoAEstrela();
    }
}
