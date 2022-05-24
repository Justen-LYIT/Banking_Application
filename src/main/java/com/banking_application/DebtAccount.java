package com.banking_application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class DebtAccount implements BankAccount, Serializable {
    Double balance;
    Double debtLimit;
    Double interestRate;
    ArrayList<Transaction> transactions = new ArrayList<>();
    String IBAN;
    String bankAccountName;



    public DebtAccount(double value, double debtLimit, String bankAccountName){
        this.balance = value;
        this.debtLimit = debtLimit;
        this.interestRate = 4.7;
        this.setIBAN(generateRandomIBAN());
        this.bankAccountName = bankAccountName;
    }

    @Override
    public void addFunds(double amount) {
        new Transaction(amount,"N/A" ,this.IBAN, "Deposit of Funds on Physical Location","Deposit of Funds");
    }

    @Override
    public void withdrawFunds(double amount) {
        if(amount < 0 ) {
        } else if (amount > this.balance){
        } else {
            new Transaction(amount,this.IBAN,"N/A","Withdrawal of Funds on Physical Location","Withdrawal of Funds");
        }
    }


    @Override
    public String getIBANNumber() {
        return this.IBAN;
    }

    @Override
    public String generateRandomIBAN() {
        String IBANPrefix = "IE42JUST";
        while (true) {
            String randomIBANSuffix = "";
            for (int i = 0; i < 14; i++) {
                randomIBANSuffix = randomIBANSuffix + new Random().nextInt(9);
            }
            DatabaseFileManager databaseFileManager = new DatabaseFileManager();
            if (databaseFileManager.uniqueIbanGenerated(IBANPrefix + randomIBANSuffix)){
                return IBANPrefix + randomIBANSuffix;
            }
        }
    }


    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void addTransaction(int index, Transaction transaction) {
        Transaction copiedTransaction = new Transaction(transaction);
        this.transactions.add(index, copiedTransaction);
        this.recalculateAccountValue();
    }

    @Override
    public void recalculateAccountValue() {
        double accountValue = 0.0;
        for (Transaction transaction : this.transactions) {
//            Outgoing Transaction
            if (transaction.originAccountIBAN.equals(this.IBAN)){
                accountValue -= transaction.value;
            }
//            Incoming Transaction
            if (transaction.targetAccountIBAN.equals(this.IBAN)){
                accountValue += transaction.value;
            }
        }
        this.balance = accountValue;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getDebtLimit() {
        return debtLimit;
    }

    public void setDebtLimit(Double debtLimit) {
        this.debtLimit = debtLimit;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public ArrayList<Transaction> retrieveTransactions(){
        return this.transactions;
    }
}
