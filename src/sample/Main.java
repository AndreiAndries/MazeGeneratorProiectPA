package sample;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    Stage window;
    String algorithmSelected;
    String dimensionSelected;
    int sizeSelected;
    String usersName = "no";
    Color pathColor;
    Color wallColor;
    Color cellColor;

    //setting up the layouts for the first screen
    BorderPane initialLayout = new BorderPane();
    BorderPane settingsLayout = new BorderPane();
    VBox initialMenu = new VBox(30);
    HBox bottomMenu = new HBox(30);
    VBox settingsMenu = new VBox(10);
    HBox bottomSettingsMenu = new HBox(30);


    //Setting up menu bar
    MenuBar menuBar = new MenuBar();
    Menu algorithmMenu = new Menu("_Algorithm");
    Menu mazeDimension = new Menu("_Dimension");
    Menu mazeSize = new Menu("_Size");

    ToggleGroup algorithm = new ToggleGroup();
    ToggleGroup dimension = new ToggleGroup();
    ToggleGroup size = new ToggleGroup();


    RadioMenuItem haka = new RadioMenuItem("Hunt and Kill algorithm");
    RadioMenuItem rka = new RadioMenuItem("Randomized Kruskal's Algorithm");
    RadioMenuItem gtr = new RadioMenuItem("Growing tree Random");
    RadioMenuItem gto = new RadioMenuItem("Growing tree Oldest");
    RadioMenuItem gtn = new RadioMenuItem("Growing tree Newest");

    RadioMenuItem d2D = new RadioMenuItem("2D");
    RadioMenuItem d3D = new RadioMenuItem("3D");

    RadioMenuItem s40x40 = new RadioMenuItem("40x40");
    RadioMenuItem s50x50 = new RadioMenuItem("50x50");
    RadioMenuItem s30x30 = new RadioMenuItem("30x30");
    RadioMenuItem s20x20 = new RadioMenuItem("20x20");
    RadioMenuItem s10x10 = new RadioMenuItem("10x10");

    Scene scene;
    Scene settingsScene;

    ChoiceBox<String> wallChoise = new ChoiceBox<>();
    ChoiceBox<String> pathChoise = new ChoiceBox<>();
    ChoiceBox<String> cellChoise = new ChoiceBox<>();
    ChoiceBox<String> algorithmChoise = new ChoiceBox<>();
    ChoiceBox<String> dimensionChoise = new ChoiceBox<>();
    ChoiceBox<String> sizeChoise = new ChoiceBox<>();

    Button closeButton = new Button("Close");
    Button newButton = new Button("New");
    Button settingsButton = new Button("Settings");
    Button setButton = new Button("SET");

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        window=primaryStage;
        window.setTitle("Welcome to our application");
        window.setOnCloseRequest(e->{
            e.consume();
            closeProgram();
        });
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        initialMenu.setPadding(new Insets(20,20,20,20));
        bottomMenu.setPadding(new Insets(20,20,20,20));
        bottomMenu.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET,CornerRadii.EMPTY,Insets.EMPTY)));
        bottomMenu.setMinHeight(100);
        setToggleAlgorithm();
        setToggleDimension();
        setToggleSize();

        setAlgorithmSelectedAction();
        setDimensionSelectedAction();
        setSizeSelectedAction();

        Button testButton = new Button("Print Data");
        testButton.setBackground(new Background(new BackgroundFill(Color.LINEN,new CornerRadii(20), Insets.EMPTY)));
        testButton.setTextFill(Color.web("#0100FF",0.8));
        testButton.setStyle("-fx-font-weight: bold");
        Label label = new Label("Perfect 3D maze?");
        label.setStyle("-fx-font-weight: bold");
        label.setTextFill(Color.web("#FFFFFF",0.8));
        label.setFont(new Font(20));
        VBox vBox = new VBox(5);
        TextField username = new TextField();
        username.setPromptText("yes/no");
        username.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));
        vBox.getChildren().addAll(label,username);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET,CornerRadii.EMPTY,Insets.EMPTY)));
        testButton.setOnAction(e ->{
            System.out.println(algorithmSelected);
            System.out.println(dimensionSelected);
            System.out.println(sizeSelected);
            System.out.println(wallColor);
            System.out.println(cellColor);
            System.out.println(pathColor);
            usersName = username.getText();
            System.out.println(usersName);
            System.out.println();
        });
        generateInitialMenu();
        bottomMenu.getChildren().addAll(vBox,testButton);
        bottomMenu.setAlignment(Pos.CENTER_RIGHT);
        menuBar.getMenus().addAll(algorithmMenu,mazeDimension,mazeSize);
        initialLayout.setTop(menuBar);
        initialLayout.setBottom(bottomMenu);
        initialLayout.setCenter(initialMenu);

        setInitialColors();
        setBottomSettings();
        setSettingsMenu();
        settingsLayout.setCenter(settingsMenu);
        settingsLayout.setBottom(bottomSettingsMenu);
        window.setResizable(false);
        scene = new Scene(initialLayout,400,500);
        settingsScene = new Scene(settingsLayout,400,500);
        window.setScene(scene);
        window.show();
    }

    private void setSettingsMenu() {
        settingsMenu.setAlignment(Pos.CENTER);
        settingsMenu.setBackground(new Background(new BackgroundFill(Color.DARKBLUE,CornerRadii.EMPTY,Insets.EMPTY)));
        Label algorithmTxt = new Label("Please choose the algorithm");
        algorithmTxt.setStyle("-fx-font-weight: bold");
        algorithmTxt.setTextFill(Color.web("#FFFFFF",0.8));
        algorithmTxt.setFont(new Font(20));
        VBox alg = new VBox();
        alg.setAlignment(Pos.CENTER);
        algorithmChoise.getItems().addAll(
                "Hunt and Kill algorithm",
                "Randomized Kruskal's Algorithm",
                "Growing tree Random",
                "Growing tree Oldest",
                "Growing tree Newest");
        algorithmChoise.setValue("Recursive Back Tracker");
        alg.getChildren().addAll(algorithmTxt,algorithmChoise);
        Label dimensionTxt = new Label("Please choose the dimension");
        dimensionTxt.setStyle("-fx-font-weight: bold");
        dimensionTxt.setTextFill(Color.web("#FFFFFF",0.8));
        dimensionTxt.setFont(new Font(20));
        VBox dim = new VBox();
        dim.setAlignment(Pos.CENTER);
        dimensionChoise.getItems().addAll("2D","3D");
        dimensionChoise.setValue("2D");
        dimensionChoise.setMinWidth(200);
        dim.getChildren().addAll(dimensionTxt, dimensionChoise);
        Label sizeTxt = new Label("Please choose the size");
        sizeTxt.setStyle("-fx-font-weight: bold");
        sizeTxt.setTextFill(Color.web("#FFFFFF",0.8));
        sizeTxt.setFont(new Font(20));
        VBox sz = new VBox();
        sz.setAlignment(Pos.CENTER);
        sizeChoise.getItems().addAll("10","20","30","40","50");
        sizeChoise.setValue("40");
        sizeChoise.setMinWidth(200);
        sz.getChildren().addAll(sizeTxt,sizeChoise);
        Label wallTxt = new Label("Please choose wall's color");
        wallTxt.setStyle("-fx-font-weight: bold");
        wallTxt.setTextFill(Color.web("#FFFFFF",0.8));
        wallTxt.setFont(new Font(20));
        VBox wal = new VBox();
        wal.setAlignment(Pos.CENTER);
        wallChoise.getItems().addAll("red","black","gray");
        wallChoise.setValue("red");
        wallChoise.setMinWidth(200);
        wal.getChildren().addAll(wallTxt,wallChoise);
        Label cellTxt = new Label("Please choose cell's color");
        cellTxt.setStyle("-fx-font-weight: bold");
        cellTxt.setTextFill(Color.web("#FFFFFF",0.8));
        cellTxt.setFont(new Font(20));
        VBox cl = new VBox();
        cl.setAlignment(Pos.CENTER);
        cellChoise.getItems().addAll("white", "blue", "green");
        cellChoise.setValue("white");
        cellChoise.setMinWidth(200);
        cl.getChildren().addAll(cellTxt,cellChoise);
        Label pathTxt = new Label("Please choose path's color");
        pathTxt.setStyle("-fx-font-weight: bold");
        pathTxt.setTextFill(Color.web("#FFFFFF",0.8));
        pathTxt.setFont(new Font(20));
        VBox pth = new VBox();
        pth.setAlignment(Pos.CENTER);
        pathChoise.getItems().addAll("Azure", "Pink" , "Lime Green");
        pathChoise.setValue("Azure");
        pathChoise.setMinWidth(200);
        pth.getChildren().addAll(pathTxt,pathChoise);
        settingsMenu.getChildren().addAll(alg,dim,sz,wal,cl,pth);
    }


    private void setBottomSettings() {
        bottomSettingsMenu.setAlignment(Pos.CENTER_RIGHT);
        bottomSettingsMenu.setMinHeight(80);
        setButton.setBackground(new Background(new BackgroundFill(Color.LINEN,new CornerRadii(20), Insets.EMPTY)));
        setButton.setTextFill(Color.web("#0100FF",0.8));
        setButton.setStyle("-fx-font-weight: bold");
        setButton.setFont(new Font(25));
        setButton.setMinWidth(100);
        setButton.setMaxWidth(100);
        setButton.setMinHeight(60);
        setButton.setMaxHeight(60);
        setButton.setOnAction(e->{
            getSettingsItems();
            window.setScene(scene);
        });
        bottomSettingsMenu.setPadding(new Insets(10,10,10,10));
        bottomSettingsMenu.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET,CornerRadii.EMPTY,Insets.EMPTY)));
        bottomSettingsMenu.getChildren().addAll(setButton);
    }

    private void getSettingsItems() {
        String algorithmSwitch = algorithmChoise.getValue();
        String dimensionSwitch = dimensionChoise.getValue();
        String sizeSwitch = sizeChoise.getValue();
        String wallSwitch = wallChoise.getValue();
        String cellSwitch = cellChoise.getValue();
        String pathSwitch = pathChoise.getValue();
        switch (algorithmSwitch){

            case "Hunt and Kill algorithm" :
                algorithmSelected = "haka";
                haka.setSelected(true);
                rka.setSelected(false);
                gto.setSelected(false);
                gtn.setSelected(false);
                gtr.setSelected(false);
                break;
            case "Randomized Kruskal's Algorithm":
                algorithmSelected = "rka";
                haka.setSelected(false);
                rka.setSelected(true);
                gto.setSelected(false);
                gtn.setSelected(false);
                gtr.setSelected(false);
                break;
            case "Growing tree Random" :
                algorithmSelected = "gtr";
                haka.setSelected(false);
                rka.setSelected(false);
                gto.setSelected(false);
                gtn.setSelected(false);
                gtr.setSelected(true);
                break;
            case "Growing tree Oldest" :
                algorithmSelected = "gto";
                haka.setSelected(false);
                rka.setSelected(false);
                gto.setSelected(true);
                gtn.setSelected(false);
                gtr.setSelected(false);
                break;
            case "Growing tree Newest" :
                algorithmSelected = "gtn";
                haka.setSelected(false);
                rka.setSelected(false);
                gto.setSelected(false);
                gtn.setSelected(true);
                gtr.setSelected(false);
                break;
            default:
                algorithmSelected = "rbt";
                break;
        }

        switch (dimensionSwitch){
            case "2D" :
                dimensionSelected = "2D";
                d2D.setSelected(true);
                d3D.setSelected(false);
                break;
            case "3D" :
                dimensionSelected = "3D";
                d2D.setSelected(false);
                d3D.setSelected(true);
                break;
            default:
                dimensionSelected = "2D";
                break;
        }
        switch (sizeSwitch){
            case  "10" :
                sizeSelected = 10;
                s40x40.setSelected(false);
                s50x50.setSelected(false);
                s20x20.setSelected(false);
                s30x30.setSelected(false);
                s10x10.setSelected(true);
                break;
            case "20" :
                sizeSelected = 20;
                s40x40.setSelected(false);
                s50x50.setSelected(false);
                s20x20.setSelected(true);
                s30x30.setSelected(false);
                s10x10.setSelected(false);
                break;
            case "30" :
                sizeSelected = 30;
                s40x40.setSelected(false);
                s50x50.setSelected(false);
                s20x20.setSelected(false);
                s30x30.setSelected(true);
                s10x10.setSelected(false);
                break;
            case "40" :
                sizeSelected = 40;
                s40x40.setSelected(true);
                s50x50.setSelected(false);
                s20x20.setSelected(false);
                s30x30.setSelected(false);
                s10x10.setSelected(false);
                break;
            case "50" :
                sizeSelected = 50;
                s40x40.setSelected(false);
                s50x50.setSelected(true);
                s20x20.setSelected(false);
                s30x30.setSelected(false);
                s10x10.setSelected(false);
                break;
        }
        switch (wallSwitch){
            case "red" :
                wallColor = Color.RED;
                break;
            case "black" :
                wallColor = Color.BLACK;
                break;
            case "gray" :
                wallColor = Color.GRAY;
        }
        switch (cellSwitch){
            case "white" :
                cellColor = Color.WHITE;
                break;
            case "blue" :
                cellColor = Color.BLUE;
                break;
            case "green" :
                cellColor = Color.GREEN;
                break;
        }

        switch (pathSwitch){
            case "Azure" :
                pathColor = Color.rgb(128,128,255);
                break;
            case "Pink" :
                pathColor = Color.PINK;
                break;
            case "Lime Green" :
                pathColor = Color.LIMEGREEN;
                break;
        }
    }


    public void setToggleAlgorithm(){
        haka.setSelected(true);
        algorithmSelected = "haka";
        haka.setToggleGroup(algorithm);
        rka.setToggleGroup(algorithm);
        gtr.setToggleGroup(algorithm);
        gto.setToggleGroup(algorithm);
        gtn.setToggleGroup(algorithm);
        algorithmMenu.getItems().addAll(haka,rka,gtn,gto,gtr);
    }

    public void setToggleDimension(){
        d2D.setToggleGroup(dimension);
        d2D.setSelected(true);
        dimensionSelected = "2D";
        d3D.setToggleGroup(dimension);
        mazeDimension.getItems().addAll(d2D,d3D);
    }

    public void setInitialColors(){
        wallColor = Color.RED;
        cellColor = Color.WHITE;
        pathColor = Color.rgb(128,128,255);
    }
    public void setToggleSize(){
        s40x40.setToggleGroup(size);
        s40x40.setSelected(true);
        sizeSelected = 40;
        s50x50.setToggleGroup(size);
        s10x10.setToggleGroup(size);
        s20x20.setToggleGroup(size);
        s30x30.setToggleGroup(size);

        mazeSize.getItems().addAll(s10x10,s20x20,s30x30,s40x40,s50x50);
    }

    public void setAlgorithmSelectedAction(){

        rka.setOnAction(e->{
            algorithmSelected = "rka";
            haka.setSelected(false);
            rka.setSelected(true);
            gto.setSelected(false);
            gtn.setSelected(false);
            gtr.setSelected(false);
        });
        haka.setOnAction(e->{
            algorithmSelected = "haka";
            haka.setSelected(true);
            rka.setSelected(false);
            gto.setSelected(false);
            gtn.setSelected(false);
            gtr.setSelected(false);
        });
        gto.setOnAction(e->{
            algorithmSelected = "gto";
            haka.setSelected(true);
            rka.setSelected(false);
            gto.setSelected(true);
            gtn.setSelected(false);
            gtr.setSelected(false);
        });
        gtn.setOnAction(e->{
            algorithmSelected = "gtn";
            haka.setSelected(true);
            rka.setSelected(false);
            gto.setSelected(false);
            gtn.setSelected(true);
            gtr.setSelected(false);
        });
        gtr.setOnAction(e->{
            algorithmSelected = "gtr";
            haka.setSelected(true);
            rka.setSelected(false);
            gto.setSelected(false);
            gtn.setSelected(false);
            gtr.setSelected(false);
        });
    }

    public void setDimensionSelectedAction(){
        d2D.setOnAction(e->{
            d2D.setSelected(true);
            d3D.setSelected(false);
            dimensionSelected = "2D";
        });
        d3D.setOnAction(e->{
            d2D.setSelected(false);
            d3D.setSelected(true);
            dimensionSelected = "3D";
        });
    }

    public void setSizeSelectedAction(){
        s40x40.setOnAction(e->{
            s40x40.setSelected(true);
            s50x50.setSelected(false);
            s10x10.setSelected(false);
            s20x20.setSelected(false);
            s30x30.setSelected(false);
            sizeSelected = 40;
        });
        s50x50.setOnAction(e->{
            s40x40.setSelected(false);
            s50x50.setSelected(true);
            s10x10.setSelected(false);
            s20x20.setSelected(false);
            s30x30.setSelected(false);
            sizeSelected = 50;
        });
        s20x20.setOnAction(e->{
            s40x40.setSelected(false);
            s50x50.setSelected(false);
            s20x20.setSelected(true);
            s10x10.setSelected(false);
            s30x30.setSelected(false);
            sizeSelected = 20;
        });
        s30x30.setOnAction(e->{
            s40x40.setSelected(false);
            s50x50.setSelected(false);
            s10x10.setSelected(false);
            s30x30.setSelected(true);
            s20x20.setSelected(false);
            sizeSelected = 30;
        });
        s10x10.setOnAction(e->{
            s40x40.setSelected(false);
            s50x50.setSelected(false);
            s20x20.setSelected(false);
            s30x30.setSelected(false);
            s10x10.setSelected(true);
            sizeSelected = 10;
        });
    }

    private void closeProgram(){
        boolean answer = ConfirmBox.display("Are you sure?","Are you sure you want to exit?");
        if(answer) {
            window.close();
            System.out.println("Closed program!");
        }
    }

    public void generateInitialMenu(){
        initialMenu.setAlignment(Pos.CENTER);
        initialMenu.setBackground(new Background(new BackgroundFill(Color.DARKBLUE,CornerRadii.EMPTY,Insets.EMPTY)));

        newButton.setBackground(new Background(new BackgroundFill(Color.LINEN,new CornerRadii(20), Insets.EMPTY)));
        newButton.setTextFill(Color.web("#0100FF",0.8));
        newButton.setStyle("-fx-font-weight: bold");
        Font font = new Font(30);
        newButton.setMinWidth(150);
        newButton.setMinHeight(75);
        newButton.setFont(font);
        newButton.setOnAction(e->{
            MazeGenerator mazeGenerator = new MazeGenerator(dimensionSelected,sizeSelected,usersName,algorithmSelected,wallColor,cellColor,pathColor);
            mazeGenerator.startMaze();
        });

        closeButton.setBackground(new Background(new BackgroundFill(Color.LINEN,new CornerRadii(20), Insets.EMPTY)));
        closeButton.setTextFill(Color.web("#0100FF",0.8));
        closeButton.setStyle("-fx-font-weight: bold");
        closeButton.setMinWidth(150);
        closeButton.setMinHeight(75);
        closeButton.setFont(font);
        closeButton.setOnAction(e->{
            e.consume();
            closeProgram();
        });

        settingsButton.setBackground(new Background(new BackgroundFill(Color.LINEN,new CornerRadii(20), Insets.EMPTY)));
        settingsButton.setTextFill(Color.web("#0100FF",0.8));
        settingsButton.setStyle("-fx-font-weight: bold");
        settingsButton.setMinWidth(150);
        settingsButton.setMinHeight(75);
        settingsButton.setFont(font);
        settingsButton.setOnAction(e -> window.setScene(settingsScene));
        initialMenu.getChildren().addAll(newButton,settingsButton,closeButton);
    }
}
