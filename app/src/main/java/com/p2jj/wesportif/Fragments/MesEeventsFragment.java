package com.p2jj.wesportif.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Adapters.AdaptorAllEvnets;
import com.p2jj.wesportif.Adapters.AdaptorEv;
import com.p2jj.wesportif.Adapters.AdaptorEvRecommended;
import com.p2jj.wesportif.Adapters.AdaptorMesEvents;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MesEeventsFragment extends Fragment  {

    ImageView back_button;
    List<Event> mList;
    View rootView;
    RecyclerView recyclerView;
    AdaptorMesEvents adaptorEv;
    ImageView eventImg;
    Bitmap imgBmp;
    Event e;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_mes_eevents, container, false);


        loadMyEvents lme=new loadMyEvents();
        lme.execute();
        eventImg=rootView.findViewById(R.id.event_img);

        return rootView;
    }
    public class loadMyEvents extends AsyncTask {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mList = new ArrayList<>();

            //progress bar



        }


        @Override
        protected Object doInBackground(Object[] objects) {


            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        getMyEvents();

                        // Toast.makeText(getApplicationContext(),"loaded",Toast.LENGTH_LONG).show();


                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        System.out.println("error");
                    }
                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {

                    recyclerView = rootView.findViewById(R.id.MesEvents_recyclerview);
                    adaptorEv = new AdaptorMesEvents(getContext(),mList);

                    recyclerView.setAdapter(adaptorEv);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));






                } // This is your code
            };
            mainHandler.post(myRunnable);


        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }
    }

    public void getMyEvents()
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetUserEvents(Session.getInstance().getUser().getCin());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("eventInformations");
                    if(msg.equals("User events retrieved"))
                    {

                        JSONArray evts=jsonResp.getJSONArray("events");

                        for (int i = 0 ; i < evts.length(); i++) {
                            JSONObject obj = evts.getJSONObject(i);
                            e=new Event();
                            e.setImg_event(obj.getString("img"));
                            e.setDate_debut(obj.getString("date_debut"));
                            e.setTitre(obj.getString("titre"));
                            e.setCategorieSport(obj.getString("nom"));
                            e.setUserCreator(obj.getString("event_user_admin"));


                            mList.add(e);


                        }



                    }
                    else
                    {
                        Toast.makeText(getContext(), "Erreur", Toast.LENGTH_LONG).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getStackTrace());
            }
        });
    }


}
