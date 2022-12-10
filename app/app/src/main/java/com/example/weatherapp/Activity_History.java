package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Activity_History extends AppCompatActivity {
    private RecyclerView recyclerView1;
    private HAdapter hAdapter;
     List<ListH> list;
    private final FirebaseDatabase db1 = FirebaseDatabase.getInstance();
    private final DatabaseReference root_history = db1.getReference().child("History_Save_Data");
     String time,cityName;
    private final String myApi ="https://testing-6700e-default-rtdb.firebaseio.com/responseHistory.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView1 = findViewById(R.id.rcvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView1.addItemDecoration(itemDecoration);
        list = new ArrayList<>();
        hAdapter = new HAdapter(list,this);
        recyclerView1.setAdapter(hAdapter);
        CheckInternetConnection();
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("cityName");
        CallHistory(myApi,cityName);

    }
    public void CheckInternetConnection(){
        Intent intent = new Intent(Activity_History.this,Internet_check.class);
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

    public void CallHistory(String myApi, String name){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                list.clear();
                try {
                   JSONArray HArray = new JSONArray(response);
                   for (int i = 0 ; i<HArray.length();i++){
                       String time = HArray.getJSONObject(i).getJSONArray("list").getJSONObject(0).getString("dt");
                       long l = Long.valueOf(time);
                       Date date = new Date(l*1000L);
                       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                       time = simpleDateFormat.format(date);
                       list.add(new ListH(i+1+"",time));
                   }
                    hAdapter.notifyDataSetChanged();

                    HashMap<String, String> HistoryData = new HashMap<>();
                    for (int i = 0 ; i < HArray.length(); i++){
                        JSONObject indexObj = HArray.getJSONObject(i);
                        JSONArray list = indexObj.getJSONArray("list");
                        HistoryData.clear();
                        for ( int j = 0 ; j < list.length(); j ++){
                            JSONObject subList = list.getJSONObject(j);
                            time = subList.getString("dt");
                            long l11 = Long.valueOf(time);
                            Date date11 = new Date(l11 * 1000L);
                            SimpleDateFormat simpleDateFormat33 = new SimpleDateFormat("dd:MM:yyyy");
                            time = simpleDateFormat33.format(date11);

                            String feels_like = subList.getJSONObject("main").getString("feels_like");
                            String humidity = subList.getJSONObject("main").getString("humidity");
                            String temp = subList.getJSONObject("main").getString("temp");
                            String tempMax = subList.getJSONObject("main").getString("temp_max");
                            String tempMin = subList.getJSONObject("main").getString("temp_min");

                            String description = subList.getJSONArray("weather").getJSONObject(0).getString("description");
                            String wind_deg = subList.getJSONObject("wind").getString("deg");
                            String wind_speed = subList.getJSONObject("wind").getString("speed");
                            HistoryData.put("Cảm giác (°C)",feels_like);
                            HistoryData.put("Độ ẩm (%)",humidity);
                            HistoryData.put("Nhiệt độ (°C)",temp);
                            HistoryData.put("Nhiệt độ tối đa (°C)",tempMax);
                            HistoryData.put("Nhiệt độ tối thiểu (°C)",tempMin);
                            HistoryData.put("Mô tả",description);
                            HistoryData.put("Hướng gió",wind_deg);
                            HistoryData.put("Tốc độ gió",wind_speed);
                            Log.d("oasss", "onResponse: "+name);
                            root_history.child(name).child(time).child(j+"").setValue(HistoryData);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("TAG2", "Loi cmnr");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG2", "Loi cmnr");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hAdapter!=null){
            hAdapter.release();
        }

    }
}