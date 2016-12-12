package aiproject4;

import tracks.RaceTrack;
import java.lang.Math;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
import policyPackage.IPolicyAlgorithm;
import policyPackage.QLearning;
import policyPackage.ValueIteration;
import java.util.Random;

/**
 *
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */
public class RTSimulator {

    private char[][] track;                 //the racetrack
    private ArrayList<Coordinate> start;    //the starting line
    private ArrayList<Coordinate> finish;   //the finish line
    private ArrayList<Coordinate> walls;    //the crashable walls
    private IPolicyAlgorithm ipa;           //stores the algorithm used to make the policy
    private Coordinate startPoint;
    private Coordinate finishLinePoint;
    private Racer racer;
    private int crashType;                  //0 soft crash | 1 hard crash
    private int time = 0;
    private boolean done = false;

    public RTSimulator(char c, int crashType, IPolicyAlgorithm ipa) {
        start = new ArrayList<>();
        finish = new ArrayList<>();
        walls = new ArrayList<>();
        racer = new Racer(this);    //setting up the racer
        this.ipa = ipa;             //read in the algorithm to use to make the policy
        this.crashType = crashType; //read in the chosen type of crashing 

        readMap(c);         //read in the racetrack 
        ipa.TELL(track);    //give the racetrack to the algorithm
        getAbsorbtion();    //get all absorbtion states
        getCrashableWalls();//get list of crashable walls
        setupCar();         //place the car on the starting line
        printTrack();

        ////////reset the accel not failing in racer class
        drive();            //drive through the map
        printTrack();
        System.out.println("Finished");
        System.out.println("Total Race Time: " + time);

    }

    public void readMap(char c) {  //reading in the desired map
        RaceTrack rt = new RaceTrack(c);
        track = rt.getTrack();
    }

    public void setupCar() {
        //get a random start point
        startPoint = getRandomStart();

        //set the racer's position
        setRacer(startPoint);
    }

    public void drive() {

        while (!done) {

            int move = ipa.ASK(racer.getYPos(), racer.getXPos());
            time++;
            Coordinate a = null;
            if (crashType == 1) {
                a = getSafeAcceleration(move);
            } else {
                a = getAcceleration(move);
            }

            printTrack();
            
            
            System.out.print("Next Move: ");
            switch (move) {
                case 0:
                    System.out.print("N");
                    break;
                case 1:
                    System.out.print("NE");
                    break;
                case 2:
                    System.out.print("E");
                    break;
                case 3:
                    System.out.print("SE");
                    break;
                case 4:
                    System.out.print("S");
                    break;
                case 5:
                    System.out.print("SW");
                    break;
                case 6:
                    System.out.print("W");
                    break;
                case 7:
                    System.out.print("NW");
                    break;
            }
            System.out.println("");
            System.out.println("Acceleration: X " + a.x + " Y " + a.y);

            //add the acceleration
            racer.addAcceleration(a.x, a.y);

            if (checkPassFinish()) { //check if the racer passed the finish line
                done = true;
                return;
            }

            if (!collision()) { //check if the racer did not crash
                //update the position and the track
                int newXPos = racer.getXVel() + racer.getXPos();
                int newYPos = racer.getYVel() + racer.getYPos();
                Coordinate c = new Coordinate(newXPos, newYPos);
                setRacer(c);
            }

        }

    }

    public Coordinate getRandomStart() { //get the coordinate of a random starting point
        Random rand = new Random();
        int max = start.size() - 1;
        int min = 0;
        int randomIndex = rand.nextInt((max - min) + 1) + min;
        Coordinate co = new Coordinate(start.get(randomIndex).x, start.get(randomIndex).y);

        return co;
    }

    public void setRacer(Coordinate co) { //put the racer at a specific point

        if (racer.getLoc() != 'n') { //if this isn't the first time the racer is placed

            //set the racer's position on the track to the previous ASCII character
            track[racer.getYPos()][racer.getXPos()] = racer.getLoc();

        }
        //save the ASCII character at the point
        racer.setLoc(track[co.y][co.x]);

        //set the racer's position
        racer.setPosition(co.x, co.y);

        //set the point on the track to 'R'
        track[co.y][co.x] = 'R';

    }

    public boolean checkPassFinish() { //check if the car passed the finish line
        boolean finished1 = false;
        boolean finished2 = false;
        for (int i = 0; i < finish.size(); i++) {
            double rxp = (double) racer.getXPos();
            double ryp = (double) racer.getYPos();
            double rxpv = (double) (racer.getXPos() + racer.getXVel());
            double rypv = (double) (racer.getYPos() + racer.getYVel());
            double finishx1 = ((double) (finish.get(i).x)) - .5;
            double finishy1 = ((double) (finish.get(i).y)) - .5;
            double finishx2 = ((double) (finish.get(i).x)) + .5;
            double finishy2 = ((double) (finish.get(i).y)) + .5;

            finished1 = Line2D.linesIntersect(rxp, ryp, rxpv, rypv, finishx1, finishy1, finishx2, finishy2);
            finished2 = Line2D.linesIntersect(rxp, ryp, rxpv, rypv, finishx2, finishy1, finishx1, finishy2);

            if (finished1 || finished2) { //if any of the finish spots were intersected
                
                //setup the finish line coordinate
                Coordinate f = new Coordinate(finish.get(i).x, finish.get(i).y);
                setRacer(f);
                return true;
            }
        }
        return false;
    }

    public boolean collision() { //checks if the car collided with a wall
        boolean crashed1 = false;
        boolean crashed2 = false;

        for (int i = 0; i < walls.size(); i++) { //check if collided with any of the walls

            double rxp = (double) racer.getXPos();
            double ryp = (double) racer.getYPos();
            double rxpv = (double) (racer.getXPos() + racer.getXVel());
            double rypv = (double) (racer.getYPos() + racer.getYVel());
            double wallx1 = ((double) (walls.get(i).x)) - .49;
            double wally1 = ((double) (walls.get(i).y)) - .49;
            double wallx2 = ((double) (walls.get(i).x)) + .49;
            double wally2 = ((double) (walls.get(i).y)) + .49;

            crashed1 = Line2D.linesIntersect(rxp, ryp, rxpv, rypv, wallx1, wally1, wallx2, wally2);
            crashed2 = Line2D.linesIntersect(rxp, ryp, rxpv, rypv, wallx2, wally1, wallx1, wally2);
            if (crashed1 || crashed2) {
                Coordinate crashSpot = new Coordinate(walls.get(i).x, walls.get(i).y);
                System.out.println("Crashed");
                crash(crashSpot); //perform the crash at that wall
                return true;
            }
        }
        return false;
    }

    public Coordinate getAcceleration(int move) {
        Coordinate a = null;
        switch (move) {
            case (0): //N
                a = new Coordinate(0, -1);
                break;
            case (1): //NE
                a = new Coordinate(1, -1);
                break;
            case (2): //E
                a = new Coordinate(1, 0);
                break;
            case (3): //SE
                a = new Coordinate(1, 1);
                break;
            case (4): //S
                a = new Coordinate(0, 1);
                break;
            case (5): //SW
                a = new Coordinate(-1, 1);
                break;
            case (6): //W
                a = new Coordinate(-1, 0);
                break;
            case (7): //NW
                a = new Coordinate(-1, -1);
                break;
            default: //SXSW
                a = new Coordinate(0, 0);
                break;
        }
        return a;
    }

    public Coordinate getSafeAcceleration(int move) { //keep an acceleration that will follow the path as straightforward as possible
        Coordinate a = null;
        if (racer.getXVel() != 0 || racer.getYVel() != 0) { //if the velocity is not 0
            Coordinate old = racer.getPrevAccel();
            //find an acceleration that will make it 0
            if (old.x == 0 && old.y == -1) {
                a = new Coordinate(0, 1);
            } else if (old.x == 1 && old.y == -1) {
                a = new Coordinate(-1, 1);
            } else if (old.x == 1 && old.y == 0) {
                a = new Coordinate(-1, 0);
            } else if (old.x == 1 && old.y == 1) {
                a = new Coordinate(-1, -1);
            } else if (old.x == 0 && old.y == 1) {
                a = new Coordinate(0, -1);
            } else if (old.x == -1 && old.y == 1) {
                a = new Coordinate(1, -1);
            } else if (old.x == -1 && old.y == 0) {
                a = new Coordinate(1, 0);
            } else if (old.x == -1 && old.y == -1) {
                a = new Coordinate(1, 1);
            }
        } else { //if acceleration is not 0 find one that will follow the path
            switch (move) {
                case (0): //N
                    a = new Coordinate(0, -1);
                    break;
                case (1): //NE
                    a = new Coordinate(1, -1);
                    break;
                case (2): //E
                    a = new Coordinate(1, 0);
                    break;
                case (3): //SE
                    a = new Coordinate(1, 1);
                    break;
                case (4): //S
                    a = new Coordinate(0, 1);
                    break;
                case (5): //SW
                    a = new Coordinate(-1, 1);
                    break;
                case (6): //W
                    a = new Coordinate(-1, 0);
                    break;
                case (7): //NW
                    a = new Coordinate(-1, -1);
                    break;
                default:
                    a = new Coordinate(0, 0);
                    break;
            }
        }
        if(a == null){
            System.out.println("Null");
        }
        return a;
    }

    public void crash(Coordinate c) {
        if (crashType == 0) { //soft crashing
            softCrash(c);
        } else if (crashType == 1) {//hard crashing
            hardCrash();
        }
    }

    public void softCrash(Coordinate c) {
        //set racer's velocity to 0
        racer.setVelocity(0, 0);

        //set position to closest valid position
        Coordinate spot = spotNearCrash(c.x, c.y);
        setRacer(spot);
    }

    public void hardCrash() {
        //set racer's velocity to 0
        racer.setVelocity(0, 0);

        //reset the racer to the previous starting position
        setRacer(startPoint);

    }

    public void getCrashableWalls() { //creates a list of the crashable walls
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == '#') {
                    if (nearTrack(j, i)) {
                        Coordinate w = new Coordinate(j, i);
                        walls.add(w);
                    }
                }
            }
        }
    }

    public void getAbsorbtion() {
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                if (track[i][j] == 'S') {
                    Coordinate s = new Coordinate(j, i);
                    start.add(s);
                } else if (track[i][j] == 'F') {
                    Coordinate f = new Coordinate(j, i);
                    finish.add(f);
                }
            }
        }
    }

    public Coordinate spotNearCrash(int x, int y) { //return the closest spot on the track next to the crashsite
        char[] safe = {'.', 'F', 'S', 'R'};
        ArrayList<Coordinate> list = new ArrayList<>();
        Coordinate spot = null;

        for (int i = 0; i < safe.length; i++) {
            try {
                if (track[y + 1][x] == safe[i]) {
                    Coordinate c = new Coordinate(x, y + 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x] == safe[i]) {
                    Coordinate c = new Coordinate(x, y - 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y][x + 1] == safe[i]) {
                    Coordinate c = new Coordinate(x + 1, y);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y][x - 1] == safe[i]) {
                    Coordinate c = new Coordinate(x - 1, y);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y + 1][x + 1] == safe[i]) {
                    Coordinate c = new Coordinate(x + 1, y + 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y + 1][x - 1] == safe[i]) {
                    Coordinate c = new Coordinate(x - 1, y + 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x + 1] == safe[i]) {
                    Coordinate c = new Coordinate(x + 1, y - 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x - 1] == safe[i]) {
                    Coordinate c = new Coordinate(x - 1, y - 1);
                    list.add(c);
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }
        Random r = new Random();
        int index = r.nextInt(list.size());
        spot = list.get(index);

        return spot;
    }

    public boolean nearTrack(int x, int y) { //check if the wall is near the track or not
        char[] safe = {'.', 'F', 'S'};

        for (int i = 0; i < safe.length; i++) {
            try {
                if (track[y + 1][x] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y][x + 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y][x - 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y + 1][x + 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y + 1][x - 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x + 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
            try {
                if (track[y - 1][x - 1] == safe[i]) {
                    return true;
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return false;
    }

    public Coordinate getFinishLinePoint() {
        return this.finishLinePoint;
    }

    public void printTrack() {
        System.out.println("");
        for (int i = 0; i < track.length; i++) {
            for (int j = 0; j < track[0].length; j++) {
                System.out.print(track[i][j]);
            }
            System.out.println();
        }
        racer.printStats();
    }

    public void printWalls() {
        System.out.println("walls:");
        System.out.println("walls size:" + walls.size());
        for (int i = 0; i < walls.size(); i++) {
            System.out.println("X: " + walls.get(i).x + "  Y: " + walls.get(i).y);
        }

    }

    public void printAbsorbtion() {
        System.out.println("START");
        for (int i = 0; i < start.size(); i++) {
            System.out.println("X: " + start.get(i).x + "  Y: " + start.get(i).y);
        }
        System.out.println("FINISH");
        for (int i = 0; i < finish.size(); i++) {
            System.out.println("X: " + finish.get(i).x + "  Y: " + finish.get(i).y);
        }
    }

}
