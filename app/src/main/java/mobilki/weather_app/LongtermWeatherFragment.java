package mobilki.weather_app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import mobilki.weather_app.Database.CurrentWeather;
import mobilki.weather_app.Database.DataManagerImp;
import mobilki.weather_app.Database.LongTerm;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LongtermWeatherFragment extends Fragment {

    protected TextView day1;
    protected TextView day2;
    protected TextView day3;
    protected TextView day4;
    protected TextView day5;


    protected BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences.Editor editor = context.getSharedPreferences("PREFS_1", MODE_PRIVATE).edit();
            Bundle extras = intent.getExtras();
            final String data = (String) extras.get("output");
            final String typeOfForecast = (String) extras.get("typeOfForecast");
            if (typeOfForecast.equals("forecast")) {
                try {
                    JSONObject obj = new JSONObject(data);
                    if(obj.toString().length()<1000){
                        SharedPreferences prefs = context.getSharedPreferences("PREFS_1", MODE_PRIVATE);
                        String data1 = prefs.getString("Json", null);
                        obj = new JSONObject(data1);
                    }
                    else{
                        String yourString =  obj.toString();
                        editor.putString("Json",yourString  );
                        editor.commit();

                    }

                    String temperature1 = obj.getJSONArray("list").getJSONObject(4).getJSONObject("main").getString("temp");


                    String temperature2 = obj.getJSONArray("list").getJSONObject(8).getJSONObject("main").getString("temp");
                    String temperature3 = obj.getJSONArray("list").getJSONObject(12).getJSONObject("main").getString("temp");
                    String temperature4 = obj.getJSONArray("list").getJSONObject(16).getJSONObject("main").getString("temp");
                    String temperature5 = obj.getJSONArray("list").getJSONObject(20).getJSONObject("main").getString("temp");


                    String date1 = obj.getJSONArray("list").getJSONObject(4).getString("dt_txt");
                    String date2 = obj.getJSONArray("list").getJSONObject(8).getString("dt_txt");
                    String date3 = obj.getJSONArray("list").getJSONObject(12).getString("dt_txt");
                    String date4 = obj.getJSONArray("list").getJSONObject(16).getString("dt_txt");
                    String date5 = obj.getJSONArray("list").getJSONObject(20).getString("dt_txt");

                    //oryginalny output jest w kelvinach wiec zamieniam na celsjusze
                    double temp1double = Double.parseDouble(temperature1);
                    double temp2double = Double.parseDouble(temperature2);
                    double temp3double = Double.parseDouble(temperature3);
                    double temp4double = Double.parseDouble(temperature4);
                    double temp5double = Double.parseDouble(temperature5);

                    double temp1celsius = temp1double - 273.15;
                    double temp2celsius = temp2double - 273.15;
                    double temp3celsius = temp3double - 273.15;
                    double temp4celsius = temp4double - 273.15;
                    double temp5celsius = temp5double - 273.15;

                    day1 = getView().findViewById(R.id.day1);
                    day2 = getView().findViewById(R.id.day2);
                    day3 = getView().findViewById(R.id.day3);
                    day4 = getView().findViewById(R.id.day4);
                    day5 = getView().findViewById(R.id.day5);

                    updateUnits(day1,temp1celsius, date1);
                    updateUnits(day2,temp2celsius, date2);
                    updateUnits(day3,temp3celsius, date3);
                    updateUnits(day4,temp4celsius, date4);
                    updateUnits(day5,temp5celsius, date5);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void updateUnits(TextView day, double temp1InCelsius, String date)
    {

        DecimalFormat df = new DecimalFormat("#.#");

        SharedPreferences prefs = getActivity().getSharedPreferences("PREFS", getActivity().MODE_PRIVATE);
        int option = prefs.getInt("option", 0);
        if (option == 0){
            day.setText(date+": " + String.valueOf(df.format(temp1InCelsius)) + " \u2103");
        }
        else if (option == 1){
            double temp1InKelvin = temp1InCelsius + 273.15;
            day.setText(date+": " + String.valueOf(df.format(temp1InKelvin)) + " K");
        }
        else if (option == 2){
            double temp1InFahrenheit = 32 + (temp1InCelsius * 9 / 5);
            day.setText(date+": " + String.valueOf(df.format(temp1InFahrenheit)) + " F");
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
