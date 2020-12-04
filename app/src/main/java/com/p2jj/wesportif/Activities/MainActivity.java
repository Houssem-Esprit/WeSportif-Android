package com.p2jj.wesportif.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.p2jj.wesportif.Adapters.AdaptorEv;
import com.p2jj.wesportif.Adapters.AdaptorEvRecommended;
import com.p2jj.wesportif.Data_Managers.RetrofitClient;
import com.p2jj.wesportif.Fragments.AllEventsFragment;
import com.p2jj.wesportif.Fragments.CreateEvent_fragment;
import com.p2jj.wesportif.Fragments.FavorisEventsFragment;
import com.p2jj.wesportif.Fragments.MesEeventsFragment;
import com.p2jj.wesportif.Fragments.ProfileFragment;

import com.p2jj.wesportif.Model.Event;
import com.p2jj.wesportif.Model.Session;
import com.p2jj.wesportif.R;
import de.hdodenhof.circleimageview.CircleImageView;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    public NavController navController;
    Toolbar toolbar;


    public static final String SHARED_FILE_NAME = "userPreferences";
    SharedPreferences data;
    //ShimmerFrameLayout shimmerFrameLayout ;


    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpNavigation();








        // shimmerFrameLayout =findViewById(R.id.Shimmer_view_container);

//        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,
//                R.string.drawer_open,R.string.drawer_close);
//        mDrawerLayout.addDrawerListener(drawerToggle);
//        drawerToggle.syncState();
    }


    public void setUpNavigation(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this,navController,mDrawerLayout);
        NavigationUI.setupWithNavController(navigationView,navController);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        TextView tvUser=header.findViewById(R.id.drawer_navBar_UserName);
        String name=Session.getInstance().getUser().getPrenom()+" "+Session.getInstance().getUser().getNom();
        tvUser.setText(name);
        CircleImageView civ=header.findViewById(R.id.drawer_navBar_UserImg);
        String url="http://wessporitf.eu-4.evennode.com/uploads/users/"+Session.getInstance().getUser().getImg_user();
        Glide.with(this).load(url).into(civ);
    }

    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.nav_host_fragment), mDrawerLayout);
    }






    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mDrawerLayout.closeDrawers();
        switch (menuItem.getItemId()){

            case R.id.evenements:
                //getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new AllEventsFragment()).commit();
                navController.navigate(R.id.allEventsFragment);
                break;
            case R.id.profile:
              //  getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new ProfileFragment()).commit();
                navController.navigate(R.id.profileFragment);
                break;
            case R.id.mes_event:
                //getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new MesEeventsFragment()).commit();
                navController.navigate(R.id.mesEeventsFragment);
                break;
            case R.id.favoris:
               // getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new FavorisEventsFragment()).commit();
                navController.navigate(R.id.favorisEventsFragment);
                break;
            case R.id.Create_event:
              //  getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout,new CreateEvent_fragment()).commit();
                navController.navigate(R.id.createEvent_fragment);
                break;

            case R.id.deconnecter:
                disUser();
                break;


        }
        return true;
    }
    public void disUser()
    {
        data = getSharedPreferences(SHARED_FILE_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.clear().apply();
        Intent i=new Intent(this,Login.class);
        startActivity(i);
    }




}
