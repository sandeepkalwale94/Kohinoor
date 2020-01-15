package com.kohinoor.sandy.kohinnor.Fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kohinoor.sandy.kohinnor.R;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private LinearLayout mContactAddressButton;
    private LinearLayout mContactEmailButton;
    private LinearLayout mContactMobNumberButton;

    private TextView mContactAddress;
    private TextView mContactEmail;
    private TextView mContactMobNumber;

    private ImageView mFacebook;
    private ImageView mTelegram;
    private ImageView mYoutube;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_contact, container, false);

        mContactMobNumberButton = (LinearLayout) view.findViewById(R.id.mobnumber);
        mContactEmailButton = (LinearLayout) view.findViewById(R.id.email);
        mContactAddressButton = (LinearLayout) view.findViewById(R.id.address);

        mContactMobNumber = (TextView) view.findViewById(R.id.mobnumbertext);
        mContactEmail = (TextView) view.findViewById(R.id.emailtext);
        mContactAddress = (TextView) view.findViewById(R.id.addresstext);

        mFacebook = (ImageView) view.findViewById(R.id.fb);
        mTelegram = (ImageView) view.findViewById(R.id.tele);
        mYoutube = (ImageView) view.findViewById(R.id.youtube);

        final String mobNumber = mContactMobNumber.getText().toString();
        final double latitude = 19.2609958; //Need to find the exact latLon
        final double longitude = 76.776665; //Need to find the exact latLon
        final String label = "Kohinoor Academy";
        mContactAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f (%s)", latitude, longitude,latitude, longitude, label);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException ex)
                {
                    Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                }


            }
        });
        mContactEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO,Uri.fromParts(
                        "mailto","kanguleram88@gmail.com", null));
                //i.setType("message/rfc822");
              //  i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kanguleram88@gmail.com"});
              //  i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
             //   i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mContactMobNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobNumber));
                startActivity(intent);
            }
        });

        final String pageId = "1347480401964190"; //Need to replace page Id with Kohinoor academy
        mFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + pageId));
                    startActivity(intent);
                }

            }
        });

        mTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // http://www.telegram.me/kohinooracademy
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/kohinooracademy"));
                startActivity(intent);
            }
        });

      //  final String channel = "Sandeep Kalwale";
        mYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/c/FadooTricks"));
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/c/FadooTricks")));
                }
            }
        });

        return view;
    }

}
