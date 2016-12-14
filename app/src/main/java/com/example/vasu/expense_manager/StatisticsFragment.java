package com.example.vasu.expense_manager;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


public class StatisticsFragment extends Fragment {

    int[] pieChartValues={1,0,0,0,0,0,0,0};
    String[] pieChartKeys = {"Food","Travel","Health","Transport","Bills","Groceries","Clothes","Other"};
    int[] pieChartColors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.RED, Color.GRAY, Color.YELLOW, Color.BLACK };
    static View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("", "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("", "onCreateView: ");
        view = inflater.inflate(R.layout.fragment_statistics, container, false);
        return view;
    }

    private void createBarGraph(View view) {
        setpiechartdata();
        XYSeries expenseSeries = new XYSeries("Expense");
        for(int i=0;i<pieChartKeys.length;i++){
            expenseSeries.add(i,pieChartValues[i]);

        }
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        dataset.addSeries(expenseSeries);
        XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
        incomeRenderer.setColor(Color.BLUE); //color of the graph set to cyan
        incomeRenderer.setFillPoints(true);
        incomeRenderer.setDisplayChartValues(true);
        incomeRenderer.setChartValuesTextSize(20);
        incomeRenderer.setChartValuesTextAlign(Paint.Align.RIGHT);


        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        multiRenderer.setChartTitle("Expense Distribution");
        multiRenderer.setYTitle("Money Spent");
        multiRenderer.setXTitle("Categories");
        multiRenderer.addSeriesRenderer(incomeRenderer);
        multiRenderer.setBarSpacing(0.5);
        multiRenderer.setBarWidth(50);
        multiRenderer.setXAxisMin(-0.5);
        multiRenderer.setXAxisMax(7.5);
        multiRenderer.setXLabels(0);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setXTitle("");
        multiRenderer.setYTitle("");
        multiRenderer.setChartTitleTextSize(30);

        multiRenderer.setLabelsTextSize(20);

        int max = 0;
        for (int i=0; i<pieChartValues.length;i++){
            if(max<pieChartValues[i]){
                max = pieChartValues[i];
            }
        }
        multiRenderer.setYAxisMax(1.2 * max);

        multiRenderer.setAxesColor(Color.BLACK);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(false, false);

        multiRenderer.setLabelsColor(Color.BLACK);
        multiRenderer.setXLabelsColor(Color.BLACK);
        multiRenderer.setYLabelsColor(0,Color.BLACK);

        multiRenderer.setMarginsColor(Color.WHITE);

        multiRenderer.setBackgroundColor(Color.TRANSPARENT);
        for(int i=0; i< pieChartKeys.length;i++){
            multiRenderer.addXTextLabel(i, pieChartKeys[i]);
        }

        LinearLayout chartContainer = (LinearLayout) view.findViewById(R.id.bar_graph);

        GraphicalView mChart = ChartFactory.getBarChartView(getActivity(), dataset, multiRenderer, BarChart.Type.DEFAULT);
        chartContainer.addView(mChart, new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));



    }

    private void createPieChart(View view){
        setpiechartdata();
        CategorySeries distributionSeries = new CategorySeries("Expense Distribution");
        for (int i = 0; i < pieChartKeys.length; i++) {
            if(pieChartValues[i]!=0)
                distributionSeries.add(pieChartKeys[i], pieChartValues[i]);
        }

        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i = 0; i < pieChartKeys.length; i++) {
            SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
            if(pieChartValues[i]!=0) {
                seriesRenderer.setColor(pieChartColors[i]);
                seriesRenderer.setDisplayChartValues(true);
                //Adding colors to the chart
                defaultRenderer.setBackgroundColor(Color.TRANSPARENT);
                defaultRenderer.setLabelsTextSize(25f);
                defaultRenderer.setLabelsColor(Color.BLACK);
                defaultRenderer.setShowLegend(false);
                defaultRenderer.setApplyBackgroundColor(false);
                // Adding a renderer for a slice
                defaultRenderer.addSeriesRenderer(seriesRenderer);
            }
        }

        defaultRenderer.setZoomEnabled(false);
        defaultRenderer.setPanEnabled(false);


        LinearLayout chartContainer = (LinearLayout) view.findViewById(R.id.chart);


        View mChart = ChartFactory.getPieChartView(getActivity(),
                distributionSeries, defaultRenderer);

        chartContainer.addView(mChart, new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500));

    }


    private void setpiechartdata() {
        for(int i=0;i<8;i++){
            pieChartValues[i] = 0;
        }

        for (int i=0;i< HomeFragment.expenses.size();i++){
            int value = HomeFragment.expenses.get(i).getValue();
            switch (HomeFragment.expenses.get(i).getCatagory()){
                case "Food":
                    pieChartValues[0]+=value;
                    break;
                case "Travel":
                    pieChartValues[1]+=value;
                    break;
                case "Health":
                    pieChartValues[2]+=value;
                    break;
                case "Transport":
                    pieChartValues[3]+=value;
                    break;
                case "Bills":
                    pieChartValues[4]+=value;
                    break;
                case "Groceries":
                    pieChartValues[5]+=value;
                    break;
                case "Clothes":
                    pieChartValues[6]+=value;
                    break;
                case "Other":
                    pieChartValues[7]+=value;
                    break;
            }
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("", "onActivityCreated: ");
        createPieChart(view);
        createBarGraph(view);
    }
}
