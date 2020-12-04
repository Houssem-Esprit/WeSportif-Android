package com.p2jj.wesportif;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p2jj.wesportif.Adapters.DataAdapter;

public class CustomListViewDialog extends Dialog implements View.OnClickListener {

    public Context activity;
    public Dialog dialog;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DataAdapter adapter;
    ImageView coach_img;
    TextView coach_name;
    ImageView envoyer_button;
    Button envoyer_tout;
    SearchView rechercher;
    Button cancel;


    DisplayMetrics metrics = new DisplayMetrics(); //get metrics of screen
    CustomListViewDialog customListViewDialog;




    public CustomListViewDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);

    }

    public  CustomListViewDialog(Context a,DataAdapter adapter){
        super(a);
        this.activity = a;
        this.adapter = adapter;
        sutupLayout();

    }

    public void sutupLayout(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_send_coach);
        coach_img =findViewById(R.id.coach_img) ;
        coach_name=findViewById(R.id.coach_name);
        envoyer_button=findViewById(R.id.envoyer_button);
        // popup_send_coach components declaration
        recyclerView = findViewById(R.id.recyclerViewcoach);
        envoyer_tout = findViewById(R.id.envoyerTout);
        rechercher = findViewById(R.id.sendCoachSearch);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);

        cancel = findViewById(R.id.cancel);

        recyclerView.setAdapter(adapter);
        cancel.setOnClickListener(this);

        rechercher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
                default:
                    break;
        }
        dismiss();
    }
}
