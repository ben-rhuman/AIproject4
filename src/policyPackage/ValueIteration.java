
package policyPackage;

/**
 *
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */
import aiproject4.Coordinate;
import java.lang.Math;

public class ValueIteration implements IPolicyAlgorithm {

    private char track[][];
    private double table[][]; //Holds each positions V-values
    private final double chance = .99; //Causes value iteration to take into account walls
    private double V = 0; //Utility value
    private final double discount = .95; //Update discount value
    private final double reward = -.1; //Cost value
    private int lastX = 0;
    private int lastY = 0;

    public ValueIteration() {
    }

    @Override
    public int ASK(int x, int y) { //Requests the policy at the given position (x,y)
        int bestX = 0;
        int bestY = 0;
        V = Double.NEGATIVE_INFINITY;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (table[x + i][y + j] > V) {
                    if (i == 0 && j == 0) {
                        //Do nothing
                    } else {
                        V = table[x + i][y + j];
                        bestX = i;
                        bestY = j;
                    }
                }
                if (table[x + i][y + j] >= V && lastX == i && lastY == j) { //Promotes continuing in the same direction, so as to increase velocity in a single direction
                    if (i == 0 && j == 0) {                                 // This smooths out the jumpy movement we were seeing
                        //Do nothing
                    } else {
                        V = table[x + i][y + j];
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        lastX = bestX;
        lastY = bestY;
        return getDirection(bestX, bestY); //returns the direction that will maximize the next state's utility
    }

    @Override
    public void TELL(char[][] track) {  //Provides the track and starts the Value Iteration policy creation
        this.track = track;
        table = new double[track.length][track[0].length];
        createTable();
        createPolicy();
        //printMap();
        //printTable();
    }

    private void createPolicy() {    //Iteratively fills the table with values

        for (int m = 0; m < 100000; m++) {
            for (int g = 0; g < table.length; g++) {
                for (int h = 0; h < table[0].length; h++) {
                    V = Double.NEGATIVE_INFINITY;
                    if (track[g][h] == '#' || track[g][h] == 'F' || track[g][h] == 'S') {
                        //Do not calculate rating for walls
                    } else {
                        //Calculation for each position
                        int bestX = 0;
                        int bestY = 0;
                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                if (table[g + i][h + j] > V) {
                                    if (i == 0 && j == 0) {
                                        //Do nothing
                                    } else {
                                        V = table[g + i][h + j];
                                        bestX = g + i;
                                        bestY = h + j;
                                    }
                                }
                            }
                        }

                        V *= chance;
                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                if (bestX != g + i && bestY != h + j) {
                                    if (i == 0 && j == 0) {
                                        //Do nothing
                                    } else {
                                        V += table[g + i][h + j] * (1 - chance) / 7; //Version that takes into account walls
                                    }
                                }
                            }
                        }
                        table[g][h] = ((discount * V) + reward);  // Equation for calculating state utility
                    }
                }
            }
        }
        //}
    }

    private void createTable() {     //Initializes the table values with zero states and absorption states
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                switch (track[i][j]) {
                    case '.':
                        table[i][j] = 0.0;
                        break;
                    case '#':
                        table[i][j] = -50.0;
                        break;
                    case 'F':
                        table[i][j] = 100.0;
                        break;
                    case 'S':
                        table[i][j] = -100.0;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private int getDirection(int x, int y) {
        switch (x) {
            case -1:
                switch (y) {
                    case -1:
                        return 7;
                    case 0:
                        return 0;
                    case 1:
                        return 1;
                }
                break;
            case 0:
                switch (y) {
                    case -1:
                        return 6;
                    case 0:
                        System.out.println("ERROR.getDirection(): Invalid Direction 0-0");
                        break;
                    case 1:
                        return 2;
                }
                break;
            case 1:
                switch (y) {
                    case -1:
                        return 5;
                    case 0:
                        return 4;
                    case 1:
                        return 3;
                }
                break;
            default:
                System.out.println("ERROR.getDirection(): Default");
                break;
        }
        return -1;
    }

    private void printMap() { //Prints out the track
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                System.out.print(track[i][j]);
            }
            System.out.println();
        }

    }

    public void printTable() { //Prints out the Value iteration table. Used for testing
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                System.out.printf("%.4f", table[i][j]);
                System.out.print(" | ");
            }
            System.out.println("\n------------------------------------------------------------------------------");
        }
    }
}
