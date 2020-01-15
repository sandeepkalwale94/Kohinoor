package com.kohinoor.sandy.kohinnor.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kohinoor.sandy.kohinnor.Activities.StudyDataActivity;
import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.R;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;

import java.io.File;
import java.util.List;

import static com.kohinoor.sandy.kohinnor.Activities.StudyDataActivity.notesType;

/**
 * Created by Dell on 31-12-2017.
 */

public class StudyMFragAdapter extends RecyclerView.Adapter<StudyMFragAdapter.ViewHolder>{

    private ClickListener clickListener;
    private List<StudyMData> MaterialType;


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView textView;


    public StudyMFragAdapter(Context context1,List<StudyMData> MaterialType){

        this.MaterialType = MaterialType;
        context = context1;


    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        public TextView mTitle;
        public TextView mDescription;

        public ImageView imageView;;
        public ViewHolder(View v){

            super(v);

            mTitle = (TextView)v.findViewById(R.id.textView);
            imageView = (ImageView)v.findViewById(R.id.imageView) ;
            itemView.setOnClickListener(this);
         //   mDescription = (TextView)v.findViewById(R.id.text2);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null)
            {
                clickListener.ItemClicked(view,MaterialType.size()- getAdapterPosition() -1);
            }

        }


    }




    @Override
    public StudyMFragAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.studydata_listlayout,parent,false);


        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mTitle.setText(MaterialType.get(MaterialType.size()-position-1).getmTitle());
        //  holder.mDescription.setText(MaterialType.get(position).getmDescription());
        if(notesType.equals("QPapers"))
        {
            holder.imageView.setImageResource(R.drawable.ic_qpaper);
        }
        //  holder.imageView.setImageDrawable(R.id.);
    }
   public void setClickListener(ClickListener itemClickListener)
   {
       this.clickListener = itemClickListener;
   }
    @Override
    public int getItemCount(){

        return MaterialType.size();
    }


}