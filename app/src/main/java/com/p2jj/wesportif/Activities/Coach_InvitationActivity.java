package com.p2jj.wesportif.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p2jj.wesportif.Adapters.AdaptorAllEvnets;
import com.p2jj.wesportif.Adapters.AdaptorInvitCoach;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Coach_InvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach__invitation);


        List<User> DataUser=new ArrayList<>();
        List<Event> DataEvent=new ArrayList<>();


        DataEvent.add(new Event("Footing for all","img.jpg", "12/10/2019" ));
        DataEvent.add(new Event("Workout","img.jpg","12/10/2019"));
        DataEvent.add(new Event("Taekwondo in the air","img.jpg", "12/10/2019"));
        DataEvent.add(new Event("Yoga earth","img.jpg","12/10/2019"));
        DataEvent.add(new Event("Manzah++","img.jpg","12/10/2019"));

        DataUser.add(new User("Mohsen Rabhi","img.jpg"));
        DataUser.add(new User("Rabii Bouden","img.jpg"));
        DataUser.add(new User("Frida","img.jpg"));
        DataUser.add(new User("Zazya LAhlaliya","img.jpg"));
        DataUser.add(new User("Samya Abou","img.jpg"));

            //RecyclerView
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.CoachInvitation_RecyclerView);
        AdaptorInvitCoach adaptorEv = new AdaptorInvitCoach(this,DataUser,DataEvent);

        recyclerView.setAdapter(adaptorEv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
