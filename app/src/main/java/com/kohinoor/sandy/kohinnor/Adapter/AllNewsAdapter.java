package com.kohinoor.sandy.kohinnor.Adapter;


import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kohinoor.sandy.kohinnor.Model.DataModel;
import com.kohinoor.sandy.kohinnor.Model.ExpandableData;
import com.kohinoor.sandy.kohinnor.Model.Repo;
import com.kohinoor.sandy.kohinnor.R;
import com.squareup.picasso.Picasso;

public class AllNewsAdapter extends BaseExpandableListAdapter {

    private Context c;
    private List<ExpandableData> mRvExpData;
    private LayoutInflater inflater;

    public AllNewsAdapter(Context c,List<ExpandableData> team)
    {
        this.c=c;
        this.mRvExpData=team;
        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //GET A SINGLE PLAYER
    @Override
    public Object getChild(int groupPos, int childPos) {
        // TODO Auto-generated method stub
        return mRvExpData.get(groupPos).mExpnSubData.get(childPos);
    }

    //GET PLAYER ID
    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET PLAYER ROW
    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT

        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.exp_list_data, null);
        }

        //GET CHILD/PLAYER NAME
        DataModel child = (DataModel) getChild(groupPos, childPos);

        //SET CHILD NAME
        TextView mSubHeading =(TextView) convertView.findViewById(R.id.exp_sub_heading);
        TextView mSubDate =(TextView) convertView.findViewById(R.id.exp_sub_date);
        ImageView mSubImage = (ImageView) convertView.findViewById(R.id.exp_sub_image);

        mSubHeading.setText(child.getmHeading());
        mSubDate.setText(child.getmDate());

        if(child.getmImageUrl().equals(""))
        {
            mSubImage.setImageResource(R.mipmap.ic_default_image);
        }
        else {
            Picasso.with(c)
                    .load(child.getmImageUrl())
                    .resize(250, 250)
                    .placeholder(R.mipmap.ic_default_image)
                    .error(R.mipmap.ic_default_image)
                    .into(mSubImage);
        }
        //GET TEAM NAME
        String teamName= getGroup(groupPos).toString();


        return convertView;
    }

    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        // TODO Auto-generated method stub
        return mRvExpData.get(groupPosw).mExpnSubData.size();
    }

    //GET TEAM
    @Override
    public Object getGroup(int groupPos) {
        // TODO Auto-generated method stub
        return mRvExpData.get(groupPos);
    }

    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mRvExpData.size();
    }

    //GET TEAM ID
    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    //GET TEAM ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if(convertView == null)
        {
            convertView=inflater.inflate(R.layout.exp_list_header, null);
        }

        //GET GROUP/TEAM ITEM
        ExpandableData mExpData = (ExpandableData) getGroup(groupPosition);

        //SET GROUP NAME
        TextView mHeaderIcon=(TextView) convertView.findViewById(R.id.rv_header_Icon);
        TextView mHeader=(TextView) convertView.findViewById(R.id.rv_header_data);

        String headerIcon=mExpData.mRvExpDate;
        mHeaderIcon.setText(headerIcon);

        String header=mExpData.mRvExpHeading;
        mHeader.setText(header);

        //ASSIGN TEAM IMAGES ACCORDING TO TEAM NAME


        //SET TEAM ROW BACKGROUND COLOR
/*
        convertView.setBackgroundColor(Color.LTGRAY);
*/

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

}