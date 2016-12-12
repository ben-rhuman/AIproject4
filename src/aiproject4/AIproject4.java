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
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */
public class AIproject4 {

    public static void main(String[] args) throws ExecutionException, TimeoutException {
        
        char track = 'o';
        int crash = 1;
        char policy = 'q';
        
        IPolicyAlgorithm q = new QLearning();
        IPolicyAlgorithm v = new ValueIteration();

        System.out.println("CSCI 446 Project 4");
        System.out.println("Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf\n");
        System.out.println("Track: " + track);
       
        if(crash == 1){
            System.out.println("Crash Type: Hard");
        } else{
            System.out.println("Crash Type: Soft");
        }
        
        if(policy == 'q'){
            System.out.println("Policy: Q-Learning");
        }else{
            System.out.println("Policy: Value Iteration");
        }
        
        RTSimulator rt = new RTSimulator(track,crash,q); //Begins the racing simulation
    }
}
