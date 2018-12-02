package mobilki.weather_app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mobilki.weather_app.Database.CurrentWeather;
import mobilki.weather_app.Database.DataManagerImp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService extends Service {

    public Context context = this;
    private Long Id;
    private static MainActivity parent;

    public WeatherService(MainActivity parent) {
        this.parent = parent;
    }

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
        final String typeOfForecast = (String) extras.get("typeOfForecast");
        SharedPreferences prefs = context.getSharedPreferences("PREFS", context.MODE_PRIVATE);
        final String localization = prefs.getString("localization", "");



        Thread thread = new Thread(new Runnable() {
            public void run() {


                DataManagerImp db = new DataManagerImp(context);
                CurrentWeather weather = new CurrentWeather();
                String API_KEY = "3550b77377dff0538aab7ef52d1449c3";
                String url;
                if (!localization.equals("")){
                     url = "http://api.openweathermap.org/data/2.5/"+typeOfForecast+"?" +
                            "q="+ localization+
                            "&APPID="+ API_KEY;
                }
                else {
                     url = "http://api.openweathermap.org/data/2.5/" + typeOfForecast + "?" +
                            "lat=" + latitude +
                            "&lon=" + longtitude +
                            "&APPID=" + API_KEY;
                }
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String output = response.body().string();
                    Intent intent = new Intent ("weatherdata");
                    intent.putExtra("output", output);
                    intent.putExtra("typeOfForecast", typeOfForecast);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    try {
                        JSONObject obj = new JSONObject(output);
                        String cityName = obj.getString("name");
                        Log.d(cityName, "testow");
                        String temperature = obj.getJSONObject("main").getString("temp");
                        String humidity = obj.getJSONObject("main").getString("humidity");
                        String pressure = obj.getJSONObject("main").getString("pressure");
                        String clouds = obj.getJSONObject("clouds").getString("all");

                        weather.setCity(cityName);
                        weather.setCloud(clouds);
                        weather.setHum(humidity);
                        Log.v(humidity, "wartosci");
                        weather.setPres(pressure);
                        Log.v(pressure, "wartosci");
                        weather.setTemp(temperature);
                        db.saveCurrent(weather);
                        Id = weather.getId();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                catch(IOException e) {
                    CurrentWeather weather1 = new CurrentWeather();
                    if(Id == null){
                        CharSequence text = "Brark połaczenia, brak danych zapisanych w pamieci!";
                        int duration = Toast.LENGTH_LONG;

                   //toast ????


                    }else
                        {
                        weather1 = db.getCurrent(Id);

                        //toast
                        //"main":{"temp":285.514,"pressure":1013.75,"humidity":100,"temp_min":285.514,"temp_max":285.514,"sea_level":1023.22,"grnd_level":1013.75}
                        String output = "{ \"name\":\"" + weather1.getCity() + "\",\"main\":{\"temp\":\"" + weather1.getTemp() + "\", \"humidity\": \"" + weather1.getHum() + "\", " +
                                "\"pressure\": \"" + weather1.getPres() + "\"}, \"clouds\":{\"all\": \"" + weather1.getCloud() + "\"}}";
                        Intent intent = new Intent("weatherdata");
                        intent.putExtra("output", output);
                        intent.putExtra("typeOfForecast", typeOfForecast);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return Service.START_NOT_STICKY;
    }

}
