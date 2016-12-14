package com.example.vasu.expense_manager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Vasu on 11/27/2016.
 */
public class TransactionHolder extends RecyclerView.ViewHolder{

    TextView Date;
    TextView Amount;
    TextView Description;
    public TransactionHolder(View itemView) {
        super(itemView);
        Amount = (TextView) itemView.findViewById(R.id.transaction_amount);
        Date = (TextView) itemView.findViewById(R.id.transaction_date);
        Description = (TextView) itemView.findViewById(R.id.transaction_description);
    }
}
