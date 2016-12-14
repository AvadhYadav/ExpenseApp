package com.example.vasu.expense_manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasu on 11/26/2016.
 */
public class RecyclerAdapterDebt extends RecyclerView.Adapter<DebtViewHolder> {

    public static List<NetDebt> NetDebts = new ArrayList<NetDebt>();
    private Context context;
    LayoutInflater inflater;

    public RecyclerAdapterDebt(Context context, List<NetDebt> netDebts) {
        this.context = context;
        this.NetDebts = netDebts;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DebtViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.debt_card_view, parent, false);
        DebtViewHolder viewHolder = new DebtViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DebtViewHolder holder, final int position) {
        NetDebt currNetDebt = this.NetDebts.get(position);

        holder.netDebtCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,TransactionActivity.class);
                intent.putExtra("user",NetDebts.get(position).getUser());
                context.startActivity(intent);
            }
        });

        holder.name.setText(currNetDebt.getUser());
        int netDebt = currNetDebt.getNetDebt();
        if(netDebt<0){
            netDebt = -netDebt;
            holder.net_owed.setText("You Owe: Rs: "+netDebt);
            holder.net_loaned.setText("You Get: Rs: 0");
            holder.DebtCard.setBackgroundColor(Color.parseColor("#0ae30022"));
            //holder.DebtCard.setBackgroundResource(R.drawable.owe_back);
        }else{
            holder.net_owed.setText("You Owe: Rs: 0");
            holder.net_loaned.setText("You Get: Rs: "+netDebt);
            holder.DebtCard.setBackgroundColor(Color.parseColor("#0a00ff6f"));
            //holder.DebtCard.setBackgroundResource(R.drawable.get_back);
        }



    }



    @Override
    public int getItemCount() {
        if (this.NetDebts == null) {
            return 0;
        } else {
            return this.NetDebts.size();
        }
    }


}
