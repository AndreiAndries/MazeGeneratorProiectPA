package sample;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HuntAndKillMazeGenerator implements Runnable{
    private int maze[][];

    private final int backgroundCode = 0;
    private final int wallCode = 1;
    private final int pathCode = 2;
    private final int emptyCode = 3;
    private final int visitedCode = 4;

    private Canvas canvas;
    private GraphicsContext g;
    private Color[] color;
    private int rows;
    private int columns;
    private int blockSize;
    private int sleepTime = 4000;
    int speedSleep;//speed of
    private BorderPane mainMaze;
    private HBox bottomMenu;
    Button startButton = new Button("Start");
    Button stopButton = new Button("Stop");
    Button saveButton = new Button("Save");
    ChoiceBox<String> speed = new ChoiceBox<>();
    ChoiceBox<String> format = new ChoiceBox<>();
    javafx.scene.control.TextField url;
    public static Stage stage = new Stage();

    HuntAndKillMazeGenerator(int size,Color wall,Color cell,Color path){
        this.rows = size+1;
        this.columns = size+1;
        switch (size){
            case 10 :
                this.blockSize = 70;
                break;
            case 20 :
                this.blockSize = 35;
                break;
            case 30 :
                this.blockSize = 24;
                break;
            case 40 :
                this.blockSize = 18;
                break;
            case 50 :
                this.blockSize = 15;
                break;
        }
        this.speedSleep = 20;
        color = new Color[] {
                wall,
                wall,
                path,
                cell,
                cell
        };
    }

    public void display(){
        maze = new int[rows][columns];
        canvas = new Canvas(columns*blockSize, rows*blockSize);
        g = canvas.getGraphicsContext2D();
        g.setFill(color[backgroundCode]);
        g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        Pane root = new Pane(canvas);
        mainMaze = new BorderPane();
        mainMaze.setCenter(root);
        bottomMenu = new HBox(30);
        speed = new ChoiceBox<>();
        speed.getItems().addAll("slow","normal","fast");
        speed.setValue("normal");
        format = new ChoiceBox<>();
        format.getItems().addAll("JPG","PNG","TXT");
        format.setValue("TXT");
        url = new TextField("maze.txt");
        url.setPromptText("url...");
        url.setMaxWidth(70);
        bottomMenu.setAlignment(Pos.CENTER);

        bottomMenu.getChildren().addAll(speed,startButton,stopButton,saveButton,format,url);
        mainMaze.setBottom(bottomMenu);
        Scene scene = new Scene(mainMaze);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Maze Generator/Solve");
        stage.show();
        Thread runner = new Thread(this);
        runner.setDaemon(true); //thread-ul nu va opri programul din rulat

        runner.start();
        startButton.setOnAction(e->{
            runner.stop();
            String genetateSpeed = speed.getValue();
            switch (genetateSpeed){
                case "slow" :
                    speedSleep = 40;
                    break;
                case "normal" :
                    speedSleep = 20;
                    break;
                case "fast" :
                    speedSleep = 5;
                    break;
            }
            display();


        });
        stopButton.setOnAction(e->{runner.stop();
        for(int i=1;i<rows-1;++i){
            for (int j = 1 ; j<columns-1;++j){
                if(maze[i][j]==-1)
                    System.out.print(9 + " ");
                else System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        });
        saveButton.setOnAction(e->{
            String saveFormat = format.getValue();
            switch (saveFormat) {
                case "TXT":
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter("maze.txt");
                    } catch (FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                    for (int i = 0; i < rows; ++i) {
                        for (int j = 0; j < columns; ++j) {
                            out.print(maze[i][j]);
                            out.print(" ");
                        }
                        out.print("\n");
                    }
                    out.close();
                    break;
                case "PNG":
                    BufferedImage bufferedImage = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                    String saveUrl = url.getText();
                    Graphics2D g2d = bufferedImage.createGraphics();
                    for (int i = 0; i < rows; ++i) {
                        for (int j = 0; j < columns; ++j) {
                            if (maze[i][j] == wallCode) {
                                if (color[wallCode] == Color.RED)
                                    g2d.setColor(java.awt.Color.RED);
                                else if (color[wallCode] == Color.BLACK)
                                    g2d.setColor(java.awt.Color.BLACK);
                                else
                                    g2d.setColor(java.awt.Color.GRAY);
                            } else if (maze[i][j] == pathCode) {
                                if (color[pathCode] == Color.rgb(128, 128, 255))
                                    g2d.setColor(java.awt.Color.cyan);
                                else if (color[pathCode] == Color.PINK)
                                    g2d.setColor(java.awt.Color.pink);
                                else
                                    g2d.setColor(java.awt.Color.YELLOW);
                            } else {
                                if (color[emptyCode] == Color.WHITE)
                                    g2d.setColor(java.awt.Color.WHITE);
                                else if (color[emptyCode] == Color.GREEN)
                                    g2d.setColor(java.awt.Color.GREEN);
                                else
                                    g2d.setColor(java.awt.Color.BLUE);
                            }

                            g2d.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);

                        }

                    }

                    g2d.dispose();
                    File file = new File(saveUrl);
                    try {
                        ImageIO.write(bufferedImage, "png", file);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                case "JPG":
                    BufferedImage bufferedImage1 = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                    String saveUrl1 = url.getText();
                    Graphics2D g2d1 = bufferedImage1.createGraphics();
                    for (int i = 0; i < rows; ++i) {
                        for (int j = 0; j < columns; ++j) {
                            if (maze[i][j] == wallCode) {
                                if (color[wallCode] == Color.RED)
                                    g2d1.setColor(java.awt.Color.RED);
                                else if (color[wallCode] == Color.BLACK)
                                    g2d1.setColor(java.awt.Color.BLACK);
                                else
                                    g2d1.setColor(java.awt.Color.GRAY);
                            } else if (maze[i][j] == pathCode) {
                                if (color[pathCode] == Color.rgb(128, 128, 255))
                                    g2d1.setColor(java.awt.Color.cyan);
                                else if (color[pathCode] == Color.PINK)
                                    g2d1.setColor(java.awt.Color.pink);
                                else
                                    g2d1.setColor(java.awt.Color.YELLOW);
                            } else {
                                if (color[emptyCode] == Color.WHITE)
                                    g2d1.setColor(java.awt.Color.WHITE);
                                else if (color[emptyCode] == Color.GREEN)
                                    g2d1.setColor(java.awt.Color.GREEN);
                                else
                                    g2d1.setColor(java.awt.Color.BLUE);
                            }

                            g2d1.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);

                        }

                    }

                    g2d1.dispose();
                    File file1 = new File(saveUrl1);
                    try {
                        ImageIO.write(bufferedImage1, "png", file1);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        break;
                    }
            }

        });

    }

    void drawSquare (int row, int column, int colorCode){
        Platform.runLater( () -> {
            g.setFill(color[colorCode]);
            int x = blockSize * column;
            int y = blockSize * row;
            g.fillRect(x,y,blockSize,blockSize);
        });
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) { }
        makeMaze();
        solveMaze(1,1);
        synchronized (this){
            try {
                wait(sleepTime);
            }catch (InterruptedException e) {}
        }
    }

    public void makeMaze(){
        for(int i = 0 ; i<rows ; ++i) {
            maze[i][0] = 100;
            maze[i][columns-1] = 100;
        }
        for(int i = 0 ; i<columns ; ++i) {
            maze[0][i] = 100;
            maze[rows-1][i] = 100;
        }

        for(int i=1;i<rows-1;++i){
            for(int j=1;j<columns-1;++j){
                maze[i][j] = -1;
            }
        }
        for(int i=0;i<rows;++i) {
            for (int j = 0; j < columns; ++j) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        int row = (int) (Math.random() * (rows-2)) + 1 ;
        int column = (int) (Math.random() * (columns-2)) + 1 ;;
        boolean notOver= true;
        while(notOver){
            boolean hasNeighbour = true;
            while (hasNeighbour){
                maze[row][column] = emptyCode;
                List<Pair<Integer, Integer>> neighbours = new ArrayList<Pair<Integer,Integer>>();
                drawSquare(row,column,emptyCode);
                synchronized (this){
                    try { wait(speedSleep);}
                    catch (InterruptedException e){ }
                }
                if(maze[row-1][column] < 0)
                    neighbours.add(new Pair<>(row-1,column));
                if(maze[row+1][column] < 0)
                    neighbours.add(new Pair<>(row+1,column));
                if(maze[row][column-1] < 0)
                    neighbours.add(new Pair<>(row,column-1));
                if(maze[row][column+1] < 0)
                    neighbours.add(new Pair<>(row,column+1));
                if(neighbours.size()!=0){
                    int next = (int) (Math.random() * neighbours.size());
                    int row1 = neighbours.get(next).getKey();
                    int column1 = neighbours.get(next).getValue();
                    if(row == row1)
                    {
                        if(column1>column){
                            if(maze[row -1][column -1]<0)
                                maze[row -1][column -1] = wallCode;
                            if(maze[row+1][column-1]<0)
                                maze[row+1][column-1] = wallCode;
                        }
                        else{
                            if(maze[row -1][column+1]<0)
                                maze[row -1][column+1]=wallCode;
                            if(maze[row +1][column+1]<0)
                                maze[row +1][column+1]=wallCode;
                        }
                    }
                    if(column1 == column)
                    {
                        if(row1>row){
                            if(maze[row -1][column -1]<0)
                                maze[row -1][column -1] = wallCode;
                            if(maze[row -1][column+1]<0)
                                maze[row -1][column+1] = wallCode;
                        }
                        else{
                            if(maze[row +1][column-1]<0)
                                maze[row +1][column-1]=wallCode;
                            if(maze[row +1][column+1]<0)
                                maze[row +1][column+1]=wallCode;
                        }
                    }
                    row = row1;
                    column = column1;
                    neighbours.remove(next);
                    if(neighbours.size()!=0) {
                        for (Pair<Integer, Integer> ngh : neighbours) {
                            maze[ngh.getKey()][ngh.getValue()] = wallCode;
                        }
                    }
                }
                else
                    hasNeighbour = false;
            }
            notOver = false;
            for(int i = 1 ; i < rows - 1 && notOver == false ;i++){
                for (int j = 1 ; j <columns - 1 && notOver == false ; j++){
                    if(maze[i][j] < 0 && maze[i-1][j] == wallCode && maze[i-2][j]== emptyCode  && i-2 >0){
                        row = i;
                        column = j;
                        maze[i-1][j]=emptyCode;
                        drawSquare(i-1,j,emptyCode);
                        notOver = true;
                    }
                    else if(maze[i][j] < 0 && maze[i+1][j] == wallCode && maze[i+2][j]== emptyCode && i+2 <rows){
                        row = i;
                        column = j;
                        maze[i+1][j]=emptyCode;
                        drawSquare(i+1,j,emptyCode);
                        notOver = true;
                    }
                    else if(maze[i][j] < 0 && maze[i][j-1] == wallCode && maze[i][j-2]== emptyCode && j-2 >0){
                        row = i;
                        column = j;
                        maze[i][j-1]=emptyCode;
                        drawSquare(i,j-1,emptyCode);
                        notOver = true;
                    }
                    else if(maze[i][j] < 0 && maze[i][j+1] == wallCode && maze[i][j+2]== emptyCode && j+2 <columns){
                        row = i;
                        column = j;
                        maze[i][j+1]=emptyCode;
                        drawSquare(i,j+1,emptyCode);
                        notOver = true;
                    }
                }
            }
        }
        for(int i = 0 ; i<rows ; ++i) {
            maze[i][0] = wallCode;
            maze[i][columns-1] = wallCode;
        }
        for(int i = 0 ; i<columns ; ++i) {
            maze[0][i] = wallCode;
            maze[rows-1][i] = wallCode;
        }
    }

    boolean solveMaze(int row,int col){
        if(maze[row][col] == emptyCode){
            maze[row][col] = pathCode;
            drawSquare(row,col,pathCode);
            if(row == rows -2 && col == columns-2)
                return true;
            try {
                Thread.sleep(speedSleep);
            }catch (InterruptedException e){}
            if(solveMaze(row-1,col) ||
                    solveMaze(row,col-1) ||
                    solveMaze(row+1,col) ||
                    solveMaze(row,col+1)
            )
                return true;
            maze[row][col] = visitedCode;
            drawSquare(row,col,visitedCode);
            synchronized (this){
                try {
                    wait(speedSleep);
                }catch (InterruptedException e){}
            }
        }
        return false;
    }
}
