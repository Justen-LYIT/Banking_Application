package com.banking_application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                , "country");


        authentication.createAccount("JamesBrown"
                , "James"
                , ""
                , "Brown"
                , "1947-03-25"
                ,"password123"
                , "+447923777666"
                , "addressLine1"
                , "addressLine2"
                , "addressLine3"
                , "city"
                , "zip"
                , "country");


        authentication.createAccount("Michael Jackson"
                , "Michael"
                , ""
                , "Jackson"
                , "1947-03-25"
                ,"password123"
                , "+447975777666"
                , "addressLine1"
                , "addressLine2"
                , "addressLine3"
                , "city"
                , "zip"
                , "country");

        List<String> nameList = Arrays.asList("Abraham Lincoln",
                "Ada Lovelace",
                "Albert Einstein",
                "Alexander Fleming",
                "Andrew Jackson",
                "Andrew Johnson",
                "Andy Murray",
                "Andy Warhol",
                "Anne Boleyn",
                "Anne Bonny",
                "Anne Frank",
                "Aretha Franklin",
                "Arthur Rostron",
                "Barack Obama",
                "Benjamin Franklin",
                "Benjamin Harrison",
                "Bessie Coleman",
                "Bill Clinton",
                "Bruce Ismay",
                "Calvin Coolidge",
                "Captain Cook",
                "Captain Tom",
                "Carl Lewis",
                "Catherine Howard",
                "Catherine Parr",
                "Cathy Freeman",
                "Charles Darwin",
                "Charles Dickens",
                "Charlotte Brontë",
                "Chester Arthur",
                "Chris Hoy",
                "Cristiano Ronaldo",
                "Christopher Columbus",
                "Daley Thompson",
                "David Attenborough",
                "David Beckham",
                "Donald Trump",
                "Dhyan Chand",
                "Edmund Hillary",
                "Edward Smith",
                "Emil Zatopek",
                "Emily Brontë",
                "Emmeline Pankhurst",
                "Enid Blyton",
                "Florence Nightingale",
                "Franklin Pierce",
                "Frederick Douglass",
                "Galileo Galilei",
                "George Bush",
                "George Eliot",
                "George Washington",
                "Gerald Ford",
                "Grover Cleveland",
                "Guy Fawkes",
                "Haile Gebrselassie",
                "Harriet Tubman",
                "Harry Styles",
                "Harry Truman",
                "Prince Harry",
                "Helen Keller",
                "Herbert Hoover",
                "Ian Thorpe",
                "Isaac Newton",
                "Isabella Bird",
                "James I",
                "James II",
                "James Buchanan",
                "James Garfield",
                "James Madison",
                "James Monroe",
                "James Polk",
                "Jane Austen",
                "Jane Goodall",
                "Jane Seymour",
                "Jenny Jones",
                "Jesse Owens",
                "Jimmy Carter",
                "Joe Biden",
                "John Adams",
                "John Tyler",
                "Julia Donaldson",
                "Kelly Holmes",
                "Lasse Viren",
                "Lewis Carroll",
                "Liam Payne",
                "Lionel Messi",
                "Louis Tomlinson",
                "Mae Jemison",
                "Mahatma Gandhi",
                "Marie Curie",
                "Mark Antony",
                "Mark Spitz",
                "Mark Twain",
                "Mary I",
                "Mary II",
                "Mary Anning",
                "Mary Berry",
                "Mary Seacole",
                "Meghan Markle",
                "Michael Johnson",
                "Michael Phelps",
                "Millard Fillmore",
                "Millicent Fawcett",
                "Molly Brown",
                "Mother Teresa",
                "Muhammad Ali",
                "Nadia Comaneci",
                "Ned Kelly",
                "Neil Armstrong",
                "Nelson Mandela",
                "Niall Horan",
                "Olga Korbut",
                "Orville Wright",
                "Prince Harry",
                "Princess Diana",
                "Pope Francis",
                "Richard Nixon",
                "Roald Dahl",
                "Rob Roy",
                "Robert Burns",
                "Roger Federer",
                "Ronald Reagan",
                "Rosa Parks",
                "Sadako Sasaki",
                "Saint Andrew",
                "Saint David",
                "Saint George",
                "Saint Patrick",
                "Samuel Pepys",
                "Sebastian Coe",
                "Serena Williams",
                "Stephen Hawking",
                "Steve Irwin",
                "Steve Redgrave",
                "Taylor Swift",
                "Theodore Roosevelt",
                "Thomas Jefferson",
                "Tiger Woods",
                "Tim Peake",
                "Ulysses Grant",
                "Usain Bolt",
                "Vichai Srivaddhanaprabha",
                "Wallace Hartley",
                "Walter Raleigh",
                "Warren Harding",
                "Wilbur Wright",
                "William Harrison",
                "William McKinley",
                "William Shakespeare",
                "William Taft",
                "William Wallace",
                "William Wordsworth",
                "Winston Churchill",
                "Woodrow Wilson",
                "Wright brothers",
                "Zachary Taylor",
                "Zayn Malik");
        for (String name : nameList){
            authentication.createAccount(name.replaceAll(" ","")
                    , name.split(" ")[0]
                    , ""
                    ,  name.split(" ")[1]
                    , "1947-03-25"
                    ,"password123"
                    , "+447975777666"
                    , "addressLine1"
                    , "addressLine2"
                    , "addressLine3"
                    , "city"
                    , "zip"
                    , "country");
        }

        ArrayList<Customer> testing = databaseFileManager.readCustomerObjectsInFile();
        this.createTestTransactions(testing.get(0));
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
                new Transaction(Double.parseDouble(transactionData[0]), transactionData[1], transactionData[2], transactionData[3], "Bank Transfer");
            } else {
//                Transaction without a Memo
                new Transaction(Double.parseDouble(transactionData[0]), transactionData[1], transactionData[2], "Bank Transfer");
            }
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