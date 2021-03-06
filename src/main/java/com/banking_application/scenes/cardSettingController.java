package com.banking_application.scenes;


import com.banking_application.BankAccount;
import com.banking_application.Card;
import com.banking_application.Customer;
import com.banking_application.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

    public class cardSettingController implements Initializable {
        private Scene scene;
        private Stage stage;
        private Parent parent;
        private Customer loggedInCustomer;
        private Card selectedCard;
        private BankAccount connectedBankAccount;


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
        private Label cardNameLabel;
        @FXML
        private Label cardNumberLabel;
        @FXML
        private Label cardExpLabel;
        @FXML
        private Label cardCVVLabel;
        @FXML
        private Label cardConnectedIBAN;
        @FXML
        private Button cancelButtonCard;

        @FXML
        private TextField simulateTransactionValue;
        @FXML
        private TextField simulateTransactionValueCents;
        @FXML
        private TextField simulateDepositValue;
        @FXML
        private TextField simulateDepositValueCents;
        @FXML
        private TextField simulateWithdrawalValue;
        @FXML
        private TextField simulateWithdrawalValueCents;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
        }

        public void initData(Customer customer, Card selectedCard){
            this.loggedInCustomer=customer;
            this.loggedInCustomer.initializeCustomer();
            this.selectedCard = selectedCard;

            for (BankAccount bankAccount : this.loggedInCustomer.retrieveAllBankAccounts()){
                if (bankAccount.getIBANNumber().equals(this.selectedCard.getConnectedBankAccount())){
                    this.connectedBankAccount = bankAccount;
                }
            }

            cardNameLabel.setText( selectedCard.getCardName() );
            cardNumberLabel.setText(  String.valueOf( selectedCard.getReadableCardNumber()) );
            cardExpLabel.setText( selectedCard.getExpMonth() + "/" + selectedCard.getExpYear() );
            cardCVVLabel.setText( String.valueOf( selectedCard.getCVV() ) );
            cardConnectedIBAN.setText( selectedCard.getConnectedBankAccount() );
            cardConnectedIBAN.setCursor(Cursor.HAND);
            cardConnectedIBAN.setOnMouseClicked(mouseEvent -> {
                try {
                    switchToSpecificAccountsScreen();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if(this.selectedCard.getClass().getSimpleName().equals("DebitCard")){
                this.cancelButtonCard.setDisable(true);
            }
        }


        public void cancelCard() throws IOException{
            if (this.connectedBankAccount.getBalance() > 0.0 ) {
                // If balance on Debt account is higher than 0, create transaction towards current account of remaining balance, and close debt account
                new Transaction(this.connectedBankAccount.getBalance()
                        , this.connectedBankAccount.getIBANNumber()
                        ,this.loggedInCustomer.retrieveCurrentAccount().getIBANNumber()
                        ,"Positive Balance on Debt Account"
                        , "Closing Debt Account");
                this.loggedInCustomer.getOwnedAssets().cancelBankAccount(this.connectedBankAccount);
            } else if (this.connectedBankAccount.getBalance() == 0.0) {
                // Balance is zero, closing the debt account
                this.loggedInCustomer.getOwnedAssets().cancelBankAccount(this.connectedBankAccount);
            }

            this.loggedInCustomer.getOwnedAssets().cancelCard(this.selectedCard);
            this.loggedInCustomer.initializeCustomer();
            switchToTransitionScreenCard( "Card Cancelled" , "Proceeding to card Overview");
        }

        public void replaceCard() throws IOException{
            Card replacedCard = this.loggedInCustomer.getOwnedAssets().replaceCard(this.selectedCard);
            this.loggedInCustomer.initializeCustomer();
            switchToTransitionScreenCard(replacedCard, "Card Replaced" , "Proceeding to newly created Card Settings");
        }

        public void simulateTransaction() throws IOException{
            String userInputInt = simulateTransactionValue.getText();
            String userInputCents = simulateTransactionValueCents.getText();
            if (validateSimulationInput(simulateTransactionValue,simulateTransactionValueCents)){
                String rawInput = userInputInt + "." + userInputCents;
                Double inputValue = Double.valueOf(rawInput);
                //Checking if the withdrawing amount is allowed
                if (this.connectedBankAccount.getBalance() - this.connectedBankAccount.getDebtLimit() >= inputValue) {
                    this.selectedCard.makePayment(inputValue);
                    this.loggedInCustomer.initializeCustomer();
                    switchToTransitionScreenBank("Transaction Created Successfully" , "Proceeding to connected Bank Account");
                }
            }
        }


        public void simulateDeposit() throws IOException{
            String userInputInt = simulateDepositValue.getText();
            String userInputCents = simulateDepositValueCents.getText();
            if (validateSimulationInput(simulateDepositValue,simulateDepositValueCents)){
                String rawInput = userInputInt + "." + userInputCents;
                Double inputValue = Double.valueOf(rawInput);
                this.connectedBankAccount.addFunds(inputValue);
                this.loggedInCustomer.initializeCustomer();
                switchToTransitionScreenBank("Deposit Successful" , "Proceeding to connected Bank Account");
            }
        }


        public void simulateWithdrawal() throws IOException{
            String userInputInt = simulateWithdrawalValue.getText();
            String userInputCents = simulateWithdrawalValueCents.getText();
            if (validateSimulationInput(simulateWithdrawalValue,simulateWithdrawalValueCents)){
                String rawInput = userInputInt + "." + userInputCents;
                Double inputValue = Double.valueOf(rawInput);
                //Checking if the withdrawing amount is allowed
                if (this.connectedBankAccount.getBalance() - this.connectedBankAccount.getDebtLimit()  >= inputValue) {
                    this.connectedBankAccount.withdrawFunds(inputValue);
                    this.loggedInCustomer.initializeCustomer();
                    switchToTransitionScreenBank("Withdrawal Successful" , "Proceeding to connected Bank Account");
                }
            }
        }


        public boolean validateSimulationInput(TextField value, TextField cents){
            // Input of cents is incorrect format
            if (cents.getText().matches("^[0-9]{2}$") == false)  {
                cents.getStyleClass().add("incorrectTextInput");
                return false;
            } else {
                cents.getStyleClass().removeAll("incorrectTextInput");
            }
            // Input of Int is incorrect format
            if (value.getText().matches("^[0-9]+$") == false ) {
                value.getStyleClass().add("incorrectTextInput");
                return false;
            } else {
                value.getStyleClass().removeAll("incorrectTextInput");
            }
            // Input of casted value is incorrect format
            String rawInput = value.getText() + "." + cents.getText();
            Double inputValue = Double.valueOf(rawInput);
            if (inputValue <= 0) {
                value.getStyleClass().removeAll("incorrectTextInput");
                cents.getStyleClass().removeAll("incorrectTextInput");
                return false;
            }
            return true;
        }
        public void switchToTransitionScreenCard(String message1, String message2) throws IOException{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("transitionScene.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            transitionController transitionController = loader.getController();
            transitionController.initData(this.loggedInCustomer
                    ,(Card)null
                    ,message1
                    ,message2 );
            stage = (Stage) homePageImage.getScene().getWindow();
            stage.setScene(scene);
            transitionController.transition("cardOverview");
        }

        public void switchToTransitionScreenCard(Card card, String message1, String message2) throws IOException{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("transitionScene.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            transitionController transitionController = loader.getController();
            transitionController.initData(this.loggedInCustomer
                    ,card
                    ,message1
                    ,message2 );
            stage = (Stage) homePageImage.getScene().getWindow();
            stage.setScene(scene);
            transitionController.transition("card");
        }

        public void switchToTransitionScreenBank(String message1, String message2) throws IOException{
            BankAccount selectedBankAccount = null;
            for (BankAccount bankAccount : this.loggedInCustomer.retrieveAllBankAccounts()){
                if (bankAccount.getIBANNumber().equals(this.selectedCard.getConnectedBankAccount())){
                    selectedBankAccount = bankAccount;
                }
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("transitionScene.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            transitionController transitionController = loader.getController();
            transitionController.initData(this.loggedInCustomer
                    ,selectedBankAccount
                    ,message1
                    ,message2 );
            stage = (Stage) homePageImage.getScene().getWindow();
            stage.setScene(scene);
            transitionController.transition("bankAccount");
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

        public void switchToSpecificAccountsScreen() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("accountsScene.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            accountsController accountsController = loader.getController();
            BankAccount selectedBankAccount = null;
            for (BankAccount bankAccount : this.loggedInCustomer.retrieveAllBankAccounts()){
                if (bankAccount.getIBANNumber().equals(this.selectedCard.getConnectedBankAccount())){
                    selectedBankAccount = bankAccount;
                }
            }
            accountsController.initData(this.loggedInCustomer,selectedBankAccount);
            stage = (Stage) homePageImage.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

        public void switchToServicesScreen() throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("servicesScene.fxml"));
            parent = loader.load();
            scene = new Scene(parent);
            com.banking_application.scenes.servicesController servicesController = loader.getController();
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


    }
