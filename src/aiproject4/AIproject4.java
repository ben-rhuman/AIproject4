package aiproject4;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import policyPackage.*;

import policyPackage.IPolicyAlgorithm;
import policyPackage.QLearning;

/**
 *
 * @author Ben
 */
public class AIproject4 {

    public static void main(String[] args) throws ExecutionException, TimeoutException {
        
        IPolicyAlgorithm q = new QLearning();
        IPolicyAlgorithm v = new ValueIteration();
        //RTSimulator rt = new RTSimulator('o',0,q);
        //RTSimulator rt = new RTSimulator('l',0,q);
        RTSimulator rt = new RTSimulator('l',0,v);
        

    }

}
