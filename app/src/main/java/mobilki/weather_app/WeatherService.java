package mobilki.weather_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;


    public WeatherService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onCreate() {

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        Bundle extras = intent.getExtras();
        final String latitude = (String) extras.get("latitude");
        final String longtitude = (String) extras.get("longtitude");

        Thread thread = new Thread(new Runnable() {
            public void run() {
                String API_KEY = "3550b77377dff0538aab7ef52d1449c3";
                String url = "http://api.openweathermap.org/data/2.5/weather?" +
                        "lat="+ latitude+
                        "&lon="+ longtitude+
                        "&APPID="+ API_KEY;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String output = response.body().string();
                    Intent intent = new Intent ("weatherdata");
                    intent.putExtra("output", output);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return Service.START_NOT_STICKY;
    }

}
