package com.example.vasu.expense_manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasu on 11/16/2016.
 */
public class DebtFragment extends Fragment {

    public static List<Debt> debts = new ArrayList<Debt>();
    public static RecyclerAdapterDebt adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("DebtFragment", "onCreateView: ");
        View view = inflater.inflate(R.layout.debt_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.debt_recyclerView);
        adapter = new RecyclerAdapterDebt(getActivity(),RecyclerAdapterDebt.NetDebts);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}
