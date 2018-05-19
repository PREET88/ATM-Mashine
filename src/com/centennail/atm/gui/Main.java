package com.centennail.atm.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        GUI_Class gui = new GUI_Class();
        primaryStage.setScene(new Scene(gui));
        primaryStage.setTitle("ATM Mashine");
        primaryStage.show();

       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


    }


    public static void main(String[] args) {

        launch(args);
    }
}
