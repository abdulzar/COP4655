package com.example.hw8_5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherData {
    private String feelsLike;
    private String temp;
    private String cityName;
    private String tempMax;
    private String tempMin;
    private String lat;
    private String lon;
    private String description;
    private String humidity;
    private String country;
    private String pressure;
    private Date sunrise;
    private Date sunset;

    public WeatherData() {
    }

    ;

    public String getTemp() {
        return this.temp;
    }

    public void setTemp(String t) {
        this.temp = t;
    }

    public String getLat() {
        return this.lat;
    }

    public void setLat(String l) {
        this.lat = l;
    }

    public String getLon() {
        return this.lon;
    }

    public void setLon(String l) {
        this.lon = l;
    }

    public String getFeelsLike() {
        return this.feelsLike;
    }

    public void setFeelsLike(String fl) {
        this.feelsLike = fl;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String c) {
        this.cityName = c;
    }

    public String getTempMax() {
        return this.tempMax;
    }

    public void setTempMax(String t) {
        this.tempMax = t;
    }

    public String getTempMin() {
        return this.tempMin;
    }

    public void setTempMin(String t) {
        this.tempMin = t;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }


    public Date getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(String str) {
        try {
            setSunrise(new Date(Long.parseLong(str) * 1000));
        } catch (Exception unused) {
            try {
                setSunrise(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(str));
            } catch (ParseException e) {
                setSunrise(new Date());
                e.printStackTrace();
            }
        }
    }

    public void setSunrise(Date date2) {
        this.sunrise = date2;
    }

    public Date getSunset() {
        return this.sunset;
    }

    public void setSunset(String str) {
        try {
            setSunset(new Date(Long.parseLong(str) * 1000));
        } catch (Exception unused) {
            try {
                setSunrise(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(str));
            } catch (ParseException e) {
                setSunset(new Date());
                e.printStackTrace();
            }
        }
    }

    public void setSunset(Date date2) {
        this.sunset = date2;
    }
}
