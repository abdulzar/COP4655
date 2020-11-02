package com.example.hw8_5;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

//implementing the OnSuccessListener in this activity since there is only a single callback.
public class MainActivity extends AppCompatActivity implements OnSuccessListener<Location> {

    //Bundle key
    public static final String DATA_TAG = "weather.data.go";
    //LOCATION REQUEST CODE KEY, not important since only asking for a single permission and don't need to distinguish
    public static final int LOCATION_REQUEST_CODE = 11;
    public static WeatherData data = new WeatherData();
    //Used for getting location
    private FusedLocationProviderClient fusedLocationClient;
    //Declare UI elements
    private Button goButton;
    private ImageButton locButton;
    private TextView idTextView;
    private EditText locEditText;
    private Context context;
    //Volley request queue;
    private RequestQueue queue;

    public static WeatherData getWeatherInstance() {
        return data;
    }

    public static String getRainString1(JSONObject jSONObject) {
        String str = "0";
        if (jSONObject == null) {
            return str;
        }
        String strFail = "fail";
        String optString = jSONObject.optString("3h", strFail);
        return strFail.equals(optString) ? jSONObject.optString("1h", str) : optString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Remove that uggo TitleBar
        getSupportActionBar().hide();

        //Set context
        context = getApplicationContext();
        checkLocationPermission();


        //Attach references to UI elements
        goButton = findViewById(R.id.goButton);
        locButton = findViewById(R.id.locButton);
        idTextView = findViewById(R.id.textView2);
        locEditText = findViewById(R.id.locEditText);

        //Instantiate the request queue
        queue = Volley.newRequestQueue(this);
        getLoactionNow();


    }

    public void onLocClick(View v) {
        System.out.println("Location Button Clicked");
        getLoactionNow();
        getWeatherByLocation(String.valueOf(latitude), String.valueOf(longitude));

    }


    public void onGoClick(View v) {
        System.out.println("Go Button Clicked");
        String text = locEditText.getText().toString();
        int zipcode;
        try {
            System.out.println("ZIPCODE DETECTED!");
            zipcode = Integer.parseInt(text);
            getWeatherByZip(zipcode);
        } catch (Exception e) {
            //Opps, try by city
            System.out.println("CITY DETECTED!");
            getWeatherByCity(text);
        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        System.out.println("onRequestPermissionsResult Callback Entered");

        //Check that permission was granted
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }


    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, this);


    }

    @Override
    //onSuccess for Location Services
    public void onSuccess(Location location) {
        //Get Weather by Location
        if(location!=null) {
            String lat = String.valueOf(location.getLatitude());
            String lon = String.valueOf(location.getLongitude());
            System.out.println("Lattitude = " + lat);
            System.out.println("Longitude = " + lon);
            data.setLat(lat);
            data.setLon(lon);
            getWeatherByLocation(lat, lon);
        }

    }

    public void getWeatherByCity(String city) {
        String url = getString(R.string.WEATHER_API_URL_CITY) + city + getString(R.string.WEATHER_API_KEY);
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        /*JSONObject main = null;
                        try {
                            main = response.getJSONObject("main");
                            JSONObject coords = response.getJSONObject("coord");
                            data.setLat(coords.getString("lat"));
                            data.setLon(coords.getString("lon"));
                            data.setTemp(main.getString("temp"));
                            data.setFeelsLike(main.getString("feels_like"));
                            data.setCityName(response.getString("name"));
                            data.setTempMax(main.getString("temp_max"));
                            data.setTempMin(main.getString("temp_min"));
                            //TODO : Bundle the weather object and send to next activity
                            //Current implementation is just using static member

                            Intent intent = new Intent(context, DisplayActivity.class);
                            startActivity(intent);


                        } catch (JSONException e) {
                            System.out.println("JSON EXPLOSION");
                        }*/
                        parseJsonToModel(response);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR WITH VOLLEY REQUEST");

                    }
                });

        queue.add(jsonObjectRequest);

    }

    public void getWeatherByZip(int zip) {
        String zipcode = String.valueOf(zip);
        String url = getString(R.string.WEATHER_API_URL_ZIP) + zipcode + getString(R.string.WEATHER_API_KEY);
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        /*JSONObject main = null;
                        try {
                            main = response.getJSONObject("main");
                            JSONObject coords = response.getJSONObject("coord");
                            data.setLat(coords.getString("lat"));
                            data.setLon(coords.getString("lon"));
                            data.setTemp(main.getString("temp"));
                            data.setFeelsLike(main.getString("feels_like"));
                            data.setCityName(response.getString("name"));
                            data.setTempMax(main.getString("temp_max"));
                            data.setTempMin(main.getString("temp_min"));
                            //TODO : Bundle the weather object and send to next activity
                            //Current implementation is just using static member

                            Intent intent = new Intent(context, DisplayActivity.class);
                            startActivity(intent);


                        } catch (JSONException e) {
                            System.out.println("JSON EXPLOSION");
                        }*/
                        parseJsonToModel(response);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR WITH VOLLEY REQUEST");

                    }
                });

        queue.add(jsonObjectRequest);


    }

    public void getWeatherByLocation(String lat, String lon) {
        String url = getString(R.string.WEATHER_API_URL_LAT) + lat + getString(R.string.WEATHER_LON_SUFFIX) + lon
                + getString(R.string.WEATHER_API_KEY);

        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonResponse) {

                        try {
                            if ("404".equals(jsonResponse.optString("cod"))) {

                                System.out.println("CITY_NOT_FOUND");
                            }

                            JSONObject jsonMain = jsonResponse.getJSONObject("main");
                            data.setTemp(jsonMain.getString("temp"));
                            data.setFeelsLike(jsonMain.getString("feels_like"));
                            data.setTempMax(jsonMain.getString("temp_max"));
                            data.setTempMin(jsonMain.getString("temp_min"));
                            data.setPressure(jsonMain.getString("pressure"));
                            data.setHumidity(jsonMain.getString("humidity"));

                            //JSONObject coords = jsonResponse.getJSONObject("coord");
                            data.setLat(lat);
                            data.setLon(lon);

                            data.setCityName(jsonResponse.getString("name"));
                            data.setDescription(jsonResponse.optJSONArray("weather").getJSONObject(0).getString("description"));

                            JSONObject optJSONObject = jsonResponse.optJSONObject("sys");
                            data.setSunrise(optJSONObject.getString("sunrise"));
                            data.setSunset(optJSONObject.getString("sunset"));
                            data.setCountry(optJSONObject.getString("country"));

                            String weatherTime = jsonResponse.getString("dt") + "000";
                            Calendar instance = Calendar.getInstance();
                            instance.setTimeInMillis(Long.parseLong(weatherTime));
                            // goToNextActivity
                            Intent intent = new Intent(context, DisplayActivity.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            Log.e("JSONException Data", jsonResponse.toString());
                            e.printStackTrace();

                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR WITH VOLLEY REQUEST");

                    }
                });

        queue.add(jsonObjectRequest);
    }

    public String parseJsonToModel(JSONObject jsonResponse) {

        try {
            if ("404".equals(jsonResponse.optString("cod"))) {

                return "CITY_NOT_FOUND";
            }

            JSONObject jsonMain = jsonResponse.getJSONObject("main");
            data.setTemp(jsonMain.getString("temp"));
            data.setFeelsLike(jsonMain.getString("feels_like"));
            data.setTempMax(jsonMain.getString("temp_max"));
            data.setTempMin(jsonMain.getString("temp_min"));
            data.setPressure(jsonMain.getString("pressure"));
            data.setHumidity(jsonMain.getString("humidity"));

            JSONObject coords = jsonResponse.getJSONObject("coord");
            data.setLat(coords.getString("lat"));
            data.setLon(coords.getString("lon"));

            data.setCityName(jsonResponse.getString("name"));
            data.setDescription(jsonResponse.optJSONArray("weather").getJSONObject(0).getString("description"));

            JSONObject optJSONObject = jsonResponse.optJSONObject("sys");
            data.setSunrise(optJSONObject.getString("sunrise"));
            data.setSunset(optJSONObject.getString("sunset"));
            data.setCountry(optJSONObject.getString("country"));

            String weatherTime = jsonResponse.getString("dt") + "000";
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(Long.parseLong(weatherTime));
            // goToNextActivity
            Intent intent = new Intent(context, DisplayActivity.class);
            startActivity(intent);

            return "OK";
        } catch (JSONException e) {
            Log.e("JSONException Data", jsonResponse.toString());
            e.printStackTrace();
            return "JSON_EXCEPTION";
        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    //Location Getting Code
    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public void getLoactionNow() {
        OnGPS();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }


        //You can still do this if you like, you might get lucky:
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d("TAG", "GPS is on");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

        } else {
            //This is what you need:
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        Toast.makeText(this,"Latitude : " + latitude + "Longitude : " + longitude,Toast.LENGTH_SHORT);

    }
    private void OnGPS() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setMessage("Please Enable GPS !").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
}