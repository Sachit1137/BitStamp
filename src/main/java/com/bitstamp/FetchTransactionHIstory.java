package com.bitstamp;

import android.content.Context;
import android.graphics.Color;

import android.os.AsyncTask;
import android.view.View;

import com.bitstamp.ui.main.TransactionTab;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.bitstamp.ui.main.TransactionTab.lineChart;
import static com.bitstamp.ui.main.TransactionTab.progressBar;

public class FetchTransactionHIstory extends AsyncTask<Void,Void,Void> {
    String data;
    public static LineDataSet lineDataSet;

    public static ArrayList<Entry> dataVal;
    private Context context;
    public FetchTransactionHIstory(Context context){
        this.context = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    protected Void doInBackground(Void... voids) {//background thread
        try {

            URL url = new URL("https://www.bitstamp.net/api/v2/transactions/btcusd/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            data = convertStreamToString(in);
            JSONArray jsonArray = new JSONArray(data);
            dataVal = new ArrayList<Entry>();

            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jobj = (JSONObject) jsonArray.get(i);
                float seconds = Float.parseFloat(""+jobj.get("date"));
                float price = Float.parseFloat(""+jobj.get("price"));
                dataVal.add(new Entry(seconds,price));
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
    private String getDate(float seconds) throws JSONException {
        long val = (long) seconds;
        Date date = new java.util.Date(val*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("kk:mm");
//        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-5"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    @Override
    protected void onPostExecute(Void aVoid) {//UI thread
        super.onPostExecute(aVoid);

        lineDataSet = new LineDataSet(dataVal, "Price V/S Current Time");
        lineDataSet.setLineWidth(1f);

        lineDataSet.setDrawCircles(false);
        lineDataSet.setColor(Color.RED);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.getAxisLeft().setValueFormatter(new MyYValueFormatter());
        lineChart.getAxisLeft().setLabelCount(5,true);
        lineChart.getAxisRight().setValueFormatter(new MyYValueFormatter());
        lineChart.getAxisRight().setLabelCount(5,true);
        lineChart.getXAxis().setValueFormatter(new MyXValueFormatter());
        lineChart.getXAxis().setLabelCount(8,true);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(10f);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setData(data);
        lineChart.animateX(1000,Easing.EaseInCirc);
        if (lineDataSet!=null) {

            float currVal = lineDataSet.getValues().get(0).getY();
            float openVal = lineDataSet.getValues().get(lineDataSet.getValues().size() - 1).getY();
            float percDiff = ((currVal - openVal) / currVal) * 100;

            TransactionTab.maxVal.setText("$"+String.format("%.2f",currVal-openVal) + " ("+String.format("%.2f",percDiff)+"%) | 1h" );
            TransactionTab.currVal.setText("$"+lineDataSet.getValues().get(0).getY());
        }
        progressBar.setVisibility(View.INVISIBLE);

    }

    class MyYValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return "$ "+(int)value;
        }
    }

    class MyXValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            String date="";
            try {
                 date = getDate(value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return date;
        }
    }

}
