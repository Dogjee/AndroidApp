package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView textViewCityName, textViewTemperature, textViewTempMax, textViewTempMin, textViewStatus, current,textViewValueKhaNangMua,textViewValueMua,
            textViewFeelLike, textViewUV, textViewSunRise, textViewSunSet, textViewWindSpeed, textViewWindDeg, textViewValueDoam,textViewChatLuongKhongKhi;
    EditText editTextSearch;
    Button ButtonChangeActivity, ButtonHistory;
    ConstraintLayout manHinh;
    Boolean isRunning  = false;
    ProgressBar progressBarDoam;
    RecyclerView RVWeather;
    weatherAdapter adapter;
    ArrayList<weather> arrayList;
    ImageView imageViewTest,imageViewLocation;
    ProgressBar progressBar;
    ConstraintLayout visible;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference root_current = db.getReference().child("Current_Save_Data");
    private final DatabaseReference requestCity = db.getReference().child("requestCity");
    private  final DatabaseReference userLocation = db.getReference().child("UserLocation");
    CountDownTimer TimerLoading;
    private String city;
    static String kinhDo, viDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBarDoam = findViewById(R.id.progressBarDoAm);
        textViewValueDoam = findViewById(R.id.ValueDoAm);
        progressBar = findViewById(R.id.progressBar);
        textViewValueKhaNangMua = findViewById(R.id.ValueKhaNangMua);
        textViewValueMua = findViewById(R.id.ValueLuongMua);
        ButtonHistory = findViewById(R.id.ButtonHistory);
        visible = findViewById(R.id.visible);
        textViewCityName = findViewById(R.id.textViewCityName);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        current = findViewById(R.id.current);
        imageViewTest = findViewById(R.id.ImageViewTest);
        imageViewLocation = findViewById(R.id.imageViewLocation);
        textViewTempMax = findViewById(R.id.textViewTempMax);
        textViewTempMin = findViewById(R.id.textViewTempMin);
        textViewStatus = findViewById(R.id.textViewStatus);
        editTextSearch = findViewById(R.id.editTextNameSearch);
        textViewFeelLike = findViewById(R.id.ValueCamGiac);
        textViewUV = findViewById(R.id.ValueTuNgoai);
        textViewSunRise = findViewById(R.id.ValueMatTroiMoc);
        textViewSunSet = findViewById(R.id.ValueMatTroiLan);
        textViewWindSpeed = findViewById(R.id.ValueTocDoGio);
        textViewWindDeg = findViewById(R.id.ValueHuongGio);
        textViewChatLuongKhongKhi = findViewById(R.id.kkIndex);

        manHinh = findViewById(R.id.ManHinh1);
        ButtonChangeActivity = findViewById(R.id.ButtonChangeActivity);

        arrayList = new ArrayList<>();
        RVWeather = findViewById(R.id.RVWeather);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        RVWeather.setLayoutManager(horizontalLayoutManager);
        adapter = new weatherAdapter(arrayList, MainActivity.this);
        RVWeather.setAdapter(adapter);
        Picasso.get().setLoggingEnabled(true);
        Loading2();
        CheckInternetConnection();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });


        imageViewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0); //danger
                search(view);
            }
        });


        ButtonChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("myApi", "https://testing-6700e-default-rtdb.firebaseio.com/.json");
                startActivity(intent);
            }
        });

        ButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,Activity_History.class);
                intent2.putExtra("cityName",textViewCityName.getText().toString());
                startActivity(intent2);
            }
        });
        call();
        String a = kinhDo;
        String b = viDo;
        Log.d("TAG22", "onCreate: "+a+b);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();

            }
        }
    }

    public void search(View view) {
        city = editTextSearch.getText().toString().trim();
        if (city.equals("")) {
            Toast.makeText(MainActivity.this, "Tên thành phố không rỗng", Toast.LENGTH_SHORT).show();
            return;
        }
        requestCity.child("Name").setValue(city);
        editTextSearch.setText(null);
        CheckInternetConnection();
        Loading();
    }

    private void call() {
        CheckInternetConnection();
        String url = "https://testing-6700e-default-rtdb.firebaseio.com/.json";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObjectTT = new JSONObject(response);
                    if (jsonObjectTT.getString("error").trim().equals("city not found")){
                        Toast.makeText(MainActivity.this, "Không tìm thấy thành phố bạn yêu cầu!", Toast.LENGTH_SHORT).show();
                        requestCity.child("Name").setValue("Can Tho");
                        return;
                    }
                    JSONObject jsonObjectOnecall = jsonObjectTT.getJSONObject("responseOneCall").getJSONObject("thoitiet");
                    JSONObject Current = jsonObjectOnecall.getJSONObject("current");
                    JSONArray Daily = jsonObjectOnecall.getJSONArray("daily");
//                    JSONObject jsonKhongKhi = jsonObjectTT.getJSONObject("khongkhi");
                    JSONArray ArrayKhongKhi = jsonObjectTT.getJSONObject("responseOneCall").getJSONObject("khongkhi").getJSONArray("list");

                    String chatluongkk = ArrayKhongKhi.getJSONObject(0).getJSONObject("main").getString("aqi");
//                    chatluongkk = chatluongkhongkhi(chatluongkk);
                    String windSpeed = Current.getString("wind_speed");

                    String uvi = Current.getString("uvi");

                    String cityName = jsonObjectTT.getString("responseName");

                    String status = Current.getJSONArray("weather").getJSONObject(0).getString("description");

                    String currentTime = Current.getString("dt");

                    long l = Long.valueOf(currentTime);
                    Date date = new Date(l * 1000L);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat simpleDateFormatFirebase = new SimpleDateFormat("dd:MM");
                    currentTime = simpleDateFormat.format(date);
                    current.setText("Cập nhật lần cuối lúc: " + currentTime);
                    String currentTimeFireBase = simpleDateFormatFirebase.format(date) + ":" + currentTime;


//                  Current thay doi sang phut

                    kinhDo = jsonObjectOnecall.getString("lon");
                    viDo = jsonObjectOnecall.getString("lat");
                    Log.d("TAG", kinhDo);
                    Log.d("TAG", viDo);
                    String firstLetter = status.substring(0, 1);
                    String remainingLetters = status.substring(1);
                    firstLetter = firstLetter.toUpperCase();
                    status = firstLetter + remainingLetters;


                    String tempCurrent = Current.getString("temp");
                    tempCurrent = cut(tempCurrent);
//                    Save data to firebase database

                    String TempMin = Daily.getJSONObject(0).getJSONObject("temp").getString("min");
                    String TempMax = Daily.getJSONObject(0).getJSONObject("temp").getString("max");
                    TempMin = "/" + cut(TempMin) + "°";
                    TempMax = cut(TempMax) + "°";
                    String feelLike = Current.getString("feels_like");
                    feelLike = cut(feelLike) + "°";
                    String sunRise = Current.getString("sunrise");
                    long lsunrise = Long.valueOf(sunRise);
                    Date datesunrise = new Date(lsunrise * 1000L);
                    sunRise = simpleDateFormat.format(datesunrise);
                    String sunSet = Current.getString("sunset");
                    long lsunset = Long.valueOf(sunSet);
                    Date datesunset = new Date(lsunset * 1000L);
                    sunSet = simpleDateFormat.format(datesunset);

                    String windDeg = Current.getString("wind_deg");
                    windDeg = wind_deg(windDeg);

                    textViewChatLuongKhongKhi.setText("Chỉ số chất lượng không khí: "+chatluongkk +" ("+chatluongkhongkhi(chatluongkk)+")");
                    textViewTempMax.setText(TempMax);
                    textViewTempMin.setText(TempMin);
                    textViewFeelLike.setText(feelLike);
                    textViewUV.setText(uvi);
                    textViewSunRise.setText(sunRise);
                    textViewSunSet.setText(sunSet);
                    textViewWindSpeed.setText(windSpeed);
                    textViewWindDeg.setText(windDeg);
                    textViewCityName.setText(cityName);
                    textViewTemperature.setText(tempCurrent + "°C");
                    textViewStatus.setText(status);
                    String doAm = Current.getString("humidity");
                    int doAmInt = Integer.parseInt(doAm);
                    progressBarDoam.setProgress(doAmInt);
                    doAm = doAm + "%";
                    textViewValueDoam.setText(doAm);

                    arrayList.clear();
                    for (int i = 0; i < jsonObjectTT.getJSONObject("responseOneCall").getJSONObject("thoitiet").getJSONArray("hourly").length(); i++) {
                        JSONObject objectHours = jsonObjectTT.getJSONObject("responseOneCall").getJSONObject("thoitiet").getJSONArray("hourly").getJSONObject(i);
                        String timeHours = objectHours.getString("dt");
                        long lHours = Long.valueOf(timeHours);
                        Date dateHours = new Date(lHours * 1000L);
                        SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("HH");
                        timeHours = simpleDateFormatHours.format(dateHours);

                        String img = objectHours.getJSONArray("weather").getJSONObject(0).getString("icon");

                        String temperatureHours = objectHours.getString("temp");
                        temperatureHours = cut(temperatureHours);

                        arrayList.add(new weather(timeHours, img, temperatureHours));
                    }
                    adapter.notifyDataSetChanged();

                    HashMap<String, String> currentData = new HashMap<>();
                    currentData.put("Nhiệt Độ (°C)", tempCurrent);
                    currentData.put("Trạng thái", status);
                    currentData.put("Tốc độ gió", windSpeed);
                    currentData.put("Chỉ số UV", uvi);
                    currentData.put("Độ ẩm(%)", Current.getString("humidity"));
                    currentData.put("Mức độ mây", Current.getString("clouds"));
                    currentData.put("Chất lượng không khí","Chỉ số chất lượng không khí: "+chatluongkk +" ("+chatluongkhongkhi(chatluongkk)+")");


                    root_current.child(cityName).child("Kinh Độ").setValue(kinhDo);
                    root_current.child(cityName).child("Vĩ Độ").setValue(viDo);
                    root_current.child(cityName).child("Thời gian: " +currentTimeFireBase).setValue(currentData);


                    if (Current.has("rain")){
                        Log.d("mua", "onResponse: 2");
                        String rain = Current.getJSONObject("rain").getString("1h");
                            float rainFl = Float.parseFloat(rain);
                            if (rainFl > 0){
                                textViewValueKhaNangMua.setText("Có");
                                textViewValueMua.setText(rainFl+" mm");
                            }
                    }
                    else{
                        Log.d("mua", "onResponse: 1");
                        textViewValueKhaNangMua.setText("Không");
                        textViewValueMua.setText("Chưa có dữ liệu");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("heheboiz", "onResponse: "+"loi");
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

    public void Loading() {
        progressBar.setVisibility(View.VISIBLE);
        visible.setVisibility(View.INVISIBLE);
        if (isRunning){
            TimerLoading.cancel();
        }
        Log.d("TAG22", isRunning.toString());
        CountDownTimer countDownTimer = new CountDownTimer(4000, 500) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                call();
                progressBar.setVisibility(View.GONE);
                visible.setVisibility(View.VISIBLE);
                startTimer(ButtonHistory);
            }
        }.start();
    }
    public void Loading2() {
        progressBar.setVisibility(View.VISIBLE);
        visible.setVisibility(View.INVISIBLE);
        CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                call();
                progressBar.setVisibility(View.GONE);
                visible.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    public String cut(String tempCurrent) {
        if (tempCurrent.length() > 3) {
            if (tempCurrent.charAt(0) == '-' && tempCurrent.charAt(1) == '0') {
                tempCurrent = tempCurrent.substring(1, 2);
            } else {
                tempCurrent = tempCurrent.substring(0, tempCurrent.lastIndexOf("."));
            }
        }
        return tempCurrent;
    }
    public void CheckInternetConnection(){
        Intent intent = new Intent(MainActivity.this,Internet_check.class);
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

    public void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            requestCity.child("Name").setValue(addresses.get(0).getAdminArea());
                            Log.d("TAG22", "onComplete: "+addresses.get(0).getAdminArea());

                            userLocation.setValue(addresses.get(0).getAdminArea());
                            Loading();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.release();
        }
    }

    private  void startTimer(Button abc){
        abc.setEnabled(false);
        abc.setTextColor(getResources().getColor(R.color.light_white));
        TimerLoading = new CountDownTimer(60000,1000) {
            int time = 60;
            @Override
            public void onTick(long l) {
                time = time - 1;
                abc.setText("Xem lịch sử thời tiết "+"(" + time +"s)");
                isRunning = true;
            }
            @Override
            public void onFinish() {
                abc.setEnabled(true);
                abc.setText("Xem lịch sử thời tiết");
                abc.setTextColor(getResources().getColor(R.color.white));
                isRunning = false;
            }
        }.start();
    }

    private String chatluongkhongkhi(String a){
        int index = Integer.parseInt(a);
        if (index == 1){
            return "Trong lành";
        }
        else if (index==2){
            return "Tốt";
        }
        else if (index==3){
            return "Trung bình";
        }
        else if (index==4){
            return "Tệ";
        }
        else{
            return "Rất tệ";
        }
    }

}

