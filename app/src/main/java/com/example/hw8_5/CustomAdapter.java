package com.example.hw8_5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hw8_5.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<HistoryData> historyList;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<HistoryData> historyData) {
        this.context = context;
        this.historyList = historyData;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        TextView temp = (TextView) view.findViewById(R.id.txtView);
        String dataString="Date: "+historyList.get(i).getdayDate()+"\nTemp: "
                +String.valueOf(Math.floor(KtoC(Double.parseDouble(historyList.get(i).getTemp())))) + "°C" +
                "\t Humidity: "+historyList.get(i).getHumidity()+"\t Pressure: "+historyList.get(i).getPressure()+"\n" +
                "Sunrise: "+formatCurrentDate(historyList.get(i).getSunrise())+
                "\t Sunset: "+formatCurrentDate(historyList.get(i).getSunset());
        temp.setText(dataString);
        return view;
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
}
