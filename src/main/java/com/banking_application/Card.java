package com.banking_application;


public interface Card {

    /**
     * Method to generate a payment on a Card
     * @param value takes in a Double as the value of the transaction that will be created
     */
    void makePayment(double value);

    /**
     * Getter method to return the 16 digit Card Number
     * @return long value of the Card Number
     */
    long getCardNumber();

    /**
     * Getter method to return the month value of the expiration date of the card
     * @return int value of the month in which the card expires
     */
    int getExpMonth();

    /**
     * Getter method to return the year value of the expiration date of the card
     * @return int value of the year in which the card expires.
     */
    int getExpYear();

    /**
     * Getter method to return the CVV code of the Card
     * @return int value of the CVV code of the card
     */
    int getCVV();

    /**
     * Getter method for the Name that is assigned to the card
     * @return a String value of the name connected to the card
     */
    String getCardName();

    /**
     * Getter method to retrieve the IBAN of the bank that is connected to this card
     * @return a String of the full IBAN of the connected bank account
     */
    String getConnectedBankAccount();

    /**
     * Getter method to review if this card is active or deactivated
     * @return a boolean value for the status of the card. True if the card is still active
     */
    Boolean getIsActive();

    /**
     * Setter method, in which the card can be set to active or inactive. In practice, a card should only be deactivated using this setter method
     * @param active a Boolean value in which the card is set to Inactive using False value
     */
    void setIsActive(Boolean active);

    /**
     * Getter method to retrieve the human readable card number. Includes whitespace every 4 characters
     * @return String version of the Card Number
     */
    String getReadableCardNumber();
}
