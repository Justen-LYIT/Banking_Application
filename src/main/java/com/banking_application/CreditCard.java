package com.banking_application;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.Random;

public class CreditCard  implements Serializable, Card {
    String cardName;
    long cardNumber;
    int expMonth;
    int expYear;
    int CVV;
    String connectedBankAccount;
    Boolean isActive;

    public CreditCard(String connectedBankAccount, String cardName){
        this.connectedBankAccount = connectedBankAccount;
        this.generateNewCardNumber();
        this.isActive = true;
        this.cardName = cardName;
    }


    @Override
    public void makePayment(double value) {
        CurrentAccount currentAccount = new CurrentAccount();
        new Transaction(value,this.connectedBankAccount,currentAccount.generateRandomIBAN(),"", "Card Payment");
    }

    @Override
    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    @Override
    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    @Override
    public int getCVV() {
        return CVV;
    }

    public void setCVV(int CVV) {
        this.CVV = CVV;
    }

    @Override
    public String getConnectedBankAccount() {
        return connectedBankAccount;
    }

    public void setConnectedBankAccount(String connectedBankAccount) {
        this.connectedBankAccount = connectedBankAccount;
    }

    @Override
    public Boolean getIsActive() {
        return isActive;
    }

    @Override
    public void setIsActive(Boolean active) {
        isActive = active;
    }

    private void generateNewCardNumber(){
        String ccNumber = "";
        for (int i=0;i<16;i++){
            ccNumber = ccNumber + Integer.toString( new Random().nextInt(9) );
        }
        System.out.println(Instant.now());
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String toString(){
        return "Credit Card registered to IBAN %s\nCard Number %s".formatted(this.connectedBankAccount,this.cardNumber);
    }
}
