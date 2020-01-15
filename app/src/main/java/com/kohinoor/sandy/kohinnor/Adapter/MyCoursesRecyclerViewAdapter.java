package com.kohinoor.sandy.kohinnor.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Model.CoursesDataModel;
import com.kohinoor.sandy.kohinnor.R;

import java.util.List;

public class MyCoursesRecyclerViewAdapter extends RecyclerView.Adapter {

    private final List<CoursesDataModel> mValues;
    public MyCoursesRecyclerViewAdapter(List<CoursesDataModel> items) {
        mValues = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CoursesDataModel.HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_detailsitem_header, parent, false);
                return new HeaderViewHolder(view);
            case CoursesDataModel.CONTENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_detailsitem, parent, false);
                return new ContentViewHolder(view);


        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mValues.get(position).type) {
            case 0:
                return CoursesDataModel.HEADER_TYPE;
            case 1:
                return CoursesDataModel.CONTENT_TYPE;
            default:
                return -1;
        }
    }
        @Override
        public void onBindViewHolder ( final RecyclerView.ViewHolder holder, int position){

            CoursesDataModel coursesDataModel = mValues.get(position);

            if (coursesDataModel != null) {
                switch (coursesDataModel.type) {
                    case CoursesDataModel.HEADER_TYPE:
                        ((HeaderViewHolder) holder).mHeader.setText(coursesDataModel.text);
                        /*((HeaderViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != mListener) {
                                    // Notify the active callbacks interface (the activity, if the
                                    // fragment is attached to one) that an item has been selected.
                                    mListener.onListFragmentInteraction(((HeaderViewHolder) holder).mItem);
                                }
                            }
                        });*/
                        break;
                    case CoursesDataModel.CONTENT_TYPE:
                        ((ContentViewHolder) holder).mContent.setText(coursesDataModel.text);
                        break;
                }
            }
            /*holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getmHeading());
        holder.mContentView.setText(mValues.get(position).getmNewsData());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
        }

        @Override
        public int getItemCount () {
            return mValues.size();
        }

        public static class HeaderViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mHeader;
            public CoursesDataModel mItem;

            public HeaderViewHolder(View view) {
                super(view);
                mView = view;
                mHeader = view.findViewById(R.id.header);
            }

        }
        public static class ContentViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContent;
            public CoursesDataModel mItem;

            public ContentViewHolder(View view) {
                super(view);
                mView = view;
                mContent = view.findViewById(R.id.content);
            }

        }

}
