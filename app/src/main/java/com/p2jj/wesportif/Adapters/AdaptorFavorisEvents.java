package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.p2jj.wesportif.Fragments.CommentFragment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdaptorFavorisEvents extends RecyclerView.Adapter<AdaptorFavorisEvents.myViewHolder>  implements Filterable {

    Context mContext;
    List<Event> mData;
    Fragment CommentsFragment;
    List<Event> listBackup;

    SharedPreferences data;


    public AdaptorFavorisEvents(Context mContext, List<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
        listBackup=new ArrayList<>(mData);

    }
    public AdaptorFavorisEvents(Context mContext, List<Event> mData,SharedPreferences data) {
        this.mContext = mContext;
        this.mData = mData;
        listBackup=new ArrayList<>(mData);

        this.data=data;

    }

    @Override
    public myViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflator= LayoutInflater.from(mContext);
        View v = inflator.inflate(R.layout.all_event,parent,false);
        CommentsFragment = new CommentFragment();
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final myViewHolder holder,final int position) {

       // holder.event_img.setImageResource(mData.get(position).getImg_event());
        String url="http://wessporitf.eu-4.evennode.com/uploads/events/"+mData.get(position).getImg_event();
        Glide.with(mContext).load(url).into(holder.event_img);


        holder.titleEvent.setText(mData.get(position).getTitre());
        holder.date.setText(mData.get(position).getDate_debut());
        holder.CategorieSport.setText(mData.get(position).getCategorieSport());
        CommentsFragment = new CommentFragment();
        final FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();


        holder.CommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentsFragment = new CommentFragment();

                if(holder.commentFrame.getChildCount()<1){
                    int newContainerId = getUniqueId();
                    holder.commentFrame.setId(newContainerId);
                    fragmentManager.beginTransaction().replace(newContainerId, CommentsFragment).commit();
                }
            }

            public int getUniqueId(){
                return (int) SystemClock.currentThreadTimeMillis();
            }
        });


        Event e=mData.get(position);


        if(Session.getInstance().getFavEvents().contains(e))
        {
            holder.likeBtn.setLiked(true);
            Log.d("Liked event",e.toString());
        }
        else
        {
            holder.likeBtn.setLiked(false);

            Log.d("Unliked event",e.toString());

        }


        holder.likeBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Event e=mData.get(position);

                SharedPreferences.Editor prefsEditor = data.edit();
                Gson gson = new Gson();
                String json = gson.toJson(e);
                prefsEditor.putString(e.getId()+"", json);
                prefsEditor.apply();

                Session.getInstance().getFavEvents().add(e);

                Log.d("Current fav events",Session.getInstance().getFavEvents().toString());
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Event e=mData.get(position);
                SharedPreferences.Editor prefsEditor = data.edit();
                //  Gson gson = new Gson();
                // String json = gson.toJson(e);
                prefsEditor.remove(e.getId()+"");
                prefsEditor.apply();
                Session.getInstance().getFavEvents().remove(e);

                // Session.getInstance().delEvent(e);
                Log.d("Current fav events",Session.getInstance().getFavEvents().toString());

            }
        });

    }

    public final View.OnClickListener CommentButtonClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CommentsFragment = new CommentFragment();
            setFragment(CommentsFragment);

        }
    };

    @Override
    public int getItemCount() {
        return mData.size();
     }

    public class myViewHolder extends  RecyclerView.ViewHolder{
        ImageView event_img;
        TextView titleEvent, date , CategorieSport;
        ImageButton CommentButton;
        LinearLayout commentFrame;
        LikeButton likeBtn;

        public myViewHolder( View itemView) {
            super(itemView);
            event_img = itemView.findViewById(R.id.allEvent_img);
            titleEvent= itemView.findViewById(R.id.allEvent_name);
            date =itemView.findViewById(R.id.detaileventDATE_input);
            CategorieSport=itemView.findViewById(R.id.detaileventCategorie_input);
            CommentButton = itemView.findViewById(R.id.AllEvent_CommentButton);
            commentFrame=itemView.findViewById(R.id.comment_fram);
            CommentsFragment = new CommentFragment();
            likeBtn=itemView.findViewById(R.id.allEvent_fav);

        }
    }


    public void setFragment(Fragment fragment){

        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.comment_fram,fragment);
        fragmentTransaction.commit();

    }
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private Filter listFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Event> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0)
            {
                filteredList.addAll(listBackup);
            }
            else
            {

                String filterPatern=constraint.toString().toLowerCase().trim();

                for(Event item:listBackup)
                {
                    if(item.getTitre().toLowerCase().contains(filterPatern) || item.getCategorieSport().toLowerCase().contains(filterPatern))
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
            mData.clear();
            mData.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


}
