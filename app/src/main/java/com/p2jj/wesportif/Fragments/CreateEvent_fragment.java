package com.p2jj.wesportif.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.Login;
import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Activities.SignUpActivity;
import com.p2jj.wesportif.Adapters.DataAdapter;
import com.p2jj.wesportif.CustomListViewDialog;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.CategorieSport;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.User;
import com.p2jj.wesportif.MultiSelectionSpinner;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.widget.SearchView;


import static android.app.Activity.RESULT_OK;


public class CreateEvent_fragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 3;
    private static final int RESULT_LOAD_IMAGE = 999 ;
    DatePickerDialog picker;
    DatePickerDialog picker1;
    TimePickerDialog picker2;
    TimePickerDialog picker3;
    EditText date_debut;
    EditText Heure_debut;
    EditText date_fin;
    EditText Heure_fin;
    Spinner selectSport_Spinner;
    EditText getCoach;
    Context mContext;
    CustomListViewDialog customDialog;
    ImageView Event_img;
    Button Upload_img;
    Button btnCree;
    EditText titre;
    EditText desc;
    EditText capacite;
    EditText lieu;
    TextView userOrganiser;

    List<User> mDataset;
    Bitmap imgToUpload;

    SearchView search;
    DataAdapter dataAdapter;
    List<CategorieSport> sports;
    ArrayAdapter adapter;
    private static final int STORAGE_PERMISSION_CODE = 102;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View viewroot = inflater.inflate(R.layout.fragment_create_event, container, false);
        btnCree=viewroot.findViewById(R.id.btnCreateEvent);
        titre=viewroot.findViewById(R.id.createEventTitle);
        desc=viewroot.findViewById(R.id.createEventDetails);
        capacite=viewroot.findViewById(R.id.createEventCapacite);
        lieu=viewroot.findViewById(R.id.createEventLieu);
        userOrganiser=viewroot.findViewById(R.id.createEventUser);

        String userOrg=Session.getInstance().getUser().getNom()+" "+Session.getInstance().getUser().getPrenom();
        userOrganiser.setText(userOrg);

        retrieveCats(viewroot);


        btnCree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String title=titre.getText().toString();
                String description=desc.getText().toString();
                String cap=capacite.getText().toString();
                String dateDeb=date_debut.getText().toString();
                String dateFin=date_fin.getText().toString();
                String heureDeb=Heure_debut.getText().toString();
                String heureFin=Heure_fin.getText().toString();
                String adr=lieu.getText().toString();
                String lat="36.8371179";
                String lng="10.1821407";
                String img="img.jpg";
               // String coach=getCoach.getText().toString();
                CategorieSport cat=(CategorieSport) selectSport_Spinner.getSelectedItem();

                if(title!="" && description !="" && cap !=""  && dateDeb!="" && dateFin!=""
                        && heureDeb!="" && heureFin!=""&& adr!="" && cat != null && img != "" && lat !="" && lng !=""  ){ }
                    System.out.println("title"+title+"description"+description+"cap"+cap+"dateDeb"+dateDeb+"datefin"+dateFin+"HD"+heureDeb
                    +"HF"+heureFin+"adr"+adr);

                try {


                Call call = RetrofitClient
                        .getInstance()
                        .getApi_service_node()
                        .AddEvent(title,description,Integer.parseInt(cap),adr,lat,lng,dateDeb,dateFin,heureDeb,heureFin,cat.getIdCategorie(),img,Session.getInstance().getUser().getCin());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        try {

                            JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                            String msg=jsonResp.getString("eventInformations");

                            if(msg.equals("Event added")){


                                Toast.makeText(getContext(),"Evenement Crée !",Toast.LENGTH_LONG).show();
                                int eventId=jsonResp.getInt("event");

                                uploadImage(imgToUpload,eventId);

                                startActivity(new Intent(getContext(),MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Erreur",Toast.LENGTH_LONG).show();

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
                }catch (Exception e){
                    System.out.println(e);
                }
                //

            }

        });
        date_debut = viewroot.findViewById(R.id.getDate_debut);
        date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month= cldr.get(Calendar.MONTH);
                int year= cldr.get(Calendar.YEAR);

                // date picker diaglog

                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        date_debut.setText(year+"-"+(month +1)+"-"+dayOfMonth);
                    }
                },year,month,day);

                picker.show();
            }
        });

        date_fin=viewroot.findViewById(R.id.getDate_fin);
        date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr1 = Calendar.getInstance();
                int day =cldr1.get(Calendar.DAY_OF_MONTH);
                 int month = cldr1.get(Calendar.MONTH);
                int year =cldr1.get(Calendar.YEAR);

                // date picker diaglog

                picker1 = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                date_fin.setText(year+"-"+(month +1)+"-"+dayOfMonth);
                            }
                        },year,month,day);

                        picker1.show();

            }
        });



        Heure_debut=viewroot.findViewById(R.id.getHeure_debut);
        Heure_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar timeNow = Calendar.getInstance();

                final int hour = timeNow.get(Calendar.HOUR_OF_DAY);
                final int minut = timeNow.get(Calendar.MINUTE);
                final Boolean is24Hour = true;

                // time picker dialog

                picker2 = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Heure_debut.setText(""+checkDigit(hourOfDay) +":"+ checkDigit(minute));

                            }
                        },hour,minut,is24Hour);

                picker2.show();
            }
        });

        Heure_fin =viewroot.findViewById(R.id.getHeure_fin);
        Heure_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar timeNow = Calendar.getInstance();

                final int hour = timeNow.get(Calendar.HOUR_OF_DAY);
                final int minut = timeNow.get(Calendar.MINUTE);
                final Boolean is24Hour = true;

                // time picker dialog

                picker3 = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Heure_fin.setText(""+checkDigit(hourOfDay) +":"+ checkDigit(minute));

                            }
                        },hour,minut,is24Hour);

                picker3.show();
            }
        });


        // Categorie sport spinner
        // custom spinner



        getCoach = viewroot.findViewById(R.id.createEvent_invitCoach);
        getCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataset = new ArrayList<>();

                getCoachs();

                dataAdapter = new DataAdapter(getActivity(),mDataset,this);
                customDialog = new CustomListViewDialog(getContext(), dataAdapter);

                customDialog.show();
                Window window = customDialog.getWindow();
                window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

                customDialog.setCanceledOnTouchOutside(true);
            }
        });

        // Upload image for event

        Event_img = viewroot.findViewById(R.id.eventImg_toUpload);
        Upload_img = viewroot.findViewById(R.id.Upload_img);

        Upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickFromGallery();
            }
        });




        return  viewroot;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }



    private void pickFromGallery() {

        //---------------------
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(getContext(), galleryPermissions)) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESULT_LOAD_IMAGE);
            } else {
                //Create an Intent with action as ACTION_PICK
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        } else {
            EasyPermissions.requestPermissions(getActivity(), "Access for storage",
                    101, galleryPermissions);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            File imgFile = new  File(picturePath);
            if(imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                Event_img.setImageBitmap(bitmap);
                imgToUpload=bitmap;
            }
        }

    }

    public void getCoachs()
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetCoachs();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("userInformations");

                    if(msg.equals("coachs retrieved")){

                        JSONArray coachs=jsonResp.getJSONArray("users");

                        for (int i = 0 ; i < coachs.length(); i++) {
                            JSONObject userJson = coachs.getJSONObject(i);
                            User e=new User();

                            String cin = userJson.getString("cin");
                            String nom=userJson.getString("nom");
                            String prenom=userJson.getString("prenom");
                            String email=userJson.getString("email");
                            int numTel=userJson.getInt("numTel");
                            String ddn=userJson.getString("ddn");
                            String img=userJson.getString("img");
                            String imgCov=userJson.getString("coverImg");
                            String role=userJson.getString("role");

                            e.setCin(cin);
                            e.setNom(nom);
                            e.setPrenom(prenom);
                            e.setEmail(email);
                            e.setNumTelephone(numTel);
                            e.setDate_naissance(ddn);
                            e.setImg_user(img);
                            e.setRole(role);
                            e.setCoverImg(imgCov);

                            mDataset.add(e);

                        }



                    }
                    else {

                        Toast.makeText(getContext(),"Erreur",Toast.LENGTH_LONG).show();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage(Bitmap mBitmap,int eventId) {
        try {

            if (mBitmap != null) {
                File filesDir = getContext().getFilesDir();
                File file = new File(filesDir, "image" + ".png");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();


                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");


                Call call = RetrofitClient
                        .getInstance()
                        .getApi_service_node()
                        .uploadImageEvent(body, name, eventId);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        try {

                            //JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));

                            Toast.makeText(getContext(), "Image uploaded", Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                        Toast.makeText(getContext(), "Erreur uploading Image", Toast.LENGTH_LONG).show();
                    }
                });


            }
        }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retrieveCats(final View viewroot)
    {

        sports=new ArrayList<>();

        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetCats();
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("catInformations");
                    if(msg.equals("Categories retrieved"))
                    {


                        JSONArray evts=jsonResp.getJSONArray("cats");

                        for (int i = 0 ; i < evts.length(); i++) {
                            JSONObject obj = evts.getJSONObject(i);
                            CategorieSport cat=new CategorieSport();
                            cat.setIdCategorie(obj.getInt("idCat"));
                            cat.setNameSport(obj.getString("nom"));

                            sports.add(cat);
                        }

                        adapter = new ArrayAdapter<>(getContext(), R.layout.event_spinner_selected_item_text_color, sports);
                        adapter.setDropDownViewResource(R.layout.event_spinner_dropdawn_text_color);
                        selectSport_Spinner=viewroot.findViewById(R.id.Spinner_create_event);
                        selectSport_Spinner.setAdapter(adapter);
/*
                        selectSport_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                CategorieSport cat=(CategorieSport)parent.getItemAtPosition(position);

                                Toast.makeText(getContext(),cat.getIdCategorie()+"",Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
*/
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
            }
        });


    }




}
