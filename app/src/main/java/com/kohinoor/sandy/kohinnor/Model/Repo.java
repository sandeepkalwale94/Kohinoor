package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 03-02-2018.
 */

public class Repo {

    public String mSubHeader;
    public String mSubDate;
    public String mImageUrl;

    public Repo() {

    }

    public Repo(String mSubHeader, String mSubDate, String mImageUrl) {
        this.mSubHeader = mSubHeader;
        this.mSubDate = mSubDate;
        this.mImageUrl = mImageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmSubHeader() {
        return mSubHeader;
    }

    public void setmSubHeader(String mSubHeader) {
        this.mSubHeader = mSubHeader;
    }

    public String getmSubDate() {
        return mSubDate;
    }

    public void setmSubDate(String mSubDate) {
        this.mSubDate = mSubDate;
    }
}
