package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {

    static boolean answer;
    public static boolean display(String title,String message) {
        Stage window = new Stage();
        window.setResizable(false);
        window.initModality(Modality.APPLICATION_MODAL);//we are going to block other windows until we finish all work in current window
        window.setTitle(title);
        window.setMinWidth(400);
        Label label = new Label();
        label.setText(message);
        label.setTextFill(Color.web("#FFFFFF",0.8));
        Button yesButton = new Button("Yes");
        yesButton.setTextFill(Color.web("#FFFFFF",0.8));
        yesButton.setBackground(new Background(new BackgroundFill(Color.BLUE,new CornerRadii(10), Insets.EMPTY)));
        yesButton.setOnAction(e ->{
            answer = true;
            window.close();
        });
        Button noButton = new Button("No");
        noButton.setBackground(new Background(new BackgroundFill(Color.RED,new CornerRadii(10),Insets.EMPTY)));
        noButton.setTextFill(Color.web("#FFFFFF",0.9));
        noButton.setOnAction(e->{
            answer = false;
            window.close();
        });
        VBox layout = new VBox(10);
        layout.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGREY,CornerRadii.EMPTY,Insets.EMPTY)));
        HBox layout1 = new HBox(20);
        layout1.setBackground(new Background(new BackgroundFill(Color.DARKSLATEGREY,CornerRadii.EMPTY,Insets.EMPTY)));
        layout1.getChildren().addAll(yesButton,noButton);
        layout1.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label,layout1);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,60,80);
        window.setScene(scene);
        window.showAndWait();
        return answer;
    }
}