package com.kohinoor.sandy.kohinnor.Model;


public class CourseList {

    public String mTitle;
    public int mImage;

    public CourseList()
    {

    }

    public CourseList(String title, int image) {
        this.mTitle = title;
        this.mImage = image;
    }

    public String getmTitle() {
        return mTitle;
    }

    public int getmImage() {
        return mImage;
    }
}