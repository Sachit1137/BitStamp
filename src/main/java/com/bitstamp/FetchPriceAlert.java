package com.bitstamp;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bitstamp.ui.main.BookOrderTab;

import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static android.content.ContentValues.TAG;



public class FetchPriceAlert extends AsyncTask<Void,Void,Void> {
    public static String response,lastPrice;
    public static String NOTIFICATION_ID = "notification-id" ;
    public static String CHANNEL_NAME = "channel" ;
    public static String channelDes = "notification ";

    private Context context;
    public FetchPriceAlert(Context context){
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {//background thread

        try {
            URL url = new URL("https://www.bitstamp.net/api/v2/ticker_hour/btcusd/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            JSONObject jobj = new JSONObject(response);
            lastPrice = (String)jobj.get("last");

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
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
        if (Float.parseFloat(lastPrice) < Float.parseFloat(BookOrderTab.price)){
            System.out.println("lastPrice = "+lastPrice+" and user price = "+BookOrderTab.price);

            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = CHANNEL_NAME;
                String description = channelDes;
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_ID).setSmallIcon(R.mipmap.ic_launcher_foreground).setContentTitle("Price Alert").setContentText("Price has fallen...").setStyle(new NotificationCompat.BigTextStyle()
                    .bigText("Price has fallen below the set price.Make Transaction. Hurry!!")).setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pendingIntent).setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(10001, builder.build());
        }
    }
}
