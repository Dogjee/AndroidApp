package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HAdapter extends RecyclerView.Adapter<HAdapter.HAdaperHoler> {

    private List<ListH> mListH;
    private Context mContext;


    public HAdapter(List<ListH> mListH, Context mContext) {
        this.mListH = mListH;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HAdaperHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_day_history,parent,false);

        return new HAdaperHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HAdaperHoler holder, int position) {
            ListH listH = mListH.get(position);
            if (listH == null){
                return;
            }
            Picasso.get().load("https://img.icons8.com/color/96/000000/calendar-"+listH.getImgSource()+".png").into(holder.imageView1);
            holder.textView1.setText(listH.getNgay());
            holder.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDetails(listH);
                }
            });


    }
    public void release(){
        mContext = null;
    }
    private void getDetails(ListH listH) {
        Intent intent = new Intent(mContext,Details_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",listH);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mListH !=null){
          return mListH.size();
        }
        return 0;
    }

    public class HAdaperHoler extends RecyclerView.ViewHolder{

        private ImageView imageView1;
        private TextView textView1;
        private RelativeLayout list_item;

        public HAdaperHoler(@NonNull View itemView) {
            super(itemView);
            imageView1 = itemView.findViewById(R.id.imageViewHSTT);
            textView1 = itemView.findViewById(R.id.textViewHDate);
            list_item = itemView.findViewById(R.id.list_item);
        }
    }
}
