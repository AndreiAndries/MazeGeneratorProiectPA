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
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class MazeGenerator3DPerfect implements Runnable{
        private int[][][] maze;
        private final int backgroundCode = 0;
        private final int wallCode = 1;
        private final int pathCode = 2;
        private final int emptyCode = 3;
        private final int visitedCode = 4;
        private final int upColor = 5;
        private final int downColor = 6;

        private Canvas canvas;
        private Canvas canvas2;
        private GraphicsContext g;
        private GraphicsContext g2;
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
        Button switchButton = new Button("Switch");
        ChoiceBox<String> speed = new ChoiceBox<>();
        ChoiceBox<String> format = new ChoiceBox<>();
        javafx.scene.control.TextField url;
        public static Stage stage = new Stage();

        MazeGenerator3DPerfect(int size,Color wall,Color cell,Color path){
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
                    cell,
                    Color.YELLOW,
                    Color.PURPLE
            };
        }

        public void display(){
            maze = new int[rows][columns][2];
            canvas = new Canvas(columns*blockSize, rows*blockSize);
            canvas2 = new Canvas(columns*blockSize, rows*blockSize);
            g = canvas.getGraphicsContext2D();
            g.setFill(color[backgroundCode]);
            g.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            g2 = canvas2.getGraphicsContext2D();
            g2.setFill(color[backgroundCode]);
            g2.fillRect(0,0,canvas2.getWidth(),canvas2.getHeight());
            Pane root = new Pane(canvas);
            Pane root2 = new Pane(canvas2);
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
            bottomMenu.getChildren().addAll(speed,startButton,stopButton,saveButton,format,url,switchButton);
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
                        if(maze[i][j][0]==-1)
                            System.out.print(9 + " ");
                        else System.out.print(maze[i][j][0] + " ");
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
                                if(maze[i][j][0]==emptyCode)
                                    out.print("1");
                                else if(maze[i][j][0]==wallCode)
                                    out.print("*");
                                else if(maze[i][j][0]==upColor)
                                    out.print("#");
                            }
                            out.print("\n");
                        }
                        out.print("\n\n\n\n\n");
                        for (int i = 0; i < rows; ++i) {
                            for (int j = 0; j < columns; ++j) {
                                if(maze[i][j][1]==emptyCode)
                                    out.print("1");
                                else if(maze[i][j][1]==wallCode)
                                    out.print("*");
                                else if(maze[i][j][1]==downColor)
                                    out.print("#");
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
                                if (maze[i][j][0] == wallCode) {
                                    if (color[wallCode] == Color.RED)
                                        g2d.setColor(java.awt.Color.RED);
                                    else if (color[wallCode] == Color.BLACK)
                                        g2d.setColor(java.awt.Color.BLACK);
                                    else
                                        g2d.setColor(java.awt.Color.GRAY);
                                } else if (maze[i][j][0] == pathCode) {
                                    if (color[pathCode] == Color.rgb(128, 128, 255))
                                        g2d.setColor(java.awt.Color.cyan);
                                    else if (color[pathCode] == Color.PINK)
                                        g2d.setColor(java.awt.Color.pink);
                                    else
                                        g2d.setColor(java.awt.Color.CYAN);
                                }else if(maze[i][j][0]==upColor){
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
                        File file = new File(saveUrl+"\\stage0.png");
                        try {
                            ImageIO.write(bufferedImage, "png", file);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        BufferedImage bufferedImage1 = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                        String saveUrl1 = saveUrl;
                        Graphics2D g2d1 = bufferedImage1.createGraphics();
                        for (int i = 0; i < rows; ++i) {
                            for (int j = 0; j < columns; ++j) {
                                if (maze[i][j][1] == wallCode) {
                                    if (color[wallCode] == Color.RED)
                                        g2d1.setColor(java.awt.Color.RED);
                                    else if (color[wallCode] == Color.BLACK)
                                        g2d1.setColor(java.awt.Color.BLACK);
                                    else
                                        g2d1.setColor(java.awt.Color.GRAY);
                                } else if (maze[i][j][1] == pathCode) {
                                    if (color[pathCode] == Color.rgb(128, 128, 255))
                                        g2d1.setColor(java.awt.Color.cyan);
                                    else if (color[pathCode] == Color.PINK)
                                        g2d1.setColor(java.awt.Color.pink);
                                    else
                                        g2d1.setColor(java.awt.Color.CYAN);
                                }else if(maze[i][j][1]==downColor){
                                    g2d1.setColor(java.awt.Color.MAGENTA);
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
                        File file1 = new File(saveUrl+"\\stage1.png");
                        try {
                            ImageIO.write(bufferedImage1, "png", file1);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        break;
                    case "JPG":
                        BufferedImage bufferedImage2 = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                        String saveUrl2 = url.getText();
                        Graphics2D g2d2 = bufferedImage2.createGraphics();
                        for (int i = 0; i < rows; ++i) {
                            for (int j = 0; j < columns; ++j) {
                                if (maze[i][j][0] == wallCode) {
                                    if (color[wallCode] == Color.RED)
                                        g2d2.setColor(java.awt.Color.RED);
                                    else if (color[wallCode] == Color.BLACK)
                                        g2d2.setColor(java.awt.Color.BLACK);
                                    else
                                        g2d2.setColor(java.awt.Color.GRAY);
                                } else if (maze[i][j][0] == pathCode) {
                                    if (color[pathCode] == Color.rgb(128, 128, 255))
                                        g2d2.setColor(java.awt.Color.cyan);
                                    else if (color[pathCode] == Color.PINK)
                                        g2d2.setColor(java.awt.Color.pink);
                                    else
                                        g2d2.setColor(java.awt.Color.CYAN);
                                }else if(maze[i][j][0]==upColor){
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
                        File file2 = new File(saveUrl2+"\\stage0.jpg");
                        try {
                            ImageIO.write(bufferedImage2, "jpg", file2);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        BufferedImage bufferedImage3 = new BufferedImage(columns * blockSize, rows * blockSize, BufferedImage.TYPE_INT_RGB);
                        String saveUrl3 = saveUrl2;
                        Graphics2D g2d3 = bufferedImage3.createGraphics();
                        for (int i = 0; i < rows; ++i) {
                            for (int j = 0; j < columns; ++j) {
                                if (maze[i][j][1] == wallCode) {
                                    if (color[wallCode] == Color.RED)
                                        g2d3.setColor(java.awt.Color.RED);
                                    else if (color[wallCode] == Color.BLACK)
                                        g2d3.setColor(java.awt.Color.BLACK);
                                    else
                                        g2d3.setColor(java.awt.Color.GRAY);
                                } else if (maze[i][j][1] == pathCode) {
                                    if (color[pathCode] == Color.rgb(128, 128, 255))
                                        g2d3.setColor(java.awt.Color.cyan);
                                    else if (color[pathCode] == Color.PINK)
                                        g2d3.setColor(java.awt.Color.pink);
                                    else
                                        g2d3.setColor(java.awt.Color.CYAN);
                                }else if(maze[i][j][1]==downColor){
                                    g2d3.setColor(java.awt.Color.MAGENTA);
                                } else {
                                    if (color[emptyCode] == Color.WHITE)
                                        g2d3.setColor(java.awt.Color.WHITE);
                                    else if (color[emptyCode] == Color.GREEN)
                                        g2d3.setColor(java.awt.Color.GREEN);
                                    else
                                        g2d3.setColor(java.awt.Color.BLUE);
                                }

                                g2d3.fillRect(j * blockSize, i * blockSize, blockSize, blockSize);

                            }

                        }
                        g2d3.dispose();
                        File file3 = new File(saveUrl3+"\\stage1.jpg");
                        try {
                            ImageIO.write(bufferedImage3, "jpg", file3);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        break;
                }

            });
            AtomicBoolean switchScene = new AtomicBoolean(true);
            switchButton.setOnAction(e->{
                if(switchScene.get()){
                    mainMaze.setCenter(root2);
                    switchScene.set(false);
                }
                else if(!switchScene.get()){
                    mainMaze.setCenter(root);
                    switchScene.set(true);
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

        void drawSquare1 (int row, int column, int colorCode){
            Platform.runLater( () -> {
                g2.setFill(color[colorCode]);
                int x = blockSize * column;
                int y = blockSize * row;
                g2.fillRect(x,y,blockSize,blockSize);
            });
        }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        }catch(InterruptedException e) { }
        makeMaze(1,1,0);
        synchronized (this){
            try {
                wait(sleepTime);
            }catch (InterruptedException e) {}
        }
    }

    public void makeMaze(int inputRow,int inputColumn,int inputLevel){
        int row = inputRow;
        int column = inputColumn;
        int level = inputLevel;
        for(int i = 0 ; i<rows ; ++i) {
            maze[i][0][0] = 100;
            maze[i][columns-1][0] = 100;
            maze[i][0][1] = 100;
            maze[i][columns-1][1] = 100;
        }
        for(int i = 0 ; i<columns ; ++i) {
            maze[0][i][0] = 100;
            maze[rows-1][i][0] = 100;
            maze[0][i][1] = 100;
            maze[rows-1][i][1] = 100;
        }

        for(int i=1;i<rows-1;++i){
            for(int j=1;j<columns-1;++j){
                maze[i][j][0] = -1;
                maze[i][j][1] = -1;
            }
        }
        drawSquare(row,column,emptyCode);
        maze[row][column][level]= emptyCode;
        Stack<Pair<Pair<Integer,Integer>,Integer>> stack = new Stack<>();
        stack.push(new Pair<>(new Pair<>(row,column),level));
        while(stack.size()>0){

            synchronized (this){
                try { wait(speedSleep);}
                catch (InterruptedException e){ }
            }
            List<Pair<Pair<Integer,Integer>, Integer>> neighbours = new ArrayList<Pair<Pair<Integer,Integer>,Integer>>();
            if(row-2 > 0){
                if(maze[row-2][column][level] < 0)
                    neighbours.add(new Pair<>(new Pair<>(row-2,column),level));
            }
            if( row+2< rows-1 ){
                if(maze[row+2][column][level] < 0 )
                    neighbours.add(new Pair<>(new Pair<>(row+2,column),level));
            }

            if(column-2>0) {
                if (maze[row][column - 2][level] < 0)
                    neighbours.add(new Pair<>(new Pair(row, column - 2),level));
            }
            if(column + 2 < columns -1) {
                if (maze[row][column + 2][level] < 0 && column + 2 < columns - 1)
                    neighbours.add(new Pair<>(new Pair<>(row, column + 2),level));
            }
            if(level == 0){
                if (maze[row][column][level+1] < 0)
                    neighbours.add(new Pair<>(new Pair<>(row, column),level+1));
            }
            else
            {
                if (maze[row][column][level-1] < 0)
                    neighbours.add(new Pair<>(new Pair<>(row, column),level-1));
            }

            if(neighbours.size()!=0) {
                int next = (int) (Math.random() * neighbours.size());
                int row1 = neighbours.get(next).getKey().getKey();
                int column1 = neighbours.get(next).getKey().getValue();
                int level1 = neighbours.get(next).getValue();
                if(row==row1 && level == level1){
                    if(column1>column){
                        maze[row][column+1][level]=emptyCode;
                        maze[row][column+2][level]=emptyCode;
                        if(level==0) {
                            drawSquare(row, column + 1, emptyCode);
                            drawSquare(row, column + 2, emptyCode);
                        }
                        else {
                            drawSquare1(row, column + 1, emptyCode);
                            drawSquare1(row, column + 2, emptyCode);
                        }
                        synchronized (this){
                            try { wait(speedSleep);}
                            catch (InterruptedException e){ }
                        }
                    }
                    else{
                        maze[row][column-1][level]=emptyCode;
                        maze[row][column-2][level]=emptyCode;
                        if(level==0) {
                            drawSquare(row, column - 1, emptyCode);
                            drawSquare(row, column - 2, emptyCode);
                        }
                        else {
                            drawSquare1(row, column - 1, emptyCode);
                            drawSquare1(row, column - 2, emptyCode);
                        }
                        synchronized (this){
                            try { wait(speedSleep);}
                            catch (InterruptedException e){ }
                        }
                    }
                }
                if(column1 == column && level == level1) {
                    if (row1 > row) {
                        maze[row + 1][column][level] = emptyCode;
                        maze[row + 2][column][level] = emptyCode;
                        if(level == 0) {
                            drawSquare(row + 1, column, emptyCode);
                            drawSquare(row + 2, column , emptyCode);
                        }
                        else {
                            drawSquare1(row + 1, column , emptyCode);
                            drawSquare1(row + 2, column , emptyCode);
                        }
                        synchronized (this) {
                            try {
                                wait(speedSleep);
                            } catch (InterruptedException e) {
                            }
                        }
                    } else {
                        maze[row - 1][column][level] = emptyCode;
                        maze[row - 2][column][level] = emptyCode;
                        if(level==0) {
                            drawSquare(row - 1, column, emptyCode);
                            drawSquare(row - 2, column , emptyCode);
                        }
                        else {
                            drawSquare1(row - 1, column , emptyCode);
                            drawSquare1(row - 2, column , emptyCode);
                        }
                        synchronized (this) {
                            try {
                                wait(speedSleep);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
                if(column==column1 && row == row1)
                {
                    if(level < level1) {
                        maze[row][column][level+1] = downColor;
                        maze[row][column][level] = upColor;
                        drawSquare(row,column,upColor);
                        drawSquare1(row,column,downColor);
                    }else{
                        maze[row][column][level-1] = upColor;
                        maze[row][column][level] = downColor;
                        drawSquare(row,column,upColor);
                        drawSquare1(row,column,downColor);
                    }
                }
                row = row1;
                column = column1;
                level = level1;
                stack.push(new Pair<>(new Pair<>(row,column),level));
            }
            else{
                row = stack.peek().getKey().getKey();
                column = stack.peek().getKey().getValue();
                level = stack.peek().getValue();
                stack.pop();
            }
        }
        for(int i = 0; i <rows;++i){
            for (int j = 0 ; j<columns;++j){
                if(maze[i][j][0]==100 || maze[i][j][0]==-1){
                    maze[i][j][0]=wallCode;
                }
                if(maze[i][j][1]==100 || maze[i][j][1]==-1){
                    maze[i][j][1]=wallCode;
                }
            }
        }
    }

}
