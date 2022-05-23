package com.banking_application;
import com.banking_application.scenes.homePageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class UserInterface extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(UserInterface.class.getResource("scenes/startUpScene.fxml"));
////        FXMLLoader fxmlLoader = new FXMLLoader(UserInterface.class.getResource("scenes/loginScene.fxml"));
//        Scene scene = new Scene(fxmlLoader.load());
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("scenes/homePageScreen.fxml"));
        Scene scene = new Scene(loader.load());
        homePageController homePageController = loader.getController();
        Authentication authentication = new Authentication();
        homePageController.initData(authentication.findCustomerViaUsername("EltonJohn"));
        stage.setTitle("Justen's Java Bank");
        stage.getIcons().add(new Image( "file:/src/main/resources/com/banking_application/logoSingle.png"));
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}