package com.kohinoor.sandy.kohinnor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Fragments.HomeFragment;
import com.kohinoor.sandy.kohinnor.Fragments.HomeFragment.OnListFragmentInteractionListener;
import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.Database;

import com.kohinoor.sandy.kohinnor.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.ViewHolder> {

    private final List<DataModel> mDatabase;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;


    public MyHomeRecyclerViewAdapter(Context context, List<DataModel> items, OnListFragmentInteractionListener listener) {
        mDatabase = items;
        mListener = listener;
        mContext = context;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mDatabase.get(mDatabase.size() - position - 1);
        holder.mHeading.setText(mDatabase.get(mDatabase.size() - position - 1).getmHeading());
        String mDate = getDateCurrentTimeZone(mDatabase.get(mDatabase.size() - position - 1).getmTimeStamp());
        holder.mDate.setText(mDate);
        if(mDatabase.get(mDatabase.size() - position - 1).getmImageUrl().equals(""))
        {
            holder.mImage.setImageResource(R.mipmap.ic_default_image);
        }
        else {
            Picasso.with(mContext)
                    .load(mDatabase.get(mDatabase.size() - position - 1).getmImageUrl())
                    .resize(250, 250)
                    .placeholder(R.mipmap.ic_default_image)
                    .error(R.mipmap.ic_default_image)
                    .into(holder.mImage);
        }

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.dismissProgressDialog();


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatabase.size();
    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            java.util.Date currenTimeZone=new java.util.Date(timestamp);

            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mImage;
        public TextView mHeading;
        public TextView mDate;
        public DataModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.imageview);
            mHeading = (TextView) view.findViewById(R.id.heading);
            mDate = (TextView) view.findViewById(R.id.date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHeading.getText() + "'";
        }
    }
}
