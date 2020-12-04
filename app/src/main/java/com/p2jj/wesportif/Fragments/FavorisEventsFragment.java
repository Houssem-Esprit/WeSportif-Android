package com.p2jj.wesportif.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Adapters.AdaptorFavorisEvents;
import com.p2jj.wesportif.Adapters.AdaptorMesEvents;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FavorisEventsFragment extends Fragment {

    ImageView back_button;
    public static final String SHARED_FILE_NAMEEVENT = "favEventsPreferences";
    SharedPreferences data ;
    SearchView search;
    AdaptorFavorisEvents adaptorEv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_favoris_events, container, false);


        data = getActivity().getSharedPreferences(SHARED_FILE_NAMEEVENT,MODE_PRIVATE);




        RecyclerView recyclerView = rootView.findViewById(R.id.FavorisEvents_recyclerview);
        adaptorEv = new AdaptorFavorisEvents(getContext(), Session.getInstance().getFavEvents(),data);

        recyclerView.setAdapter(adaptorEv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search=rootView.findViewById(R.id.searchFavEvent);

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

        return rootView;
    }



}
