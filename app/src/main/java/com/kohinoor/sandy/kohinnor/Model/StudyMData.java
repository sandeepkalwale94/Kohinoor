package com.kohinoor.sandy.kohinnor.Model;


public class StudyMData {

    public String mTitle;
    public String mUrl;
    public Long mTimeStamp;

    public StudyMData()
    {

    }

    public StudyMData(String title, String description, Long mTimeStamp) {
        this.mTitle = title;
        this.mUrl = description;
        this.mTimeStamp = mTimeStamp;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public Long getmTimeStamp() {
        return mTimeStamp;
    }
}