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

import java.util.ArrayList;
import java.util.List;

public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.WeatherWeekAdapterHolder> {
    private List<WeatherWeek> list;
    private Context context;

    public WeatherWeekAdapter(List<WeatherWeek> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherWeekAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemweek,parent,false);
        return new WeatherWeekAdapterHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WeatherWeekAdapterHolder holder, int position) {
        WeatherWeek weatherWeek = list.get(position);
        if (weatherWeek == null){
            return;
        }
        Picasso.get().load("http://openweathermap.org/img/wn/" +weatherWeek.getResourceAnh() +"@2x.png").into(holder.hinhAnh);
        holder.thoiGian.setText(weatherWeek.getThoiGian());
        holder.moTa.setText(weatherWeek.getMoTa());
        holder.nhietDoMax.setText(weatherWeek.getNhietDoMin()+"°~"+weatherWeek.getNhietDoMax()+"°");
        holder.tvLuongMua.setText("Lượng mưa : "+weatherWeek.getLuongMua());
        holder.tvDoAm.setText("Độ ẩm : "+weatherWeek.getDoAm()+"%");
        holder.tvSunRise.setText("Mặt trời mọc : "+weatherWeek.getSunRise());
        holder.tvSunSet.setText("Mặt trời lặn : "+weatherWeek.getSunSet());
        holder.tvMay.setText("Mây che phủ : "+weatherWeek.getMay()+"%");
        holder.tvTocGio.setText("Tốc độ gió : "+weatherWeek.getTocgio()+"m/s");
        holder.tvHuongGio.setText("Hướng gió : "+weatherWeek.getHuonggio());
        holder.tvUV.setText("Chỉ số UV : "+weatherWeek.getUv());
    }

    @Override
    public int getItemCount() {
        if (list!= null){
            return  list.size();
        }
        return 0;
    }
    public void release(){
        context = null;
    }

    public class WeatherWeekAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView hinhAnh;
        private TextView thoiGian;
        private TextView moTa;
        private TextView nhietDoMax;

        private TextView tvLuongMua;
        private TextView tvDoAm;
        private TextView tvSunRise;
        private TextView tvSunSet;
        private TextView tvMay;
        private TextView tvTocGio;
        private TextView tvHuongGio;
        private TextView tvUV;


        public WeatherWeekAdapterHolder(@NonNull View itemView) {
            super(itemView);
            hinhAnh = itemView.findViewById(R.id.itemHinhAnh);
            thoiGian = itemView.findViewById(R.id.itemThoiGian);
            moTa = itemView.findViewById(R.id.tv_moTaWeek);
            nhietDoMax = itemView.findViewById(R.id.itemNhietDoMax);
            tvLuongMua = itemView.findViewById(R.id.tv_luongMua);
            tvDoAm = itemView.findViewById(R.id.tv_doAm);
            tvSunRise = itemView.findViewById(R.id.tv_SunRise);
            tvSunSet = itemView.findViewById(R.id.tv_sunSet);
            tvMay = itemView.findViewById(R.id.tv_may);
            tvTocGio = itemView.findViewById(R.id.tv_tocgio);
            tvHuongGio = itemView.findViewById(R.id.tv_huonggio);
            tvUV = itemView.findViewById(R.id.cs_uv);

        }
    }
}
