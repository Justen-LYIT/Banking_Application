package com.banking_application.scenes;

import com.banking_application.BankAccount;
import com.banking_application.Customer;
import com.banking_application.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

public class accountsController {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;
    private BankAccount selectedBankAccount;
    private static final DecimalFormat df = new DecimalFormat("0.00");

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
    private ScrollPane accountOptionsScroll;
    @FXML
    private HBox accountOptionsScrollPane;
    @FXML
    private TableView tableViewAccountTransactions;

    public void initData(Customer customer, BankAccount bankAccount) throws IOException{
        this.selectedBankAccount=bankAccount;
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();
        this.initData(customer);
        createTransactionTable();
    }

    public void initData(Customer customer) throws IOException{
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        bankAccounts.add(this.loggedInCustomer.retrieveCurrentAccount());
        bankAccounts.addAll(this.loggedInCustomer.retrieveSavingAccounts());
        bankAccounts.addAll(this.loggedInCustomer.retrieveDebtAccounts());

        for (BankAccount bankAccount : bankAccounts) {
            AnchorPane anchorPane = new AnchorPane();
            GridPane gridPane = new GridPane();
            ImageView imageView = new ImageView();
            InputStream imgStream;
            if(bankAccount.getClass().getSimpleName().equals("CurrentAccount")){
                imgStream = new FileInputStream("src/main/resources/com/banking_application/currentAccount.png");
            }else if (bankAccount.getClass().getSimpleName().equals("DebtAccount")){
                imgStream = new FileInputStream("src/main/resources/com/banking_application/debtAccount.png");
            }else if (bankAccount.getClass().getSimpleName().equals("SavingAccount")){
                imgStream = new FileInputStream("src/main/resources/com/banking_application/savingAccount.png");
            }else{
                imgStream = new FileInputStream("src/main/resources/com/banking_application/wallet.png");
            }
            Image image = new Image( imgStream );
            imageView.setImage( image );
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);
            imageView.getStyleClass().add("labelIconAccount");
            Label labelName = new Label(bankAccount.getBankAccountName());
            labelName.getStyleClass().add("labelNameAccount");
            Label labelIBAN = new Label(bankAccount.getIBANNumber());
            labelIBAN.getStyleClass().add("labelIBANAccount");
            Label labelValue = new Label("€ " + df.format(bankAccount.getBalance())) ;
            labelValue.getStyleClass().add("labelBalanceAccount");
            GridPane.setConstraints(labelName , 0 , 0);
            GridPane.setConstraints(imageView , 0 , 1);
            GridPane.setConstraints(labelValue , 0 , 2);
            GridPane.setConstraints(labelIBAN , 0 , 3);
            gridPane.getChildren().add(labelName);
            gridPane.getChildren().add(imageView);
            gridPane.getChildren().add(labelIBAN);
            gridPane.getChildren().add(labelValue);
            GridPane.setHalignment(labelName, HPos.CENTER);
            GridPane.setHalignment(labelIBAN, HPos.CENTER);
            GridPane.setHalignment(labelValue, HPos.CENTER);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setMargin(labelName,new Insets(30,10,0,10));
            GridPane.setMargin(imageView,new Insets(0,10,0,10));
            GridPane.setMargin(labelIBAN,new Insets(0,10,10,10));
            GridPane.setMargin(labelValue,new Insets(0,10,0,10));
            anchorPane.getChildren().add(gridPane);
            anchorPane.getStyleClass().add("bankAccountOption");
            anchorPane.setId(bankAccount.getIBANNumber());
            gridPane.setOnMouseClicked(mouseEvent -> {
                this.selectedBankAccount = bankAccount;
                createTransactionTable();
            });
            accountOptionsScrollPane.getChildren().add(anchorPane);
        }
    }

    public void createTransactionTable(){
        for (Node node : accountOptionsScrollPane.getChildrenUnmodifiable() ) {
            if (node.getStyleClass().contains("bankAccountOption") &&
                !node.getId().equals(this.selectedBankAccount.getIBANNumber())) {
                node.getStyleClass().removeAll("selectedItem");
            } else if (node.getId().equals(this.selectedBankAccount.getIBANNumber())){
                node.getStyleClass().add("selectedItem");
            }
        }

        tableViewAccountTransactions.getItems().clear();
        tableViewAccountTransactions.getColumns().clear();
        //Timestamp Column
        TableColumn<Transaction, Timestamp> timestampColumn = new TableColumn<>("Transaction Date");
        timestampColumn.setMinWidth(160);
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("creationTimestamp"));
        //Type of Transaction Column
        TableColumn<Transaction, Timestamp> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(120);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        //Sender IBAN Column
        TableColumn<Transaction, String> senderColumn = new TableColumn<>("Sender IBAN");
        senderColumn.setMinWidth(180);
        senderColumn.setCellValueFactory(new PropertyValueFactory<>("originAccountIBAN"));
        //Receiver IBAN Column
        TableColumn<Transaction, String> receiverColumn = new TableColumn<>("Receiver IBAN");
        receiverColumn.setMinWidth(180);
        receiverColumn.setCellValueFactory(new PropertyValueFactory<>("targetAccountIBAN"));
        //Memo Column
        TableColumn<Transaction, String> memoColumn = new TableColumn<>("Memo");
        memoColumn.setMinWidth(184);
        memoColumn.setCellValueFactory(new PropertyValueFactory<>("memo"));
        //Credit Value Column
        TableColumn<Transaction, Double> creditColumn = new TableColumn<>("Credit");
        creditColumn.setMinWidth(120);
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("debitValue"));
        //Debit Value Column
        TableColumn<Transaction, Double> debitColumn = new TableColumn<>("Debit");
        debitColumn.setMinWidth(120);
        debitColumn.setCellValueFactory(new PropertyValueFactory<>("creditValue"));


        ObservableList<Transaction> observableList = FXCollections.observableArrayList();
        for (Transaction transaction : this.selectedBankAccount.retrieveTransactions()){
            observableList.add(transaction);
        }
        tableViewAccountTransactions.setItems(observableList);
        tableViewAccountTransactions.getColumns().addAll(timestampColumn, typeColumn, senderColumn, receiverColumn, memoColumn, creditColumn, debitColumn);
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

    public void switchToCreateTransactionScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("createTransactionScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        createTransactionController createTransactionController = loader.getController();
        createTransactionController.initData(this.loggedInCustomer, this.selectedBankAccount);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void switchToTransactionBetweenAccountsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("transactionBetweenAccountsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        transactionBetweenAccountsController transactionBetweenAccountsController = loader.getController();
        transactionBetweenAccountsController.initData(this.loggedInCustomer, this.selectedBankAccount);
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
