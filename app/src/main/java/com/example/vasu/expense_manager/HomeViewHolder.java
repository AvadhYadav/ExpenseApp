package com.example.vasu.expense_manager;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vasu on 11/16/2016.
 */
public class HomeViewHolder extends RecyclerView.ViewHolder{
    TextView tv1, tv2,tv3;
    public ImageView imageView;
    public HomeViewHolder(View itemView) {
        super(itemView);
        tv1 = (TextView) itemView.findViewById(R.id.catagoty);
        tv2 = (TextView) itemView.findViewById(R.id.value);
        tv3 = (TextView) itemView.findViewById(R.id.desc);
        imageView = (ImageView) itemView.findViewById(R.id.card_icon);
    }
}
