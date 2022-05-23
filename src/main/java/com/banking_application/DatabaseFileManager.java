package com.banking_application;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DatabaseFileManager {
    String Path = System.getProperty("user.dir");
    String customerFile = "/Storage/Customers";
    String transactionFile = "/Storage/Transactions";



    public void verifyPath(String directoryName,String fileName){
        File directory = new File(directoryName);
        if (! directory.exists()){
            directory.mkdir();
        }
        File file = new File(directoryName + "/" +fileName);
        if (! file.exists()){
            try {
                ArrayList<Customer> firstEntry = new ArrayList<>();
                FileOutputStream fileOut = new FileOutputStream(this.Path + "/Storage/" + fileName);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(firstEntry);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public void writeCustomerObjectToFile(Customer customer) {
        ArrayList<Customer> writeableList = concatCustomerExistingNewData(customer);
        try {
            FileOutputStream fileOut = new FileOutputStream(this.Path + "/" + this.customerFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(writeableList);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<Customer> concatCustomerExistingNewData(Customer customer){
        ArrayList<Customer> previousInput = readCustomerObjectsInFile();
        boolean newElementExists = false;
        int existingElementIndex = -1;
        for( int i=0;i<previousInput.size();i++){
            if( previousInput.get(i).id == customer.id ) {
                newElementExists = true;
                existingElementIndex = i;
            }
        }
        if (newElementExists){
            previousInput.remove(existingElementIndex);
        }
        previousInput.add(customer);
        return previousInput;
    }



    public ArrayList<Customer> readCustomerObjectsInFile(){
        verifyPath("Storage","Customers");
        ArrayList<Customer> output = new ArrayList<>();
        try{
            FileInputStream readData = new FileInputStream(this.Path + "/" + this.customerFile);
            ObjectInputStream readStream = new ObjectInputStream(readData);

            output = (ArrayList<Customer>) readStream.readObject();
            readStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }










    public void writeTransactionObjectToFile(Transaction transaction) {
        ArrayList<Transaction> writeableList = concatTransactionExistingNewData(transaction);
        try {
            FileOutputStream fileOut = new FileOutputStream(this.Path + "/" + this.transactionFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(writeableList);
            objectOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public ArrayList<Transaction> concatTransactionExistingNewData(Transaction transaction){
        ArrayList<Transaction> previousInput = readTransactionObjectsInFile();
        boolean newElementExists = false;
        int existingElementIndex = -1;
        for( int i=0;i<previousInput.size();i++){
            if( previousInput.get(i).id == transaction.id ) {
                newElementExists = true;
                existingElementIndex = i;
            }
        }
        if (newElementExists){
            previousInput.remove(existingElementIndex);
        }
        previousInput.add(transaction);
        return previousInput;
    }



    public ArrayList<Transaction> readTransactionObjectsInFile(){
        verifyPath("Storage","Transactions");
        ArrayList<Transaction> output = new ArrayList<>();
        try{
            FileInputStream readData = new FileInputStream(this.Path + "/" + this.transactionFile);
            ObjectInputStream readStream = new ObjectInputStream(readData);

            output = (ArrayList<Transaction>) readStream.readObject();
            readStream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }





    public boolean uniqueIbanGenerated(String iban){
        for (Transaction transaction: readTransactionObjectsInFile()){
            if(transaction.getOriginAccountIBAN().equals(iban) ||
                transaction.getTargetAccountIBAN().equals(iban)) {
                return false;
            }
        }
        return true;
    }






}
