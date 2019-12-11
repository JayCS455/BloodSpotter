package com.example.bloodspotter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bloodspotter.Adapter.UserAdapter;
import com.example.bloodspotter.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public  class TabDonnerFragment extends Fragment {

    DatabaseReference databaseReference;
    ArrayList<User> users;
    ListView listview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_donner, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("Blood Donor's");
        listview = root.findViewById(R.id.listview_donners);

        users = new ArrayList<>();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        //attaching value event listener
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    User user = postSnapshot.getValue(User.class);
                    //adding artist to the list
                    users.add(user);
                }

                //creating adapter
                UserAdapter userAdapter = new UserAdapter(getActivity(), users);
                //attaching adapter to the listview
                listview.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}