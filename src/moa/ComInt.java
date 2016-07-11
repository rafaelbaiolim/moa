
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

class Main {
    
    static final int[] CONFIG_FINAL = {1, 12, 11, 10, 2, 13, 0, 9, 3, 14, 15, 8, 4, 5, 6, 7};
    static final HashMap<Integer, Integer> CONFIG_HASH = new HashMap<>();
    static final int[][] CONFIG_SNAKE = {{0, 4}, {4, 8}, {8, 12}, {12, 13},
    {13, 14}, {14, 15}, {15, 11}, {11, 7}, {7, 3}, {3, 2}, {2, 1}, {1, 5}, {5, 9},
    {9, 10}, {10, 6}, {6, 6}};

    class Estado implements Comparable<Estado> {

        public int custo = 0;
        public int[] elementos;
        public String key;

        public int hS;
        public int fn;
        Estado pai = null;

        Estado(int[] elementos, Estado pai, int custo) {
            this.custo = custo;
            this.elementos = elementos;
            this.key = Arrays.toString(elementos);
            this.pai = pai;

        }

        Estado(int[] elementos) {
            this.elementos = elementos;
            this.key = Arrays.toString(elementos);
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

    protected int[] criarVetor(String entrada) {
        String[] config = entrada.split(" ");
        int[] array = Arrays.asList(config).stream().mapToInt(Integer::parseInt).toArray();
        return array;
    }

    protected int calcularH1(int[] config) {
        int out = 0;
        for (int i = 0; i < config.length - 1; i++) {
            if (config[i] != CONFIG_FINAL[i]) {
                out++;
            }
        }
        return out;
    }

    protected int calcularH2(int[] config) {
        int out = 0;
        int limit = CONFIG_SNAKE.length - 2;
        for (int i = 0; i < CONFIG_SNAKE.length - 1; i++) {
            int[] coord = CONFIG_SNAKE[i];

            if (config[coord[0]] != 0) {
                if (i == limit && config[coord[1]] == 0) {
                    continue;
                }

                if (config[coord[0]] + 1 != config[coord[1]]) {
                    out++;
                }
            }
        }
        return out;
    }

    protected int calcularH3(int[] config) {
        int distanciaR = 0;
        for (int i = 0; i < 16; i++) {
            int currentElment = config[i];
            if (CONFIG_FINAL[i] != currentElment) {
                int posInicial = CONFIG_HASH.get(currentElment);
                distanciaR += Math.abs((posInicial % 4) - (i % 4)) + Math.abs((posInicial / 4) - (i / 4));
            }
        }
        return distanciaR;
    }

    protected int calcularH4(int[] config) {
        Double r1 = 0.4 * calcularH1(config);
        Double r2 = 0.3 * calcularH2(config);
        Double r3 = 0.1 * calcularH3(config);
        Double result = (r1 + r2 + r3);
        return result.intValue();
    }

    protected int calcularH5(int[] config) {
        int h1, h2, h3;
        h1 = calcularH1(config);
        h2 = calcularH2(config);
        h3 = calcularH3(config);

        return Math.max(Math.max(h1, h2), h3);
    }

    protected int[] getArrayPosTrocada(int zeroPos, int posTroca, int[] config) {
        int[] newConfig = config.clone();
        int aux = newConfig[posTroca];
        newConfig[posTroca] = newConfig[zeroPos];
        newConfig[zeroPos] = aux;

        return newConfig;
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

    protected ArrayList<Estado> getPossibilidadesPermuta(Estado estado) {
        int posTroca;
        int zeroPosition = this.getZeroPosition(estado.elementos);
        int maxPositionMatriz = estado.elementos.length - 1;
        int custoAtual = estado.custo + 1;
        ArrayList<Estado> lstFilhos = new ArrayList<>();
        ArrayList<Integer> lastColRight = new ArrayList<>();
        lastColRight.addAll(Arrays.asList(3, 7, 11, 15));
        String key = "";

        posTroca = zeroPosition + 1;
        if (posTroca % 4 != 0) {
            int[] x = getArrayPosTrocada(zeroPosition,
                    posTroca, estado.elementos);
            Estado est = new Estado(x, estado, custoAtual);

            lstFilhos.add(est);
        }

        posTroca = zeroPosition - 1;
        if ((posTroca >= 0)
                && (lastColRight.indexOf(posTroca) == -1)) {
            int[] x = getArrayPosTrocada(zeroPosition,
                    posTroca, estado.elementos);
            Estado est = new Estado(x, estado, custoAtual);

            lstFilhos.add(est);
        }

        posTroca = zeroPosition - 4;
        if (posTroca >= 0) {
            int[] x = getArrayPosTrocada(zeroPosition,
                    posTroca, estado.elementos);
            Estado est = new Estado(x, estado, custoAtual);

            lstFilhos.add(est);

        }

        posTroca = zeroPosition + 4;
        if (posTroca <= maxPositionMatriz) {
            int[] x = getArrayPosTrocada(zeroPosition,
                    posTroca, estado.elementos);
            Estado est = new Estado(x, estado, custoAtual);

            lstFilhos.add(est);
        }

        return lstFilhos;
    }

    protected void algoritmoAEstrela(String configInicial) {
        HashMap<String, Estado> cnjtA = new HashMap<>();
        HashMap<String, Estado> cnjtF = new HashMap<>();
        ArrayList<Estado> cnjtSuss = new ArrayList<>();
        PriorityQueue<Estado> Q = new PriorityQueue<>();

        int[] asVet = criarVetor(configInicial);

        Estado m = new Estado(asVet);
        m.hS = calcularH5(asVet);
        m.fn = m.hS + 0;
        String key = m.key;
        String cg = Arrays.toString(CONFIG_FINAL);

        while ((m != null) && (!m.key.equals(cg))) {
            cnjtF.put(key, m);
            cnjtA.remove(key);

            cnjtSuss = getPossibilidadesPermuta(m);

            for (Estado filho : cnjtSuss) {
                key = filho.key;
                Estado cnjAEst = cnjtA.get(key);
                Estado cnjFEst = cnjtF.get(key);

                if (cnjAEst != null && filho.custo < cnjAEst.custo) {
                    cnjtA.remove(key);
                    if (Q.contains(filho)) {
                        Q.remove(filho);
                    }
                }

                if (cnjAEst == null && cnjFEst == null) {
                    filho.hS = calcularH5(filho.elementos);
                    filho.fn = filho.custo + filho.hS;
                    cnjtA.put(key, filho);
                    Q.add(filho);

                }
            }

            m = Q.remove();

            //m = getMinValue(cnjtA);
        }
        System.out.println(m.custo);
    }

    public static void main(String[] args) {
        Main main = new Main();
        for (int i = 0; i < 16; i++) {
            CONFIG_HASH.put(CONFIG_FINAL[i], i);
        }

        String input = "4 2 15 11 1 12 3 10 9 7 6 8 5 0 13 14";
        //try (Scanner sc = new Scanner(System.in)) {
        //  input = sc.nextLine();
        //}

        //main.algoritmoAEstrela(input.trim().replaceAll(" +", " "));
        main.algoritmoAEstrela(input);

    }
}
