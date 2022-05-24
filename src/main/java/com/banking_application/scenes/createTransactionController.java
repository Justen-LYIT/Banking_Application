package com.banking_application.scenes;

import com.banking_application.BankAccount;
import com.banking_application.Customer;
import com.banking_application.DatabaseFileManager;
import com.banking_application.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createTransactionController implements Initializable {
    private Scene scene;
    private Stage stage;
    private Parent parent;
    private Customer loggedInCustomer;
    private BankAccount selectedBankAccount;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final Pattern regexIBANPattern = Pattern.compile("^([A-Z]{2}[ \\-]?[0-9]{2})(?=(?:[ \\-]?[A-Z0-9]){9,30}$)((?:[ \\-]?[A-Z0-9]{3,5}){2,7})([ \\-]?[A-Z0-9]{1,3})?$");

    private final Popup popup = new Popup();
    private String popupSearchValue = "";

    @FXML
    private ChoiceBox fromIBAN;
    @FXML
    private TextField toIBAN;
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
    @FXML
    private ImageView contactsIcon;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactsIcon.setOnMouseClicked(mouseEvent -> {
            openContactsPopup();
        });
    }

    public void initData(Customer customer, BankAccount bankAccount, Stage stage){
        this.loggedInCustomer=customer;
        this.selectedBankAccount = bankAccount;
        this.loggedInCustomer.initializeCustomer();
        this.stage = stage;

        toIBAN.setTextFormatter(new TextFormatter<Object>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        String separatorSpace = "                  € ";
        ArrayList<BankAccount> availableBankAccounts = new ArrayList<>(this.loggedInCustomer.retrieveAllBankAccounts());
        if( this.selectedBankAccount!=null){
            fromIBAN.getItems().add(this.selectedBankAccount.getIBANNumber()+separatorSpace+df.format(this.selectedBankAccount.getBalance()));
            availableBankAccounts.remove(this.selectedBankAccount);
            fromIBAN.getSelectionModel().selectFirst();
        }
        for (BankAccount bankAccountIterate : availableBankAccounts){
            fromIBAN.getItems().add(bankAccountIterate.getIBANNumber() + separatorSpace+ df.format(bankAccountIterate.getBalance()));
        }
    }


    public void createTransactions() throws IOException {
        Boolean allowedTransactions = true;
        if(fromIBAN.getValue() == null) {
            fromIBAN.getStyleClass().add("incorrectTextInput");
            allowedTransactions = false;
        } else {
            fromIBAN.getStyleClass().removeAll("incorrectTextInput");
        }

        if(toIBAN.getText().matches( regexIBANPattern.toString() ) == false) {
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
                Matcher m = extractIBANRegex.matcher(fromIBAN.getValue().toString());
                if (m.find()){
                    String selectedFromIBAN = m.group(0);
                    if (selectedFromIBAN.equalsIgnoreCase(toIBAN.getText())) {
                        toIBAN.getStyleClass().add("incorrectTextInput");
                        return;
                    }
                    for (BankAccount bankAccount:this.loggedInCustomer.retrieveAllBankAccounts()){
                        if (bankAccount.getIBANNumber().equals(selectedFromIBAN)){
                            if(bankAccount.getBalance() + (bankAccount.getDebtLimit() * -1) >= totalTransactionValue){
                                Transaction transaction = new Transaction(totalTransactionValue,selectedFromIBAN,toIBAN.getText(),memo.getText(), "Bank Transfer");
                                DatabaseFileManager databaseFileManager = new DatabaseFileManager();
                                databaseFileManager.writeTransactionObjectToFile(transaction);
                                this.selectedBankAccount = bankAccount;
                                switchToTransitionScreen();
                            } else {
                                transactionValueInt.getStyleClass().add("incorrectTextInput");
                            }
                        }
                    }
                } else {
                    fromIBAN.getStyleClass().add("incorrectTextInput");
                }
            }
        }
    }

    public void openContactsPopup() {
        VBox popupVBox = new VBox();
        popupVBox.resize(400,400);
        popupVBox.setPrefHeight(400);
        popupVBox.setPrefWidth(400);
        popupVBox.setLayoutX(0);
        popupVBox.setLayoutY(0);
        popupVBox.setStyle("-fx-background-color: white;-fx-border-style: solid;" +
                "-fx-border-color:black;-fx-border-width: 1px;"
        );
        // Create move bar
        VBox topVBox = new VBox();
        topVBox.setStyle("-fx-background-color: #4a4652;-fx-border-color:black;-fx-border-width: 1px;");
        topVBox.setPrefHeight(20);
        topVBox.setMinHeight(20);
        popupVBox.getChildren().add(topVBox);

        // Popup Container for all elements
        VBox popupContainer = new VBox();
        popupContainer.setStyle("-fx-padding: 10px;");


        Label label = new Label("Search for a contact by their Name");
        label.setPadding(new Insets(15,0,15,0));
        label.setStyle("-fx-font-size: 14pt;-fx-font-weight: bold;");
        label.setAlignment(Pos.CENTER);
        popupContainer.getChildren().add(label);

        TextField textField = new TextField();
        textField.setPrefSize(30,30);
        textField.setMinSize(30,30);
        popupContainer.getChildren().add(textField);

        TableView tableView = new TableView();
        Label placeholder = new Label("Search for a User in the textfield above");
        tableView.setPlaceholder(placeholder);
        tableView.setStyle(
                "    -fx-padding: 5px 0;" +
                "    -fx-border-insets: 5px 0;" +
                "    -fx-background-insets: 5px 0;");

        popupContainer.getChildren().add(tableView);

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            this.popupSearchValue = newValue;
            tableView.getItems().clear();
            tableView.getColumns().clear();

            if (this.popupSearchValue.length() >=3 ) {
                DatabaseFileManager databaseFileManager = new DatabaseFileManager();
                ArrayList<Customer> allCustomers =  databaseFileManager.readCustomerObjectsInFile();
                ObservableList<Customer> observableList = FXCollections.observableArrayList();
                for (Customer customer : allCustomers) {
                    if (customer.getFullName().toLowerCase().matches( ".*" + this.popupSearchValue.toLowerCase() + ".*" )){
                        if(customer.getId() != this.loggedInCustomer.getId()) {
                            observableList.add(customer);
                        }
                    }
                }
                if (observableList.size() >0 ) {
                    //Found Values Column
                    TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
//                    nameColumn.setMinWidth(400);
                    nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    tableView.setItems(observableList);
                    tableView.getColumns().addAll(nameColumn);
                }
            }

        });

        HBox hBox = new HBox();
        Button closeButton = new Button("Close");
        closeButton.setMinWidth(70);
        closeButton.setCursor(Cursor.HAND);
        closeButton.setOnAction(actionEvent -> popup.hide());
        hBox.getChildren().add(closeButton);

        final Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(10, 1);
        hBox.getChildren().add(spacer);

        Button selectButton = new Button("Select Contact");
        selectButton.setMinWidth(130);
        selectButton.setCursor(Cursor.HAND);
        selectButton.setDisable(true);
        selectButton.setOnAction(actionEvent -> {
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                Customer selectedCustomer = (Customer) tableView.getSelectionModel().getSelectedItem();
                this.toIBAN.setText( selectedCustomer.retrieveCurrentAccount().getIBANNumber()  );
                popup.hide();
            }
        });
        hBox.getChildren().add(selectButton);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectButton.setDisable(newSelection == null);
        });

        popupContainer.getChildren().add(hBox);
        popupContainer.setAlignment(Pos.CENTER);
        popupVBox.getChildren().add(popupContainer);

        // add the label
        popup.getContent().add(popupVBox);
        if (!popup.isShowing()) {
            this.popup.show(stage);
        } else {
            popup.hide();
        }

    }

    public void switchToTransitionScreen() throws IOException{
        popup.hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("transitionScene.fxml"));
        parent = loader.load();
        scene = new Scene(parent);
        transitionController transitionController = loader.getController();
        transitionController.initData(this.loggedInCustomer
                                ,this.selectedBankAccount
                                ,"Transaction Created Successfully"
                                , "Returning back to your Bank Account." );
        stage = (Stage) homePageImage.getScene().getWindow();
        stage.setScene(scene);
        transitionController.transition("bankAccount");
    }


    public void switchToSpecificAccountsScreen() throws IOException {
        popup.hide();
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
        popup.hide();
        parent = FXMLLoader.load(getClass().getResource(("startUpScene.fxml")));
        stage = (Stage) logoutImage.getScene().getWindow();
        scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHomePageScreen() throws IOException {
        popup.hide();
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
        popup.hide();
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
        popup.hide();
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
        popup.hide();
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
