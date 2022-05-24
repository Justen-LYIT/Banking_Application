package com.banking_application;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Transaction  implements Serializable {
    long id;
    String creationTimestamp;
    Double value;
    String debitValue;
    String creditValue;
    String originAccountIBAN;
    String targetAccountIBAN;
    String memo;
    String type;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Transaction(Transaction transaction){
        this.id = transaction.id;
        this.value= transaction.value;
        this.originAccountIBAN= transaction.originAccountIBAN;
        this.targetAccountIBAN= transaction.targetAccountIBAN;
        this.memo= transaction.memo;
        this.type = transaction.type;
        this.creationTimestamp = transaction.creationTimestamp;
        this.debitValue = transaction.debitValue;
        this.creditValue = transaction.creditValue;
    }

    public Transaction(double value,String originAccountIBAN, String targetAccountIBAN, String memo, String type){
        this.id = getLatestTransactionID();
        this.value=value;
        this.originAccountIBAN=originAccountIBAN;
        this.targetAccountIBAN=targetAccountIBAN;
        this.memo=memo;
        this.type = type;
        Instant instant = Instant.now();
        this.creationTimestamp = formatter.format(instant);
        this.writeTransactionDataToFile();
    }

    public Transaction(double value,String originAccountIBAN,String targetAccountIBAN, String type){
        this.id = getLatestTransactionID();
        this.value=value;
        this.originAccountIBAN=originAccountIBAN;
        this.targetAccountIBAN=targetAccountIBAN;
        this.memo="";
        this.type = type;
        Instant instant = Instant.now();
        this.creationTimestamp = formatter.format(instant);
        this.writeTransactionDataToFile();
    }

    public void writeTransactionDataToFile(){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        databaseFileManager.writeTransactionObjectToFile(this);
    }

    public long getLatestTransactionID(){
        DatabaseFileManager transactionList = new DatabaseFileManager();
        ArrayList<Transaction> allTransactions = transactionList.readTransactionObjectsInFile();
        long newTransactionID = 1;
        for(int i=0;i<allTransactions.size();i++) {
            if (allTransactions.get(i).id >= newTransactionID) {
                newTransactionID = allTransactions.get(i).id + 1;
            }
        }
        return newTransactionID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getOriginAccountIBAN() {
        return originAccountIBAN;
    }

    public void setOriginAccountIBAN(String originAccountIBAN) {
        this.originAccountIBAN = originAccountIBAN;
    }

    public String getTargetAccountIBAN() {
        return targetAccountIBAN;
    }

    public void setTargetAccountIBAN(String targetAccountIBAN) {
        this.targetAccountIBAN = targetAccountIBAN;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getDebitValue() {
        return debitValue;
    }

    public void setDebitValue(String debitValue) {
        this.debitValue = debitValue;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){
        return String.format("Transaction ID %s: € %s\nFrom %s towards %s",id, df.format(value),originAccountIBAN,targetAccountIBAN);
    }
}
