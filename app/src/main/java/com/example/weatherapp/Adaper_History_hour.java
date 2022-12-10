package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaper_History_hour extends RecyclerView.Adapter<Adaper_History_hour.Adapter_History_hourViewHolder> {
    List<History_hour> mHistory_hour;

    public Adaper_History_hour(List<History_hour> mHistory_hour) {
        this.mHistory_hour = mHistory_hour;
    }

    @NonNull
    @Override
    public Adapter_History_hourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_history_hour,parent,false);
        return new Adapter_History_hourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_History_hourViewHolder holder, int position) {
            History_hour history_hour = mHistory_hour.get(position);
            if (history_hour == null){
                return;
            }
            Picasso.get().load("http://openweathermap.org/img/wn/" +history_hour.getIcon() +"@4x.png").into(holder.iv_icon);
            holder.tv_date.setText(history_hour.getDate());
            holder.tv_temp.setText(history_hour.getTemp());
            holder.tv_humidity.setText(history_hour.getHumidity());
            holder.tv_description.setText(history_hour.getDescription());
            holder.tv_wind.setText(history_hour.getWind());
    }

    @Override
    public int getItemCount() {
        if (mHistory_hour != null){
            return mHistory_hour.size();
        }
        return 0;
    }

    public class Adapter_History_hourViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_date;
        private TextView tv_temp;
        private TextView tv_wind;
        private TextView tv_humidity;
        private TextView tv_description;

        public Adapter_History_hourViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_h_icon);
            tv_date = itemView.findViewById(R.id.tv_h_date);
            tv_temp = itemView.findViewById(R.id.tv_h_temp);
            tv_wind = itemView.findViewById(R.id.tv_h_wind);
            tv_humidity = itemView.findViewById(R.id.tv_h_humidity);
            tv_description = itemView.findViewById(R.id.tv_h_description);
        }
    }
}
