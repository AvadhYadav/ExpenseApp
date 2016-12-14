package com.example.vasu.expense_manager.Model;

import java.util.Date;

/**
 * Created by Freeware Sys on 11/29/2016.
 */
public class Person {
    private int ID;

    private String requestUserID;

    private String confirmationUserID;

    private double amount;

    private String description;

    private double type;

    private double confirmation;

    private Date date;

    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public String getrequestUserID(){
        return requestUserID;
    }

    public void setrequestUserID(String requestUserID){
        this.requestUserID = requestUserID;
    }

    public String getconfirmationUserID(){
        return confirmationUserID;
    }

    public void setconfirmationUserID(String confirmationUserID){
        this.confirmationUserID = confirmationUserID;
    }

    public double getamount(){
        return amount;
    }

    public void setamount(double amount){
        this.amount = amount;
    }

    public String getdescription(){
        return description;
    }

    public void setdescription(String description){
        this.description = description;
    }

    public double gettype(){
        return type;
    }

    public void settype(double type){
        this.type = type;
    }

    public double getconfirmation(){
        return confirmation;
    }

    public void setconfirmation(double confirmation){
        this.confirmation = confirmation;
    }

    public Date getdate(){
        return date;
    }

    public void setdate(Date date){
        this.date = date;
    }


}
