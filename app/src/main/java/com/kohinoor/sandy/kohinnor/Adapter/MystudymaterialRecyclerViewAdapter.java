package com.kohinoor.sandy.kohinnor.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Fragments.StudyMFragment.OnListFragmentInteractionListener;

import com.kohinoor.sandy.kohinnor.Model.CoursesDataModel;
import com.kohinoor.sandy.kohinnor.Model.StudyDataModel;
import com.kohinoor.sandy.kohinnor.Model.StudyMData;
import com.kohinoor.sandy.kohinnor.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.kohinoor.sandy.kohinnor.Model.StudyMData} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MystudymaterialRecyclerViewAdapter extends RecyclerView.Adapter {

    private final List<StudyDataModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    private String m_strFlowFrom;

    public MystudymaterialRecyclerViewAdapter(List<StudyDataModel> items, OnListFragmentInteractionListener listener, String l_strFlowFrom) {
        mValues = items;
        mListener = listener;
        m_strFlowFrom = l_strFlowFrom;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case StudyDataModel.HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_studymaterialheader, parent, false);
                return new StudyHeaderViewHolder(view);
            case StudyDataModel.CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_studymaterial, parent, false);
                return new StudyContentViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mValues.get(position).type) {
            case 0:
                return StudyDataModel.HEADER_TYPE;
            case 1:
                return StudyDataModel.CONTENT_TYPE;
            default:
                return -1;
        }
    }
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        StudyDataModel studyDataModel = mValues.get(position);



        if (studyDataModel != null) {
            switch (studyDataModel.type) {
                case StudyDataModel.HEADER_TYPE:
                    ((StudyHeaderViewHolder) holder).mItem = studyDataModel;
                    ((StudyHeaderViewHolder) holder).mHeader.setText(studyDataModel.text);
                    break;
                case StudyDataModel.CONTENT_TYPE:
                    ((StudyContentViewHolder) holder).mItem = studyDataModel;
                    ((StudyContentViewHolder) holder).mContent.setText(studyDataModel.text);
                    ((StudyContentViewHolder) holder).mImage.setImageResource(studyDataModel.image);
                    ((StudyContentViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != mListener) {
                                // Notify the active callbacks interface (the activity, if the
                                // fragment is attached to one) that an item has been selected.
                                mListener.onListFragmentInteraction(((StudyContentViewHolder) holder).mItem, m_strFlowFrom);
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class StudyHeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHeader;
        public StudyDataModel mItem;



        public StudyHeaderViewHolder(View view) {
            super(view);
            mView = view;
            mHeader = view.findViewById(R.id.studyheader);

        }

    }
    public static class StudyContentViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContent;
        public final ImageView mImage;

        public StudyDataModel mItem;

        public StudyContentViewHolder(View view) {
            super(view);
            mView = view;
            mContent = view.findViewById(R.id.studycontent);
            mImage = view.findViewById(R.id.image_view);
        }

    }

}
