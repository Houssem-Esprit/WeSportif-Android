package com.p2jj.wesportif.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Adapters.AdaptorAllEvnets;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AllEventsFragment extends Fragment  {
        Toolbar toolbar;
        ImageView back_button;
        List<Event> mList;
    View rootView;
    RecyclerView recyclerView;
    AdaptorAllEvnets adaptorEv;
    SearchView search;
    Context mContext;

    public static final String SHARED_FILE_NAME = "favEventsPreferences";
    SharedPreferences data ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_all_events, container, false);






        loadAllEvents lav=new loadAllEvents();
        lav.execute();


        search=rootView.findViewById(R.id.searchAllEvent);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptorEv.getFilter().filter(newText);
                return false;
            }
        });
        data = getActivity().getSharedPreferences(SHARED_FILE_NAME,MODE_PRIVATE);




        return rootView;
    }


    public class loadAllEvents extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mList = new ArrayList<>();

        }

        @Override
        protected Object doInBackground(Object[] objects) {



            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        getAllEvents();


                        // Toast.makeText(getApplicationContext(),"loaded",Toast.LENGTH_LONG).show();


                    } catch (Exception ex) {
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
          /*  try {
               // Thread.sleep(100);


            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            Handler mainHandler = new Handler(Looper.getMainLooper());

            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {







                }

            };
            mainHandler.post(myRunnable);

        }
    }

        public void getAllEvents()
    {


        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetEvents();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("eventInformations");
                    if(msg.equals("Events retrieved"))
                    {

                        JSONArray evts=jsonResp.getJSONArray("events");

                        for (int i = 0 ; i < evts.length(); i++) {
                            JSONObject obj = evts.getJSONObject(i);
                            Event e=new Event();
                            e.setImg_event(obj.getString("img"));
                            e.setDate_debut(obj.getString("date_debut"));
                            e.setTitre(obj.getString("titre"));
                            e.setCategorieSport(obj.getString("nom"));
                            e.setId(obj.getInt("id"));
                            e.setUserCreator(obj.getString("event_user_admin"));
                            mList.add(e);

                        }


                        recyclerView = rootView.findViewById(R.id.Allevnt_recyclerview);
                        adaptorEv = new AdaptorAllEvnets(getContext(), mList,data);
                        recyclerView.setAdapter(adaptorEv);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


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
