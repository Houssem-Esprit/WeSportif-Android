package com.p2jj.wesportif.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.p2jj.wesportif.Fragments.CoachFragment;
import com.p2jj.wesportif.Fragments.MemberFragment;
import com.p2jj.wesportif.R;

public class SignUpActivity extends AppCompatActivity {

    Spinner typecompte ;
    Fragment compteMembre;
    Fragment compteCoach;
    TextView toSignup;
    private static final int GALLERY_REQUEST_CODE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        typecompte = findViewById(R.id.typecompte);
        compteMembre = new MemberFragment();
        compteCoach =new CoachFragment();
        toSignup=findViewById(R.id.ToSignup);
        toSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        ArrayAdapter<String> adapter=new ArrayAdapter<>(SignUpActivity.this,R.layout.custom_spinner,
                getResources().getStringArray(R.array.fragments));
        adapter.setDropDownViewResource(R.layout.custom_spinner_two);
        typecompte.setAdapter(adapter);

        typecompte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View v= findViewById(R.id.signup_container);
                switch(position){
                    case 0:
                        setFragment(compteMembre);
                        v.setBackgroundColor(Color.parseColor("#FF5B61"));
                        break;
                    case 1:
                        setFragment(compteCoach);
                        v.setBackgroundColor(Color.parseColor("#10948f"));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    public void onLoginClick(View v)
    {
        startActivity(new Intent(this,WelcomeActivity.class));
    }

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.SignUp_mainframe,fragment);
        fragmentTransaction.commit();

    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }



    }
