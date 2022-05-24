package com.banking_application.scenes;

import com.banking_application.BankAccount;
import com.banking_application.Card;
import com.banking_application.Customer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class transitionController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;
    private BankAccount selectedBankAccount;
    private Card selectedCard;
    private Thread delayThread;

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
    private Label textLabel1;
    @FXML
    private Label textLabel2;
    @FXML
    private Label processBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer, BankAccount bankAccount, String text1, String text2) {
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();
        this.selectedBankAccount = bankAccount;

        this.textLabel1.setText(text1);
        this.textLabel2.setText(text2);
    }

    public void initData(Customer customer, Card card, String text1, String text2) {
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();
        this.selectedCard = card;

        this.textLabel1.setText(text1);
        this.textLabel2.setText(text2);
    }

    public void transition(String transitionTo){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(800),e->{
                    this.processBar.setText(this.processBar.getText() + "." );
                }),
                new KeyFrame(Duration.millis(1600),e->{
                    this.processBar.setText(this.processBar.getText() + "." );
                }),
                new KeyFrame(Duration.millis(2400),e->{
                    this.processBar.setText(this.processBar.getText() + "." );
                }),

                new KeyFrame(Duration.millis(3000),e->{
                    try {

                        if (transitionTo.equals("bankAccount")) {
                            switchToSpecificAccountsScreen();
                        } else if (transitionTo.equals("card")){
                            switchToCardSettingScreen(this.selectedCard);
                        } else if (transitionTo.equals("cardOverview")){
                            switchToCardsSettingsScreen();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                })
        );
        timeline.playFromStart();
    }

    public void switchToCardsSettingsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("cardsSettingsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        cardsSettingsController cardsSettingsController = loader.getController();
        cardsSettingsController.initData(this.loggedInCustomer);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCardSettingScreen(Card selectedCard) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("cardSettingScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        cardSettingController cardSettingController = loader.getController();
        cardSettingController.initData(this.loggedInCustomer,selectedCard);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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


    public void switchToSpecificAccountsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("accountsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        accountsController accountsController = loader.getController();
        accountsController.initData(this.loggedInCustomer, this.selectedBankAccount);
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
