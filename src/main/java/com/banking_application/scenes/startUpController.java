package com.banking_application.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class startUpController {
    private Scene scene;
    private Stage stage;
    private Parent parent;


    public void switchToLoginScene(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass().getResource(("loginScene.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToRegisterScene(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass().getResource(("registerScene.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
}
