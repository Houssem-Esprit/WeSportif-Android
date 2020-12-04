package com.p2jj.wesportif.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.SignUpActivity;
import com.p2jj.wesportif.Model.CategorieSport;
import com.p2jj.wesportif.Activities.Login;
import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.MultiSelectionSpinner;
import com.p2jj.wesportif.R;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MemberFragment extends Fragment {

    EditText cin;
    EditText nom;
    EditText prenom;
    EditText login;
    EditText email;
    EditText password;
    EditText adresse;
    EditText tel;
    EditText dateness;
    MultiSelectionSpinner mySpinner;
    Button insrie;
    ArrayList<CategorieSport> selectedItems;
    ArrayList<CategorieSport> items;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.fragment_member_signup,container,false);
        cin=rootView.findViewById(R.id.cin);
        nom=rootView.findViewById(R.id.nom);
        prenom=rootView.findViewById(R.id.prenom);
        login=rootView.findViewById(R.id.loginName);
        password=rootView.findViewById(R.id.password);
        email=rootView.findViewById(R.id.email);
        adresse=rootView.findViewById(R.id.adresse);
        tel=rootView.findViewById(R.id.tel);
        dateness=rootView.findViewById(R.id.datenes);
        mySpinner=rootView.findViewById(R.id.spn_items);
        insrie=rootView.findViewById(R.id.inscrir);

        retrieveCats(rootView);





        insrie.setOnClickListener(iscrieClickListener);


        return rootView;
    }


    private final View.OnClickListener iscrieClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            selectedItems = mySpinner.getSelectedItems();

            String cinM = cin.getText().toString();
            System.out.println(cinM);
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
                    .Signup(cinM, nomM, prenomM, loginM, passM, emailM, 0, "img.jpg", telM, dateness.getText().toString(),catArrayToJson(selectedItems));
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

            }catch (Exception e){
                System.out.println(e);
            }

            //


        }
    };


    public JSONArray catArrayToJson(ArrayList<CategorieSport> items)
    {
        JSONArray resp= new JSONArray();
        for(int i=0;i<items.size();i++)
        {
            try {
                JSONObject it=new JSONObject();

                it.put("idCat",items.get(i).getIdCategorie());
                resp.put(it);

            }
            catch(JSONException ex)
            {
                System.out.println(ex.getStackTrace());
            }

        }
        System.out.println(resp);
        return resp;
    }
    public void retrieveCats(final View rootView)
    {

        items=new ArrayList<>();


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

                        items.add(cat);
                    }
                    mySpinner = (MultiSelectionSpinner) rootView.findViewById(R.id.spn_items);
                    mySpinner.setItems(items);

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
