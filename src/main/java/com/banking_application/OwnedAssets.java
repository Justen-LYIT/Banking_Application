package com.banking_application;

import java.io.Serializable;
import java.util.ArrayList;

public class OwnedAssets implements Serializable {
    final ArrayList<BankAccount> bankAccounts = new ArrayList<>();
    final ArrayList<Card> cards = new ArrayList<>();

    public OwnedAssets(){
        bankAccounts.add(new CurrentAccount());
        this.createNewDebitCard();
    }


    public double retrieveAccountValue(){
        double totalValue = 0.0;
        for (BankAccount account:bankAccounts){
            totalValue += account.getBalance();
        }
        return totalValue;
    }

    public double retrieveCreditValue(){
        double totalValue = 0.0;
        for (BankAccount account:bankAccounts){
            if (account instanceof DebtAccount == false) {
                totalValue += account.getBalance();
            }
        }
        return totalValue;
    }

    public double retrieveDebitValue(){
        double totalValue = 0.0;
        for (BankAccount account:bankAccounts){
            if (account instanceof DebtAccount) {
                totalValue += account.getBalance();
            }
        }
        return totalValue;
    }

    public ArrayList<Card> retrieveOwnedDebitCards(){
        ArrayList<Card> ownedDebitCards = new ArrayList<>();
        for (Card ownedCards : this.cards){
            if (ownedCards.getClass().getSimpleName().equals("DebitCard") &&
                    ownedCards.getIsActive()){
                ownedDebitCards.add(ownedCards);
            }
        }
        return ownedDebitCards;
    }

    public ArrayList<Card> retrieveOwnedCreditCards(){
        ArrayList<Card> ownedCreditCards = new ArrayList<>();
        for (Card ownedCards : this.cards){
            if (ownedCards.getClass().getSimpleName().equals("CreditCard") &&
                    ownedCards.getIsActive()){
                ownedCreditCards.add(ownedCards);
            }
        }
        return ownedCreditCards;
    }

    public CurrentAccount createNewCurrentAccount(){
        for (BankAccount account:bankAccounts){
            if( account instanceof CurrentAccount){
                return null;
            }
        }
        CurrentAccount currentAccount = new CurrentAccount();
        this.bankAccounts.add(currentAccount);
        return currentAccount;
    }

    public SavingAccount createNewSavingAccount(String bankAccountName){
        int numExistingAccounts  = 0;
        for (BankAccount account:bankAccounts){
            if( account instanceof SavingAccount){
                numExistingAccounts += 1;
            }
        }
        if (numExistingAccounts > 2){
            return  null;
        }
        SavingAccount savingAccount = new SavingAccount(bankAccountName);
        this.bankAccounts.add(savingAccount);
        return savingAccount;
    }

    public DebtAccount createNewDebtAccount(double value, double debtLimit, String bankAccountName){
        DebtAccount debtAccount = new DebtAccount(value,debtLimit,bankAccountName);
        this.bankAccounts.add(debtAccount);
        return debtAccount;
    }

    public void createNewDebitCard(){
        for (BankAccount bankAccount: this.bankAccounts){
            if( bankAccount instanceof CurrentAccount ) {
                DebitCard debitCard = new DebitCard(((CurrentAccount) bankAccount).getIBAN());
                this.cards.add(debitCard);
                return;
            }
        }
    }

    public CreditCard createNewCreditCard(DebtAccount debtAccount, String cardName){
        CreditCard creditCard = new CreditCard(debtAccount.getIBAN(), cardName);
        this.cards.add(creditCard);
        return  creditCard;
        }


        public void cancelCard(Card card){
            this.cards.remove(card);

        }

        public Card replaceCard(Card card){
            for (int i =0;i<this.cards.size(); i++) {
                if(this.cards.get(i).equals(card)) {
                    if (card.getClass().getSimpleName().equals("CreditCard")) {
                        CreditCard newCard = new CreditCard(card.getConnectedBankAccount() , card.getCardName());
                        this.cards.set(i, newCard);
                        return newCard;
                    } else if (card.getClass().getSimpleName().equals("DebitCard")) {
                        DebitCard newCard = new DebitCard(card.getConnectedBankAccount());
                        this.cards.set(i, newCard );
                    }
                }
            }
            return null;
        }

        public void cancelBankAccount(BankAccount bankAccount){
            this.bankAccounts.remove(bankAccount);
        }




}
