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

    List<Coordinate> finish = new ArrayList<>();
    List<Coordinate> start = new ArrayList<>();
    
    //Exploration values
    final double EXPLORATION_FACTOR = 0.8;
    char[][] track;
    double[][][] QTable;
    final int DIRECTIONS = 8;
    
    //Equation values
    final double ALPHA = 0; //Learning Rate
    final double GAMMA = 0; //Discount Factor
    final double REWARD = 0;
    
    

    public QLearning() {
    }

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
        for(int i = 0; i < 1000; i++){
            Coordinate pos = randomState();
            Random r = new Random();
            
            while(true){
                double maxUtility = Double.NEGATIVE_INFINITY;
                int bestDirection = 0;
                for(int j = 0; i < DIRECTIONS; j++){ //Find the maxUtility direction
                    if(QTable[pos.x][pos.y][j] > maxUtility){
                        maxUtility = QTable[pos.x][pos.y][j];
                        bestDirection = j + 1;
                    }
                }
                
                if(EXPLORATION_FACTOR < r.nextDouble()){ //Gives the algorithm some non-deterministic movement
                    bestDirection = r.nextInt(DIRECTIONS) + 1;
                }
                
                Coordinate nextPos = getNextPostition(bestDirection, pos);

                switch(bestDirection){
                    
                }
                
                
                Double nextUtility = Double.NEGATIVE_INFINITY;
                
                if(track[nextPos.x][nextPos.y] == 'F'){
                    nextUtility = 1.0;
                } else if(track[nextPos.x][nextPos.y] == 'S'){
                    nextUtility = -1.0;
                } else if(track[nextPos.x][nextPos.y] == '#') {
                    nextUtility = 0.0;
                } else{
                   for(int j = 0; j < DIRECTIONS; j++){
                       
                   }
                }
                
                
                
            }
        }
    }

    private Coordinate randomState() {
        Random r = new Random();
        Coordinate pos = new Coordinate(0,0);

        do {
            pos.x = r.nextInt(track.length);
            pos.y = r.nextInt(track[0].length);
        } while (track[pos.x][pos.y] != '.');

        return pos;
    }

    private void absorptionStates(){       
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[i].length; j++) {
                if(track[i][j] == 'S'){
                    this.start.add(new Coordinate(i,j));
                } else if(track[i][j] == 'F'){
                    this.finish.add(new Coordinate(i,j));
                }
            }
        } 
    }
    
    private Coordinate getNextPosition(int direction, Coordinate curPos){
        
    }
}
