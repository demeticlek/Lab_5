package com.example.lab_6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class DetailsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle args = getArguments();

        if (args != null) {
            TextView nameValue = view.findViewById(R.id.nameValue);
            TextView heightValue = view.findViewById(R.id.heightValue);
            TextView massValue = view.findViewById(R.id.massValue);
            TextView birthYearValue = view.findViewById(R.id.birthYearValue);

            nameValue.setText(args.getString("name", ""));
            heightValue.setText(args.getString("height", ""));
            massValue.setText(args.getString("mass", ""));
            birthYearValue.setText(args.getString("birth_year", ""));
        }

        return view;
    }

}