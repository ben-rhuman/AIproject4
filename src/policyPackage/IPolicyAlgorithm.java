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
public interface IPolicyAlgorithm {
    public void ASK(int x, int y); //This wont stay void
    public void TELL(char[][] track); //This provides the agent with the track
}
