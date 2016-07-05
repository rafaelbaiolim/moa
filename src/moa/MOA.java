package moa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Rafael Altar
 */
class Estado {

    static int custoFilho = 0;
    protected int custo = 0;
    protected int[] elementos;
    protected int hS;
    protected int fn;
    Estado pai = null;

    Estado(int[] elementos, Estado pai) {
        custoFilho++;
        this.elementos = elementos;
        this.pai = pai;
    }

    Estado(int[] elementos) {
        this.elementos = elementos;
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

    public int caclularSegundaHeuristicaMatriz(int[] configInicial, int[] configFim) {
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
            lstFilhos.add(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado));
        }

        //Pega o Elemento a esquerda
        posTroca = zeroPosition - 1;
        if ((lastColRight.indexOf(posTroca) == -1)
                && (zeroPosition - 1 > 0)) {
            lstFilhos.add(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado));
        }

        //Pega o Elemento a cima
        posTroca = zeroPosition - 4;
        if (posTroca > 0) {
            lstFilhos.add(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado));
        }

        //Pega o Elemento a baixo
        posTroca = zeroPosition + 4;
        if (posTroca < maxPositionMatriz) {
            lstFilhos.add(new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, confInicial), estado));
        }
        return lstFilhos;
    }

    // pega o estado de menor fn
    public Estado getMinValue(ArrayList<Estado> array) {
        Estado minValue = array.get(0);
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i).fn < minValue.fn) {
                minValue = array.get(i);
            }
        }
        return minValue;
    }

    public boolean isEqualFinal(int[] m, int[] configFinal) {
        HashMap<String, int[]> comparator = new HashMap<>();
        String stringM = Arrays.toString(m);
        comparator.put(Arrays.toString(m), m);
        comparator.put(Arrays.toString(configFinal), m);
        if (comparator.get(stringM) != null) {
            return true;
        }
        return false;
    }

    public void algoritmoAEstrela() {
        ArrayList<Estado> cnjtA = new ArrayList<Estado>();
        ArrayList cnjtF = new ArrayList<>();
        //ArrayList cnjtS = new ArrayList<>();
        ArrayList cnjtT = new ArrayList<>();
        ArrayList cnjtP = new ArrayList<>();
        ArrayList cnjtSuss = new ArrayList<>();

        int[] confFinal = {1, 12, 11, 10, 2, 13, 0, 9, 3, 14, 15, 8, 4, 5, 6, 7};
        int[] confInicial = {1, 2, 3, 4, 5, 13, 7, 8, 9, 10, 11, 12, 6, 14, 15, 0};

        Estado estadoInicial = new Estado(confInicial);
        estadoInicial.hS = caclularSegundaHeuristicaMatriz(estadoInicial.getElementos(), confInicial);
        estadoInicial.fn = estadoInicial.getCusto() + estadoInicial.hS;
        //estadoInicial = this.getPossibilidadesPermuta(estadoInicial);
        cnjtA.add(estadoInicial);
        Estado m = getMinValue(cnjtA);
        while (m != null && !isEqualFinal(m, confFinal)) {

            cnjtSuss = this.getPossibilidadesPermuta(m);
            for (Estado filho : cnjtSuss) {
                if (filho == cnjA.elem && filho.custo < cnjA.elem) {
                    removo filho de A
                }
                if (filho 
                    
                    
                    
                    
                    
                    notIn(conjA) && filho notIn(conjF)
                
                
                
                
                
                
                    ){
                    add M em abertos
                }
            }
            // atualiza m

        }

    }

}

public static void main(String[] args) {

        MOA moa = new MOA();
        moa.algoritmoAEstrela();

    }

    private boolean isEqualFinal(Estado m) {
        return false;
    }

}
