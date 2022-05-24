package com.banking_application;

import java.util.ArrayList;

public interface BankAccount {

    /**
     * Function to add funds to a bank account. Would be used to simulate a Deposit of Funds
     * @param amount double value of the amount that will be deposited
     */
    void addFunds(double amount);

    /**
     * Function to withdraw funds from a bank account. Would be used to simulate a Withdrawal of Funds.
     * @param amount double value of the amount that will be withdrawn
     */
    void withdrawFunds(double amount);

    /**
     * Function to retrieve the total balance on an Account
     * @return double value of the balance of the Bank Account
     */
    double getBalance();

    /**
     * Function to get all the transactions that are connected to this Bank Account, either on the Sending or the Receiving end
     * @return an ArrayList of transactions which are connected to this bank account
     */
    ArrayList<Transaction> retrieveTransactions();

    /**
     * Getter function to retrieve the IBAN number on the account
     * @return String representation of the IBAN Number on the acction
     */
    String getIBANNumber();

    /**
     * Getter function to retrieve the Name of the Bank Account
     * @return String of the Bank Account Name
     */
    String getBankAccountName();

    /**
     * Function to generate a new IBAN number. Will make sure that the generated IBAN number is not utilised in any of the recorded transactions
     * @return String value of the newly generated IBAN number
     */
    String generateRandomIBAN();

    /**
     * Function to append a transaction to the Bank Account
     * @param index the index in which the transaction needs to be added.
     * @param transaction the Transaction that needs to be added
     */
    void addTransaction(int index, Transaction transaction);

    /**
     * Function to recalculate the value of the account. When new transactions are found that should be recorded, this function will make sure the balance on the account is updated
     */
    void recalculateAccountValue();

    /**
     * Getter function to retrieve the Debt limit on the Account
     * @return double value of the debt limit on the account
     */
    double getDebtLimit();

}
