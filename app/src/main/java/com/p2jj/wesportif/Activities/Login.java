package com.p2jj.wesportif.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    EditText login;
    EditText pass;
    VideoView videoid;
    CircularProgressButton event;
    public static final String SHARED_FILE_NAME = "userPreferences";
    public static final String SHARED_FILE_NAMEEVENT = "favEventsPreferences";

    SharedPreferences data;

    User currentUser=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        getAllFavEvents();

        data = getSharedPreferences(SHARED_FILE_NAME,MODE_PRIVATE);


        String sharedUserCin = data.getString("cin","");

        if(!sharedUserCin.equals(""))
        {


            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .GetUserDetails(sharedUserCin);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("userInformations");

                        if(msg.equals("User retrieved")){
                            JSONObject userJson=jsonResp.getJSONObject("user");

                            initSession(userJson);


                            Intent i = new Intent(Login.this,MainActivity.class);
                            startActivity(i);
                        }
                        else {

                            Toast.makeText(Login.this,"Erreur",Toast.LENGTH_LONG).show();


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                    Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


//
//        videoid=(VideoView)findViewById(R.id.login_videoid);
//        videoid.bringToFront();
//        String path = "android.resource://com.p2jj.wesportif/"+R.raw.star;
//
//
//        Uri uri = Uri.parse(path);
//        videoid.setVideoURI(uri);
//
//        videoid.start();
//
//        videoid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//                mp.setVolume(0,0);
//                mp.start();
//            }
//        });

        // vedio of login screen setup

        TextView txt =findViewById(R.id.ToSignup);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUpActivity.class);
            startActivity(i);}
        });


        login=findViewById(R.id.loginName);
        pass=findViewById(R.id.loginPass);

        // event button login
         event = findViewById(R.id.loginid);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

                Call call = RetrofitClient
                        .getInstance()
                        .getApi_service_node()
                        .Login(login.getText().toString(),pass.getText().toString());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        try {

                            JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                            String msg=jsonResp.getString("userInformations");

                            if(msg.equals("Connection success")){
                                JSONObject userJson=jsonResp.getJSONObject("user");

                                initSession(userJson);


                                Toast.makeText(Login.this,"Connecté !",Toast.LENGTH_LONG).show();

                                SharedPreferences.Editor editor = data.edit();
                                String cin=userJson.getString("cin");

                                editor.putString("cin",cin);

                                editor.apply();



                                Intent i = new Intent(Login.this,MainActivity.class);
                                startActivity(i);
                            }
                            else if(msg.equals("Wrong username")){
                                //nom d'utilisateur n'existe pas
                                //afficher suggestion de creation de compte si l'erreur s'affiche 3 fois
                                Toast.makeText(Login.this,"nom utilisateur incorrecte",Toast.LENGTH_LONG).show();


                            }else{
                                // mot de passe erroné
                                //afficher suggestion de changement de mot de passe si l'erreur s'affiche 3 fois
                                Toast.makeText(Login.this,"mot de passe incorrecte",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                        Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }


        });
    }





//    @Override
//    protected void onResume() {
//        videoid.resume();
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        videoid.suspend();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        videoid.stopPlayback();
//    }
        public void initSession(JSONObject userJson)
        {

            try {
                String cin = userJson.getString("cin");
                String nom=userJson.getString("nom");
                String prenom=userJson.getString("prenom");
                String email=userJson.getString("email");
                int numTel=userJson.getInt("numTel");
                String ddn=userJson.getString("ddn");
                String img=userJson.getString("img");
                String role=userJson.getString("role");
                String coverImg=userJson.getString("coverImg");

                currentUser.setCin(cin);
                currentUser.setNom(nom);
                currentUser.setPrenom(prenom);
                currentUser.setEmail(email);
                currentUser.setNumTelephone(numTel);
                currentUser.setDate_naissance(ddn);
                currentUser.setImg_user(img);
                currentUser.setRole(role);
                currentUser.setCoverImg(coverImg);

                Session.getInstance().setUser(currentUser);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    public void getAllFavEvents()
    {
        final SharedPreferences data = getSharedPreferences(SHARED_FILE_NAMEEVENT,MODE_PRIVATE);

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
                            String json = data.getString(e.getId()+"", "");

                            if(!json.equals(""))
                            {
                                Session.getInstance().getFavEvents().add(e);

                            }


                        }




                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println(t.getStackTrace());
            }
        });


    }

}

