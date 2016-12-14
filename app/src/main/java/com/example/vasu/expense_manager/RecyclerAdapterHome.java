package com.example.vasu.expense_manager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Vasu on 11/16/2016.
 */
public class RecyclerAdapterHome extends RecyclerView.Adapter<HomeViewHolder>{


    private final List<Expense> expenses;
    Context context;
    LayoutInflater inflater;

    public RecyclerAdapterHome(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.home_card_view, parent, false);

        HomeViewHolder viewHolder = new HomeViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeViewHolder vholder = (HomeViewHolder) v.getTag();
                int position = vholder.getPosition();

            }
        };


        Expense expense = HomeFragment.expenses.get(getItemCount() - position - 1);
        if(expense!=null) {
            holder.tv1.setText(expense.getCatagory());
            String money = "Rs. " + String.valueOf(expense.getValue());
            holder.tv2.setText(money);
            holder.tv3.setText(expense.getDesc());
            String catg = expense.getCatagory();
            if (catg.equals("Food")) {
                holder.imageView.setImageResource(R.drawable.stand);
            }
            else if(catg.equals("Transport")) {
                holder.imageView.setImageResource(R.drawable.racing);
            }
            else if(catg.equals("Health")) {
                holder.imageView.setImageResource(R.drawable.health);
            }
            else if(catg.equals("Travel")) {
                holder.imageView.setImageResource(R.drawable.airplane);
            }
            else if(catg.equals("Bills")) {
                holder.imageView.setImageResource(R.drawable.receipt);
            }
            else if(catg.equals("Groceries")) {
                holder.imageView.setImageResource(R.drawable.groceries);
            }
            else if(catg.equals("Other")) {
                holder.imageView.setImageResource(R.drawable.question);
            }
            else if(catg.equals("Clothes")) {
                holder.imageView.setImageResource(R.drawable.suit);
            }

        }



    }

    @Override
    public int getItemCount() {
        return HomeFragment.expenses.size();
    }
}
