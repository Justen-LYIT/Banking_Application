package com.banking_application.scenes;

import com.banking_application.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class homePageController implements Initializable {
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

    private static final DecimalFormat df = new DecimalFormat("0.00");
    @FXML
    private Label assetTotalFigureLabel;
    @FXML
    private Label assetCreditFigureLabel;
    @FXML
    private Label assetDebtFigureLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer){
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        welcomeLabel.setText("Welcome %s!".formatted(this.loggedInCustomer.getFirstName()));
        assetTotalFigureLabel.setText("€ " +  df.format(loggedInCustomer.getOwnedAssets().retrieveAccountValue())) ;
        if (loggedInCustomer.getOwnedAssets().retrieveAccountValue() > 0){
            assetTotalFigureLabel.getStyleClass().add("greenText");
        } else if (loggedInCustomer.getOwnedAssets().retrieveAccountValue() < 0){
            assetTotalFigureLabel.getStyleClass().add("redText");
        }
        assetCreditFigureLabel.setText("€ " +  df.format(loggedInCustomer.getOwnedAssets().retrieveCreditValue())) ;
        if (loggedInCustomer.getOwnedAssets().retrieveCreditValue() > 0){
            assetCreditFigureLabel.getStyleClass().add("greenText");
        } else if (loggedInCustomer.getOwnedAssets().retrieveCreditValue() < 0){
            assetCreditFigureLabel.getStyleClass().add("redText");
        }
        assetDebtFigureLabel.setText("€ " +  df.format(loggedInCustomer.getOwnedAssets().retrieveDebitValue())) ;
        if (loggedInCustomer.getOwnedAssets().retrieveDebitValue() > 0){
            assetDebtFigureLabel.getStyleClass().add("greenText");
        } else if (loggedInCustomer.getOwnedAssets().retrieveDebitValue() < 0){
            assetDebtFigureLabel.getStyleClass().add("redText");
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
