package com.kohinoor.sandy.kohinnor.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 03-02-2018.
 */

public class ExpandableData {

    public String mRvExpHeading;
    public String mRvExpDate;
    public List<DataModel> mExpnSubData = new ArrayList<>();

    public ExpandableData() {
    }

    public ExpandableData(String mRvExpHeading, String mRvExpDate) {
        this.mRvExpHeading = mRvExpHeading;
        this.mRvExpDate = mRvExpDate;
    }

    public String getmRvExpHeading() {
        return mRvExpHeading;
    }

    public void setmRvExpHeading(String mRvExpHeading) {
        this.mRvExpHeading = mRvExpHeading;
    }

    public String getmRvExpDate() {
        return mRvExpDate;
    }

    public void setmRvExpDate(String mRvExpDate) {
        this.mRvExpDate = mRvExpDate;
    }
}
