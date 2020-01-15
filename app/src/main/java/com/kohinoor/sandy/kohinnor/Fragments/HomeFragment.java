package com.kohinoor.sandy.kohinnor.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kohinoor.sandy.kohinnor.Activities.AdminMainActivity;
import com.kohinoor.sandy.kohinnor.Activities.AllNewsActivity;
import com.kohinoor.sandy.kohinnor.Activities.HomeActivity;
import com.kohinoor.sandy.kohinnor.Adapter.MyHomeRecyclerViewAdapter;
import com.kohinoor.sandy.kohinnor.CustomClasses.ConnectivityReceiver;
import com.kohinoor.sandy.kohinnor.CustomClasses.MyApplication;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.Database;
import com.kohinoor.sandy.kohinnor.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<DataModel> mDatabase;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private Context context;

    private static ProgressDialog mProgressDialog;

    private boolean isConnected;

    RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance(int columnCount) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = new ArrayList<>();
        mProgressDialog = new ProgressDialog(getActivity());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("NewsData");
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        Button mShowAllButton = (Button) view.findViewById(R.id.showall);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
        final Long timestamp = new Date().getTime();
        final String lDay = getDay(timestamp);

        mShowAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllNewsActivity.class);
                startActivity(intent);
            }
        });

        // Set the adapter//
        if (view instanceof LinearLayout) {
            context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
                myRef.orderByChild("TimeStamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        mDatabase = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            DataModel post = dataSnapshot1.getValue(DataModel.class);
                            if(lDay.equals(getDay(post.getmTimeStamp())))
                            {
                                mDatabase.add(post);
                            }
                        }
                        Database data = new Database();
                        data.Database(mDatabase);
                        mDatabase = data.getmDatabase();

                        if(mDatabase.size() == 0)
                        {
                            mDatabase.add(new DataModel("No Data","","","",timestamp));
                        }
                        recyclerView.setAdapter(new MyHomeRecyclerViewAdapter(getContext(), mDatabase, mListener));

                        // displaySelectedScreen(R.id.nav_camera) ;
                        // String value = dataSnapshot.getValue(String.class);
                        // Log.d(TAG, "Value is: " + value);
                    }


                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        mProgressDialog.dismiss();
                    }


                });
        }
        else
        {
            mProgressDialog.dismiss();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        //showSnack(isConnected);
        return isConnected;
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected){
       // showSnack(isConnected);

    }

    public void dismissProgressDialog()
    {

        mProgressDialog.dismiss();
    }

    public  String getDay(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date currenTimeZone=new java.util.Date(timestamp);

            return String.valueOf(currenTimeZone.getDate());
        }catch (Exception e) {
        }
        return "";
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DataModel item);
    }
}
