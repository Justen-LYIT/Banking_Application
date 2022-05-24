package com.banking_application.scenes;

import com.banking_application.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class servicesController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;

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
    private AnchorPane creditCardPane;
    @FXML
    private AnchorPane loanPane;
    @FXML
    private AnchorPane savingAccountPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer){
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        creditCardPane.setOnMouseClicked(mouseEvent -> {
            try {
                switchToRequestCreditCardScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        loanPane.setOnMouseClicked(mouseEvent -> {
            try {
                switchToRequestLoanScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        savingAccountPane.setOnMouseClicked(mouseEvent -> {
            try {
                switchToRequestSavingAccountScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    public void switchToRequestLoanScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("requestLoanScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        requestLoanController requestLoanController = loader.getController();
        requestLoanController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRequestCreditCardScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("requestCreditCardScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        requestCreditCardController requestCreditCardController = loader.getController();
        requestCreditCardController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRequestSavingAccountScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("requestSavingAccountScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        requestSavingAccountController requestSavingAccountController = loader.getController();
        requestSavingAccountController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
