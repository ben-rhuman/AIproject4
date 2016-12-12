package policyPackage;

import aiproject4.Coordinate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */
public class QLearning implements IPolicyAlgorithm {

    //Exploration values
    final double EXPLORATION_FACTOR = 0.8; //This allows the agent to stray from the known policy to better develop the rest of the policy
    char[][] track;
    double[][][] QTable;
    final int DIRECTIONS = 8;

    //Equation values
    final double ALPHA = 0.5; //Learning Rate
    final double GAMMA = 0.9; //Discount Factor
    final double REWARD = -0.0002;//-0.04;

    public QLearning() {
    }

    @Override
    public int ASK(int x, int y) { //N = 0; NE = 1; E = 2; SE = 3; S = 4; SW = 5; W = 6; NW = 7
        double maxUtility = Double.NEGATIVE_INFINITY;
        int direction = 0;
        Coordinate next;
        Coordinate current = new Coordinate(y, x);

            for (int i = 0; i < DIRECTIONS; i++) {
                next = getNextPosition(i, current);
                if (QTable[x][y][i] > maxUtility && track[next.y][next.x] != '#') {
                    maxUtility = QTable[x][y][i];
                    direction = i;
                }
        }
        return direction; //Returns the direction the policy tells it to go
    }

    @Override
    public void TELL(char[][] track) {
        this.track = track;
        QTable = new double[track.length][track[0].length][DIRECTIONS];
        setWalls();
        
        final long NANOSEC_PER_SEC = 1000l * 1000 * 1000;

        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < 15 * 60 * NANOSEC_PER_SEC) {  //Runs the policy for a given amount of time
            createPolicy();
        }
        
        
        //This was used for making txt files containing policy information
        //so that they wouldnt need to be calculated more then once.
        
//        loadPolicy();

//        PrintStream out;
//        try {
//            out = new PrintStream(new FileOutputStream("L-Track-Policy.txt"));
//            System.setOut(out);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(QLearning.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        for (int i = 0; i < QTable.length; i++) {
//            for (int j = 0; j < QTable[i].length; j++) {
//                if (track[i][j] != '#') {
//                    System.out.println("Position: (" + i + "," + j + ")");
//                    System.out.println("Track Value: " + track[i][j]);
//                    for (int k = 0; k < DIRECTIONS; k++) {
//                        System.out.println("    Direction " + k + ": " + QTable[i][j][k]);
//                        //System.out.print(QTable[i][j][k] + ",");
//                    }
//                }
//            }
//        }

    }

    private void createPolicy() {

        Coordinate pos = randomState();
        Random r = new Random();
        boolean absorbed = false;
        while (!absorbed) {
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

            Coordinate nextPos = getNextPosition(bestDirection, pos);

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
                    nextUtility = 0.0;//-0.10;
                    break;
                default:
                    for (int j = 0; j < DIRECTIONS; j++) {
                        if (QTable[nextPos.y][nextPos.x][j] > nextUtility) {
                            nextUtility = QTable[nextPos.y][nextPos.x][j];
                        }
                    }
                    break;
            }

            QTable[pos.y][pos.x][bestDirection] += ALPHA * (REWARD + GAMMA * nextUtility - QTable[pos.y][pos.x][bestDirection]); //Q-Learning equation that calculated the Utility of going a given direction

            if (track[nextPos.y][nextPos.x] != '#') {
                pos.y = nextPos.y;
                pos.x = nextPos.x;
            }
        }
    }

    private Coordinate randomState() { //This picks a new starting point for the algorithm anytime an absorbtion state is achieved.
        Random r = new Random();
        Coordinate pos = new Coordinate(0, 0);

        do {
            pos.y = r.nextInt(track.length);
            pos.x = r.nextInt(track[0].length);
        } while (track[pos.y][pos.x] != '.');
        return pos;
    }

    private Coordinate getNextPosition(int direction, Coordinate curPos) { //Provides the next location based off of a given direction
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

    private void setWalls() { //Sets the walls and start line to -1.0 utility
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[i].length; j++) {
                if (track[i][j] == '#' || track[i][j] == 'S' || track[i][j] == 'R') {
                    for (int k = 0; k < DIRECTIONS; k++) {
                        QTable[i][j][k] = -1.0;
                    }
                }
            }
        }
    }

    private void loadPolicy() {
        String filePath = "O-Track-Policy.txt"; //Creates the file path of the desired data set for windows

        File file = new File(filePath);

        if (file.isFile()) {
            BufferedReader inputStream = null;
            try {
                inputStream = new BufferedReader(new FileReader(file));

                String line = inputStream.readLine();
                String[] tokens = line.split(",");

                int t = 0;
                for (int i = 0; i < QTable.length; i++) {
                    for (int j = 0; j < QTable[i].length; j++) {
                        for (int k = 0; k < DIRECTIONS; k++) {
                            QTable[i][j][k] = Double.parseDouble(tokens[t]);
                            t++;
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                System.out.println("file not found");;
            } catch (IOException ex) {

            }
        } else {
            System.out.println("File not found");
        }

        //L-track 11,17
        //O-Track 25,25
        //R-Track 28,30
    }
}
