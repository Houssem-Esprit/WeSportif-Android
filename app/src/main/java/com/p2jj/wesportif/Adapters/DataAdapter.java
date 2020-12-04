package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.R;
import com.p2jj.wesportif.Model.User;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.CoachViewHolder> implements Filterable {


    List<User> mDataset;
    List<User> listBackup;
    Context mContext;

    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(Context context,List<User> myDataset, RecyclerViewItemClickListener listener) {
        mContext=context;
    mDataset = myDataset;
    this.recyclerViewItemClickListener = listener;
        listBackup=new ArrayList<>(mDataset);


    }

    public DataAdapter(Context context,List<User> myDataset, View.OnClickListener onClickListener) {
        this.mContext=context;
        mDataset = myDataset;
        onClickListener = onClickListener;
        listBackup=new ArrayList<>(mDataset);

    }


    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_of_coaches,parent,false);
        CoachViewHolder vh = new CoachViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CoachViewHolder holder, int position) {
        //holder.coach_img.setImageResource(mDataset.get(position).getImg_user());
        String url="http://wessporitf.eu-4.evennode.com/uploads/users/"+mDataset.get(position).getImg_user();
        Glide.with(mContext).load(url).into(holder.coach_img);


        String name=mDataset.get(position).getNom()+" "+mDataset.get(position).getPrenom();
        holder.coach_name.setText(name);

        if(listBackup.size()==0)
        {
            listBackup=new ArrayList<>(mDataset);

        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public  class CoachViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView coach_img;
        TextView coach_name;
        ImageView envoyer_button;

        public CoachViewHolder(View v) {
            super(v);
            coach_img =v.findViewById(R.id.coach_img) ;
            coach_name=v.findViewById(R.id.coach_name);
            envoyer_button=v.findViewById(R.id.envoyer_button);

            v.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
          //  recyclerViewItemClickListener.clickOnItem(v);

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(View data);
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(listBackup);
            }
            else
            {
                String filterPatern=constraint.toString().toLowerCase().trim();

                for(User item:listBackup)
                {
                    if(item.getNom().toLowerCase().contains(filterPatern) || item.getPrenom().toLowerCase().contains(filterPatern))
                    {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataset.clear();
            mDataset.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}

