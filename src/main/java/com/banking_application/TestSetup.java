package com.banking_application;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TestSetup {
    String Path;
    String customerFile;
    String transactionFile;



    public void clearCustomersAndCreateTestAccount(){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        this.Path = databaseFileManager.Path;
        this.customerFile = databaseFileManager.customerFile;
        this.transactionFile = databaseFileManager.transactionFile;
        clearPastData();
        databaseFileManager.verifyPath("Storage","Customers");
        Authentication authentication = new Authentication();
        if (
                authentication.createAccount("EltonJohn"
                        , "Elton"
                        , "Hercules"
                        , "John"
                        , "1947-03-25"
                        ,"password123"
                        , "+447975777666"
                        , "addressLine1"
                        , "addressLine2"
                        , "addressLine3"
                        , "city"
                        , "zip"
                        , "country")
        ){
            System.out.println("Account was created. \nUsername: EltonJohn\nPassword: password123\n" );
        } else {
            System.out.println("Error creating account");
        }
        ArrayList<Customer> testing = databaseFileManager.readCustomerObjectsInFile();
        testing.forEach((n) -> System.out.println(n));
        this.createTestTransactions(testing.get(0));
        for (Transaction transaction : databaseFileManager.readTransactionObjectsInFile()) {
            System.out.println(transaction.toString());
        }
    }



    public void createTestTransactions(Customer customer){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        databaseFileManager.verifyPath("Storage","Transactions");
        String[] transaction1Array = {"1300.95","IE00AIBK00000000000000",customer.retrieveCurrentAccount().getIBANNumber(), "Salary"};
        String[] transaction2Array = {"500","IE00AIBK00000000000000",customer.retrieveCurrentAccount().getIBANNumber(), "Lottery Win"};
        String[] transaction3Array = {"140.30",customer.retrieveCurrentAccount().getIBANNumber(),"IE00AIBK00000000000000", "First Transaction"};
        String[] transaction4Array = {"40",customer.retrieveCurrentAccount().getIBANNumber(),"IE00AIBK00000000000000", "Sending funds"};
        String[] transaction5Array = {"10.50",customer.retrieveCurrentAccount().getIBANNumber(),"IE00AIBK00000000000000"};
        ArrayList<String[]> testTransactions = new ArrayList<>();
        testTransactions.add(transaction1Array);
        testTransactions.add(transaction2Array);
        testTransactions.add(transaction3Array);
        testTransactions.add(transaction4Array);
        testTransactions.add(transaction5Array);
        for (String[] transactionData : testTransactions) {
            // Transactions with Memo
            Transaction newTransaction;
            if( transactionData.length == 4) {
                newTransaction = new Transaction(Double.parseDouble(transactionData[0]), transactionData[1], transactionData[2], transactionData[3], "Bank Transfer");
            } else {
//                Transaction without a Memo
                newTransaction = new Transaction(Double.parseDouble(transactionData[0]), transactionData[1], transactionData[2], "Bank Transfer");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        customer.OwnedAssets.cards.get(0).makePayment(150);
        customer.OwnedAssets.cards.get(0).makePayment(49.99);
    }


    public void clearPastData(){
        File customerFile = new File(this.Path + "/" + this.customerFile);
        if (customerFile.delete()){
            System.out.println("Customer Data File was deleted.");
        } else {
            System.out.println("Something went wrong, Customer Data File was not deleted.");
        }


        File transactionFile = new File(this.Path + "/" + this.transactionFile);
        if (transactionFile.delete()){
            System.out.println("Transaction Data File was deleted.");
        } else {
            System.out.println("Something went wrong, Transaction Data File was not deleted.");
        }
    }



    public void createCreditCard(Customer customer){
        int numCreditCards = customer.getOwnedAssets().retrieveOwnedCreditCards().size();
        customer.createNewCreditCard(1000.0, "Credit Card #" + (numCreditCards +1), "Platinum Credit Card");
    }

}