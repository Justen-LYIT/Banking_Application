package com.banking_application;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;

public class Customer implements Serializable {
    String userName;
    long id;
    String firstName;
    String middleName;
    String lastName;
    String fullName;
    String dateOfBirth;
    final long creationTimestamp;
    String passwordHash;
    String phone;
    String addressLine1;
    String addressLine2;
    String addressLine3;
    String city;
    String zip;
    String country;
    OwnedAssets OwnedAssets = new OwnedAssets();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Customer(String userName, String firstName, String middleName, String lastName, String dateOfBirth , String phone , String addressLine1 , String addressLine2 , String addressLine3 , String city , String zip , String country){
        this.userName = userName;
        this.id                 = getLatestCustomerID();
        this.firstName          = firstName;
        this.middleName         = middleName;
        this.lastName           = lastName;
        this.dateOfBirth        = dateOfBirth;
        this.creationTimestamp  = Instant.now().toEpochMilli();
        this.phone              = phone;
        this.addressLine1       = addressLine1;
        this.addressLine2       = addressLine2;
        this.addressLine3       = addressLine3;
        this.city               = city;
        this.zip                = zip;
        this.country            = country;
        this.fullName           = firstName;
        if(middleName.length() >0 ) { this.fullName += " " + middleName;}
        this.fullName           += " " + lastName;
        initializeCustomer();
        writeCustomerDataToFile();
    }

    public void initializeCustomer(){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        ArrayList<Transaction> allTransactions = databaseFileManager.readTransactionObjectsInFile();
        for (BankAccount ownedBankAccount : this.getOwnedAssets().bankAccounts) {
            for (Transaction transaction:allTransactions){
                if (transaction.targetAccountIBAN.equals(ownedBankAccount.getIBANNumber()) ||
                        transaction.originAccountIBAN.equals(ownedBankAccount.getIBANNumber())){
                    Boolean alreadyRecorded = false;
                    for (Transaction pastTransactions : ownedBankAccount.retrieveTransactions()){
                        if (pastTransactions.id == transaction.id) {
                            alreadyRecorded = true;
                            break;
                        }
                    }
                    if(!alreadyRecorded) {
                        Transaction newRecordedTransaction = new Transaction(transaction);
                        if(transaction.originAccountIBAN.equals(ownedBankAccount.getIBANNumber())) {
//                            Sending transaction, updating the Debit value
                            newRecordedTransaction.setDebitValue("€ " + df.format(transaction.getValue()));
                        } else if (transaction.targetAccountIBAN.equals(ownedBankAccount.getIBANNumber())){
//                            Receiving transaction, updating the Credit Value
                            newRecordedTransaction.setCreditValue("€ " + df.format(transaction.getValue()));
                        }
                        ownedBankAccount.addTransaction(0,newRecordedTransaction);
                    }
                }
            }
        }

    }

    public void setPassword(String hashedPassword){
        this.passwordHash = hashedPassword;
        writeCustomerDataToFile();
    }

    public long getLatestCustomerID(){
        DatabaseFileManager customerList = new DatabaseFileManager();
        ArrayList<Customer> allAccounts = customerList.readCustomerObjectsInFile();
        long newCustomerID = 1;
        for(int i=0;i<allAccounts.size();i++) {
            if (allAccounts.get(i).id >= newCustomerID) {
                newCustomerID = allAccounts.get(i).id + 1;
            }
        }
        return newCustomerID;
    }


    public void writeCustomerDataToFile(){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        databaseFileManager.writeCustomerObjectToFile(this);
    }

    public BankAccount retrieveCurrentAccount(){
        for (BankAccount account : this.getOwnedAssets().bankAccounts){
            if (account instanceof CurrentAccount){
                return account;
            }
        }
        return  null;
    }

    public ArrayList<BankAccount> retrieveSavingAccounts(){
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        for (BankAccount account : this.getOwnedAssets().bankAccounts){
            if (account instanceof SavingAccount){
                bankAccounts.add(account);
            }
        }
        return  bankAccounts;
    }


    public ArrayList<BankAccount> retrieveDebtAccounts(){
        ArrayList<BankAccount> bankAccounts = new ArrayList<>();
        for (BankAccount account : this.getOwnedAssets().bankAccounts){
            if (account instanceof DebtAccount){
                bankAccounts.add(account);
            }
        }
        return  bankAccounts;
    }

    public ArrayList<BankAccount> retrieveAllBankAccounts(){
        return this.getOwnedAssets().bankAccounts;
    }


    public CurrentAccount createNewCurrentAccount(){
        CurrentAccount outcome = this.OwnedAssets.createNewCurrentAccount();
        writeCustomerDataToFile();
        return outcome;
    }
    public SavingAccount createNewSavingAccount(String bankAccountName){
        SavingAccount outcome = this.OwnedAssets.createNewSavingAccount(bankAccountName);
        writeCustomerDataToFile();
        return outcome;
    }
    public DebtAccount createNewDebtAccount(Double value, double debtLimit, String bankAccountName){
        DebtAccount outcome =  this.OwnedAssets.createNewDebtAccount(value, debtLimit,bankAccountName);
        writeCustomerDataToFile();
        return outcome;
    }

    public CreditCard createNewCreditCard(Double debtLimit, String bankAccountName, String cardName){
        DebtAccount debtAccount = this.createNewDebtAccount(0.0,debtLimit*-1, bankAccountName);
        CreditCard creditCard = this.OwnedAssets.createNewCreditCard(debtAccount, cardName);
        writeCustomerDataToFile();
        return creditCard;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public com.banking_application.OwnedAssets getOwnedAssets() {
        return OwnedAssets;
    }

    public void setOwnedAssets(com.banking_application.OwnedAssets ownedAssets) {
        OwnedAssets = ownedAssets;
    }


    public String getFullName() {
        return fullName;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String toString(){
        return String.format("Customer ID %s: %s",id, userName);
    }

}

