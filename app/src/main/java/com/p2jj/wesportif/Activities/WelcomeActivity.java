package com.p2jj.wesportif.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.p2jj.wesportif.R;

public class WelcomeActivity extends AppCompatActivity {

    VideoView videoid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        videoid=(VideoView)findViewById(R.id.videoid);
        String path = "android.resource://com.p2jj.wesportif/"+R.raw.nike;


        Uri uri = Uri.parse(path);
        videoid.setVideoURI(uri);
        // vedio setting for full screen
//
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoid.getLayoutParams();
//        params.width = metrics.widthPixels;
//        params.height = metrics.heightPixels;
//        params.leftMargin = 0;
//        videoid.setLayoutParams(params);
        // end settings full screen

        videoid.start();

        videoid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0,0);
                mp.start();
            }
        });

        // Event of Se connecter button
        Button Seconnecter=(Button) findViewById(R.id.seconnecter);
        Seconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WelcomeActivity.this,Login.class);
                startActivity(i);
            }
        });


        Button btn = findViewById(R.id.devenirMemb);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onResume() {
        videoid.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoid.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoid.stopPlayback();
    }
}
