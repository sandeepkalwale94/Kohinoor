package com.kohinoor.sandy.kohinnor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.Model.ExpandableData;
import com.kohinoor.sandy.kohinnor.Model.QuizData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.List;

/**
 * Created by Dell on 31-12-2017.
 */

public class RvExpandableAdapter extends RecyclerView.Adapter<RvExpandableAdapter.ViewHolder>{

    private ClickListener clickListener;
    private List<ExpandableData> mRvExpData;


    Context context;
    View view1;
    ViewHolder viewHolder1;

    public RvExpandableAdapter(Context context1, List<ExpandableData> mRvExpData){


        context = context1;
        this.mRvExpData = mRvExpData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        public TextView mRvExpHeading;
        public TextView mRvExpDate;

        public ViewHolder(View v){

            super(v);

            mRvExpHeading = (TextView)v.findViewById(R.id.rv_exp_heading);
            mRvExpDate = (TextView) v.findViewById(R.id.rv_exp_date);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(clickListener != null)
            {
                clickListener.ItemClicked(view, getAdapterPosition());
            }

        }


    }

    @Override
    public RvExpandableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.rv_expandable_list_layout,parent,false);


        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mRvExpHeading.setText(mRvExpData.get(position).getmRvExpHeading());
        holder.mRvExpDate.setText(mRvExpData.get(position).getmRvExpDate());

    }
    public void setClickListener(ClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
    @Override
    public int getItemCount(){

        return mRvExpData.size();
    }


}