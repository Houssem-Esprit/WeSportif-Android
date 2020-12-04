package com.p2jj.wesportif.Model;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;


public class Session {
    private static Session instance;
    private User user;
    private List<Event> favEvents=new ArrayList<>();
    private List<Event> EventsByCat = new ArrayList<>();
    private List<Event> NearestEvent = new ArrayList<>();


    private Session()
    {

    }

    public static Session getInstance()
    {
        if(instance == null)
            instance = new Session();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Event> getFavEvents() {
        return favEvents;
    }

    public void setFavEvents(List<Event> favEvents) {
        this.favEvents = favEvents;
    }



    public boolean isEventLiked(Event e){

        for(Event evt: favEvents){
            if(evt.getId()==e.getId())
            {
                return true;
            }
        }
        return false;
    }

    public List<Event> getEventByCat(){return EventsByCat; }
    public void setEventsByCat(List<Event> eventsByCat){this.EventsByCat=eventsByCat;}

    public List<Event> getNearestEvent(){return NearestEvent; }
    public void setNearestEvent(List<Event> nearestEvent){this.NearestEvent=nearestEvent;}


}