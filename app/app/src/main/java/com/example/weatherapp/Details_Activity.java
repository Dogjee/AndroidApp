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
import android.widget.TextView;

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
import java.util.List;

public class Details_Activity extends AppCompatActivity {
    RecyclerView rcvDetails;
    List<History_hour> history_hours;
    String imageResource, TimeCall;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();

    Integer index;
    Adaper_History_hour adaper_history_hour;
    private final String myApi = "https://testing-6700e-default-rtdb.firebaseio.com/responseHistory.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        CheckInternetConnection();
        Bundle bundle = getIntent().getExtras();
        rcvDetails = findViewById(R.id.rcvHDate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDetails.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rcvDetails.addItemDecoration(itemDecoration);
        history_hours = new ArrayList<>();
        adaper_history_hour = new Adaper_History_hour(history_hours);
        rcvDetails.setAdapter(adaper_history_hour);
        if (bundle == null){
            return;
        }
        ListH listH = (ListH) bundle.get("object");
        TextView textView = findViewById(R.id.detail_day);
        textView.setText("Trong ng??y "+listH.getNgay());
        imageResource = listH.getImgSource();
        index = Integer.parseInt(imageResource)-1;
        CallHistory(myApi,index);
    }
    public void CallHistory(String myApi, int index){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myApi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int vip = 0;
                Log.d("Response Details", response);
                history_hours.clear();
                try {
                    JSONArray HArray = new JSONArray(response);
                    JSONArray list = HArray.getJSONObject(index).getJSONArray("list");
                    if (index != 0){
                        vip = list.length()-1;
                    }else{
                        vip = list.length();
                    }
                    for (int i = 0 ; i < vip ; i++){
                        String time = list.getJSONObject(i).getString("dt");
                        long l = Long.valueOf(time);
                        Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM HH:mm");
                        time = simpleDateFormat.format(date);
                        JSONObject main = list.getJSONObject(i).getJSONObject("main");
                        String humidity = main.getString("humidity");
                        String tempMax = main.getString("temp_max");
//                        String tempMin = main.getString("temp_min");

                        String icon = list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                        String description = list.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                        description = translate(description);
                        String wind = list.getJSONObject(i).getJSONObject("wind").getString("speed");
                        String windDeg = list.getJSONObject(i).getJSONObject("wind").getString("deg");
                        windDeg = wind_deg(windDeg);
                        tempMax = cut(tempMax);
//                        tempMin = cut(tempMin);
                        history_hours.add(new History_hour(icon,time,tempMax+"??","Gi??: "+wind+" ("+windDeg+")","????? ???m:"+humidity+"%",description));

                    }
                    adaper_history_hour.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Details", "Loi");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG2", "Loi");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public String wind_deg(String deg) {
        int i = Integer.parseInt(deg);
        String result = "";
        switch (i) {
            case 0:
                result = "B???c";
                break;
            case 90:
                result = "????ng";
                break;
            case 180:
                result = "Nam";
                break;
            case 270:
                result = "T??y";
                break;
        }
        if (i > 0 && i < 90) {
            result = "????ng B???c";
        }
        if (i > 90 && i < 180) {
            result = "????ng Nam";
        }
        if (i > 180 && i < 270) {
            result = "T??y Nam";
        }
        if (i > 270 && i < 360) {
            result = "T??y B???c";
        }
        return result;
    }
    public String translate(String a){
        String vn =a;
        switch (a){
            case "clear sky":
                vn = "B???u tr???i quang ????ng";
                break;
            case "few clouds":
                vn = "Tr???i ??t m??y";
                break;
            case "scattered clouds":
                vn = "M??y r???i r??c";
                break;
            case "broken clouds":
                vn = "M??y th??a";
                break;
            case "shower rain":
                vn = "M??a l??m r??m";
                break;
            case "rain":
                vn = "C?? m??a";
                break;
            case "thunderstorm":
                vn = "C?? s???m ch???p";
                break;
            case "snow":
                vn = "C?? tuy???t r??i";
                break;
            case "mist":
                vn = "C?? s????ng m??";
                break;
            case "light rain":
                vn = "M??a nh???";
                break;
            case "moderate rain":
                vn = "M??a v???a";
                break;
            case "overcast clouds":
                vn = "M??y u ??m";
                break;
            case "fog":
                vn = "S????ng m??";
                break;
            case "haze":
                vn = "M??a ph??n";
                break;
            case "light snow":
                vn = "Tuy???t r??i nh???";
                break;
            case "tornado":
                vn = "L???c xo??y";
                break;
            case "squalls":
                vn = "M??a ????";
                break;
            case "volcanic ash":
                vn = "Tro b???i n??i l???a";
                break;
            case "dust":
                vn = "Kh??i b???i";
                break;
            case "sand":
                vn = "C??t";
                break;

        }
        return vn;
    }
    public String cut(String tempCurrent) {
        if (tempCurrent.length() >= 3) {
            if (tempCurrent.charAt(0) == '-' && tempCurrent.charAt(1) == '0') {
                tempCurrent = tempCurrent.substring(1, 2);
            } else {
                tempCurrent = tempCurrent.substring(0, tempCurrent.lastIndexOf("."));
            }
        }
        return tempCurrent;
    }
    public void CheckInternetConnection(){
        Intent intent = new Intent(Details_Activity.this,Internet_check.class);
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
}