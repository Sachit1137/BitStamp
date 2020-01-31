package com.bitstamp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bitstamp.FetchBookOrder;
import com.bitstamp.PriceAlert;
import com.bitstamp.R;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;


public class BookOrderTab extends Fragment implements View.OnClickListener {
    private static final String FILE_NAME = "example.txt";
    public static RecyclerView recyclerView;
    public static String price;
    LinearLayout linearLayout;
    Button refresh,button,buy,sell;
    EditText editText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2,container,false);
        recyclerView = view.findViewById(R.id.bidRecyclerView);
        linearLayout =  view.findViewById(R.id.linear_layout1);
        refresh = view.findViewById(R.id.refresh);
        editText = view.findViewById(R.id.edit_text);
        button = view.findViewById(R.id.setAlert);
        buy = view.findViewById(R.id.buy);
        sell = view.findViewById(R.id.sell);
        button.setOnClickListener(this);
        buy.setOnClickListener(this);
        sell.setOnClickListener(this);
        refresh.setOnClickListener(this);

        updateData();
        //loadPrice
        loadPrice();

        return view;
    }

    public void updateData(){
        FetchBookOrder fetch = new FetchBookOrder(getActivity().getApplicationContext());
        fetch.execute();
    }

    public void loadPrice(){

        FileInputStream fis = null;

        try {
            fis = getActivity().getApplicationContext().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            if (sb.toString()!=null)
            editText.setHint("Alert showing for $"+sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void savePrice(View v,String price){
        FileOutputStream fos = null;

        try {
            fos = getActivity().getApplicationContext().openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(price.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        if( v.getId()==R.id.refresh){
            updateData();//refresh api data
            Toast.makeText(getActivity(), "prices updated", Toast.LENGTH_SHORT).show();
        }

        if( v.getId()==R.id.buy){
            Toast.makeText(getActivity(), "Buy currency", Toast.LENGTH_SHORT).show();
        }

        if( v.getId()==R.id.sell){
            Toast.makeText(getActivity(), "sell currency", Toast.LENGTH_SHORT).show();
        }

        if (v.getId()==R.id.setAlert){

            if (editText.getText().length()>0){
                price = editText.getText().toString();
                //save price
                savePrice(v,price);
                //schedule job for every 1 hour
                Driver driver = new GooglePlayDriver(getActivity().getApplicationContext());
                FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(driver);

                Job constraintReminderJob = firebaseJobDispatcher.newJobBuilder()
                        .setService(PriceAlert.class)
                        .setTag("PriceAlert Service")
                        .setLifetime(Lifetime.FOREVER)
                        .setRecurring(true)
                        .setTrigger(Trigger.executionWindow(3600,3660))
                        .build();

                firebaseJobDispatcher.schedule(constraintReminderJob);
                Toast.makeText(getActivity(),"$ "+editText.getText()+" has been set for alert", Toast.LENGTH_LONG).show();
                editText.setHint("Alert showing for $"+editText.getText());
                editText.setText(null);
            }
            else{
                Toast.makeText(getActivity(),"Price not set", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
