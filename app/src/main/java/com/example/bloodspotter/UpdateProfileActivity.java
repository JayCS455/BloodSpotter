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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bloodspotter.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateProfileActivity extends AppCompatActivity {

    TextView donor_name,donor_email;
    EditText donor_contact,donor_dob;
    Spinner donor_bloodgroup,donor_city,donor_state,donor_country;
    Button btn_update;
    String name ,email,id,userId,contact_no,country,str_state,city,dob,blood_group;
    String st;

    SharedPreferences profile_data;


    private static final String KEY_STATE = "state";
    private static final String KEY_CITIES = "cities";
    private ProgressDialog pDialog;
    private String cities_url = "http://technocometsolutions.in/BloodSpotter/city.php";
    DatabaseReference databaseReference;
    DatePickerDialog.OnDateSetListener dateSetListener;


    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);

        //Validation for field.
        awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);


        donor_name = (TextView) findViewById(R.id.txt_donor_name);
        donor_email = (TextView) findViewById(R.id.txt_donor_email);
        donor_contact = (EditText) findViewById(R.id.edt_donor_conatct);
        donor_city = (Spinner) findViewById(R.id.edt_donor_city);
        donor_state = (Spinner) findViewById(R.id.edt_donor_state);
        donor_country = (Spinner) findViewById(R.id.edt_donor_country);
        donor_dob = (EditText) findViewById(R.id.edt_donor_dob);
        donor_bloodgroup = (Spinner) findViewById(R.id.edt_donor_bloodgroup);
        btn_update = findViewById(R.id.btn_update);


        //show calender dialog..................
        donor_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day= calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog dialog= new DatePickerDialog(UpdateProfileActivity.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,dateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        //Store the User's Date of Birth
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month= month+1;
                Log.d("HealthActivity", "onDateset : mm/dd/yyyy : " + dayOfMonth + "-" +month+ "-" +year);

                String date= dayOfMonth + "-" +month+ "-" +year;

                donor_dob.setText(date);
            }
        };


        displayLoader();

        //for load the State and City details..........
        loadStateCityDetails();

        id = prefs.getString("id","");
        name= prefs.getString("first_name","");
        email= prefs.getString("email","");
        donor_name.setText(name);
        donor_email.setText(email);

        profile_data= getSharedPreferences("profile_data", Context.MODE_PRIVATE);
        donor_contact.setText(String.valueOf(profile_data.getString("contact_no","")));
        donor_dob.setText(String.valueOf(profile_data.getString("dob","")));


        //If user want to edit details ............
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                name = donor_name.getText().toString().trim();
                email = donor_email.getText().toString().trim();
                contact_no = donor_contact.getText().toString().trim();
                city = donor_city.getSelectedItem().toString();
                str_state = st;
                country = donor_country.getSelectedItem().toString();
                dob = donor_dob.getText().toString().trim();
                blood_group = donor_bloodgroup.getSelectedItem().toString();


                String MobilePattern = "[0-9]{10}";


                awesomeValidation.addValidation(donor_contact, RegexTemplate.NOT_EMPTY,"Contact should not be Empty");
                awesomeValidation.addValidation(donor_contact, MobilePattern,"Contact Must have 10 number");

                awesomeValidation.addValidation(donor_dob, RegexTemplate.NOT_EMPTY,"Date Of Birth should not be Empty");

                if(awesomeValidation.validate()) {
                    //Retrieve an instance of your database using getInstance() and reference the location you want to write to..
                    databaseReference = FirebaseDatabase.getInstance().getReference("Blood Donor's");

                    //Edit Details by User id....
                    databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!TextUtils.isEmpty(name)) {

                                profile_data=getSharedPreferences("mypref",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor= profile_data.edit();
                                editor.putString("name",name);
                                editor.putString("email",email);
                                editor.putString("contact_no",contact_no);
                                editor.putString("city",city);
                                editor.putString("state",str_state);
                                editor.putString("country",country);
                                editor.putString("dob",dob);
                                editor.putString("blood_group",blood_group);

                                editor.apply();

                                //Update data......
                                updateArtist(id, name,email,contact_no ,country,st,city,dob,blood_group);
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean updateArtist( String id, String name, String email, String contact_no, String country, String str_state, String city, String dob, String blood_group) {
        //getting the specified user reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Blood Donor's").child(id);

        //updating user
        User user = new User(id, name,email,contact_no ,country,str_state,city,dob,blood_group );
        dR.setValue(user);
       // Toast.makeText(getApplicationContext(), "Data Updated"+str_state, Toast.LENGTH_LONG).show();

        //If user profile edit Successfully then rediect to main page.....
        Intent intent = new Intent(UpdateProfileActivity.this,MainActivity.class);
        startActivity(intent);
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(UpdateProfileActivity.this);
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
                            final StateAdapter stateAdapter = new StateAdapter(UpdateProfileActivity.this, R.layout.state_list, R.id.spinnerText, statesList);
                            donor_state.setAdapter(stateAdapter);

                            donor_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    //Populate City list to the second spinner when
                                    // a state is chosen from the first spinner
                                    States cityDetails = stateAdapter.getItem(position);
                                    List<String> cityList = cityDetails.getCities();
                                    ArrayAdapter citiesAdapter = new ArrayAdapter<>(UpdateProfileActivity.this, R.layout.city_list, R.id.citySpinnerText, cityList);
                                    donor_city.setAdapter(citiesAdapter);
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