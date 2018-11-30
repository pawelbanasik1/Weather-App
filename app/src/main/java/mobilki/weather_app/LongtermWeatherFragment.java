package mobilki.weather_app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class LongtermWeatherFragment extends Fragment {


    protected TextView day1;
    protected TextView day2;
    protected TextView temp1;
    protected TextView temp2;


    protected BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            final String data = (String) extras.get("output");
            final String typeOfForecast = (String) extras.get("typeOfForecast");
            if (typeOfForecast.equals("forecast")) {
                try {
                    JSONObject obj = new JSONObject(data);
                    String temperature1 = obj.getJSONArray("list").getJSONObject(8).getJSONObject("main").getString("temp");
                    String temperature2 = obj.getJSONArray("list").getJSONObject(16).getJSONObject("main").getString("temp");

                    //oryginalny output jest w kelvinach wiec zamieniam na celsjusze
                    double temp1double = Double.parseDouble(temperature1);
                    double temp2double = Double.parseDouble(temperature2);
                    double temp1celsius = temp1double - 273.15;
                    double temp2celsius = temp2double - 273.15;

                    updateUnits(temp1celsius, temp2celsius);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void updateUnits(double temp1InCelsius, double temp2InCelsius)
    {
        day1 = getView().findViewById(R.id.day1);
        day2 = getView().findViewById(R.id.day2);
        DecimalFormat df = new DecimalFormat("#.#");

        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        int option = prefs.getInt("option", 0);
        if (option == 0){
            day1.setText(getResources().getString(R.string.tomorrow)+": " + String.valueOf(df.format(temp1InCelsius)) + " \u2103");
            day2.setText(getResources().getString(R.string.day_after)+": " + String.valueOf(df.format(temp2InCelsius)) + " \u2103");

        }
        else if (option == 1){
            double temp1InKelvin = temp1InCelsius + 273.15;
            double temp2InKelvin = temp2InCelsius + 273.15;
            day1.setText(getResources().getString(R.string.tomorrow)+": " + String.valueOf(df.format(temp1InKelvin)) + " K");
            day2.setText(getResources().getString(R.string.day_after)+": " + String.valueOf(df.format(temp2InKelvin)) + " K");

        }
        else if (option == 2){
            double temp1InFahrenheit = 32 + (temp1InCelsius * 9 / 5);
            double temp2InFahrenheit = 32 + (temp2InCelsius * 9 / 5);
            Log.d("Test1", String.valueOf(temp2InFahrenheit));
            day1.setText(getResources().getString(R.string.tomorrow)+": " + String.valueOf(df.format(temp1InFahrenheit)) + " F");
            day2.setText(getResources().getString(R.string.day_after)+": " + String.valueOf(df.format(temp2InFahrenheit)) + " F");

        }
    }

    public LongtermWeatherFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_longterm_weather, container, false);
    }
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        //TODO zastapic defValues tymi z bazy czy cos
        String latitude = prefs.getString("latitude", "0");
        String longtitude = prefs.getString("longtitude", "0");
        Intent serviceIntent = new Intent(getActivity(),WeatherService.class);
        serviceIntent.putExtra("latitude", latitude);
        serviceIntent.putExtra("longtitude", longtitude);
        serviceIntent.putExtra("typeOfForecast", "forecast");
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
