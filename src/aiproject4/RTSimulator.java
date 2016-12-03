package aiproject4;

import tracks.RaceTrack;

public class RTSimulator {

    private char[][] track;
    
    public RTSimulator(char c) {
        
        readMap(c);
    }

    public void readMap(char c) {
        RaceTrack rt = new RaceTrack(c);
        track = rt.getTrack();
    }

}
