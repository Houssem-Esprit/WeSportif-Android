package com.p2jj.wesportif.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.p2jj.wesportif.Adapters.AdaptorEv;
import com.p2jj.wesportif.Adapters.AdaptorEvRecommended;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {
    private TextView mTxvMenuItem;
    ImageView firstScrollingButton;
    ImageView secondScrollingButton;
    TextView navUserName;

    List<Event> mList;
    List<Event>mListR;

    ProgressBar pb;

    RecyclerView recyclerView;
    AdaptorEv adaptorEv;
    LinearLayoutManager mLayoutManager;

    RecyclerView recyclerViewEventRecommended;
    AdaptorEvRecommended adaptorEvRecommended;
    LinearLayoutManager mLayoutManager2;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewroot= inflater.inflate(R.layout.fragment_main, container, false);

        mListR = new ArrayList<>();
        mList = new ArrayList<>();


        //** Set up Recyclers View

        // Nearest Event Recycler View

        recyclerView = viewroot.findViewById(R.id.recyclerViewcoach);
        mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        // scrolling button 1

        firstScrollingButton = viewroot.findViewById(R.id.firstSCrolingButton);
        firstScrollingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisibleItemIndex = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                int totalItemCount = recyclerView.getAdapter().getItemCount();
                if (totalItemCount <= 0) return;
                int lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemIndex >= totalItemCount) return;
                mLayoutManager.smoothScrollToPosition(recyclerView,null,lastVisibleItemIndex+1);
            }
        });

        // Recommanded Event Recycler View

        recyclerViewEventRecommended = viewroot.findViewById(R.id.recyclerViewRecommend);
        mLayoutManager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewEventRecommended.setLayoutManager(mLayoutManager2);

        // Scrolling button 2

        secondScrollingButton =viewroot.findViewById(R.id.secondScrollingButton);
        secondScrollingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int firstVisibleItemIndex = mLayoutManager2.findFirstCompletelyVisibleItemPosition();
                int totalItemCount = recyclerViewEventRecommended.getAdapter().getItemCount();
                if (totalItemCount <= 0) return;
                int lastVisibleItemIndex = mLayoutManager2.findLastVisibleItemPosition();

                if (lastVisibleItemIndex >= totalItemCount) return;
                mLayoutManager2.smoothScrollToPosition(recyclerViewEventRecommended,null,lastVisibleItemIndex+1);
            }

        });

        //**  End set up Recyclers View

        //** AsyncTasks Load Data



        loadNearEvents lnv = new loadNearEvents();
        lnv.execute();

        loadRecommandedEvents lrv = new loadRecommandedEvents();
        lrv.execute();

        //** End AsyncTasks Load Data


        return viewroot;
    }


    public class loadRecommandedEvents extends AsyncTask<String,Event,List<Event>> {
        List<Event> myList = new ArrayList<>();

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected List<Event> doInBackground(String... strings) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaa");
            // getNearestEvents();
            // *********************************************************
            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .GetUserEventsByCat(Session.getInstance().getUser().getCin());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        Log.d("native response ", "response :"+response);
                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("eventInformations");
                        Log.d("evnt Response","msg :"+msg);
                        if(msg.equals("Events with cat retrieved"))
                        {


                            JSONArray evts=jsonResp.getJSONArray("events");
                            System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL"+evts);
                            for (int i = 0 ; i < evts.length(); i++) {
                                System.out.println("IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
                                JSONObject obj = evts.getJSONObject(i);
                                Event e=new Event();
                                e.setImg_event(obj.getString("img"));
                                e.setDate_debut(obj.getString("date_debut"));
                                e.setTitre(obj.getString("titre"));
                                e.setCategorieSport(obj.getString("nom"));
                                e.setId(obj.getInt("id"));
                                Log.d("event","this is the new event "+e.toString());
                                System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF : "+e.toString());
                                myList.add(e);
                                publishProgress(e);

                            }
                        }
                        else
                        {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                }
            });
            return myList;
        }

        @Override
        protected void onProgressUpdate(Event ... event) {
            super.onProgressUpdate(event);

            if(event != null){
                for (int i=0;i<event.length;i++){

                    mListR.add(event[i]);
                    adaptorEvRecommended = new AdaptorEvRecommended(getContext(), mListR);
                    recyclerViewEventRecommended.setAdapter(adaptorEvRecommended);

                }

            }

         /*   recyclerView = (RecyclerView)findViewById(R.id.recyclerViewcoach);
            adaptorEv = new AdaptorEv(getApplicationContext(),mList);

            mLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
            recyclerView.setAdapter(adaptorEv);
            recyclerView.setLayoutManager(mLayoutManager);

            // scrolling button 1

            firstScrollingButton = findViewById(R.id.firstSCrolingButton);
            firstScrollingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int firstVisibleItemIndex = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                    int totalItemCount = recyclerView.getAdapter().getItemCount();
                    if (totalItemCount <= 0) return;
                    int lastVisibleItemIndex = mLayoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItemIndex >= totalItemCount) return;
                    mLayoutManager.smoothScrollToPosition(recyclerView,null,lastVisibleItemIndex+1);
                }
            });  */

            //-------------------- get recommened events




        }

        @Override
        protected void onPostExecute(List<Event> o) {
            super.onPostExecute(o);
            // This is your code
            if(o !=null){

                System.out.println("not NUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUL");
                if(Session.getInstance().getEventByCat() == null){
                    Session.getInstance().setEventsByCat(o);
                } else{
                    for(int i=0;i<o.size();i++){

                        for(int j=0;j<Session.getInstance().getEventByCat().size();j++){
                            if (o.get(i).getId()!=Session.getInstance().getEventByCat().get(j).getId()){
                                Session.getInstance().getEventByCat().add(o.get(i));
                            }

                        }
                    }
                }
            }
        }


    }


    public class loadNearEvents extends AsyncTask<String,Event,List<Event>>{

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<Event> doInBackground(String... strings) {
            // *********************************************************
            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .GetNearEvents();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("eventInformations");
                        if(msg.equals("Near Events retrieved"))
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
                                publishProgress(e);
                            }

                            //   shimmerFrameLayout.stopShimmer();
                            //shimmerFrameLayout.setVisibility(View.GONE);
                        }
                        else{}

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Event ... event) {
            super.onProgressUpdate(event);

            if(event != null){
                for (int i=0;i<event.length;i++){

                    mList.add(event[i]);
                    adaptorEv = new AdaptorEv(getContext(),mList);
                    recyclerView.setAdapter(adaptorEv);
                }
            }
        }

        @Override
        protected void onPostExecute(List<Event> o) {
            super.onPostExecute(o);
            // This is your code
        }
    }
}
