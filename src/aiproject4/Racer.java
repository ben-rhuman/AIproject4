package aiproject4;


public class Racer{
    private int xVelocity; //racer's velocity
    private int yVelocity;
    private int xPos;      //racer's position
    private int yPos;
    private char loc = 'n';      //the character the racer is ontop of, default is set to 'n'
    private RTSimulator rt;
    
    public Racer(RTSimulator rt){
        this.rt = rt;
    }
    
    public void addAcceleration(int xAccel, int yAccel) { //update the racer's position and velocity
        //check whether the acceleration failed
        boolean succAccel = chance();
        succAccel = true; //////////////////////
        if (!succAccel) { //set acceleration to 0
            xAccel = 0;
            yAccel = 0;
            System.out.println("fail to accelerate****");
        }
            //check if the acceleration will not go over the maximum or under the minimum
            int tempX = xVelocity + xAccel;
            int tempY = yVelocity + yAccel;
            
            if (tempX  <= 5 && tempX >= -5 && tempY <= 5 && tempY >= -5) { //
                
                //update the velocity
                xVelocity += xAccel;
                yVelocity += yAccel;
            }
    }
    
    public boolean chance() {
        double g = Math.random();
        if (g < .8) {
            return true;
        } else {
            return false;
        }
    }
    
    public char getLoc(){
        return this.loc;
    }
    public int getXVel(){
        return this.xVelocity;
    }
    public int getYVel(){
        return this.yVelocity;
    }
    public int getXPos(){
        return this.xPos;
    }
    public int getYPos(){
        return this.yPos;
    }
    public void setVelocity(int xV, int yV){
        this.xVelocity = xV;
        this.yVelocity = yV;
    }
    public void setLoc(char c){ //set the character that the racer is on top of 
        this.loc = c;
    }
    
    public void setPosition(int x, int y){
        this.xPos = x;
        this.yPos = y;
    }
    public void printStats(){
        System.out.println("Position: X "+ xPos + " Y " + yPos);
        System.out.println("Velocity: X "+ xVelocity + " Y " + yVelocity);
        System.out.println("Current Location: " + loc);
    }
}