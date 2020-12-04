package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p2jj.wesportif.Activities.DetailEvent;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AdaptorEvRecommended extends RecyclerView.Adapter<AdaptorEvRecommended.myViewHolder> {

    Context mContext;
    List<Event> mData;

    public AdaptorEvRecommended(Context mContext, List<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public myViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflator= LayoutInflater.from(mContext);
        View v = inflator.inflate(R.layout.eventcard2,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {

        String url="http://wessporitf.eu-4.evennode.com/uploads/events/"+mData.get(position).getImg_event();
        Glide.with(mContext).load(url).into(holder.event_img);


        // holder.event_img.setImageResource(mData.get(position).getImg_event());
        holder.titleEvent.setText(mData.get(position).getTitre());
        holder.date.setText(mData.get(position).getDate_debut());
        holder.CategorieSport.setText(mData.get(position).getCategorieSport());
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event e=mData.get(position);
                Intent i=new Intent(mContext, DetailEvent.class);
                i.putExtra("eventId",e.getId());
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
     }

    public class myViewHolder extends  RecyclerView.ViewHolder{
        ImageView event_img;
        TextView titleEvent, date , CategorieSport;
        View view;

        public myViewHolder( View itemView) {
            super(itemView);
            view=itemView;
            event_img = itemView.findViewById(R.id.event_img);
            titleEvent= itemView.findViewById(R.id.eventname_input);
            date =itemView.findViewById(R.id.dateevnt_input);
            CategorieSport=itemView.findViewById(R.id.categorie_input);

        }
        public View getView()
        {
            return view;
        }
    }

}
