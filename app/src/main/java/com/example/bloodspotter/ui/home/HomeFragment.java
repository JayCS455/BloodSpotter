package com.example.bloodspotter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.bloodspotter.R;
import com.example.bloodspotter.TabBloodBankFragment;
import com.example.bloodspotter.TabDonnerFragment;
public class HomeFragment extends Fragment {

    View root;
    Button btn_donner,btn_blood_bank;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);


        btn_donner = root.findViewById(R.id.btn_donner);
        btn_donner.setActivated(true);
        btn_blood_bank = root.findViewById(R.id.btn_blood_bank);



        //Click Event for donor's and blood bank .....
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                if (view == view.findViewById(R.id.btn_donner))
                {
                    fragment = new TabDonnerFragment();
                }
                else
                {
                    fragment = new TabBloodBankFragment();
                }
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_content, fragment);
                transaction.commit();
            }
        };
        btn_donner.setOnClickListener(listener);
        btn_blood_bank.setOnClickListener(listener);

        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Fragment fragment = null;
        fragment = new TabDonnerFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.commit();
    }
}