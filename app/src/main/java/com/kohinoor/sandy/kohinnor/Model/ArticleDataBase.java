package com.kohinoor.sandy.kohinnor.Model;

import java.util.List;

/**
 * Created by Dell on 12-01-2018.
 */

public class ArticleDataBase {


    public static List<ArticleData> mArticleData;

    public void ArticleDataBase()
    {

    }

    public void ArticleDataBase(List<ArticleData> mArticleData)
    {
        this.mArticleData = mArticleData;
    }

    public List<ArticleData> getmArticleData()
    {
        return mArticleData;
    }

}
