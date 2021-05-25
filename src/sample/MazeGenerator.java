package sample;

import javafx.scene.paint.Color;

import java.util.Locale;

public class MazeGenerator {

    private String dimension;
    private int Size;
    private String username;
    private String algorithm;
    private Color wallColor;
    private Color cellColor;
    private Color pathColor;

    public MazeGenerator(String dimension, int size, String username, String algorithm, Color wallColor, Color cellColor, Color pathColor) {
        this.dimension = dimension;
        Size = size;
        this.username = username;
        this.algorithm = algorithm;
        this.wallColor = wallColor;
        this.cellColor = cellColor;
        this.pathColor = pathColor;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
    }

    public Color getCellColor() {
        return cellColor;
    }

    public void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    public Color getPathColor() {
        return pathColor;
    }

    public void setPathColor(Color pathColor) {
        this.pathColor = pathColor;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void startMaze(){
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

        /*Recursive BackTracker*/
    }
}
