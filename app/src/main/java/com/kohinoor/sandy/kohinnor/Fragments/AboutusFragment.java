package com.kohinoor.sandy.kohinnor.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutusFragment extends Fragment {

    private TextView mAboutusText;


    public AboutusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view =  inflater.inflate(R.layout.fragment_aboutus, container, false);

        mAboutusText = view.findViewById(R.id.aboutustext);

        mAboutusText.setText(R.string.AboutUsText);

        return view;

    }

}
