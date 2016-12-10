/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policyPackage;

/**
 *
 * @author k28h885
 */
import aiproject4.Coordinate;
import java.lang.Math;

public class ValueIteration implements IPolicyAlgorithm {

    public char track[][];
    public double table[][];
    public Coordinate refPos;
    public double V = 0;
    public double discount = .9;
    public double reward = 3;
    public double error = 5;

    public ValueIteration() {
    }

    @Override
    public int ASK(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void TELL(char[][] track) {
        this.track = track;
        table = new double[track.length][track[0].length];
        createTable();
        createPolicy();
        printMap();
    }

    public void createPolicy() {    //Iteratively fills the table with values
        double U = 0;
        double U1 = 100;

        while (Math.abs(U1 - U) < error * ((1 - discount) / discount)) {
            //traversing all positions
            for (int g = 0; g < table.length; g++) {
                for (int h = 0; h < table[0].length; h++) {
                    if (track[g][h] == '#') {
                        //Do not calculate rating for walls
                    } else {
                        //Calculation for each position
                        for (int i = -1; i < 1; i++) {
                            for (int j = -1; j == 1; j++) {
                                if (table[g + i][h + j] > V && i!= 0 && j != 0) {
                                    V = table[g + i][h + j];
                                }
                            }
                        }
                        table[g][h] = ((discount * V) - reward);
                        U += table[g][h];
                    }
                }
            }
            U1 = U;
        }
    }

    public void createTable() {     //Initializes the table values with zero states and absorption states
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == '.' || track[i][j] == '#') {
                    table[i][j] = 0;
                } else if (track[i][j] == 'F') {
                    table[i][j] = 100;
                    refPos.x = i;
                    refPos.y = j;
                } else if (track[i][j] == 'S') {
                    table[i][j] = -100;
                }
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                System.out.print(track[i][j]);
            }
            System.out.println();
        }

    }

}
