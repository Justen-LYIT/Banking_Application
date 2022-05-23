package com.banking_application;

import java.io.Serializable;
import java.time.Instant;
import java.util.Random;

public class DebitCard  implements Serializable, Card{
    String cardName;
    long cardNumber;
    int expMonth;
    int expYear;
    int CVV;
    String connectedBankAccount;
    Boolean isActive;


    public DebitCard(String connectedBankAccount){
        this.connectedBankAccount = connectedBankAccount;
        this.generateNewCardNumber();
        this.isActive = true;
        this.cardName = "Current Account Debit Card";
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
        String cardNumber = "";
        for (int i=0;i<16;i++){
            if (i%4==0 & i!=0) {
                cardNumber += "  ";
            }
            cardNumber = cardNumber + ( new Random().nextInt(9) );
        }
        this.cardName = cardNumber;
        System.out.println(cardNumber);
        System.out.println("Assign Card Exp + CVV");
//        TODO implement card number generator
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
        return "Debit Card registered to IBAN %s\nCard Number %s".formatted(this.connectedBankAccount,this.cardNumber);
    }
}
