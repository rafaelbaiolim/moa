/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moa;

/**
 *
 * @author rafaellb
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int matriz[][] = new int[4][4];
       matriz[0][0] = 1;
       matriz[0][1] = 2;
       matriz[0][2] = 3;
       
       System.out.println("Matriz a00 : " + matriz[0][0]);
       System.out.println("Matriz a01 : " + matriz[0][1]);
       
       int aux = 0;
       aux = matriz[0][0];
       matriz[0][0] = matriz[0][1];
       matriz[0][1] = aux;
       
       System.out.println("Matriz a00 : " + matriz[0][0]);
       System.out.println("Matriz a01 : " + matriz[0][1]);
       
       
       
       
    }
    
}
