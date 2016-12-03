
package aiproject4;
import policyPackage.*;

/**
 *
 * @author Ben
 */
public class AIproject4 {

    
    public static void main(String[] args) {
        
        //RTSimulator rt = new RTSimulator('r');
        IPolicyAlgorithm p = new QLearning();
        char[][] track = {{'#','#','#','#','#','#'},{'#','S','.','.','.','#'},{'#','.','.','.','F','#'},{'#','#','#','#','#','#'}};
        for(int i = 0; i < track.length; i++){
            for(int j = 0; j < track[0].length; j++){
                System.out.print(track[i][j]);
            }
            System.out.println("");
        }
        p.TELL(track);
    }
    
}
