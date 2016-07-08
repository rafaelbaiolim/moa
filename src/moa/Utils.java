/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moa;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author rafaellb
 */
public class Utils {

    public static int[][] criarMatriz(int[] entrada) {
        int matrizInicial[][] = new int[4][4];

        int total = 0;
        for (int i = 0; i < matrizInicial.length; i++) {
            for (int j = 0; j < matrizInicial[0].length; j++) {
                matrizInicial[i][j] = entrada[total++];
            }
        }
        return matrizInicial;
    }

    public static void printArrayList(ArrayList<MOA.Estado> lst) {
        for (MOA.Estado vet : lst) {
            System.out.println(vet.elementos);
        }
    }

    public static void printArray(int[] arr) {
        int[][] m = criarMatriz(arr);
        printMatriz(m);
        //System.out.println(Arrays.toString(arr));
    }

    public static void printMatriz(int matrix[][]) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("\n");
    }

}
