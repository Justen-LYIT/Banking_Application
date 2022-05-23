package com.banking_application.scenes;

import com.banking_application.Card;
import com.banking_application.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class cardsSettingsController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;
    private int cardItemsPerRow = 4;

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
    private VBox vboxCardContainer;

    private static final DecimalFormat df = new DecimalFormat("0.00");


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer) throws  IOException{
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();


        ArrayList<Card> ownedCards = this.loggedInCustomer.getOwnedAssets().retrieveOwnedDebitCards();
        ownedCards.addAll(this.loggedInCustomer.getOwnedAssets().retrieveOwnedCreditCards());
        HBox hBox = new HBox();
        for (int i=0;i<ownedCards.size();i++){
            Card itterItem = ownedCards.get(i);
            // Create new VBox upon init and every 4 elements
            if (i%cardItemsPerRow == 0){
                hBox = new HBox();
            }

            // Create container for element
            GridPane gridPane = new GridPane();
            gridPane.setHgap(5);
            gridPane.setVgap(5);

            ImageView imageView = new ImageView();
            InputStream inputStream = new FileInputStream("src/main/resources/com/banking_application/card.png");
            Image image = new Image(inputStream);
            imageView.setImage(image);
            imageView.setFitHeight(70);
            imageView.setFitWidth(70);
            Label cardName = new Label(ownedCards.get(i).getCardName());
            Label cardLast4 = new Label(Long.toString( ownedCards.get(i).getCardNumber()) );
            cardLast4.getStyleClass().add("labelIBANAccount");
            Label connectedIBAN = new Label(ownedCards.get(i).getConnectedBankAccount());
            connectedIBAN.getStyleClass().add("labelIBANAccount");
            GridPane.setConstraints(imageView , 0 , 0);
            GridPane.setConstraints(cardName , 0 , 1);
            GridPane.setConstraints(cardLast4 , 0 , 2);
            GridPane.setConstraints(connectedIBAN , 0 , 3);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setHalignment(cardName, HPos.CENTER);
            GridPane.setHalignment(cardLast4, HPos.CENTER);
            GridPane.setHalignment(connectedIBAN, HPos.CENTER);
            GridPane.setMargin(imageView, new Insets(0,0,0,0));
            gridPane.getChildren().addAll(imageView,cardName,cardLast4,connectedIBAN);
            StackPane stackPane = new StackPane();
            gridPane.setAlignment(Pos.CENTER);
            stackPane.getChildren().add(gridPane);
            stackPane.getStyleClass().add("cardSettingItem");
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setCursor(Cursor.HAND);
            final Card iterCard = ownedCards.get(i);
            stackPane.setOnMouseClicked(mouseEvent -> {
                try {
                    switchToCardSettingScreen(iterCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            hBox.getChildren().add(stackPane);

            // Last element or 3rd element, pushing HBox to DOM
            if (i == ownedCards.size() -1
                    || i%cardItemsPerRow==cardItemsPerRow-1 )  {
                vboxCardContainer.getChildren().add(hBox);
            }

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

}
