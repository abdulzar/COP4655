package com.example.hw8_5;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryData {
    private String temp;
    private String pressure;
    private String humidity;
    private Date sunrise;
    private Date sunset;
    private Date dayDate;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
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

    public Date getdayDate() {
        return this.sunset;
    }

    public void setdayDate(String str) {
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

    public void setdayDate(Date date2) {
        this.sunset = date2;
    }
}
