package com.banking_application.scenes;

import com.banking_application.Customer;
import com.banking_application.DebtAccount;
import com.banking_application.Transaction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class requestLoanController implements Initializable {
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
    private Slider loanSliderValue;
    @FXML
    private Label loanValue;
    @FXML
    private Label loanRate;
    @FXML
    private Label loanCost;
    @FXML
    private Label loanRepayment;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void initData(Customer customer){
        this.loggedInCustomer=customer;
        this.loggedInCustomer.initializeCustomer();

        loanSliderValue.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                double slidedValue = loanSliderValue.getValue();
                long tickValue = Math.round( slidedValue / 500 ) * 500 ;

                loanSliderValue.setValue(tickValue);
                double setLoanRate;
                loanValue.setText("€ " + df.format( tickValue) );
                if (tickValue <= 2000.0) {
                    setLoanRate = 2.30;
                } else if (tickValue <= 5000.0) {
                    setLoanRate = 3.10;
                } else {
                    setLoanRate = 3.80;
                }
                loanRate.setText(setLoanRate + "%");
                double calculatedLoanCost = tickValue * (1+  (setLoanRate / 100));
                double monthlyPayPrice = Math.floor( (calculatedLoanCost / 24) * 100 ) / 100;
                loanCost.setText("€ " + df.format(calculatedLoanCost - tickValue)) ;
                loanRepayment.setText("€ " + df.format( monthlyPayPrice) );

            }
        });
    }

    public void applyForLoan() throws IOException{
        String requestedAmountRaw = loanValue.getText().replaceFirst("€ ", "");
        double requestedAmount = Double.valueOf( requestedAmountRaw );
        String interestRateRaw = loanRate.getText().replaceAll("%", "");
        double interestRate = Double.valueOf(interestRateRaw);
        DebtAccount debtAccount = this.loggedInCustomer.createNewDebtAccount(Double.valueOf(requestedAmount * -1), requestedAmount * -1,"Loan");
        debtAccount.setInterestRate(interestRate);
        new Transaction(requestedAmount,debtAccount.getIBAN(),this.loggedInCustomer.retrieveCurrentAccount().getIBANNumber(),"Loan Request of € " + df.format(requestedAmount), "Loan Request");
        this.loggedInCustomer.initializeCustomer();
        switchToAccountsScreen();
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
