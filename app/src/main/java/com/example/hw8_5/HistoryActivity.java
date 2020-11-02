package com.example.hw8_5;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hw8_5.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    public ListView simpleList;
    public List<HistoryData> historyData;
    public ProgressBar progressBar;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        queue = Volley.newRequestQueue(this);
        historyData=new ArrayList<>();
        progressBar=findViewById(R.id.progressBar);
        simpleList = (ListView) findViewById(R.id.simpleListView);
        //getHistoryByLocation
        WeatherData w= com.example.hw8_5.MainActivity.getWeatherInstance();
        setTitle("History of "+w.getCityName() );
        getHistoryByLocation(w.getLat(),w.getLon());

    }
    public void getHistoryByLocation(String lat, String lon) {
        String url = getString(R.string.WEATHER_API_URL_HISTORY) + lat +
                getString(R.string.WEATHER_LON_SUFFIX) + lon
                + "&exclude=weekly" +getString(R.string.WEATHER_API_KEY);

        //String url2="https://api.openweathermap.org/data/2.5/onecall?lat=26.3683&lon=-80.1289&exclude=weekly&appid=be9a8abe28b1d899e1ceab1c1049fcd7";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                        JSONArray weatherArray = response.getJSONArray("daily");
                        for (int i = 0; i < weatherArray.length(); i++) {
                            JSONObject dayHistory = weatherArray.getJSONObject(i);
                            HistoryData data=new HistoryData();
                            data.setdayDate(dayHistory.getString("dt"));
                            data.setSunrise(dayHistory.getString("sunrise"));
                            data.setSunset(dayHistory.getString("sunset"));
                            data.setPressure(dayHistory.getString("pressure"));
                            data.setHumidity(dayHistory.getString("humidity"));
                            //temp object
                            JSONObject tempObjt = dayHistory.getJSONObject("temp");
                            data.setTemp(tempObjt.getString("day"));
                            historyData.add(data);
                            }
                        }
                        catch (JSONException e) {
                            System.out.println("JSON EXPLOSION:" +e.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        com.example.hw8_5.CustomAdapter customAdapter = new com.example.hw8_5.CustomAdapter(getApplicationContext(), historyData);
                        simpleList.setAdapter(customAdapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR WITH VOLLEY REQUEST");

                    }
                });

        queue.add(jsonObjectRequest);
    }
}