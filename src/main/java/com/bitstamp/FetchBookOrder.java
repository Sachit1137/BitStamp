package com.bitstamp;

import android.content.Context;
import android.os.AsyncTask;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.ArrayList;

import static com.bitstamp.ui.main.BookOrderTab.recyclerView;

public class FetchBookOrder extends AsyncTask<Void,Void,Void> {
    String data;
    //    LineChart lineChart;
    public static ArrayList<BidAskModel> bidData;
    private Context context;
    public FetchBookOrder(Context context){
        this.context = context;
    }



    @Override
    protected Void doInBackground(Void... voids) {//background thread
        try {

            URL url = new URL("https://www.bitstamp.net/api/v2/order_book/btcusd/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            data = convertStreamToString(in);
            JSONObject jobj = new JSONObject(data);
            JSONArray bids = jobj.getJSONArray("bids");
            JSONArray asks = jobj.getJSONArray("asks");
            bidData = new ArrayList<BidAskModel>();

            System.out.println(bids.length()+"="+asks.length());
            for(int i=0;i<bids.length();i++){
                bidData.add(new BidAskModel(Float.parseFloat((String)bids.getJSONArray(i).get(1)),Float.parseFloat((String)bids.getJSONArray(i).get(0)),Float.parseFloat((String)asks.getJSONArray(i).get(1)),Float.parseFloat((String)asks.getJSONArray(i).get(0))));
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

    @Override
    protected void onPostExecute(Void aVoid) {//UI thread
        super.onPostExecute(aVoid);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(bidData);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
    }

}
