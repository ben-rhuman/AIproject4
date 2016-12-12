
package policyPackage;

/**
 *
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */
public interface IPolicyAlgorithm {
    public int ASK(int x, int y); //Returns a int representing a direction
    public void TELL(char[][] track); //This provides the agent with the track
}
