package com.example.hw8_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hw8_5.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DisplayActivity extends AppCompatActivity {

    TextView city;
    TextView temp;
    TextView min;
    TextView max;
    TextView feels;
    TextView country, sunrise, sunset, pressure, humadity,location;
    Button goToMap,goToHistory;

    public static double KtoF(Double k) {
        return ((k - 273.15) * 9 / 5 + 32);
    }

    public double KtoC(Double k) {
        return (k - 273.15);

    }
    public static String formatCurrentDate(Date date) {
        String DATE_FORMAT = "hh:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        WeatherData w = com.example.hw8_5.MainActivity.getWeatherInstance();
        city = findViewById(R.id.cityView);
        temp = findViewById(R.id.tempView);
        min = findViewById(R.id.minView);
        feels = findViewById(R.id.feelsView);
        max = findViewById(R.id.maxView);
        sunrise = findViewById(R.id.sunriseView);
        sunset = findViewById(R.id.sunsetView);
        pressure = findViewById(R.id.pressureView);
        humadity = findViewById(R.id.humatidyView);
        country = findViewById(R.id.countryView);
        location = findViewById(R.id.locationView);
        goToMap=findViewById(R.id.goMap);
        goToHistory=findViewById(R.id.goHistory);

        city.setText("Weather for " + w.getCityName());
        temp.setText("Temperature : " + String.valueOf(Math.floor(KtoC(Double.parseDouble(w.getTemp())))) + "°C");
        min.setText("Low : " + String.valueOf(Math.floor(KtoC(Double.parseDouble(w.getTempMin())))) + "°C");
        max.setText("Maximum : " + String.valueOf(Math.floor(KtoC(Double.parseDouble(w.getTempMax())))) + "°C");
        feels.setText("Feels like : " + String.valueOf(Math.floor(KtoC(Double.parseDouble(w.getFeelsLike())))) + "°C");
        sunrise.setText("Sunrise: " + formatCurrentDate(w.getSunrise()));
        sunset.setText("Sunset: " + formatCurrentDate(w.getSunset()));
        pressure.setText("Pressure: " + w.getPressure());
        humadity.setText("Humidity: " + w.getHumidity());
        country.setText("CountryCode: " + w.getCountry());
        location.setText("Lat: " + w.getLat() +" Lon: "+w.getLon());

        goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.hw8_5.DisplayActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        goToHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.hw8_5.DisplayActivity.this, com.example.hw8_5.HistoryActivity.class);
                startActivity(intent);
            }
        });


    }
}