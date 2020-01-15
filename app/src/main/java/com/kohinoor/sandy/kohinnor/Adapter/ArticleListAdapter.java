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
import com.kohinoor.sandy.kohinnor.Model.ArticleData;
import com.kohinoor.sandy.kohinnor.R;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;

import java.io.File;
import java.util.List;

import static com.kohinoor.sandy.kohinnor.Activities.StudyDataActivity.notesType;

/**
 * Created by Dell on 31-12-2017.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder>{

    private ClickListener clickListener;
    private List<ArticleData> mArticleData;


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView mArticleTitle;
    TextView mArticleDescription;


    public ArticleListAdapter(Context context1,List<ArticleData> articleData){

        this.mArticleData = articleData;
        context = context1;


    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        public TextView mTitle;
        public TextView mDescription;

        public ViewHolder(View v){

            super(v);

            mTitle = (TextView)v.findViewById(R.id.articleHeading);
            mDescription = (TextView) v.findViewById(R.id.articleDescription);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(clickListener != null)
            {
                clickListener.ItemClicked(view, mArticleData.size() - getAdapterPosition() - 1);
            }

        }


    }




    @Override
    public ArticleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.article_listlayout,parent,false);


        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String firstLineofDesciption;
    if(mArticleData.get(mArticleData.size() - position - 1).getmArticleData().length() > 50) {
        firstLineofDesciption = mArticleData.get(mArticleData.size() - position - 1).getmArticleData().substring(0, Math.min(mArticleData.get(mArticleData.size() - position - 1).getmArticleData().length(), 50));
    }
    else {
        firstLineofDesciption = mArticleData.get(mArticleData.size() - position - 1).getmArticleData();
    }
        holder.mTitle.setText(mArticleData.get(mArticleData.size() - position - 1).getmArticleTitle());
        holder.mDescription.setText(firstLineofDesciption);

    }
    public void setClickListener(ClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
    @Override
    public int getItemCount(){

        return mArticleData.size();
    }


}