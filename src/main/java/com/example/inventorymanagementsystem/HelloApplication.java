package com.example.inventorymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HelloApplication extends Application  {
    private double x=0;
    private double y=0;
    @Override
    public void start(Stage stage) throws IOException {
        Image image=new Image(getClass().getResource("/logo.png").toExternalForm());
        stage.getIcons().add(image);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        Scene scene = new Scene(root);
        root.setOnMousePressed((MouseEvent event) -> {
            x= event.getSceneX();
            y=event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) ->{
            stage.setX(event.getScreenX()-x);
            stage.setY((event.getSceneY()-y));
            stage.setOpacity(.8);
        });
        root.setOnMouseReleased((MouseEvent event)->{
            stage.setOpacity(1);
        });
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Point of Sales");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}