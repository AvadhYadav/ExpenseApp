package com.example.vasu.expense_manager;

/**
 * Created by Nishant on 23-11-2016.
 */
public class Expense {

    private int value;
    private String desc;
    private String catagory;
    private String Date;

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public Expense(int value, String catagory, String desc, String date) {
        this.value = value;
        this.desc = desc;
        this.catagory = catagory;
        this.Date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) { this.value = value; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }



}
