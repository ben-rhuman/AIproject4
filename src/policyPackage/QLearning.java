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

public class QLearning implements IPolicyAlgorithm {
    
    char[][] track;
    
    public QLearning(){}
    
    @Override
    public void ASK(int x, int y) {
    
    }

    @Override
    public void TELL(char[][] track) {
        this.track = track;
    }
    
    
    
}