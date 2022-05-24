package com.banking_application.scenes;

import com.banking_application.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class editAccountDetailsController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;

    @FXML
    private Label welcomeLabel;
    @FXML
    private ImageView logoutImage;
    @FXML
    private ImageView homePageImage;
    @FXML
    private ImageView accountsImage;
    @FXML
    private ImageView servicesImage;
    @FXML
    private ImageView settingsImage;

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
    }

    public void initData(Customer customer){
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        phoneField.setText(this.loggedInCustomer.getPhone());
        address1Field.setText(this.loggedInCustomer.getAddressLine1());
        address2Field.setText(this.loggedInCustomer.getAddressLine2());
        address3Field.setText(this.loggedInCustomer.getAddressLine3());
        cityField.setText(this.loggedInCustomer.getCity());
        zipField.setText(this.loggedInCustomer.getZip());
        countriesAvailabe.getSelectionModel().select(this.loggedInCustomer.getCountry());
    }

    public void saveAccountDetails() throws IOException{
        Boolean errorFree = true;
        TextField[] mandatoryFields = {
                phoneField
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

        if (countriesAvailabe.getSelectionModel().isEmpty()){
            countriesAvailabe.getStyleClass().add("incorrectTextInput");
            errorFree = false;
        }else {
            countriesAvailabe.getStyleClass().removeAll("incorrectTextInput");
        }

        if(errorFree){
            this.loggedInCustomer.setPhone(phoneField.getText());
            this.loggedInCustomer.setAddressLine1(address1Field.getText());
            this.loggedInCustomer.setAddressLine2(address2Field.getText());
            this.loggedInCustomer.setAddressLine3(address3Field.getText());
            this.loggedInCustomer.setCity(cityField.getText());
            this.loggedInCustomer.setZip(zipField.getText());
            this.loggedInCustomer.setCountry(countriesAvailabe.getValue().toString());

            switchToHomePageScreen();
        }
    }

    public void switchToStartUpScreen() throws IOException {
        parent = FXMLLoader.load(getClass().getResource(("startUpScene.fxml")));
        stage = (Stage) logoutImage.getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomePageScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("homePageScreen.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        homePageController homePageController = loader.getController();
        homePageController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAccountsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("accountsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        accountsController accountsController = loader.getController();
        accountsController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToServicesScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("servicesScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        servicesController servicesController = loader.getController();
        servicesController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSettingsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("settingsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        settingsController settingsController = loader.getController();
        settingsController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
