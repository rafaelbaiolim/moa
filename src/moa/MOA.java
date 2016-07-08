package moa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

public class MOA {

    static final String CONFIG_FINAL = "1 12 11 10 2 13 0 9 3 14 15 8 4 5 6 7";
    static int[] conf_inicial = {11, 12, 13, 14, 15, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    static int stcMovimentos = 0;

    class Estado {

        public int custo = 0;
        public String elementos;
        public int hS;
        public int fn;
        Estado pai = null;

        Estado(String elementos, Estado pai, int custo) {
            this.custo = custo;
            this.elementos = elementos;
            this.pai = pai;
        }

        Estado(String elementos) {
            this.elementos = elementos;
        }
    }

    public int getZeroPosition(String[] arrStr) {
        int i = 0;
        for (i = 0; i < arrStr.length; i++) {
            if (Integer.parseInt(arrStr[i]) == 0) {
                return i;
            }
        }
        return - 1;
    }

    public String getArrayPosTroca(int zeroPos, int posTroca, String[] config) {
        String[] newConfig = config.clone();
        String aux = newConfig[posTroca];
        newConfig[posTroca] = newConfig[zeroPos];
        newConfig[zeroPos] = aux;
        String ret = "";
        for (int i = 0; i < newConfig.length; i++) {
            ret += newConfig[i] + " ";
        }
        return ret.trim();
    }

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

    /**
     * Suponha Arry as Matrix[4][4]
     *
     * @param confInicial
     */
    private ArrayList<Estado> getPossibilidadesPermuta(Estado estado) {
        int posTroca;
        String[] arrConfStr = estado.elementos.split(" ");
        int zeroPosition = this.getZeroPosition(arrConfStr);
        int maxPositionMatriz = arrConfStr.length - 1;
        int custoAtual = estado.custo + 1;

        ArrayList<Estado> lstFilhos = new ArrayList<Estado>();
        ArrayList<Integer> lastColRight = new ArrayList<Integer>();
        lastColRight.addAll(Arrays.asList(3, 7, 11, 15));

        //Pega o Elemento a direita
        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);
        }

        //Pega o Elemento a esquerda
        posTroca = zeroPosition - 1;
        if ((lastColRight.indexOf(posTroca) == -1)) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);
        }

        //Pega o Elemento a cima
        posTroca = zeroPosition - 4;
        if (posTroca >= 0) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);

        }

        //Pega o Elemento a baixo
        posTroca = zeroPosition + 4;
        if (posTroca <= maxPositionMatriz) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);
        }
        return lstFilhos;
    }

    public int calcularPrimeiraHeuristica(String configuracao_i, String configuracao_f) {

        int out = 0;
        String[] a = configuracao_i.split(" ");
        String[] b = configuracao_f.split(" ");

        for (int i = 0; i < b.length - 1; i++) {
            if (Integer.parseInt(a[i]) != Integer.parseInt(b[i])) {
                out++;
            }
        }

        return out;
    }

    public int[] criaVetor(String entrada) {
        String[] entrada2 = entrada.split(" ");
        int[] valorInt = new int[entrada2.length];

        for (int i = 0; i < entrada2.length; i++) {
            valorInt[i] = Integer.parseInt(entrada2[i]);
        }
        return valorInt;
    }

    // pega o estado de menor fn
    public Estado getMinValue(HashMap<String, Estado> array) {
        Entry<String, Estado> min = null;
        for (Entry<String, Estado> entry : array.entrySet()) {
            if (min == null || min.getValue().fn > entry.getValue().fn) {
                min = entry;
            }
        }
        return min.getValue();
    }

    private void algoritmoAEstrela(String configInicial) {
        HashMap<String, Estado> cnjtA = new HashMap<>();  //Abertos 
        HashMap<String, Estado> cnjtF = new HashMap<>();  //Fechados
        ArrayList<Estado> cnjtSuss = new ArrayList<>();   //Filhos 
        Estado m = new Estado(configInicial);
        m.hS = calcularPrimeiraHeuristica(m.elementos, CONFIG_FINAL);
        int contador = 0;
        while ((m != null) && (!m.elementos.equals(CONFIG_FINAL))) {

            cnjtF.put(m.elementos, m);
            cnjtA.remove(m.elementos);

            cnjtSuss = getPossibilidadesPermuta(m);

            for (Estado filho : cnjtSuss) {
                Estado cnjAEst = cnjtA.get(filho.elementos);
                Estado cnjFEst = cnjtF.get(filho.elementos);

                if (cnjAEst != null && filho.custo < cnjAEst.custo) {
                    cnjtA.remove(filho.elementos);
                }

                if (cnjAEst == null && cnjFEst == null) {
                    filho.hS = calcularPrimeiraHeuristica(filho.elementos, CONFIG_FINAL);
                    //System.out.println(filho.custo);
                    filho.fn = filho.custo + filho.hS;
                    cnjtA.put(filho.elementos, filho);
                }
            }
            m = getMinValue(cnjtA);
            System.out.println("Jogada " + contador++ + " : " + m.elementos);

        }
    }

    public static void main(String[] args) {
        MOA moa = new MOA();

        String caso6Mov = "1 12 11 10 0 13 15 9 2 14 6 8 3 4 5 7";
        String caso7Mov = "1 12 0 11 2 13 15 10 3 14 6 9 4 5 7 8";
        String caso11Mov = "12 11 10 9 1 13 15 0 2 14 6 8 3 4 5 7";
        String caso15Mov = "2 1 12 11 3 0 15 10 4 13 6 9 5 14 7 8";
        String caso20Mov = "2 1 12 11 3 15 6 10 4 0 7 9 5 13 14 8";

        moa.algoritmoAEstrela(caso7Mov);
    }
}
