package com.kohinoor.sandy.kohinnor.Model;

import java.util.List;

/**
 * Created by Dell on 28-12-2017.
 */

public class Database {


    public static List<DataModel> mDatabase;
    public void Database()
    {

    }
    public void Database(List<DataModel> mDatabase)
    {
        this.mDatabase = mDatabase;
    }


    public List<DataModel> getmDatabase()
    {
        return mDatabase;
    }


}
