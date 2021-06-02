package Design;

import Mazes.*;
import javafx.scene.paint.Color;
/**
 * in aceasta clasa se construiesc datele in functie de maze-ul care se doreste a fi implementat
 * dupa ce constructorul modifica variabilele globale se apeleaza metoda start maze in care se deschide noul maze care se va construi
 * */

public class MazeGenerator {

    private String dimension;
    private int Size;
    private String username;
    private String algorithm;
    private Color wallColor;
    private Color cellColor;
    private Color pathColor;

    public MazeGenerator(String dimension, int size, String username, String algorithm, Color wallColor, Color cellColor, Color pathColor) {
        //constructor
        this.dimension = dimension;
        Size = size;
        this.username = username;
        this.algorithm = algorithm;
        this.wallColor = wallColor;
        this.cellColor = cellColor;
        this.pathColor = pathColor;
    }

    public void startMaze(){//metoda care verifica daca maze-ul carfe se vrea a fi construit este 2D sau 3D
        //in functie de dimensiune se construieste maze-ul dorit
        if(dimension.equals("2D")){
            switch (algorithm) {
                case "rka":
                    KruskalMazeGenerator kruskalMazeGenerator = new KruskalMazeGenerator(Size,wallColor,cellColor,pathColor);
                    kruskalMazeGenerator.display();
                    break;
                case "haka":
                    HuntAndKillMazeGenerator huntAndKillMazeGenerator = new HuntAndKillMazeGenerator(Size,wallColor,cellColor,pathColor);
                    huntAndKillMazeGenerator.display();
                    break;
                case "gto":
                    GrowingTreeOldest growingTreeOldest = new GrowingTreeOldest(Size,wallColor,cellColor,pathColor);
                    growingTreeOldest.display();
                    break;
                case "gtn":
                    GrowingTreeNewest growingTreeNewest = new GrowingTreeNewest(Size,wallColor,cellColor,pathColor);
                    growingTreeNewest.display();
                    break;
                case "gtr":
                    GrowingTreeRandom growingTreeRandom = new GrowingTreeRandom(Size,wallColor,cellColor,pathColor);
                    growingTreeRandom.display();
                    break;
            }
        }
        else{
            if(username.equals("yes")) {
                MazeGenerator3DPerfect mazeGenerator3D = new MazeGenerator3DPerfect(Size, wallColor, cellColor, pathColor);
                mazeGenerator3D.display();
            }
            else{
                MazeGenerator3D mazeGenerator3D = new MazeGenerator3D(Size, wallColor, cellColor, pathColor);
                mazeGenerator3D.display();
            }
        }
    }
}
