package com.sh4dow.climate;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WeatherController extends AppCompatActivity {

    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    final String POLLUTION_URL = "http://api.airvisual.com/v2/nearest_city";
    final String NEW_CITY_URL = "http://api.airvisual.com/v2/city";
    // App ID to use OpenWeather data
    final String APP_ID = "3febcd5834f24c2eb6f112db46287b1e";
    final String APP_ID_POL = "uLtvr8FpPAccY4TGf";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    final int REQUEST_CODE = 123;

    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    boolean started;

    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    TextView mHumidity;
    TextView mWindSpeed;
    TextView mPressure;
    TextView minTemp;
    TextView maxTemp;
    TextView condition;

    LocationManager mlocationManager;
    LocationListener mlocationListener;

    public String pollutionValue;
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = findViewById(R.id.locationTV);
        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        mHumidity = findViewById(R.id.humidity);
        mWindSpeed = findViewById(R.id.windspeed);
        mPressure = findViewById(R.id.pressure);
        minTemp = findViewById(R.id.mintemp);
        maxTemp = findViewById(R.id.maxtemp);
        condition = findViewById(R.id.condition);
        pollutionValue = "30";
        started = false;

        myDialog = new Dialog(this);

        ImageButton changeCityButton = findViewById(R.id.changeCityButton);

        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherController.this, ChangeCityController.class);
                startActivity(intent);
            }
        });
    }


    protected void onResume() {
        super.onResume();
        //Log.d("Clima", "On Resume called");

//        if (!started) {
//            started = true;
//            getWeatherForCurrentLocation();
//        }

        Intent intent = getIntent();
        String city = intent.getStringExtra("City");
        String state = intent.getStringExtra("State");
        String country = intent.getStringExtra("Country");

        if (city != null && state != null && country != null) {
            Log.d("Clima", city + " " + state + " " + country);
            /*city = "dhaka";
            state = "dhaka";
            country = "bangladesh";*/
            getWeatherForNewCity(city, state, country);
        }else{
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForNewCity(String city, String state, String country) {
//        RequestParams params = new RequestParams();
//        params.put("q",city);
//        params.put("appid",APP_ID);
//        letsDoSomeNetworking(params);

        Log.d("Clima", "Inside new city");

        RequestParams paramsPol = new RequestParams();
        paramsPol.put("city", city);
        paramsPol.put("state", state);
        paramsPol.put("country", country);
        paramsPol.put("key", APP_ID_POL);
        letsDoSomePollutionNetworking(paramsPol, true);
    }

    private void getWeatherForCurrentLocation() {
        Log.d("Clima", "Inside Current City");

        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mlocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

//                RequestParams params = new RequestParams();
//                params.put("lat",latitude);
//                params.put("lon",longitude);
//                params.put("appid",APP_ID);
//                letsDoSomeNetworking(params);

                RequestParams paramsPol = new RequestParams();
                paramsPol.put("lat", latitude);
                paramsPol.put("lon", longitude);
                paramsPol.put("key", APP_ID_POL);
                letsDoSomePollutionNetworking(paramsPol, false);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //Log.d("Clima", "onProviderDisabled() callback received");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mlocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mlocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getWeatherForCurrentLocation();
            }
        }
    }

//    private void letsDoSomeNetworking(RequestParams params){
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                //Log.e("Clima","Success! JSON "  + response.toString());
//                WeatherDataModel weatherData = WeatherDataModel.fromJSON(response);
//                updateUI(weatherData);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
//                //Log.e("Clima","Fail " + e.toString());
//                //Log.e("Clima","Status code: " + statusCode);
//                //Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void letsDoSomePollutionNetworking(RequestParams params, boolean newCity) {
        AsyncHttpClient client = new AsyncHttpClient();
        if (newCity) {
            client.get(newCity ? NEW_CITY_URL : POLLUTION_URL, params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //Log.e("Clima","Success! JSON "  + response.toString());
                    WeatherDataModel weatherData = WeatherDataModel.fromJSONPollution(response);
                    updateUI(weatherData);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
                    Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
//        else {
//            client.get(POLLUTION_URL, params, new JsonHttpResponseHandler() {
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                    Log.e("Clima", "Success! JSON " + response.toString());
//                    WeatherDataModel weatherData = WeatherDataModel.fromJSONPollution(response);
//                    updateUI(weatherData);
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject errorResponse) {
//                    Log.e("Clima", "Fail " + e.toString());
//                    Log.e("Clima", "Status code: " + statusCode);
//                    Toast.makeText(WeatherController.this, "Request Failed", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }

    private void updateUI(WeatherDataModel weather) {
        mTemperatureLabel.setText(weather.getmTemperature());
        mCityLabel.setText(weather.getmCity());
        mHumidity.setText(weather.getmHumidity());
        mWindSpeed.setText(weather.getmWindSpeed());
        mPressure.setText(weather.getmPressure());
        //minTemp.setText(weather.getMinTemp());
        //maxTemp.setText(weather.getMaxTemp());

        String tempCond = weather.getmIcon();
        tempCond = tempCond.replace("_", " ");
        condition.setText(tempCond);

        pollutionValue = weather.getmPollution();

        int resourceID = getResources().getIdentifier(weather.getmIcon(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mlocationManager != null) {
            mlocationManager.removeUpdates(mlocationListener);
        }
    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.activity_pollution);


        TextView txtclose, suggestion;
        //variables
        TextView pollutionVal;
        ImageView imageReact;

        txtclose = myDialog.findViewById(R.id.txtclose);
        suggestion = myDialog.findViewById(R.id.suggestion);

        //references
        pollutionVal = myDialog.findViewById(R.id.pollutionValue);
        imageReact = myDialog.findViewById(R.id.pollutionreact);

        pollutionVal.setText(pollutionValue);
        int value = Integer.parseInt(pollutionValue);

        if (value <= 50) {
            imageReact.setImageResource(R.drawable.pollution_great);
        } else if (value <= 100) {
            imageReact.setImageResource(R.drawable.pollution_ok);
        } else if (value <= 150) {
            imageReact.setImageResource(R.drawable.pollution_sensitive_beware);
        } else if (value <= 200) {
            imageReact.setImageResource(R.drawable.pollution_unhealthy);
        } else if (value <= 300) {
            imageReact.setImageResource(R.drawable.pollution_veryunhealthy);
        } else {
            imageReact.setImageResource(R.drawable.pollution_hazardous);
        }

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();

    }

    private void updateReactImage(int value) {

    }
}
