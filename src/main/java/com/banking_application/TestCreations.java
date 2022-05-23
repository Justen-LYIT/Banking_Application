package com.banking_application;

public class TestCreations {
    public static void main(String[] args){

        TestSetup testSetup = new TestSetup();
        testSetup.clearCustomersAndCreateTestAccount();
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        Customer testAccount = databaseFileManager.readCustomerObjectsInFile().get(0);
        testAccount.initializeCustomer();
        System.out.println(testAccount.getOwnedAssets().retrieveAccountValue());
        System.out.println(testAccount.getOwnedAssets().retrieveDebitValue());
        System.out.println(testAccount.getOwnedAssets().retrieveCreditValue());
        testAccount.getOwnedAssets().bankAccounts.forEach(e-> System.out.println("Account IBAN: " + e.getIBANNumber()));
    }

}
