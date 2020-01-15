package com.kohinoor.sandy.kohinnor.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Interface.ClickListener;
import com.kohinoor.sandy.kohinnor.Model.ArticleData;
import com.kohinoor.sandy.kohinnor.Model.QuizData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.List;

/**
 * Created by Dell on 31-12-2017.
 */

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.ViewHolder>{

    private ClickListener clickListener;
    private List<QuizData> mQuizData;
    private List<String> mQuizList;
    private int mQuetionCountValue;


    Context context;
    View view1;
    ViewHolder viewHolder1;
    TextView mQuizNumber;
    TextView mQuetionCount;


    public QuizListAdapter(Context context1, List<QuizData> quizData,int quetionCount){


        context = context1;
        this.mQuizData = quizData;
        this.mQuetionCountValue = quetionCount;


    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{


        public TextView mQuizNumber;
        public TextView mQuetionCount;

        public ViewHolder(View v){

            super(v);

            mQuizNumber = (TextView)v.findViewById(R.id.QuizNumber);
            mQuetionCount = (TextView) v.findViewById(R.id.QuetionCount);
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
    public QuizListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.quiz_listlayout,parent,false);


        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        String QuizNumber = (position+1) + ". प्रश्नसंच_" + (position+1);
        holder.mQuizNumber.setText(QuizNumber);
        holder.mQuetionCount.setText(String.valueOf(mQuetionCountValue));

    }
    public void setClickListener(ClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }
    @Override
    public int getItemCount(){

        return (mQuizData.size()/mQuetionCountValue);
    }


}