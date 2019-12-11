package com.example.bloodspotter.ui.profile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloodspotter.LoginActivity;
import com.example.bloodspotter.R;
import com.example.bloodspotter.UpdateProfileActivity;
import com.example.bloodspotter.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment  {

    Button btn_update_link;
    TextView user_name,user_email,user_conatct,user_country,user_state,user_city,user_dob,user_blood_group;
    DatabaseReference databaseReference;

    String first_name,email,id;
    SharedPreferences profile_data;
    Dialog dialog;





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("mypref", MODE_PRIVATE);


        user_name = (TextView) root.findViewById(R.id.txt_user_name);
        user_email = (TextView) root.findViewById(R.id.txt_user_email);
        user_conatct = (TextView) root.findViewById(R.id.txt_user_contact);
        user_country = (TextView) root.findViewById(R.id.txt_user_country);
        user_state = (TextView) root.findViewById(R.id.txt_user_state);
        user_city = (TextView) root.findViewById(R.id.txt_user_city);
        user_dob = (TextView) root.findViewById(R.id.txt_user_dob);
        user_blood_group = (TextView) root.findViewById(R.id.txt_user_blood_group);



        id = prefs.getString("id","");
        first_name= prefs.getString("first_name","");
        email= prefs.getString("email","");



        profile_data = getContext().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        user_name.setText(first_name);
        user_email.setText(email);
        user_conatct.setText(""+String.valueOf(profile_data.getString("contact_no", "")));
            user_country.setText(String.valueOf(profile_data.getString("country", "")));
            user_state.setText(String.valueOf(profile_data.getString("state", "")));
            user_city.setText(String.valueOf(profile_data.getString("city", "")));
            user_dob.setText(String.valueOf(profile_data.getString("dob", "")));
            user_blood_group.setText(String.valueOf(profile_data.getString("blood_group", "")));


            databaseReference = FirebaseDatabase.getInstance().getReference("Blood Donor's");

            databaseReference.orderByChild("id").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        User user = datas.getValue(User.class);
                        /*user_name.setText(user.getName());
                        user_email.setText(user.getEmail());
                        user_conatct.setText(user.getContact_no());*/
                        /*user_country.setText(String.valueOf(profile_data.getString("country", "")));
                        user_state.setText(String.valueOf(profile_data.getString("state", "")));
                        user_city.setText(String.valueOf(profile_data.getString("city", "")));
                        user_dob.setText(String.valueOf(profile_data.getString("dob", "")));
                        user_blood_group.setText(String.valueOf(profile_data.getString("blood_group", "")));*/
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });


        btn_update_link = root.findViewById(R.id.btn_update_link);

        btn_update_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.equals(""))
                {
                    dialog = new Dialog(getContext());
                    dialog.requestWindowFeature(1);
                    dialog.setContentView(R.layout.my_dialog);
                    dialog.setCancelable(true);

                    dialog.show();
					((ImageView) dialog.findViewById(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    
                    ((Button) dialog.findViewById(R.id.btn_link_login)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            getActivity().startActivity(intent);
                        }
                    });
                }
                else {
                    Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        return root;
    }


}