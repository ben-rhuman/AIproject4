package policyPackage;

import aiproject4.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public QLearning() {}

    @Override
    public int ASK(int x, int y) { //N = 1; NE = 2; E = 3; SE = 4; S = 5; SW = 6; W = 7; NW = 8
        double maxUtility = 0;
        int direction = 0;
        for (int i = 0; i < DIRECTIONS; i++) {
            if (QTable[x][y][i] > maxUtility) {
                maxUtility = QTable[x][y][i];
                direction = i + 1;
            }
        }
        return direction;
    }

    @Override
    public void TELL(char[][] track) {
        this.track = track;
        QTable = new double[track.length][track[0].length][8];
        createPolicy();
    }

    private void createPolicy() {
        //while(!converged) better estimate of policy
        for (int i = 0; i < 1000; i++) {
            Coordinate pos = randomState();
            Random r = new Random();
            boolean absorbed = false;
            while (!absorbed) {
                System.out.println("Position: (" + pos.y +"," + pos.x + ")");
                double maxUtility = Double.NEGATIVE_INFINITY;
                int bestDirection = 0;
                for (int j = 0; j < DIRECTIONS; j++) { //Find the maxUtility direction
                    if (QTable[pos.y][pos.x][j] > maxUtility) {
                        maxUtility = QTable[pos.y][pos.x][j];
                        bestDirection = j + 1;
                    }
                }

                if (EXPLORATION_FACTOR < r.nextDouble()) { //Gives the algorithm some non-deterministic movement
                    bestDirection = r.nextInt(DIRECTIONS) + 1;
                }
                System.out.println("bestDirection: " + bestDirection);
                
                Coordinate nextPos = getNextPosition(bestDirection, pos);
                System.out.println("Next Position: (" + nextPos.y +"," + nextPos.x + ")");
                
                Double nextUtility = Double.NEGATIVE_INFINITY;
                
                if (track[nextPos.y][nextPos.x] == 'F') {
                    nextUtility = 1.0;
                    absorbed = true;
                } else if (track[nextPos.y][nextPos.x] == 'S') {
                    nextUtility = -1.0;
                    absorbed = true;
                } else if (track[nextPos.y][nextPos.x] == '#') {
                    nextUtility = 0.0;
                    nextPos = pos;
                } else {
                    for (int j = 0; j < DIRECTIONS; j++) {
                        if (QTable[nextPos.y][nextPos.x][j] > nextUtility) {
                            nextUtility = QTable[nextPos.y][nextPos.x][j];
                        }
                    }
                }
                
                QTable[pos.y][pos.x][bestDirection] += ALPHA*(REWARD + GAMMA*nextUtility - QTable[pos.y][pos.x][bestDirection]);
                pos = nextPos;
            }
        }
    }

    private Coordinate randomState() {
        Random r = new Random();
        Coordinate pos = new Coordinate(0, 0);

        do {
            pos.y = r.nextInt(track.length);
            pos.x = r.nextInt(track[0].length);
        } while (track[pos.y][pos.x] != '.');
        System.out.println("Track value: "+track[pos.y][pos.x]);
        return pos;
    }

    private Coordinate getNextPosition(int direction, Coordinate curPos) {
        switch (direction) {
            case 1:
                curPos.x--;
                break;
            case 2:
                curPos.y++;
                curPos.x--;
                break;
            case 3:
                curPos.y++;
                break;
            case 4:
                curPos.y++;
                curPos.x++;
                break;
            case 5:
                curPos.x++;
                break;
            case 6:
                curPos.y--;
                curPos.x++;
                break;
            case 7:
                curPos.y--;
                break;
            case 8:
                curPos.y--;
                curPos.x--;
                break;
            default:
                System.out.println("ERROR: QLearning.getNextPosition");
        }
        return curPos;
    }
}
