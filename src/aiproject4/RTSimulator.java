package aiproject4;

import tracks.RaceTrack;
import java.lang.Math;
import java.awt.geom.Line2D;
import policyPackage.*;

public class RTSimulator {

    private char[][] track;
    
    public RTSimulator(char c) {
        
        readMap(c);
        IPolicyAlgorithm p = new QLearning();
        
        p.TELL(track);
    }

    public void readMap(char c) {
        RaceTrack rt = new RaceTrack(c);
        track = rt.getTrack();
    }


    int xvelocity;
    int yvelocity;
    int xpos;
    int ypos;
    int[][] walls = new int[100][4];
    
    public void update(int xaccel, int yaccel) {
        boolean success = chance();
        if(success){
            if(xvelocity < 5)
                xvelocity += xaccel;
            if(yvelocity < 5)
                yvelocity += yaccel;
        }
        move();
        
    }
    
    public void move(){
        if(collision() == false){
          xpos += xvelocity;
          ypos += yvelocity;
        }else if(collision()){
            softCrash();
            //hardCrash();
        } 
    }
    
    public boolean collision(){
        boolean crash = false;
        
        for(int i = 0; i<walls.length;i++){
        crash = Line2D.linesIntersect(xpos, ypos, (xpos+xvelocity), (ypos+yvelocity), walls[i][0], walls[i][1], walls[i][2], walls[i][3]);
            if(crash){
               return crash;
            }
        } 
        return false;
    }
    
    public void softCrash(){
        xvelocity = 0;
        yvelocity = 0;
        //set pos to closest valid position
    }

    public void hardCrash(){
        xvelocity = 0;
        yvelocity = 0;
        //set post to one of the finish line positions
    }
    
    public boolean chance() {
        double g = Math.random();
        if (g < .8) {
            return true;
        } else {
            return false;
        }
    }

    
}
