package tracks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * Group 22: Ben Rhuman, Isaac Sotelo, Danny Kumpf
 */

public class RaceTrack {

    private char[][] track;
    private char shape;

    public RaceTrack(char c) {
        shape = c;
        readMap();
        //printTrack();
        
    }

    public void readMap() {

        String filePath = new File("").getAbsolutePath() + "/src/tracks/" + fileName(); //Creates the file path of the desired data set for windows

        File file = new File(filePath);

        if (file.isFile()) {
            BufferedReader inputStream = null;
            try {
                inputStream = new BufferedReader(new FileReader(file));

                String line = inputStream.readLine();
                String[] tokens = line.split(",");

                int ySize = Integer.parseInt(tokens[0]);
                int xSize = Integer.parseInt(tokens[1]);
                
                this.track = new char[ySize][xSize];
                int spot;
                
                for(int i = 0; i < ySize; i++){
                    for(int j = 0; j < xSize; j++){
                       
                        char ch; 
                        do{
                        spot = inputStream.read();
                        ch = (char) spot;
                        } while (ch == '\r' || ch == '\n');
                        this.track[i][j] = ch;
                        
                    }
                }
            } catch (FileNotFoundException ex) {
                System.out.println("file not found");;
            } catch (IOException ex) {
              
            }
        } else {
            System.out.println("File not found");
        }
        //printTrack();
    }

    public String fileName() {
        if (shape == 'l' || shape == 'L') {
            return "L-track.txt";
        } else if (shape == 'o' || shape == 'O') {
            return "O-track.txt";
        } else if (shape == 'r' || shape == 'R') {
            return "R-track.txt";
        } else {
            return null;
        }
    }
    
    public void printTrack(){
        for(int i = 0; i < track.length; i++){
            for(int j = 0; j < track[0].length; j++){
                System.out.print(track[i][j]);
            }
            System.out.println();
        }
    }
    
    public char[][] getTrack(){
        return track;
    }
    
    public void setTrack(char c){
        this.shape = c;
        readMap();
    }

}
