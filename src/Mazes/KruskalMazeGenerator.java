package Mazes;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Vector;

/**
 * 1. Se alege un punct random de plecare
 *
 * 2. Se alege în mod aleatoriu un perete în acel punct și se construieste un
 * pasaj către celula adiacentă, dar numai dacă celula adiacentă nu a
 * fost încă vizitată. Aceasta devine noua celula curenta.
 *
 * 3. Daca toate celulele adiacente au fost vizitate ne intoarcem la
 * ultima celulă care are pereți necizelați și se repeta
 *
 * 4. Algoritmul se termină atunci când procesul sa întors până la punctul
 * de plecare
 *
 *
 * Throw all of the edges in the graph into a big burlap sack. (Or, you know, a set or something.)
 * Pull out the edge with the lowest weight. If the edge connects two disjoint trees, join the trees. Otherwise, throw that edge away.
 * Repeat until there are no more edges left.
 * */

public class KruskalMazeGenerator implements Runnable,Maze{

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

    public KruskalMazeGenerator(int size, Color wall, Color cell, Color path){
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
        //fuctia display este functia ce reprezinta partea de front-end al oricarui maze
        //aici se fac toate satarile pentru front-endul de generare de maze
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
        url = new TextField();
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
        stopButton.setOnAction(e->{runner.stop();});
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
                        BufferedImage bufferedImage2 = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                        String saveUrl2= url.getText();
                        Graphics2D g2d2 = bufferedImage2.createGraphics();
                        for (int i = 0; i < rows; ++i) {
                            for (int j = 0; j < columns; ++j) {
                                if (maze[i][j] == wallCode) {
                                    if (color[wallCode] == Color.RED)
                                        g2d2.setColor(java.awt.Color.RED);
                                    else if (color[wallCode] == Color.BLACK)
                                        g2d2.setColor(java.awt.Color.BLACK);
                                    else
                                        g2d2.setColor(java.awt.Color.GRAY);
                                } else if (maze[i][j] == pathCode) {
                                    if (color[pathCode] == Color.rgb(128, 128, 255))
                                        g2d2.setColor(java.awt.Color.cyan);
                                    else if (color[pathCode] == Color.PINK)
                                        g2d2.setColor(java.awt.Color.pink);
                                    else
                                        g2d2.setColor(java.awt.Color.YELLOW);
                                } else {
                                    if (color[emptyCode] == Color.WHITE)
                                        g2d2.setColor(java.awt.Color.WHITE);
                                    else if (color[emptyCode] == Color.GREEN)
                                        g2d2.setColor(java.awt.Color.GREEN);
                                    else
                                        g2d2.setColor(java.awt.Color.BLUE);
                                }

                                g2d2.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);

                            }

                        }

                        g2d2.dispose();
                        File file2 = new File(saveUrl2);
                        try {
                            ImageIO.write(bufferedImage2, "png", file2);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                            break;
                        }
                }
        });

    }

    //functia dezeneaza un patrat ce poate sa reprezinte diverse celule din maze
    public void drawSquare(int row, int column, int colorCode){
        Platform.runLater( () -> {
            g.setFill(color[colorCode]);
            int x = blockSize * column;
            int y = blockSize * row;
            g.fillRect(x,y,blockSize,blockSize);
        });
    }

    @Override
    public void run() {//functia run pentru thread
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) { }
        makeMaze(1,1);
        solveMaze(1,1);
        synchronized (this){
            try {
                wait(sleepTime);
            }catch (InterruptedException e) {}
        }
    }

    public void makeMaze(int inputRow,int inputColumn){//generarea maze-ului
        int i,j;
        int emptyCt = 0; //numarul de camere
        int wallCt = 0; //numarul de ziduri
        int[] wallrow = new int [(rows * columns)/2];//zidurile de pe randuri
        int[] wallcol = new int [(rows*columns)/2];//zidurile de pe coloane
        for (i=0;i<rows;++i){
            for(j=0;j<columns;++j){
                maze[i][j] = wallCode;
            }
        }
        for(i=1;i<rows-1;i+=2){
            for(j=1;j<columns-1;j+=2){
                emptyCt++;
                //se pun primele coloane goale
                //se iau din 2 in 2
                //si se salveaza zidurile
                maze[i][j] = -emptyCt;
                if(i<rows-2){
                    wallrow[wallCt] = i+1;
                    wallcol[wallCt] = j;
                    wallCt++;
                }
                if(j<columns-2){
                    wallrow[wallCt] = i;
                    wallcol[wallCt] = j+1;
                    wallCt++;
                }
            }
        }
        Platform.runLater( () ->{
            g.setFill(color[emptyCode]);//se alege culoare de desenare pentru celule
            for (int r=0;r<rows;++r){
                for(int c = 0; c < columns; ++c){
                    if(maze[r][c]<0)//se verifica daca o celula reprezinta o celula goala sau un zid
                        g.fillRect(c*blockSize,r*blockSize,blockSize,blockSize);//se deseneaza patratele
                }
            }
        });
        synchronized (this){
            try { wait(1000);
            }catch (InterruptedException e){

            }
        }
        int r;
        for(i = wallCt - 1 ; i > 0 ; i--){
            r = (int)(Math.random() * i);//se alege un zid rangom
            tearDown(wallrow[r],wallcol[r]);//se apeleaza functia tear down
            wallrow[r] = wallrow[i];//se schiba valoarea wallului
            wallcol[r] = wallcol[i];//se schimba valoarea coloanei
        }
        for (i=1; i<rows-1;++i){
            for(j=1;j<columns-1; j++) {
                if (maze[i][j] < 0) {//se pun valorile pentru celule in matrice in caz ca se gasesc valori negative
                    maze[i][j] = emptyCode;
                }
            }
        }
        synchronized (this) {
            try {
                wait(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    void tearDown(int row,int col){
        if(row % 2 == 1 && maze[row][col-1] != maze[row][col+1]){
            //se verifica daca randul este impar si daca doua celule vecine pe coloane celulei curente au cod diferit
            fill(row, col-1, maze[row][col-1],maze[row][col+1]);//in caz afirmativ se creeaza un pasaj intre celule
            maze[row][col] = maze[row][col+1];
            drawSquare(row,col,emptyCode);//se deseneaza celulele goale
            synchronized (this){
                try {
                    wait(speedSleep);
                }catch (InterruptedException e){}
            }
        }
        else if(row % 2 == 0 && maze[row-1][col] != maze[row+1][col]){
            //se verifica daca randul este par si daca doua celule vecine pe randuri celulei curente au cod diferit
            fill(row - 1, col, maze[row-1][col],maze[row+1][col]);//se creeaza pasaj
            maze[row][col]=maze[row+1][col];
            drawSquare(row,col,emptyCode);//se deseneaza celulele goale
            synchronized (this){
                try { wait(speedSleep);}
                catch (InterruptedException e){ }
            }
        }
    }

    void fill(int row,int col, int replace, int replaceWith){//functie care schimba valorile celulelor din maze
        if(maze[row][col] == replace){
            maze[row][col] = replaceWith;
            fill(row+1,col,replace,replaceWith);
            fill(row-1,col,replace,replaceWith);
            fill(row,col+1,replace,replaceWith);
            fill(row,col-1,replace,replaceWith);
        }
    }

    boolean solveMaze(int row,int col){
        //solver pentru maze
        //algoritmul lui Lee
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
