package com.example.bloodspotter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lav;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //Set the null value.....
        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        if(prefs.getBoolean("firsttime",true))
        {
            SharedPreferences.Editor editor =prefs.edit();
            editor.putBoolean("firsttime",false);
            editor.putString("name", "");
            editor.putString("email", "");
            editor.apply();
        }



        lav = (LottieAnimationView) findViewById(R.id.lav_splash);
        lav.setProgress(0);
        lav.pauseAnimation();
        lav.playAnimation();
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade); // Create the animation.
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
                        boolean user_id= prefs.getBoolean("islogindone",false);
                        if(user_id)
                        {

                            //If already Login then Redirect to Main Page ..
                            Intent i =new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(i);

                        }
                        else
                        {

                            //If not Login then Redirect to Login Page
                            Intent i =new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(i);
                        }

                        finish();
                    }
                },3000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        lav.startAnimation(anim);
    }
}
