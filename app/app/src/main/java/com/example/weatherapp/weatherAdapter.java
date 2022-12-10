package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class weatherAdapter extends RecyclerView.Adapter<weatherAdapter.WeatherViewHolder> {


    private List<weather> mWeathers;
    private Context context;


    public weatherAdapter(List<weather> mWeathers, Context context) {
        this.mWeathers = mWeathers;
        this.context = context;
    }

    public  void setData(List<weather> list){
        this.mWeathers = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhour,parent,false);
        return new WeatherViewHolder(view);
    }
    public void release(){
        context = null;
    }
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        weather weather = mWeathers.get(position);
        if (weather==null){
            return;
        }
//  cai nay cai cu dung       holder.imgWeather.setImageResource(weather.getResourceId());

//        Picasso.get().load("http://openweathermap.org/img/wn/"+weather.getResourceId()+"@2x.png").into(holder.imgWeather);
          Picasso.get().load("http://openweathermap.org/img/wn/" +weather.getResourceId() +"@2x.png").into(holder.imgWeather);
//        Sai thi sua Picasso
        holder.tvThoiGian.setText(weather.getThoigian()+":00");
        holder.tvNhietDo.setText(weather.getNhietDo() + "°C");
    }

    @Override
    public int getItemCount() {
        if (mWeathers != null){
            return mWeathers.size();
        }
        return 0;
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgWeather;
        private TextView tvThoiGian;
        private TextView tvNhietDo;
        private TextView tvLuongMua;
        private TextView tvDoAm;
        private TextView tvSunRise;
        private TextView tvSunSet;
        private TextView tvMay;
        private TextView tvTocGio;
        private TextView tvHuongGio;
        private TextView tvUV;
        private TextView tv_moTaWeek;




        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            imgWeather = itemView.findViewById(R.id.imageViewHoursIcon);
            tvThoiGian = itemView.findViewById(R.id.textviewHoursTime);
            tvNhietDo = itemView.findViewById(R.id.textviewHoursTemp);
            tvLuongMua = itemView.findViewById(R.id.tv_luongMua);
            tvDoAm = itemView.findViewById(R.id.tv_doAm);
            tvSunRise = itemView.findViewById(R.id.tv_SunRise);
            tvSunSet = itemView.findViewById(R.id.tv_sunSet);
            tvMay = itemView.findViewById(R.id.tv_may);
            tvTocGio = itemView.findViewById(R.id.tv_tocgio);
            tvHuongGio = itemView.findViewById(R.id.tv_huonggio);
            tvUV = itemView.findViewById(R.id.cs_uv);
            tv_moTaWeek = itemView.findViewById(R.id.tv_moTaWeek);
        }
    }
}
