package com.example.vasu.expense_manager;

/**
 * Created by Vasu on 11/26/2016.
 */
public class Debt {
    private String requestUserID;
    private String confirmationUserID;
    private int amount;
    private String description;
    private int type;
    private int confirmed;
    private String date;
    private String ID;

    public Debt(){}

    public Debt(String requestUserID, String confirmationUserID, int amount, String description, int type, int confirmed, String date, String ID) {
        this.requestUserID = requestUserID;
        this.confirmationUserID = confirmationUserID;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.confirmed = confirmed;
        this.date = date;
        this.ID = ID;
    }

    public Debt(String requestUserID, String confirmationUserID, int amount, String description, int type, int confirmed, String date) {
        this.requestUserID = requestUserID;
        this.confirmationUserID = confirmationUserID;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.confirmed = confirmed;
        this.date = date;
    }
    public String getRequestUserID() {
        return requestUserID;
    }

    public void setRequestUserID(String requestUserID) {
        this.requestUserID = requestUserID;
    }

    public String getConfirmationUserID() {
        return confirmationUserID;
    }

    public void setConfirmationUserID(String confirmationUserID) {
        this.confirmationUserID = confirmationUserID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
