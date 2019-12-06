package com.sh4dow.climate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangeCityController extends AppCompatActivity {

    EditText state,city,country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        state = findViewById(R.id.stateET);
        city = findViewById(R.id.cityET);
        country = findViewById(R.id.countryET);

        ImageButton backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        state.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                String newCity = state.getText().toString();
//                Intent newCityIntent = new Intent(ChangeCityController.this, WeatherController.class);
//                newCityIntent.putExtra("City",newCity);
//                newCityIntent.putExtra("State",)
//                startActivity(newCityIntent);
//                return false;
//            }
//        });
    }


    public void checkWeather(View view) {
        String newCity = city.getText().toString();
        String newState = state.getText().toString();
        String newCountry = country.getText().toString();
        Intent newCityIntent = new Intent(ChangeCityController.this, WeatherController.class);
        newCityIntent.putExtra("City",newCity);
        newCityIntent.putExtra("State",newState);
        newCityIntent.putExtra("Country",newCountry);

        startActivity(newCityIntent);
    }
}
