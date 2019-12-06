package com.sh4dow.climate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Pollution extends AppCompatActivity {

    ImageView pollutionReact;
    TextView pollutionValue;
    boolean inPollutionState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pollution);
        String value = getIntent().getStringExtra("State");
        pollutionReact = findViewById(R.id.pollutionreact);
        pollutionValue = findViewById(R.id.pollutionValue);
        updateReactImage(Integer.parseInt(value));

        inPollutionState = true;

        pollutionValue.setText(value+" aqi");
    }

    public void checkWeather(View view) {
        Intent intent = new Intent(Pollution.this,WeatherController.class);
        intent.putExtra("Pollution",inPollutionState);
        startActivity(intent);
    }

    private void updateReactImage(int value){
        if(value<=50){
            pollutionReact.setImageResource(R.drawable.pollution_great);
        }else if(value<=100){
            pollutionReact.setImageResource(R.drawable.pollution_ok);
        }else if(value<=150){
            pollutionReact.setImageResource(R.drawable.pollution_sensitive_beware);
        }else if(value<=200){
            pollutionReact.setImageResource(R.drawable.pollution_unhealthy);
        }else if(value<=300){
            pollutionReact.setImageResource(R.drawable.pollution_veryunhealthy);
        }else{
            pollutionReact.setImageResource(R.drawable.pollution_hazardous);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        inPollutionState = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        inPollutionState = false;
    }


}
