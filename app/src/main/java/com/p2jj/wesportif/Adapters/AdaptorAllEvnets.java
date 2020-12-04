package com.p2jj.wesportif.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.p2jj.wesportif.Activities.DetailEvent;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Fragments.CommentFragment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdaptorAllEvnets extends RecyclerView.Adapter<AdaptorAllEvnets.myViewHolder>  implements Filterable {

    Context mContext;
    List<Event> mData;
    Fragment CommentsFragment;
    List<Event> listBackup;
    SharedPreferences data;

    Gson gson = new Gson();
    Event e;
    User currentUser;

    public AdaptorAllEvnets(Context mContext, List<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
        listBackup=new ArrayList<>(mData);



    }

    public AdaptorAllEvnets(Context mContext, List<Event> mData, SharedPreferences data) {
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
    public void onBindViewHolder(final myViewHolder holder, final int position) {
        String url="http://wessporitf.eu-4.evennode.com/uploads/events/"+mData.get(position).getImg_event();
        Glide.with(mContext).load(url).into(holder.event_img);

        //  holder.event_img.setImageResource(mData.get(position).getImg_event());
        holder.titleEvent.setText(mData.get(position).getTitre());
        holder.date.setText(mData.get(position).getDate_debut());
        holder.CategorieSport.setText(mData.get(position).getCategorieSport());
        final FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();



       final CommentFragment CommentsFragment = new CommentFragment(mData.get(position).getId());



        holder.CommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.comment_fraM.getChildCount()<1){
                    int newContainerId = getUniqueId();
                    holder.comment_fraM.setId(newContainerId);
                    fragmentManager.beginTransaction().replace(newContainerId,CommentsFragment).commit();
                }
            }
            public int getUniqueId(){
                return (int) SystemClock.currentThreadTimeMillis();
            }
        });

        holder.goToDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Event e=mData.get(position);
               Intent i=new Intent(mContext, DetailEvent.class);
               i.putExtra("eventId",e.getId());
               mContext.startActivity(i);
            }
        });

        e=mData.get(position);

        userDetails(holder);

/*
            SharedPreferences.Editor prefsEditor = data.edit();
            Gson gson = new Gson();
            String json = gson.toJson(e);
            prefsEditor.putString("MyObject", json);
            prefsEditor.apply();
*/
//            String json = data.getString(e.getId()+"", "");
//
//            if(!json.equals(""))
//            {
//                //Event obj = gson.fromJson(json, Event.class);
//                Session.getInstance().getFavEvents().add(e);
//
//            }

        if(Session.getInstance().getFavEvents().contains(e))
        {
            holder.likeBtn.setLiked(true);
        }
        else
        {
            holder.likeBtn.setLiked(false);


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

            }
        });
       // holder.loadCounters(e.getId());


    }



    @Override
    public int getItemCount() {
        return mData.size();
     }

    public class myViewHolder extends  RecyclerView.ViewHolder{
        private View iView;
        ImageView event_img;
        TextView titleEvent, date , CategorieSport,nbComs,nbLikes;
        ImageButton CommentButton;
        LinearLayout comment_fraM;
        Button goToDetails;
        LikeButton likeBtn,likeEventBtn;
        ImageView user_img;
        TextView user_name;
        public class loadData extends AsyncTask {


            @Override
            protected Object doInBackground(Object[] objects) {
                return null;
            }
        }

        public myViewHolder( View itemView) {
            super(itemView);
            iView=itemView;
            event_img = itemView.findViewById(R.id.allEvent_img);
            titleEvent= itemView.findViewById(R.id.allEvent_name);
            date =itemView.findViewById(R.id.detaileventDATE_input);
            CategorieSport=itemView.findViewById(R.id.detaileventCategorie_input);
            CommentButton = itemView.findViewById(R.id.AllEvent_CommentButton);
            comment_fraM =itemView.findViewById(R.id.comment_fram);
            goToDetails=itemView.findViewById(R.id.GoToDetail_button);
            user_img=itemView.findViewById(R.id.all_event_userimg_img);
            user_name=itemView.findViewById(R.id.all_event_username_name);

            likeBtn=itemView.findViewById(R.id.allEvent_fav);
            likeEventBtn=itemView.findViewById(R.id.doLike_input);
            nbComs=itemView.findViewById(R.id.NbComment_input);
            nbLikes=itemView.findViewById(R.id.NbJaime_input);

        }
        public View getView()
        {
            return iView;
        }

        public void loadCounters(int eventId)  {

            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .CountLike(eventId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("reactionInformations");
                        if(msg.equals("Like count retrieved"))
                        {

                            JSONObject react=jsonResp.getJSONObject("reactions");
                            int nbr=react.getInt("nbrLikes");

                            nbLikes.setText(nbr+"");




                        }
                        else
                        {

                        }


                    } catch (Exception e) {
                        Log.d("error","in likes");

                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    System.out.println(t.getStackTrace());
                }
            });


            Call call2 = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .CountCom(eventId);
            call2.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("reactionInformations");
                        if(msg.equals("Comments count retrieved"))
                        {

                            JSONObject react=jsonResp.getJSONObject("reactions");
                            int nbr=react.getInt("nbrComs");

                            nbComs.setText(nbr+"");


                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    Log.d("error","in coms");
                }
            });

        }
    }


    public void userDetails(final myViewHolder holder)
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetUserDetails(e.getUserCreator());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("userInformations");

                    if(msg.equals("User retrieved")){
                        JSONObject userJson=jsonResp.getJSONObject("user");

                        currentUser=new User();
                        try {
                            String cin = userJson.getString("cin");
                            String nom=userJson.getString("nom");
                            String prenom=userJson.getString("prenom");

                            String img=userJson.getString("img");


                            currentUser.setCin(cin);
                            currentUser.setNom(nom);
                            currentUser.setPrenom(prenom);

                            currentUser.setImg_user(img);

                            String username=currentUser.getPrenom()+" "+currentUser.getNom();
                            holder.user_name.setText(username);

                            String urlUser="http://wessporitf.eu-4.evennode.com/uploads/users/"+currentUser.getImg_user();
                            Glide.with(mContext).load(urlUser).into(holder.user_img);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

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
