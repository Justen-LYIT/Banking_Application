package com.banking_application.scenes;

import com.banking_application.Card;
import com.banking_application.CreditCard;
import com.banking_application.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class requestCreditCardController implements Initializable {
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

    @FXML
    private Button applyCard1;
    @FXML
    private Button applyCard2;
    @FXML
    private Button applyCard3;



    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer){
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        for( Card card : this.loggedInCustomer.getOwnedAssets().retrieveOwnedCreditCards()){
            if (card.getCardName().equals("Student Credit Card")){
                applyCard1.setDisable(true);
            }
            if (card.getCardName().equals("Gold Credit Card")){
                applyCard2.setDisable(true);
            }
            if (card.getCardName().equals("Platinum Credit Card")){
                applyCard3.setDisable(true);
            }

        }
    }


    public void requestCard1() throws IOException {
        requestNewCreditCard(1000,"Student Credit Card");
    }

    public void requestCard2() throws IOException {
        requestNewCreditCard(5000,"Gold Credit Card");
    }

    public void requestCard3() throws IOException {
        requestNewCreditCard(10000,"Platinum Credit Card");
    }

    public void requestNewCreditCard(double debtLimit, String cardName) throws IOException {
        CreditCard card = this.loggedInCustomer.createNewCreditCard(debtLimit, cardName , cardName);
        switchToTransitionScreen(card);
    }



    public void switchToTransitionScreen(Card card) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("transitionScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        transitionController transitionController = loader.getController();
        transitionController.initData(this.loggedInCustomer
                ,card
                ,"Credit Card Application was Successful"
                , "Proceeding to your new Credit Card" );
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        transitionController.transition("card");
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
