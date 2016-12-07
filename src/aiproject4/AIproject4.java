package aiproject4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import policyPackage.*;

/**
 *
 * @author Ben
 */
public class AIproject4 {

    public static void main(String[] args) throws ExecutionException, TimeoutException {

        RTSimulator rt = new RTSimulator('o');
//        IPolicyAlgorithm p = new QLearning();
//        char[][] track = {{'#','#','#','#','#','#'},{'#','S','.','.','.','#'},{'#','.','.','.','F','#'},{'#','#','#','#','#','#'}};
//        for(int i = 0; i < track.length; i++){
//            for(int j = 0; j < track[0].length; j++){
//                System.out.print(track[i][j]);
//            }
//            System.out.println("");
//        }
//        p.TELL(track);

    }

}
