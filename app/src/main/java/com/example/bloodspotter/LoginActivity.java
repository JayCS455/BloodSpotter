package com.example.bloodspotter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.bloodspotter.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity  {


    FirebaseAuth firebaseAuth;
    EditText edt_userEmail, edt_userPass;
    Button btn_login;
    TextView txtregister;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String name ,email, password,id,contact_no,country,state,city,dob,blood_group;
    SharedPreferences profile_data;
    Button btn_skip;
    FirebaseUser firebaseUser;

    // Create an object of Firebase Database Reference
    DatabaseReference reference;

    List<User> user;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Validation for field.
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

        //shareprefrence for get login value ..
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        //Retrieve an instance of your database using getInstance() and reference the location you want to write to..
        reference = FirebaseDatabase.getInstance().getReference("Blood Donor's");
        user = new ArrayList<>();

        btn_skip = findViewById(R.id.btn_skip);



        //For Skip the Login Page...
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("islogindone",true);
                editor.putString("first_name","Hello, user" );
                editor.putString("email","user" );
                editor.putString("id","" );

                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();


     /*   firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }*/

        edt_userEmail = findViewById(R.id.edt_userEmail);
        edt_userPass = findViewById(R.id.edt_userPass);
        txtregister = findViewById(R.id.txt_linkregister);
        btn_login = findViewById(R.id.btn_login);

        progressDialog = new ProgressDialog(this);

        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(in);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = edt_userEmail.getText().toString();
                password = edt_userPass.getText().toString();


                awesomeValidation.addValidation(edt_userEmail, RegexTemplate.NOT_EMPTY,"Email Address should not be Empty");

                awesomeValidation.addValidation(edt_userPass, RegexTemplate.NOT_EMPTY,"Password Field should not be Empty");


                //Check the Validation ......................
                if(awesomeValidation.validate()) {

                    //First check if Email is Verified or not......................
                    checkIfEmailVerified();


                    //firebase auth with email and password....
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    progressDialog.dismiss();
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Toast.makeText(LoginActivity.this, "Email or Password incorrect.", Toast.LENGTH_LONG).show();
                                    } else {


                                        id = reference.push().getKey();
                                        //store  logging user  deatils....
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean("islogindone", true);
                                      //  editor.putString("first_name", name);
                                        editor.putString("email", email);
                                       // editor.putString("id", id);

                                        editor.apply();

                                    }
                                }
                            });
                }
                else
                {
                    FancyToast.makeText(LoginActivity.this, "Email or Password incorrect.!!!!", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.ic_info_outline_black_24dp, false).show();

                }
            }
        });


    }

    //check if Email is Verified or not......................
    private void checkIfEmailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       /* if (!user.isEmailVerified())
        {
           // FancyToast.makeText(this, "Check your mail......", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.ic_info_outline_black_24dp, false).show();
            // user is verified, so you can finish this activity or send user to activity which you want.

        }
        else
        {*/
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            /* FirebaseAuth.getInstance().signOut();
             */

            //restart this activity
            //FancyToast.makeText(this, "Check your mail......", FancyToast.LENGTH_LONG, FancyToast.WARNING, R.drawable.ic_info_outline_black_24dp, false).show();
         //   finish();

            FancyToast.makeText(this, "Successfully logged in", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, R.drawable.ic_done_black_24dp, false).show();

            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(in);
            finish();

       /* }*/

    }




 /*   Intent intent = new Intent(RegistrationActivity.this,EmailVerificationActivity.class);
    startActivity(intent);*/

}
