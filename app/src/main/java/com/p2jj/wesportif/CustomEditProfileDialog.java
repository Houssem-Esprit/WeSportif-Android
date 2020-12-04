package com.p2jj.wesportif;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.p2jj.wesportif.Activities.Login;
import com.p2jj.wesportif.Activities.MainActivity;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Fragments.ProfileFragment;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomEditProfileDialog extends Dialog implements View.OnClickListener {

    public Context activity;
    public Dialog dialog;

    Button cancel;
    EditText edit_email;
    EditText edit_tel;
    EditText edit_birthday;

    Button submitProf;
    Fragment parent;



    public CustomEditProfileDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);

    }

    public CustomEditProfileDialog(Context a){
        super(a);
        this.activity = a;


    }
    public CustomEditProfileDialog(Context a,Fragment parent){
        super(a);
        this.activity = a;

        this.parent=parent;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_editprofile);

        edit_email=findViewById(R.id.edit_email_input);
        edit_tel=findViewById(R.id.edit_tel_input);
        edit_birthday=findViewById(R.id.edit_birthday_input);
        submitProf=findViewById(R.id.submitEditProfile);


        edit_email.setText(Session.getInstance().getUser().getEmail());
        edit_tel.setText(Session.getInstance().getUser().getNumTelephone()+"");
        edit_birthday.setText(Session.getInstance().getUser().getDate_naissance());

        OnchangeFocus(edit_email);
        OnchangeFocus(edit_tel);
        OnchangeFocus(edit_birthday);

        submitProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail=edit_email.getText().toString();
                String tel=edit_tel.getText().toString();
                String ddn=edit_birthday.getText().toString();
                Call call = RetrofitClient
                        .getInstance()
                        .getApi_service_node()
                        .UpdateUser(Session.getInstance().getUser().getCin(),mail,tel,ddn);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        try {

                            JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                            String msg=jsonResp.getString("userInformations");

                            if(msg.equals("Account updated")){

                                Toast.makeText(activity,"Account updated !",Toast.LENGTH_LONG).show();

                                updateSession();


                                final FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                                /* FragmentTransaction ft = fragmentManager.beginTransaction();

                                ProfileFragment proFragment=new ProfileFragment();

                                ft.detach(parent).attach(proFragment).commit();*/
                                dismiss();
                                fragmentManager.beginTransaction().replace(R.id.drawer_layout,new ProfileFragment()).commit();





                            }else{
                                // mot de passe erroné
                                //afficher suggestion de changement de mot de passe si l'erreur s'affiche 3 fois
                                Toast.makeText(activity,"Error",Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }
    @Override
    public void onClick(View v) {

    }


    public void updateSession()
    {
        Call call = RetrofitClient
                .getInstance()
                .getApi_service_node()
                .GetUserDetails(Session.getInstance().getUser().getCin());
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try {

                    JSONObject jsonResp=new JSONObject(new Gson().toJson(response.body()));
                    String msg=jsonResp.getString("userInformations");

                    if(msg.equals("User retrieved")){
                        JSONObject userJson=jsonResp.getJSONObject("user");

                        User currentUser=new User();
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


    public void OnchangeFocus(EditText ed){

        ed.setBackground(null);
    }

    public void OngetBackFocus(EditText ed){

       ed=new EditText(getContext());
    }
}

