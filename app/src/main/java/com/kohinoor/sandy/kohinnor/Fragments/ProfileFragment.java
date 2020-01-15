package com.kohinoor.sandy.kohinnor.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Activities.EditeProfileActivity;
import com.kohinoor.sandy.kohinnor.Adapter.MyHomeRecyclerViewAdapter;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.Database;
import com.kohinoor.sandy.kohinnor.Model.ProfileData;
import com.kohinoor.sandy.kohinnor.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mProfileName;
    private TextView mProfileMail;
    private ImageView mProfileImage;
    private TextView mProfAboutmeText;
    private TextView mProfDOB;
    private TextView mProfAdress;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ProfileData profileData;

    private ImageView mEditProf;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        mProfileName = (TextView) view.findViewById(R.id.user_profile_name);
        mProfileMail = (TextView) view.findViewById(R.id.user_profile_short_bio);
        mProfileImage =  (ImageView) view.findViewById(R.id.user_profile_photo);
        mProfAboutmeText = (TextView) view.findViewById(R.id.prof_aboutme);
        mProfDOB = (TextView) view.findViewById(R.id.prof_DOB);
        mProfAdress = (TextView) view.findViewById(R.id.prof_address);


        mEditProf = (ImageView) view.findViewById(R.id.edit_prof_button);



        loadProfile();

       // mProfileMail.setText(mParam1);

        mEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  FragmentManager fragmentManager2 = getFragmentManager();
              //  FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
             //   EditFragment fragment2 = new EditFragment();
             //   fragmentTransaction2.addToBackStack("xyz");
             //   fragmentTransaction2.hide(ProfileFragment.this);
             //   fragmentTransaction2.add(android.R.id.content, fragment2);
             //   fragmentTransaction2.commit();

                Intent intent = new Intent(getContext(), EditeProfileActivity.class);
              /*  intent.putExtra("profName", mProfileName.getText().toString().trim());
                intent.putExtra("profEmail", mProfileMail.getText().toString().trim());
                intent.putExtra("profAboutMe", mProfAboutmeText.getText().toString().trim());
                intent.putExtra("profDOB", mProfDOB.getText().toString().trim());
                intent.putExtra("profAddress", mProfAdress.getText().toString().trim());
                mProfileImage.setDrawingCacheEnabled(true);
                Bitmap b=mProfileImage.getDrawingCache();
                intent.putExtra("profImage",b);*/
                startActivity(intent);


            }
        });
                return view;

    }

    public void loadProfile()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        final String usersId = user.getUid();
        myRef = database.getReference("Users/"+usersId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                profileData = dataSnapshot.getValue(ProfileData.class);

                if(profileData != null)
                {
                    mProfileName.setText(profileData.getName());
                    mProfileMail.setText(profileData.getEmail());
                    mProfAboutmeText.setText(profileData.getAboutMeText());
                    mProfDOB.setText(profileData.getDOB());
                    mProfAdress.setText(profileData.getAddress());
                    Picasso.with(getContext())
                            .load(profileData.getPhotoUrl())
                            .resize(250,250)
                            .into(mProfileImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) mProfileImage.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                                    mProfileImage.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError() {
                                    mProfileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
                                }
                            });
                }


            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());

            }


        });

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
