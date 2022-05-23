package com.banking_application.scenes;

import com.banking_application.Authentication;
import com.banking_application.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;


    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void switchToStartUpScreen(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass().getResource(("startUpScene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomePage(ActionEvent event, Customer customer) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("homePageScreen.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        homePageController homePageController = loader.getController();
        homePageController.initData(customer);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void authenticateCheck(ActionEvent event) throws IOException{
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();
        Authentication authentication = new Authentication();
        if (authentication.loginAttempt(userName, password) ) {
            switchToHomePage(event, authentication.findCustomerViaUsername(userName));
        } else {
            System.out.println(authentication.loginFailError(userName,password));
//            TODO provide error label
        }
    }



}