package com.banking_application;

public interface Card {

    public void makePayment(double value);
    public long getCardNumber();
    public int getExpMonth();
    public int getExpYear();
    public int getCVV();
    public String getCardName();
    public String getConnectedBankAccount();
    public Boolean getIsActive();
    public void setIsActive(Boolean active);

}
