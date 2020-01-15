package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 28-12-2017.
 */
public class CoursesDataModel {

    public static final int HEADER_TYPE=0;
    public static final int CONTENT_TYPE=1;

    public int type;
    public String text;

    public CoursesDataModel(int type, String text)
    {
        this.type=type;
        this.text=text;
    }
}

