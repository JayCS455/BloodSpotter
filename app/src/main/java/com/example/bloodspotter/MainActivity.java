package com.example.bloodspotter;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    private TextView userName,userEmail;
    String  name,email,id;
    private ImageView profileImage;
    SharedPreferences prefs;
    DrawerLayout drawer;
    TextView share,rate,privacy;
    Button logoutbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         prefs = getSharedPreferences("mypref", MODE_PRIVATE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_search, R.id.nav_profile, R.id.nav_facts,
                R.id.nav_info)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);


        logoutbtn = headerView.findViewById(R.id.btn_logout);
        userName = (TextView) headerView.findViewById(R.id.name);
        userEmail = (TextView) headerView.findViewById(R.id.email);
        profileImage = (ImageView) headerView.findViewById(R.id.profileImage);


        id = prefs.getString("id","");
        name= prefs.getString("first_name","");
        email= prefs.getString("email","");
        userName.setText(name);
        userEmail.setText(email);


        //logout botton .............
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear the user data...........
                SharedPreferences.Editor editor =prefs.edit();
                editor.putBoolean("firsttime",false);
                editor.putString("name", "");
                editor.putString("email", "");
                editor.apply();


                //Redirect  to the Login page...........
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();  // This call is missing.
            }
        });


        share  = navigationView.findViewById(R.id.txtshare);
        rate  = navigationView.findViewById(R.id.txtrate);
        privacy = navigationView.findViewById(R.id.txtprivacy);

        //share appp......
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });

        //rate us..........
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUs();
            }
        });

        //privacy policy........
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyPolicy();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public  void shareApp()
    {
        try {
            String shareText = getResources().getString(R.string.share);
            Intent sendIntent = new Intent();
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText + "\nhttps://play.google.com/store/apps/details?id=com.bloodspotterapp.bloodspotter");
            sendIntent.setType("text/plain");
            Intent chooseIntent = Intent.createChooser(sendIntent, "Share this via");
            startActivity(chooseIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void  rateUs()
    {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://play.google.com/store/apps/details?id=https://play.google.com/store/apps/details?id=com.bloodspotterapp.bloodspotter")));
        }
    }

    public  void  privacyPolicy()
    {
        Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }



}
