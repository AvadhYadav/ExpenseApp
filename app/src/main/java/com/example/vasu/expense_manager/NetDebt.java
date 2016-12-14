package com.example.vasu.expense_manager;

/**
 * Created by Vasu on 11/26/2016.
 */
public class NetDebt {
    private String User;
    private int netDebt;

    public NetDebt(String user, int netDebt) {
        User = user;
        this.netDebt = netDebt;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getNetDebt() {
        return netDebt;
    }

    public void setNetDebt(int netDebt) {
        this.netDebt = netDebt;
    }
}
