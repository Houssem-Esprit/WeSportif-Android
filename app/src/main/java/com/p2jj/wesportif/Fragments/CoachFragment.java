package com.p2jj.wesportif.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.Login;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Model.CategorieSport;
import com.p2jj.wesportif.MultiSelectionSpinner;
import com.p2jj.wesportif.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CoachFragment extends Fragment {

    EditText cin;
    EditText nom;
    EditText prenom;
    EditText login;
    EditText email;
    EditText password;
    EditText adresse;
    EditText tel;
    EditText dateness;
    Button insrie;

    List<CategorieSport> sports;
    ArrayAdapter adapter;
    Spinner selectSport_Spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView= inflater.inflate(R.layout.fragment_coach, container, false);

        cin=rootView.findViewById(R.id.cin_coach);
        nom=rootView.findViewById(R.id.nom_coach);
        prenom=rootView.findViewById(R.id.prenom_coach);
        login=rootView.findViewById(R.id.loginName_coach);
        password=rootView.findViewById(R.id.password_coach);
        email=rootView.findViewById(R.id.email_coach);
        adresse=rootView.findViewById(R.id.adresse_coach);
        tel=rootView.findViewById(R.id.tel_coach);
        dateness=rootView.findViewById(R.id.datenes_coach);
        selectSport_Spinner=rootView.findViewById(R.id.Spinner_signup_coach);
        insrie=rootView.findViewById(R.id.inscrir_coach);

        insrie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategorieSport cp=(CategorieSport) selectSport_Spinner.getSelectedItem();

                String cinM = cin.getText().toString();
                String nomM = nom.getText().toString();
                System.out.println(nomM);
                String prenomM = prenom.getText().toString();
                String loginM = login.getText().toString();
                String passM = password.getText().toString();
                String emailM = email.getText().toString();
                int telM = Integer.parseInt(tel.getText().toString());
                Date datensM = null;
                try {
                    datensM = new SimpleDateFormat("yyyy-dd-MM").parse(dateness.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {


                Call call = RetrofitClient
                        .getInstance()
                        .getApi_service_node()
                        .Signup(cinM, nomM, prenomM, loginM, passM, emailM, 1, "img.jpg", telM, dateness.getText().toString(),catToJson(cp));
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        try {

                            JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                            String msg=jsonResp.getString("userInformations");
                            if(msg.equals("User added")){

                                Toast.makeText(getContext(),"compte crée avec succes !",Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getContext(), Login.class);
                                startActivity(i);
                            }
                            else if(msg.equals("cin exists")){

                                // pop go to login screen or change cin
                            }else{

                                Toast.makeText(getContext(),"nom utilisateur exist deja !",Toast.LENGTH_LONG).show();
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
                //
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });



            retrieveCats(rootView);

        return rootView;
    }

    public JSONArray catToJson(CategorieSport cp)
    {
        JSONArray resp= new JSONArray();
       try {
                JSONObject it=new JSONObject();

                it.put("idCat",cp.getIdCategorie());
                resp.put(it);

            }
            catch(JSONException ex)
            {
                System.out.println(ex.getStackTrace());
            }


        return resp;
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

                        selectSport_Spinner=viewroot.findViewById(R.id.Spinner_signup_coach);
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
