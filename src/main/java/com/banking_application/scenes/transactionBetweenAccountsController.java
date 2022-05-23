package com.banking_application.scenes;

import com.banking_application.BankAccount;
import com.banking_application.Customer;
import com.banking_application.DatabaseFileManager;
import com.banking_application.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class transactionBetweenAccountsController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;
    private BankAccount selectedBankAccount;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final Pattern regexIBANPattern = Pattern.compile("^([A-Z]{2}[ \\-]?[0-9]{2})(?=(?:[ \\-]?[A-Z0-9]){9,30}$)((?:[ \\-]?[A-Z0-9]{3,5}){2,7})([ \\-]?[A-Z0-9]{1,3})?$");

    @FXML
    private ChoiceBox fromIBAN;
    @FXML
    private ChoiceBox toIBAN;
    @FXML
    private TextField memo;
    @FXML
    private TextField transactionValueInt;
    @FXML
    private TextField transactionValueDouble;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer, BankAccount bankAccount){
        this.loggedInCustomer=customer;
        this.selectedBankAccount = bankAccount;
        this.loggedInCustomer.initializeCustomer();

        String separatorSpace = "                  € ";
        ArrayList<BankAccount> availableBankAccounts = new ArrayList<>(this.loggedInCustomer.retrieveAllBankAccounts());
        ArrayList<BankAccount> availableBankAccountsTarget = new ArrayList<>(availableBankAccounts);
        if( this.selectedBankAccount!=null){
            fromIBAN.getItems().add(this.selectedBankAccount.getIBANNumber()+separatorSpace+df.format(this.selectedBankAccount.getBalance()));
            availableBankAccounts.remove(this.selectedBankAccount);
            fromIBAN.getSelectionModel().selectFirst();
        }
        for (BankAccount bankAccountIterate : availableBankAccounts) {
            fromIBAN.getItems().add(bankAccountIterate.getIBANNumber() + separatorSpace + df.format(bankAccountIterate.getBalance()));
        }
        for (BankAccount bankAccountIterateTarget: availableBankAccountsTarget) {
            toIBAN.getItems().add(bankAccountIterateTarget.getIBANNumber() + separatorSpace + df.format(bankAccountIterateTarget.getBalance()));
        }
    }


    public void createTransactions() throws IOException,InterruptedException{
        Boolean allowedTransactions = true;
        if(fromIBAN.getValue() == null) {
            fromIBAN.getStyleClass().add("incorrectTextInput");
            allowedTransactions = false;
        } else {
            fromIBAN.getStyleClass().removeAll("incorrectTextInput");
        }

        if(toIBAN.getValue() == null) {
            toIBAN.getStyleClass().add("incorrectTextInput");
            allowedTransactions = false;
        } else {
            toIBAN.getStyleClass().removeAll("incorrectTextInput");
        }

        if(transactionValueInt.getText().matches("^[0-9]+$") == false) {
            transactionValueInt.getStyleClass().add("incorrectTextInput");
            allowedTransactions = false;
        } else {
            transactionValueInt.getStyleClass().removeAll("incorrectTextInput");
        }

        if(transactionValueDouble.getText().matches("^[0-9]{2}$") == false) {
            transactionValueDouble.getStyleClass().add("incorrectTextInput");
            allowedTransactions = false;
        } else {
            transactionValueDouble.getStyleClass().removeAll("incorrectTextInput");
        }


//        Checking if transaction value is allowed
        String totalTransactionValueRaw = transactionValueInt.getText() + "." + transactionValueDouble.getText();
        if(allowedTransactions) {
            Double totalTransactionValue = Double.valueOf(totalTransactionValueRaw);
            if (totalTransactionValue <= 0.0){
                transactionValueInt.getStyleClass().add("incorrectTextInput");
                return;
            } else {
                Pattern extractIBANRegex = Pattern.compile("^([^ ]+)");
                Matcher mFrom = extractIBANRegex.matcher(fromIBAN.getValue().toString());
                Matcher mTo = extractIBANRegex.matcher(toIBAN.getValue().toString());
                if (mFrom.find() && mTo.find()){
                    String selectedFromIBAN = mFrom.group(0);
                    String selectedToIBAN = mTo.group(0);
                    if(selectedFromIBAN.equals(selectedToIBAN)) {
                        toIBAN.getStyleClass().add("incorrectTextInput");
                        fromIBAN.getStyleClass().add("incorrectTextInput");
                    } else {
                        for (BankAccount bankAccount : this.loggedInCustomer.retrieveAllBankAccounts()) {
                            if (bankAccount.getIBANNumber().equals(selectedFromIBAN)) {
                                if (bankAccount.getBalance() >= totalTransactionValue) {
                                    Transaction transaction = new Transaction(totalTransactionValue, selectedFromIBAN, selectedToIBAN, memo.getText(), "Bank Transfer");
                                    DatabaseFileManager databaseFileManager = new DatabaseFileManager();
                                    databaseFileManager.writeTransactionObjectToFile(transaction);
                                    System.out.println("Transaction Created Successfully");
                                    TimeUnit.MILLISECONDS.sleep(300);
                                    this.selectedBankAccount = bankAccount;
                                    switchToSpecificAccountsScreen();
                                } else {
                                    transactionValueInt.getStyleClass().add("incorrectTextInput");
                                }
                            }
                        }
                    }
                } else {
                    fromIBAN.getStyleClass().add("incorrectTextInput");
                }
            }
        }
    }


    public void switchToSpecificAccountsScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("accountsScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        accountsController accountsController = loader.getController();
        accountsController.initData(this.loggedInCustomer,this.selectedBankAccount);
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

