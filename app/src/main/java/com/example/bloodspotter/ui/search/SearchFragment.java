package com.example.bloodspotter.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.bloodspotter.Adapter.UserAdapter;
import com.example.bloodspotter.R;
import com.example.bloodspotter.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    View root;
    ArrayList<User> users;
    private ListView resultList;
    DatabaseReference databaseReference;
    SearchView edt_search;
    UserAdapter userAdapter ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);

        resultList = root.findViewById(R.id.result_list);
        edt_search = root.findViewById(R.id.search_field);

        databaseReference = FirebaseDatabase.getInstance().getReference("Blood Donor's");

        users = new ArrayList<>();


        return  root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(databaseReference != null) {
            //attaching value event listener
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.exists()) {
                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            User user = postSnapshot.getValue(User.class);
                            //adding artist to the list
                            users.add(user);
                        }
                    }

                    //creating adapter
                    UserAdapter userAdapter = new UserAdapter(getActivity(), users);
                    //attaching adapter to the listview
                    resultList.setAdapter(userAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if (edt_search != null)
        {
            edt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }
    public  void search(String str)
    {
        ArrayList<User> userlist = new ArrayList<>();
        for (User object : users)
        {
            if (object.getEmail().toLowerCase().contains((str.toLowerCase())) || object.getCity().toLowerCase().contains((str.toLowerCase())) || object.getBlood_group().toLowerCase().contains((str.toLowerCase())) || object.getName().toLowerCase().contains((str.toLowerCase())) || object.getState().toLowerCase().contains((str.toLowerCase())))
            {
                userlist.add(object);
            }
        }
        UserAdapter userAdapter = new UserAdapter(getActivity(),userlist);
        resultList.setAdapter(userAdapter);

    }
}