package moa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

class Moa {

    static final String CONFIG_FINAL = "1 12 11 10 2 13 0 9 3 14 15 8 4 5 6 7";
    static final String[] CONFIG_FINAL_STR = {"1", "12", "11", "10", "2", "13", "0", "9", "3", "14", "15", "8", "4", "5", "6", "7"};
    static final int[] CONFIG_FINAL_INT = {1, 12, 11, 10, 2, 13, 0, 9, 3, 14, 15, 8, 4, 5, 6, 7};
    static final HashMap<String, Integer> CONFIG_HASH = new HashMap<>();

    static final int[][] CONFIG_SNAKE = {{0, 4}, {4, 8}, {8, 12}, {12, 13},
    {13, 14}, {14, 15}, {15, 11}, {11, 7}, {7, 3}, {3, 2}, {2, 1}, {1, 5}, {5, 9},
    {9, 10}, {10, 6}, {6, 6}};

    static int stcMovimentos = 0;

    class Estado implements Comparable<Estado> {

        public int custo = 0;
        public String elementos;
        public String[] asVet;

        public int hS;
        public int fn;
        Estado pai = null;

        Estado(String elementos, Estado pai, int custo) {
            this.custo = custo;
            this.elementos = elementos;
            this.asVet = elementos.split(" ");
            this.pai = pai;

        }

        Estado(String elementos) {
            this.elementos = elementos;
            this.asVet = elementos.split(" ");
        }

        @Override
        public int compareTo(Estado o) {
            if (this.fn < o.fn) {
                return -1;
            }
            if (this.fn > o.fn) {
                return 1;
            }
            return 0;
        }

    }

    protected int getZeroPosition(String[] arrStr) {
        int i = 0;
        for (i = 0; i < arrStr.length; i++) {
            if (Integer.parseInt(arrStr[i]) == 0) {
                return i;
            }
        }
        return - 1;
    }

    protected String getArrayPosTroca(int zeroPos, int posTroca, String[] config) {
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

    protected int calcularH1(String[] config) {
        int out = 0;
        for (int i = 0; i < CONFIG_FINAL_STR.length - 1; i++) {
            if (Integer.parseInt(config[i]) != CONFIG_FINAL_INT[i]) {
                out++;
            }
        }
        return out;
    }

    protected int calcularH2(String[] config) {
        int out = 0;
        int limit = CONFIG_SNAKE.length - 2;
        for (int i = 0; i < CONFIG_SNAKE.length - 1; i++) {
            int[] coord = CONFIG_SNAKE[i];

            if (Integer.parseInt(config[coord[0]]) != 0) {
                if (i == limit && Integer.parseInt(config[coord[1]]) == 0) {
                    continue;
                }

                if ((Integer.parseInt(config[coord[0]]) + 1)
                        != Integer.parseInt(config[coord[1]])) {
                    out++;
                }
            }
        }
        return out;
    }

    protected int calcularH3(String[] config) {
        int distanciaR = 0;
        for (int i = 0; i < 16; i++) {
            String currentElment = config[i];
            if (CONFIG_FINAL_INT[i] != Integer.parseInt(currentElment)) {
                int posInicial = CONFIG_HASH.get(currentElment);
                distanciaR += Math.abs((posInicial % 4) - (i % 4)) + Math.abs((posInicial / 4) - (i / 4));
            }
        }
        return distanciaR;
    }

    protected int calcularH4(Estado config) {
        Double r1 = 0.1 * calcularH1(config.asVet);
        Double r2 = 0.1 * calcularH2(config.asVet);
        Double r3 = 0.8 * calcularH3(config.asVet);
        Double result = (r1 + r2 + r3);
        return result.intValue();
    }

    protected int calcularH5(Estado config) {
        int h1, h2, h3;
        h1 = calcularH1(config.asVet);
        h2 = calcularH2(config.asVet);
        h3 = calcularH3(config.asVet);

        return Math.max(Math.max(h1, h2), h3);
    }

    protected ArrayList<Estado> getPossibilidadesPermuta(Estado estado) {
        int posTroca;
        String[] arrConfStr = estado.asVet;
        int zeroPosition = this.getZeroPosition(arrConfStr);
        int maxPositionMatriz = 15;
        int custoAtual = estado.custo + 1;

        ArrayList<Estado> lstFilhos = new ArrayList<Estado>();
        ArrayList<Integer> lastColRight = new ArrayList<Integer>();
        lastColRight.addAll(Arrays.asList(3, 7, 11, 15));

        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);
        }

        posTroca = zeroPosition - 1;
        if ((posTroca >= 0)
                && (lastColRight.indexOf(posTroca) == -1)) {
            Estado est = new Estado(getArrayPosTroca(zeroPosition,
                    posTroca, arrConfStr), estado, custoAtual);

            lstFilhos.add(est);
        }

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

    protected int[] criaVetor(String entrada) {
        String[] entrada2 = entrada.split(" ");
        int[] valorInt = new int[entrada2.length];

        for (int i = 0; i < entrada2.length; i++) {
            valorInt[i] = Integer.parseInt(entrada2[i]);
        }
        return valorInt;
    }

    protected void algoritmoAEstrela(String configInicial) {
        HashMap<String, Estado> cnjtA = new HashMap<>();
        HashMap<String, Estado> cnjtF = new HashMap<>();
        ArrayList<Estado> cnjtSuss = new ArrayList<>();
        Estado m = new Estado(configInicial);
        PriorityQueue<Estado> Q = new PriorityQueue<>();

        m.hS = calcularH5(m);
        m.fn = m.hS + 0;

        while ((m != null) && (!m.elementos.equals(CONFIG_FINAL))) {

            cnjtF.put(m.elementos, m);
            cnjtA.remove(m.elementos);

            cnjtSuss = getPossibilidadesPermuta(m);

            for (Estado filho : cnjtSuss) {
                Estado cnjAEst = cnjtA.get(filho.elementos);
                boolean cnjFEst = cnjtF.containsKey(filho.elementos);

                if (cnjAEst != null && filho.custo < cnjAEst.custo) {
                    cnjtA.remove(filho.elementos);
                    if (Q.contains(filho)) {
                        Q.remove(filho);
                    }
                }

                if (cnjAEst == null && cnjFEst == false) {
                    filho.hS = calcularH5(m);
                    filho.fn = filho.custo + filho.hS;
                    cnjtA.put(filho.elementos, filho);
                    Q.add(filho);
                }
            }
            m = Q.remove();
        }
        System.out.println(m.custo);
    }

    public static void main(String[] args) {

        String caso6Mov = "1 12 11 10 0 13 15 9 2 14 6 8 3 4 5 7";
        String caso7Mov = "1 12 0 11 2 13 15 10 3 14 6 9 4 5 7 8";
        String caso11Mov = "12 11 10 9 1 13 15 0 2 14 6 8 3 4 5 7";
        String caso15Mov = "2 1 12 11 3 0 15 10 4 13 6 9 5 14 7 8";
        String caso20Mov = "2 1 12 11 3 15 6 10 4 0 7 9 5 13 14 8";
        String casoIndefinido = "11 15 4 5 0 14 2 10 3 6 1 9 13 12 8 7";

        String casoMoodle = "0 2 15 11 4 12 3 10 1 9 6 8 5 7 13 14";

        for (int i = 0; i < 16; i++) {
            CONFIG_HASH.put(CONFIG_FINAL_STR[i], i);
        }

        //       String input = "";
        //       try (Scanner sc = new Scanner(System.in)) {
        //           input = sc.nextLine();
        //       }
        Moa main = new Moa();
        main.algoritmoAEstrela("4 2 15 11 1 12 3 10 9 7 6 8 5 0 13 14");
    }
}
