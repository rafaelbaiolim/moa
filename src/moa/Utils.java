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

    public static void printArrayList(ArrayList<int[]> lst) {
        for (int[] vet : lst) {
            System.out.println(Arrays.toString(vet));
        }
    }

    public static void printArray(int[] arr) {
        System.out.println(Arrays.toString(arr));
    }

    public static void printMatriz(int matrix[][]) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

}
