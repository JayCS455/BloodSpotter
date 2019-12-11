package com.example.bloodspotter.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bloodspotter.R;
import com.example.bloodspotter.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private Activity context;
    ArrayList<User> users;

    public UserAdapter(Activity context, ArrayList<User> users) {
        super(context, R.layout.layout_user_list, users);
        this.context = context;
        this.users = users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.txt_name);
        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.txt_email);
        TextView textViewBloodGroup = (TextView) listViewItem.findViewById(R.id.txt_blood_group);
        TextView textViewContact= (TextView) listViewItem.findViewById(R.id.txt_contact);
        TextView textViewState= (TextView) listViewItem.findViewById(R.id.txt_state);
        TextView textViewCity= (TextView) listViewItem.findViewById(R.id.txt_user_city);

        User user = users.get(position);
        textViewName.setText(user.getName());
        textViewEmail.setText(user.getEmail());
        textViewContact.setText(users.get(position).getContact_no());
        textViewBloodGroup.setText(users.get(position).getBlood_group());
        textViewCity.setText(users.get(position).getState());
        textViewState.setText(users.get(position).getCity());

        return listViewItem;
    }
}