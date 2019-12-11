package com.example.bloodspotter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.bloodspotter.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RegistrationActivity extends AppCompatActivity {

    EditText reg_username,reg_useremail,reg_userpass,reg_usercontact,reg_userdob;
    Spinner reg_userbloodgroup,reg_usercity,reg_userstate,reg_usercountry;
    TextView txt_linklogin,txt_code;
    Button btnregister;
    DatabaseReference reference;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    ArrayList<User> userslist = new ArrayList<>();
    String name, email, password, id,contact,bloodgroup,city,st,country,dob,str_state,code;
    SharedPreferences sharedPreferences;
    DatePickerDialog.OnDateSetListener dateSetListener;
    AwesomeValidation awesomeValidation;

    FirebaseAuth authenticationLis ;


    private static final String KEY_STATE = "state";
    private static final String KEY_CITIES = "cities";
    private ProgressDialog pDialog;
    private String cities_url = "http://technocometsolutions.in/BloodSpotter/city.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Validation for field.
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);
        awesomeValidation.setColor(Color.YELLOW);

        //shareprefrence for get login value ..
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        //sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);

        txt_linklogin = findViewById(R.id.txt_linklogin);
        reg_username = findViewById(R.id.reg_username);
        reg_useremail = findViewById(R.id.reg_useremail);
        reg_userpass = findViewById(R.id.reg_userpass);
        reg_usercontact = findViewById(R.id.reg_userconatct);
        reg_userbloodgroup = findViewById(R.id.reg_userbloodgroup);
        reg_usercity = findViewById(R.id.reg_usercity);
        reg_userstate = findViewById(R.id.reg_userstate);
        reg_usercountry = findViewById(R.id.reg_usercountry);
        reg_userdob = findViewById(R.id.reg_userdob);
        txt_code = findViewById(R.id.txt_code);

        btnregister = findViewById(R.id.btn_SignUp);

        Random r = new Random();
        String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));
        txt_code.setText(randomNumber);

        //Retrieve an instance of your database using getInstance() and reference the location you want to write to..
        reference = FirebaseDatabase.getInstance().getReference().child("Blood Donor's");


        //reference = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);



        //show calender dialog..................
        reg_userdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog= new DatePickerDialog(RegistrationActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        //Store the User's Date of Birth
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                Log.d("DOB", "onDateset : mm/dd/yyyy : " + dayOfMonth + "-" +month+ "-" +year);

                String date= dayOfMonth + "-" +month+ "-" +year;

                reg_userdob.setText(date);
            }
        };



        new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user!= null)
                {
                    SendVerificationEmail();
                    Toast.makeText(RegistrationActivity.this, "SendVerificationEmail!!!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegistrationActivity.this, "failed!!!!!", Toast.LENGTH_SHORT).show();
                }

            }
        };



        displayLoader();

        //for load the State and City details..........
        loadStateCityDetails();


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //clear userlist..............
                userslist.clear();

                name = reg_username.getText().toString().trim();
                email = reg_useremail.getText().toString().trim();
                password = reg_userpass.getText().toString().trim();
                contact = reg_usercontact.getText().toString().trim();
                city = reg_usercity.getSelectedItem().toString();
                str_state = st;
                country = reg_usercountry.getSelectedItem().toString();
                dob = reg_userdob.getText().toString().trim();
                bloodgroup = reg_userbloodgroup.getSelectedItem().toString();
                code = txt_code.getText().toString().trim();


                String MobilePattern = "[0-9]{10}";



                awesomeValidation.addValidation(RegistrationActivity.this, R.id.reg_username, "[a-zA-Z\\s]+", R.string.err_name);
                //awesomeValidation.addValidation(reg_username, RegexTemplate.NOT_EMPTY,"Contact should not be Empty");

                awesomeValidation.addValidation(reg_useremail, RegexTemplate.NOT_EMPTY,"Email should not be Empty");


                awesomeValidation.addValidation(reg_userpass, RegexTemplate.NOT_EMPTY,"Password should not be Empty");

                awesomeValidation.addValidation(reg_usercontact, RegexTemplate.NOT_EMPTY,"Contact should not be Empty");
                awesomeValidation.addValidation(reg_usercontact, MobilePattern,"Contact Must have 10 number");

                awesomeValidation.addValidation(reg_userdob, RegexTemplate.NOT_EMPTY,"Date of Birth should not be Empty");


                //check the Validation of field...............
                if(awesomeValidation.validate()) {
                    //store  logging user  deatils....
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("islogindone", true);
                    editor.putString("first_name", name);
                    editor.putString("email", email);
                    editor.putString("contact_no", ""+contact);
                    editor.putString("country", country);
                    editor.putString("state", st);
                    editor.putString("city", city);
                    editor.putString("dob", dob);
                    editor.putString("blood_group", bloodgroup);
                    editor.apply();
                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    if (!task.isSuccessful()) {
                                      /*  Toast.makeText(RegistrationActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show()*/;
                                    } else {

                                        // check for Email verified or not.....
                                        checkIfEmailVerified();
                                        // Toast.makeText(RegisterActivity.this, users.toString().trim(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                    id = reference.push().getKey();
                    User users = new User(id, name, email, password, contact, country, st, city, dob, bloodgroup,code);
                    reference.child(id).setValue(users);

                }
                else
                {
                    Toast.makeText(RegistrationActivity.this, "Please Fill All Fields Correctly!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txt_linklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
    }


    // check for Email verified or not.....
    private void checkIfEmailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            /* Toast.makeText(RegistrationActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();*/
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.


            //send verification email......
            SendVerificationEmail();
            //restart this activity

        }

    }


    //send verification email......
    private  void SendVerificationEmail()
    {

        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        findViewById(R.id.btn_SignUp).setEnabled(true);

                        if (task.isSuccessful()) {
                          /*  Toast.makeText(RegistrationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();*/

                            Intent in = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(in);
                        } else {

                            Log.d("TAG", "sendEmailVerification"+task.getException());
                         /*   Toast.makeText(RegistrationActivity.this,
                                    "Failed to send verification email." +  task.getException(),
                                    Toast.LENGTH_SHORT).show();*/
                        }
                    }
                });


    }


    private void displayLoader() {
        pDialog = new ProgressDialog(RegistrationActivity.this);
        pDialog.setMessage("Loading Data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Helps in downloading the state and city details
     * and populating the spinner
     */
    private void loadStateCityDetails() {
        final List<States> statesList = new ArrayList<>();
        final List<String> states = new ArrayList<>();
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, cities_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
                        try {
                            //Parse the JSON response array by iterating over it
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                String state = response.getString(KEY_STATE);
                                JSONArray cities = response.getJSONArray(KEY_CITIES);
                                List<String> citiesList = new ArrayList<>();
                                for (int j = 0; j < cities.length(); j++) {
                                    citiesList.add(cities.getString(j));
                                }
                                statesList.add(new States(state, citiesList));
                                states.add(state);

                            }
                            final StateAdapter stateAdapter = new StateAdapter(RegistrationActivity.this, R.layout.state_list, R.id.spinnerText, statesList);
                            reg_userstate.setAdapter(stateAdapter);

                            reg_userstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    //Populate City list to the second spinner when
                                    // a state is chosen from the first spinner
                                    States cityDetails = stateAdapter.getItem(position);
                                    List<String> cityList = cityDetails.getCities();
                                    ArrayAdapter citiesAdapter = new ArrayAdapter<>(RegistrationActivity.this, R.layout.city_list, R.id.citySpinnerText, cityList);
                                    reg_usercity.setAdapter(citiesAdapter);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }


    public class States {
        private String stateName;
        private List<String> cities;

        public States(String stateName, List<String> cities) {
            this.stateName = stateName;
            this.cities = cities;
        }

        public String getStateName() {
            return stateName;
        }

        public List<String> getCities() {
            return cities;
        }

    }

    public class StateAdapter extends ArrayAdapter<States> {
        private List<States> stateList = new ArrayList<>();

        StateAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<States> stateList) {
            super(context, resource, spinnerText, stateList);
            this.stateList = stateList;
        }

        @Override
        public States getItem(int position) {
            return stateList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position);

        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return initView(position);
        }

        /**
         * Gets the state object by calling getItem and
         * Sets the state name to the drop-down TextView.
         *
         * @param position the position of the item selected
         * @return returns the updated View
         */
        private View initView(int position) {
            States state = getItem(position);
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.state_list, null);
            TextView textView =  v.findViewById(R.id.spinnerText);
            textView.setText(state.getStateName());
            st = state.getStateName();
            return v;

        }
    }
}