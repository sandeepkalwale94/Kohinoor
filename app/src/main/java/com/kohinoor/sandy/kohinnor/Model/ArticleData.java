package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 11-01-2018.
 */

public class ArticleData {

    public String mArticleTitle;
    public String mArticleData;
    public Long mTimeStamp;

    public ArticleData()
    {

    }

    public ArticleData(String mArticleTitle, String mArticleData, Long mTimeStamp) {
        this.mArticleTitle = mArticleTitle;
        this.mArticleData = mArticleData;
        this.mTimeStamp = mTimeStamp;
    }

    public String getmArticleTitle() {
        return mArticleTitle;
    }

    public String getmArticleData() {
        return mArticleData;
    }

    public Long getmTimeStamp() {
        return mTimeStamp;
    }
}
