package com.banking_application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Authentication {

    /**
     * Main Method to create a new Customer account. Will collect all the user inserted information, to pass it on towards the Customer Class
     * @param userName Username of the account that needs to be created
     * @param firstName First name of the user
     * @param middleName Middle name of the user
     * @param lastName Last name of the user
     * @param dateOfBirth Date of Birth of the user, in String format (YYYY-mm-DD)
     * @param password Plain Text Password of the user. Will be hashed when set in the Customer class
     * @param phone Phone number of the user, in String format
     * @param addressLine1 Address line of the customer, line 1
     * @param addressLine2 Address line of the customer, line 2
     * @param addressLine3 Address line of the customer, line 3
     * @param city City of the Customer
     * @param zip ZIP of the customer
     * @param country Country of residence of the customer
     */
    public void createAccount(String userName, String firstName, String middleName, String lastName, String dateOfBirth , String password, String phone , String addressLine1 , String addressLine2 , String addressLine3 , String city , String zip , String country){
        if(userName == null ||
            userName.length() == 0 ||
                password == null ||
                password.length() == 0){
            return;
        }
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        for( Customer existingCustomer:databaseFileManager.readCustomerObjectsInFile()) {
            if (existingCustomer.userName.equals(userName)) {
                return;
            }
        }
        Customer newUser = new Customer(userName, firstName, middleName, lastName, dateOfBirth , phone , addressLine1 , addressLine2 , addressLine3 , city , zip , country);
        newUser.setPassword( this.hashPassword(newUser,password));
    }

    /**
     * For use in combination with the create account method. Will collect all the user inserted information, to pass it on towards the Customer Class.
     * Feedback is provided which can be used to provide a message to the user
     * @return A String with user feedback about the status of why the account creation process failed
     */
    public String createAccountError(){
        return "This username is already in use. Please enter a different name";
    }


    /**
     * Authentication method. Collects the User input, and will first check if the username exists.
     * If the username exists, convert the password input into a MD5 hash and compare it to the MD5 has of the Found user
     * @param username String of the user inserted Username
     * @param password String of the user inserted Password
     * @return true for correct login credentials
     */
    public boolean loginAttempt(String username, String password){
        if(! usernameExists(username)){
            return false;
        }
        Customer customer = findCustomerViaUsername(username);
        String enteredSeededPassword = hashPassword(customer,password);
        return customer.passwordHash.equals(enteredSeededPassword);
    }

    /**
     * Feedback label to provide to the user in the event the login attempt function returned false.
     * Will provide context to the user why the login was failed
     * @param username String of the user inserted Username
     * @param password String of the user inserted Password
     * @return String with feedback about the login Fail response
     */
    public String loginFailError(String username, String password){
        if(username.length()==0 || password.length()==0){
            return "Confirm that all fields are filled in.";
        }else if(! usernameExists(username)){
            return "Please verify that you entered your username correctly, or consider creating a new account.";
        }
        Customer customer = findCustomerViaUsername(username);
        String enteredSeededPassword = hashPassword(customer,password);
        if(!customer.passwordHash.equals(enteredSeededPassword)) {
            return "Incorrect Password Entered. Please try again.";
        }
        return "Unknown error occurred, please try again later.";
    }


    /**
     * Method to confirm if the inserted username exist in our DB
     * @param username String value of the username that has to be searched for
     * @return true value for an existing user
     */
    public boolean usernameExists(String username){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        ArrayList<Customer> customers = databaseFileManager.readCustomerObjectsInFile();
        for (int i=0;i< customers.size();i++){
            if(customers.get(i).userName.equals(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * Receives a String of an exact match of the username that needs to be returned
     * @param username String value of the username that has to be searched for
     * @return Customer object of the Customer that matches the username. Returns null if nothing was found.
     */
    public Customer findCustomerViaUsername(String username){
        DatabaseFileManager databaseFileManager = new DatabaseFileManager();
        ArrayList<Customer> customers = databaseFileManager.readCustomerObjectsInFile();
        for (int i=0;i< customers.size();i++){
            if(customers.get(i).userName.equals(username)){
                return customers.get(i);
            }
        }
        return null;
    }


    /**
     * Method to hash the password into a MD5 hash.
     * Since only converting to MD5 would not be secure enough, a random seed needs to be provided
     * In this case, we are concatenating the password with the creation timestamp (UNIXTIME), and converting this to MD5
     * @param customer Customer object on which the password needs to be hashed.
     * @param password String value of the plain text password that was entered by the user
     * @return a MD5 has of the Password combined with the unixtime of the creation time
     */
    public String hashPassword(Customer customer, String password){
        String seededPassword = password +customer.creationTimestamp;
        String encryptedPassword = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(seededPassword.getBytes());
            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                s.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            encryptedPassword = s.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }


}
