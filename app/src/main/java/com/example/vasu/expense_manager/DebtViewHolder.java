package com.example.vasu.expense_manager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Vasu on 11/26/2016.
 */
public class DebtViewHolder extends RecyclerView.ViewHolder {

    TextView name, net_loaned, net_owed;
    CardView DebtCard;
    LinearLayout netDebtCard;
    public DebtViewHolder(View itemView) {
        super(itemView);
        DebtCard = (CardView) itemView.findViewById(R.id.card_view_netDebt);
        netDebtCard = (LinearLayout) itemView.findViewById(R.id.card_inside_netDebt);
        name = (TextView) itemView.findViewById(R.id.Debt_user);
        net_loaned = (TextView) itemView.findViewById(R.id.Debt_loaned);
        net_owed = (TextView) itemView.findViewById(R.id.Debt_owed);
        itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d(String.valueOf(getPosition()), "onClick: zz");
            }
        });

    }
}
