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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class registerController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private String username;
    private String password;

//    Page 1
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField1;
    @FXML
    private TextField passwordField2;
    @FXML
    private AnchorPane usernameError;
    @FXML
    private AnchorPane passwordError1;
    @FXML
    private AnchorPane passwordError2;

    //Page 2
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField middleNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private DatePicker dateOfBirthField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField address1Field;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField address3Field;
    @FXML
    private TextField cityField;
    @FXML
    private TextField zipField;
    @FXML
    private ChoiceBox countriesAvailabe;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if( this.userNameField != null) {
            this.userNameField.setOnMouseMoved(mouseEvent -> {
                if (!usernameIsAvailable()) {
                    usernameError.setVisible(true);
                }
            });
            this.userNameField.setOnMouseEntered(mouseEvent -> {
                if (!usernameIsAvailable()) {
                    usernameError.setVisible(true);
                }
            });
            this.userNameField.setOnMouseExited(mouseEvent -> {
                usernameError.setVisible(false);
            });


            this.passwordField1.setOnMouseMoved(mouseEvent -> {
                if (!passwordsAreMatching() && passwordField1.getText().length() > 0 && passwordField2.getText().length() > 0) {
                    passwordError1.setVisible(true);
                }
            });
            this.passwordField1.setOnMouseEntered(mouseEvent -> {
                if (!passwordsAreMatching() && passwordField1.getText().length() > 0 && passwordField2.getText().length() > 0) {
                    passwordError1.setVisible(true);
                }
            });
            this.passwordField1.setOnMouseExited(mouseEvent -> {
                passwordError1.setVisible(false);
            });


            this.passwordField2.setOnMouseMoved(mouseEvent -> {
                if (!passwordsAreMatching() && passwordField1.getText().length() > 0 && passwordField2.getText().length() > 0) {
                    passwordError2.setVisible(true);
                }
            });
            this.passwordField2.setOnMouseEntered(mouseEvent -> {
                if (!passwordsAreMatching() && passwordField1.getText().length() > 0 && passwordField2.getText().length() > 0) {
                    passwordError2.setVisible(true);
                }
            });
            this.passwordField2.setOnMouseExited(mouseEvent -> {
                passwordError2.setVisible(false);
            });
        }
    }

    public void initData(String username, String password){
        this.username = username;
        this.password = password;
    }

    public boolean usernameIsAvailable(){
        Authentication authentication = new Authentication();
        return ! authentication.usernameExists(userNameField.getText());
    }


    public void checkUsernameAvailable()  {
        if( ! usernameIsAvailable() ){
            userNameField.getStyleClass().add("incorrectTextInput");
        } else {
            userNameField.getStyleClass().removeAll("incorrectTextInput");
            this.usernameError.setVisible(false);
        }
    }

    public boolean passwordsAreMatching(){
        return passwordField1.getText().equals(passwordField2.getText());
    }



    public void checkMatchingPasswords() {
        if(passwordField1.getText().length() == 0 || passwordField2.getText().length() == 0 ){
            return;
        }

        if(!passwordsAreMatching()){
            passwordField1.getStyleClass().add("incorrectTextInput");
            passwordField2.getStyleClass().add("incorrectTextInput");
        } else {
            passwordField1.getStyleClass().removeAll("incorrectTextInput");
            passwordField2.getStyleClass().removeAll("incorrectTextInput");
        }
    }

    public void checkLoginCredentials(ActionEvent event) throws IOException {
        Authentication authentication = new Authentication();
        if(  (!   (userNameField.getText().length() == 0) ||
                (authentication.usernameExists(userNameField.getText())) ||
                (passwordField1.getText().length() == 0 || passwordField2.getText().length() == 0  ) ||
                (!passwordField1.getText().equals(passwordField2.getText())) )  &&
        passwordField1.getText().equals(passwordField2.getText()) ){
            switchToRegister2Scene(event);
        }
    }




    public void finaliseCreateAccount(ActionEvent event) throws IOException{
        Boolean errorFree = true;
        TextField[] mandatoryFields = {firstNameField
                ,lastNameField
                ,phoneField
                ,address1Field
                ,cityField
                ,zipField};
//        Checking if all the textfields are filled out correctly
        for(TextField mandatoryField:mandatoryFields){
            if (mandatoryField.getText().length() == 0) {
                errorFree = false;
                mandatoryField.getStyleClass().add("incorrectTextInput");
            } else {
                mandatoryField.getStyleClass().removeAll("incorrectTextInput");
            }
        }

//        checking if the other types are filled out correctly
        if (dateOfBirthField.getValue() == null) {
            errorFree = false;
            dateOfBirthField.getStyleClass().add("incorrectTextInput");
        } else {
            dateOfBirthField.getStyleClass().removeAll("incorrectTextInput");
        }
        if (countriesAvailabe.getSelectionModel().isEmpty()){
            countriesAvailabe.getStyleClass().add("incorrectTextInput");
            errorFree = false;
        }else {
            countriesAvailabe.getStyleClass().removeAll("incorrectTextInput");
        }

        if (errorFree) {
            Authentication authentication = new Authentication();
            authentication.createAccount(this.username
                    ,firstNameField.getText()
                    ,middleNameField.getText()
                    ,lastNameField.getText()
                    ,dateOfBirthField.getValue().toString()
                    ,this.password
                    ,phoneField.getText()
                    ,address1Field.getText()
                    ,address2Field.getText()
                    ,address3Field.getText()
                    ,cityField.getText()
                    ,zipField.getText()
                    ,countriesAvailabe.getValue().toString());
                switchToHomePage(event, authentication.findCustomerViaUsername(this.username));

        }
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


    public void switchToStartUpScreen(ActionEvent event) throws IOException {
        parent = FXMLLoader.load(getClass().getResource(("startUpScene.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    public void switchToRegister2Scene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("registerScene2.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        registerController registerController = loader.getController();
        registerController.initData(userNameField.getText(),passwordField1.getText());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
