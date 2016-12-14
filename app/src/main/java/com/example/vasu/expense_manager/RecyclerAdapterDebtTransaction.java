package com.example.vasu.expense_manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Vasu on 11/27/2016.
 */
public class RecyclerAdapterDebtTransaction extends RecyclerView.Adapter<TransactionHolder>{
    ArrayList<Debt> debtLedger;
    Context context;
    LayoutInflater inflater;

    public RecyclerAdapterDebtTransaction(Context context, ArrayList<Debt> debtLedger) {
        this.debtLedger = debtLedger;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_card_view, parent, false);

        TransactionHolder viewHolder = new TransactionHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransactionHolder holder, int position) {
        if(this.debtLedger.get(position).getType()=='L')
            holder.Amount.setText("You Paid Rs: "+ String.valueOf(this.debtLedger.get(position).getAmount()));
        else
            holder.Amount.setText(this.debtLedger.get(position).getConfirmationUserID()+" Paid Rs: "+String.valueOf(this.debtLedger.get(position).getAmount()));

        holder.Date.setText(this.debtLedger.get(position).getDate());
        holder.Description.setText(this.debtLedger.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return debtLedger.size();
    }
}
