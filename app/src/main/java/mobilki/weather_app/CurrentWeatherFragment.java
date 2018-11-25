package mobilki.weather_app;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentWeatherFragment extends Fragment {

    private FusedLocationProviderClient mFusedLocationClient;
    public static double latitude;
    public static double longtitude;
    protected TextView city;
    protected TextView temp;
    protected TextView hum;

    protected BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            final String data = (String) extras.get("output");
            city = getView().findViewById(R.id.city);
            temp = getView().findViewById(R.id.temp);
            hum = getView().findViewById(R.id.humidity);

            try {
                JSONObject obj = new JSONObject(data);
                String cityName = obj.getString("name");
                String temperature = obj.getJSONObject("main").getString("temp");
                String humidity = obj.getJSONObject("main").getString("humidity");

                //oryginalny output jest w kelvinach wiec zamieniam na celsjusze
                double tempdouble = Double.parseDouble(temperature);
                double tempcelsius = tempdouble - 273.15;

                city.setText("Lokalizacja: " + cityName);
                temp.setText("Temperatura: " + String.valueOf(tempcelsius) + " \u2103");
                hum.setText("Wilgotność: " + humidity + "%");

            }
                catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //TODO sprawdzanie permisions
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                            Intent serviceIntent = new Intent(getActivity(),WeatherService.class);
                            serviceIntent.putExtra("latitude", String.valueOf(latitude));
                            serviceIntent.putExtra("longtitude", String.valueOf(longtitude));
                            getActivity().startService(serviceIntent);
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();


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
