package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    ArrayList<WeatherWeek> weekArrayList;
    WeatherWeekAdapter weekAdapter;
    RecyclerView RVActivity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        String myApi = intent.getStringExtra("myApi");

        RVActivity2 = findViewById(R.id.RVActivity2);
        RVActivity2.setLayoutManager(new LinearLayoutManager(this));
        weekArrayList = new ArrayList<>();
        weekAdapter = new WeatherWeekAdapter(weekArrayList,MainActivity2.this);
        RVActivity2.setAdapter(weekAdapter);
        CheckInternetConnection();
        dataCity7Days(myApi);
    }
    public void CheckInternetConnection(){
        Intent intent = new Intent(MainActivity2.this,Internet_check.class);
        if (!isNetworkAvailable()){
            startActivity(intent);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void dataCity7Days(String myApi){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG2", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray list = jsonObject.getJSONObject("responseOneCall").getJSONObject("thoitiet").getJSONArray("daily");
                    for (int i = 0 ; i<list.length();i++){
                        JSONObject OneDay = list.getJSONObject(i);
                        JSONObject temp = OneDay.getJSONObject("temp");
                        String tempMax = temp.getString("max");
                        tempMax = cut(tempMax);

                        String tempMin = temp.getString("min");
                        tempMin = cut(tempMin);


                        JSONArray weather = OneDay.getJSONArray("weather");
                        String icon = weather.getJSONObject(0).getString("icon");
                        String moTa = weather.getJSONObject(0).getString("description");
                        String fMota = moTa.substring(0,1).toUpperCase();
                        moTa = fMota + moTa.substring(1);


                        String time = OneDay.getString("dt");
                        long l = Long.valueOf(time);
                        Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        time = simpleDateFormat.format(date);
                        if (i==0){
                            time = "Hôm nay, " + time;
                        }
                        if (i==1){
                            time = "Ngày mai, " + time;
                        }
                        String may = OneDay.getString("clouds");
                        String luongMua = null;
                        if (OneDay.has("rain")){
                            luongMua = OneDay.getString("rain") +"mm";
                        }else{
                            luongMua = "Không";
                        }
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
                        String doAm = OneDay.getString("humidity");
                        String sunRise = OneDay.getString("sunrise");
                        String sunSet = OneDay.getString("sunset");
                        Log.d("kq", "onResponse: "+may+luongMua+doAm+sunRise+sunSet);
                        long l1 = Long.valueOf(sunRise);
                        long l2 = Long.valueOf(sunSet);
                        date = new Date(l1*1000L);
                        sunRise = simpleDateFormat1.format(date);
                        date = new Date(l2*1000L);
                        sunSet = simpleDateFormat1.format(date);
                        String tocgio = OneDay.getString("wind_speed");
                        String huonggio = OneDay.getString("wind_deg");
                        huonggio = wind_deg(huonggio);
                        String uv = OneDay.getString("uvi");
//                        weekArrayList.add(new WeatherWeek(time,icon,moTa,tempMax,tempMin));
                        weekArrayList.add(new WeatherWeek(time,icon,moTa,tempMax,tempMin,luongMua,doAm,sunRise,sunSet,may,tocgio,huonggio,uv));
                    }
                    weekAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public String cut (String tempCurrent){
        if (tempCurrent.length()>3){
            if( tempCurrent.charAt(0)=='-' && tempCurrent.charAt(1)=='0'){
                tempCurrent = tempCurrent.substring(1,2);
            }
            else{
                tempCurrent = tempCurrent.substring(0,tempCurrent.lastIndexOf("."));
            }
        }
        return  tempCurrent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weekAdapter!=null){
            weekAdapter.release();
        }

    }
    public String wind_deg(String deg) {
        int i = Integer.parseInt(deg);
        String result = "";
        switch (i) {
            case 0:
                result = "Bắc";
                break;
            case 90:
                result = "Đông";
                break;
            case 180:
                result = "Nam";
                break;
            case 270:
                result = "Tây";
                break;
        }
        if (i > 0 && i < 90) {
            result = "Đông Bắc";
        }
        if (i > 90 && i < 180) {
            result = "Đông Nam";
        }
        if (i > 180 && i < 270) {
            result = "Tây Nam";
        }
        if (i > 270 && i < 360) {
            result = "Tây Bắc";
        }
        return result;
    }
}