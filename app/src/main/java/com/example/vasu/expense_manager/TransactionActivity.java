package com.example.vasu.expense_manager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerAdapterDebtTransaction mAdapter;
    private ArrayList<Debt> DebtLedger = new ArrayList<Debt>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //////////////////////////TOOLBAR/////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///////////////////////////////////////////////////////////////////
        String user = getIntent().getStringExtra("user");
        recyclerView = (RecyclerView) findViewById(R.id.debt_transaction_recyclerView);
        loadData(user);
        mAdapter = new RecyclerAdapterDebtTransaction(getApplicationContext(),DebtLedger);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void loadData(String user) {
        DebtLedger = new ArrayList<Debt>();
        for(int i=0;i<DebtFragment.debts.size();i++){
            if(DebtFragment.debts.get(i).getConfirmationUserID().equals(user)){
                DebtLedger.add(DebtFragment.debts.get(i));
            }
        }
    }

}
