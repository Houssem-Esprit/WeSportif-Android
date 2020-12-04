package com.p2jj.wesportif.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Fragments.ProfileFragment;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.androidquery.util.AQUtility.getContext;

public class DetailEvent extends AppCompatActivity {

    CircleImageView creatorImg;
    TextView creatorName;
    ImageView BlackGradiant;
    LinearLayout the_Big_Container=null;
    RelativeLayout DetailEvent_show_hide_container=null;
    Timer timer;
    ImageView eventImg;
    TextView eventName,eventDate,eventAdr,eventNbrParticipant,eventDesc;
    Button partBtn;
    int eventId;

    boolean hasParticipated=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);



         creatorImg=findViewById(R.id.creatorEvent_img);
         creatorName = findViewById(R.id.creatorEvent_name);
         BlackGradiant = findViewById(R.id.detailevent_Gradiant_img);

        eventDate=findViewById(R.id.detaileventDATE_input);
        eventName=findViewById(R.id.detaileventName_input);
        eventAdr=findViewById(R.id.detaileventPlace_input);
        eventDesc=findViewById(R.id.detaileventDesc_input);
        Intent i=getIntent();
        eventId=i.getIntExtra("eventId",0);
        loadEvent();
        checkParticipation();

        partBtn=findViewById(R.id.Pariciper_button);
        partBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!hasParticipated)
                {
                    partEvent();

                }else
                {
                    annulerPart();
                }

            }
        });

        eventImg=findViewById(R.id.detailevent_img);
        eventNbrParticipant=findViewById(R.id.detaileventNbParticipant_input);




         the_Big_Container=findViewById(R.id.First_LinearLayout_Container);
        DetailEvent_show_hide_container=findViewById(R.id.detailEvent_show_hide_container);
        DetailEvent_show_hide_container.setVisibility(View.VISIBLE);

            the_Big_Container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(DetailEvent_show_hide_container.getVisibility()== View.GONE){
                        DetailEvent_show_hide_container.setVisibility(View.VISIBLE);

                    }

                    else {

                        DetailEvent_show_hide_container.setVisibility(View.GONE);

                    }

                }
            });
/*
            the_Big_Container.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_MOVE ){
                            System.out.println(DetailEvent_show_hide_container.getVisibility());


                }
                        return true;
            }

            }); */

}
public void annulerPart()
{
    Call call = RetrofitClient
            .getInstance()
            .getApi_service_node()
            .DelParticipation(Session.getInstance().getUser().getCin(),eventId);
    call.enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) {

            try {

                JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                String msg=jsonResp.getString("eventInformations");
                if(msg.equals("Participation deleted"))
                {
                    Toast.makeText(getApplicationContext(), "Inscription annuler!", Toast.LENGTH_LONG).show();


                    finish();
                    startActivity(getIntent());

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



public void partEvent()
{
    Call call = RetrofitClient
            .getInstance()
            .getApi_service_node()
            .ParticiperEvent(Session.getInstance().getUser().getCin(),eventId);
    call.enqueue(new Callback() {
        @Override
        public void onResponse(Call call, Response response) {

            try {

                JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                String msg=jsonResp.getString("eventInformations");
                if(msg.equals("User registered"))
                {

                    Toast.makeText(getApplicationContext(), "Registered to event!", Toast.LENGTH_LONG).show();


                    finish();
                    startActivity(getIntent());



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
    public void checkParticipation() {

        if(eventId!=0)
        {
            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .CheckParticipation(Session.getInstance().getUser().getCin(),eventId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("eventInformations");
                        if(msg.equals("true"))
                        {

                            hasParticipated=true;

                            partBtn.setBackgroundColor(Color.parseColor("#ff474e"));
                            partBtn.setTextColor(Color.parseColor("#FFFFFF"));
                            partBtn.setText("Annuler participation");


                        }

                        if(msg.equals("false"))
                        {
                            hasParticipated=false;

                            partBtn.setBackgroundColor(Color.parseColor("#10948f"));
                            partBtn.setTextColor(Color.parseColor("#FFFFFF"));
                            partBtn.setText("Participer");
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

    public void loadEvent()
    {
        if(eventId!=0)
        {
            Call call = RetrofitClient
                    .getInstance()
                    .getApi_service_node()
                    .GetEventDetails(eventId);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {

                    try {

                        JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                        String msg=jsonResp.getString("eventInformations");
                        if(msg.equals("Event retrieved"))
                        {

                            JSONObject obj=jsonResp.getJSONObject("event");

                            Event e=new Event();
                            e.setImg_event(obj.getString("eventImg"));
                            e.setDate_debut(obj.getString("date_debut"));
                            e.setTitre(obj.getString("titre"));
                            e.setCategorieSport(obj.getString("nomCat"));
                            e.setId(obj.getInt("id"));
                            e.setLieu(obj.getString("lieu"));
                            e.setDiscription(obj.getString("description"));
                            e.setCapacite(obj.getInt("capacite"));

                            String url="http://wessporitf.eu-4.evennode.com/uploads/users/"+obj.getString("img");
                            Glide.with(getApplicationContext()).load(url).into(creatorImg);


                            String userName=obj.getString("prenom")+" "+obj.getString("nom");
                            creatorName.setText(userName);

                            eventDate.setText(e.getDate_debut());
                            eventName.setText(e.getTitre());
                            eventAdr.setText(e.getLieu());
                            String parts=obj.getInt("nbrP")+"/"+e.getCapacite();
                            eventNbrParticipant.setText(parts);
                            eventDesc.setText(e.getDiscription());

                           String urlE="http://wessporitf.eu-4.evennode.com/uploads/events/"+e.getImg_event();
                            Glide.with(getApplicationContext()).load(urlE).into(eventImg);




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

                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    System.out.println(t.getStackTrace());
                }
            });

        }
    }
    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {

        String imgUrl;
        ImageView imgV;

        public  DownLoadImageTask(String url,ImageView iv)
        {
            imgUrl=url;
            imgV=iv;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            Bitmap logo = null;
            try{

                InputStream is = new URL(imgUrl).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);

                is.close();


            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            //  imageView.setImageBitmap(result);
            //  imgBmp=result;

            imgV.setImageBitmap(result);



        }
    }

}