package mobilki.weather_app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import mobilki.weather_app.Database.CurrentWeather;
import mobilki.weather_app.Database.DataManagerImp;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {


    protected TextView city;
    protected TextView temp;
    protected TextView hum;
    protected TextView pres;
    protected TextView cloud;
    private Context context;

    protected BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("kolejnosc", "tu pozniej");
            Bundle extras = intent.getExtras();
            final String data = (String) extras.get("output");
            final String typeOfForecast = (String) extras.get("typeOfForecast");
            if (typeOfForecast.equals("weather")) {
                city = getView().findViewById(R.id.city);
                temp = getView().findViewById(R.id.temp);
                hum = getView().findViewById(R.id.humidity);
                pres = getView().findViewById(R.id.pressure);
                cloud = getView().findViewById(R.id.clouds);

                try {
                    JSONObject obj = new JSONObject(data);
                    String cityName = obj.getString("name");
                    Log.d(cityName, "testow");
                    String temperature = obj.getJSONObject("main").getString("temp");
                    String humidity = obj.getJSONObject("main").getString("humidity");
                    String pressure = obj.getJSONObject("main").getString("pressure");
                    String clouds = obj.getJSONObject("clouds").getString("all");
                    //oryginalny output jest w kelvinach wiec zamieniam na celsjusze
                    double tempdouble = Double.parseDouble(temperature);
                    double tempcelsius = tempdouble - 273.15;

                    DataManagerImp db = new DataManagerImp(context);
                    CurrentWeather weather = new CurrentWeather();
                    weather.setCity(cityName);
                    weather.setCloud(clouds);
                    weather.setHum(humidity);
                    weather.setPres(pressure);
                    weather.setTemp(temperature);
                    db.saveCurrent(weather);

                    city.setText(getResources().getString(R.string.city) + ": " + cityName);
                    hum.setText(getResources().getString(R.string.humidity) + ": " + humidity + "%");
                    pres.setText(getResources().getString(R.string.pressure) + ": " + pressure + "hpa");
                    cloud.setText(getResources().getString(R.string.clouds) + ": " + clouds + "%");
                    updateUnits(tempcelsius);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void updateUnits(double tempInCelsius)
    {
        temp = getView().findViewById(R.id.temp);
        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        int option = prefs.getInt("option", 0);
        if (option == 0){
            temp.setText(getResources().getString(R.string.temperature)+": " + String.valueOf(tempInCelsius) + " \u2103");
        }
        else if (option == 1){
            double tempInKelvin = tempInCelsius + 273.15;
            temp.setText(getResources().getString(R.string.temperature)+": " + String.valueOf(tempInKelvin) + " K");
        }
        else if (option == 2){
            double tempInFahrenheit = 32 + (tempInCelsius * 9 / 5);
            temp.setText(getResources().getString(R.string.temperature)+": " + String.valueOf(tempInFahrenheit) + " F");
        }
    }

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        Log.v("kolejnosc", "test");
        //TODO zastapic defValues tymi z bazy czy cos
        String latitude = prefs.getString("latitude", "0");
        String longtitude = prefs.getString("longtitude", "0");
        Intent serviceIntent = new Intent(getActivity(),WeatherService.class);
        serviceIntent.putExtra("latitude", latitude);
        serviceIntent.putExtra("longtitude", longtitude);
        serviceIntent.putExtra("typeOfForecast", "weather");
        getActivity().startService(serviceIntent);
    }
    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bReceiver, new IntentFilter("weatherdata"));
    }

    @Override
    public void onPause (){
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
    }
}
