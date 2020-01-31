package com.bitstamp.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bitstamp.FetchTransactionHIstory;
import com.bitstamp.PriceAlert;
import com.bitstamp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;



public class TransactionTab extends Fragment implements View.OnClickListener{
    public static LineChart lineChart;
    public static ProgressBar progressBar;

    LinearLayout linearLayout;
    Button refresh ;

    public static TextView maxVal;
    public static TextView currVal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1,container,false);
        lineChart = view.findViewById(R.id.line_chart);
        maxVal = view.findViewById(R.id.maxVal);
        progressBar = view.findViewById(R.id.progressBar);

        currVal = view.findViewById(R.id.currVal);
        refresh = view.findViewById(R.id.tab1Refresh);
        linearLayout =  view.findViewById(R.id.linear_layout1);

        refresh.setOnClickListener(this);

        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);

        maxVal.setTextColor(Color.BLACK);
        maxVal.setBackgroundColor(Color.rgb(255, 165, 0));

        currVal.setTextColor(Color.BLACK);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(true);
        // get the legend (only possible after setting data)
        Legend l = lineChart.getLegend();
        // modify the legend ... by default it is on the left
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setForm(Legend.LegendForm.SQUARE);

        updateData();
        return view;
    }

    public void updateData(){
        FetchTransactionHIstory fetch = new FetchTransactionHIstory(getActivity().getApplicationContext());
        fetch.execute();
    }

    @Override
    public void onClick(View v) {

        if( v.getId()==R.id.tab1Refresh){
            updateData();//refresh api data
            Toast.makeText(getActivity(), "data updated", Toast.LENGTH_SHORT).show();
        }


    }





}
