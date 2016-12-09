
package aiproject4;

import policyPackage.IPolicyAlgorithm;
import policyPackage.QLearning;

/**
 *
 * @author Ben
 */
public class AIproject4 {

    
    public static void main(String[] args) {
        
       IPolicyAlgorithm ql = new QLearning();
        RTSimulator rt = new RTSimulator('l',0,ql);
    }
    
}
