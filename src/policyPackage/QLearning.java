package policyPackage;

import aiproject4.Coordinate;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author k28h885
 */
public class QLearning implements IPolicyAlgorithm {

    //Exploration values
    final double EXPLORATION_FACTOR = 0.8;
    char[][] track;
    double[][][] QTable;
    final int DIRECTIONS = 8;

    //Equation values
    final double ALPHA = 0.5; //Learning Rate
    final double GAMMA = 0.9; //Discount Factor
    final double REWARD = -0.04;

    public QLearning() {
    }

    @Override
    public int ASK(int x, int y) { //N = 1; NE = 2; E = 3; SE = 4; S = 5; SW = 6; W = 7; NW = 8
        double maxUtility = 0;
        int direction = 0;
        for (int i = 0; i < DIRECTIONS; i++) {
            if (QTable[x][y][i] > maxUtility) {
                maxUtility = QTable[x][y][i];
                direction = i;
            }
        }
        return direction;
    }

    @Override
    public void TELL(char[][] track) {
        this.track = track;
        QTable = new double[track.length][track[0].length][DIRECTIONS];
        
        final long NANOSEC_PER_SEC = 1000l * 1000 * 1000;

        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < 180 * 60 * NANOSEC_PER_SEC) {
            createPolicy();
        }
        
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream("L-Track-Policy.txt"));
            System.setOut(out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QLearning.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < QTable.length; i++) {
            for (int j = 0; j < QTable[i].length; j++) {
                //if (track[i][j] != '#') {
                    //System.out.println("Position: (" + i + "," + j + ")");
                    //System.out.println("Track Value: " + track[i][j]);
                    for (int k = 0; k < DIRECTIONS; k++) {
                        //System.out.println("    Direction " + k + ": " + QTable[i][j][k]);
                        System.out.print(QTable[i][j][k] + ",");
                    }
                //}
            }
        }
    }

    private void createPolicy() {
        //while(!converged) better estimate of policy
        //for (int i = 0; i < 10000; i++) {
            Coordinate pos = randomState();
            Random r = new Random();
            boolean absorbed = false;
            while (!absorbed) {
                //System.out.println("Position: (" + pos.y + "," + pos.x + ")");
                //System.out.println("Track value: " + track[pos.y][pos.x]);
                double maxUtility = Double.NEGATIVE_INFINITY;
                int bestDirection = 0;
                for (int j = 0; j < DIRECTIONS; j++) { //Find the maxUtility direction
                    if (QTable[pos.y][pos.x][j] > maxUtility) {
                        maxUtility = QTable[pos.y][pos.x][j];
                        bestDirection = j;
                    }
                }

                if (EXPLORATION_FACTOR < r.nextDouble()) { //Gives the algorithm some non-deterministic movement
                    bestDirection = r.nextInt(DIRECTIONS);
                }
                //System.out.println("bestDirection: " + bestDirection);

                Coordinate nextPos = getNextPosition(bestDirection, pos);
                //System.out.println("Next Position: (" + nextPos.y + "," + nextPos.x + ")");
                //System.out.println("Track value: " + track[nextPos.y][nextPos.x]);

                Double nextUtility = Double.NEGATIVE_INFINITY;

                switch (track[nextPos.y][nextPos.x]) {
                    case 'F':
                        nextUtility = 1.0;
                        absorbed = true;
                        break;
                    case 'S':
                        nextUtility = -1.0;
                        absorbed = true;
                        break;
                    case '#':
                        nextUtility = 0.0;
                        //nextPos.y = pos.y;
                        //nextPos.x = pos.x;
                        //System.out.println("HIT A WALL");
                        break;
                    default:
                        for (int j = 0; j < DIRECTIONS; j++) {
                            if (QTable[nextPos.y][nextPos.x][j] > nextUtility) {
                                nextUtility = QTable[nextPos.y][nextPos.x][j];
                            }
                        }
                        break;
                }

                QTable[pos.y][pos.x][bestDirection] += ALPHA * (REWARD + GAMMA * nextUtility - QTable[pos.y][pos.x][bestDirection]);

                if (track[nextPos.y][nextPos.x] != '#') {
                    //System.out.println("Reset Pos");
                    pos.y = nextPos.y;
                    pos.x = nextPos.x;
                    //System.out.println("Position: (" + pos.y + "," + pos.x + ")");
                } 
            }
        //}
    }

    private Coordinate randomState() {
        Random r = new Random();
        Coordinate pos = new Coordinate(0, 0);

        do {
            pos.y = r.nextInt(track.length);
            pos.x = r.nextInt(track[0].length);
        } while (track[pos.y][pos.x] != '.');
        //System.out.println("Track value: "+track[pos.y][pos.x]);
        return pos;
    }

    private Coordinate getNextPosition(int direction, Coordinate curPos) {
        Coordinate next = new Coordinate(curPos.x, curPos.y);
        switch (direction) {
            case 0:
                next.y--;
                break;
            case 1:
                next.y--;
                next.x++;
                break;
            case 2:
                next.x++;
                break;
            case 3:
                next.y++;
                next.x++;
                break;
            case 4:
                next.y++;
                break;
            case 5:
                next.y++;
                next.x--;
                break;
            case 6:
                next.x--;
                break;
            case 7:
                next.y--;
                next.x--;
                break;
            default:
                System.out.println("ERROR: QLearning.getNextPosition");
        }
        return next;
    }
}
