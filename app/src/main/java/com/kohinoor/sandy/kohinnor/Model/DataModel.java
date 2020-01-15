package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 28-12-2017.
 */

public class DataModel
{

    public String Heading;
    public String Date;
    public String ImageUrl;
    public String NewsData;
    public Long TimeStamp;

    public DataModel()
    {

    }
   public DataModel(String heading, String date, String imageUrl, String newsData, Long timeStamp)
   {
       this.Date = date;
       this.Heading = heading;
       this.ImageUrl = imageUrl;
       this.NewsData = newsData;
       this.TimeStamp = timeStamp;
   }

    public Long getmTimeStamp() {
        return TimeStamp;
    }

    public String getmHeading()
    {
        return Heading;
    }
    public String getmDate()
    {
        return Date;
    }
    public String getmImageUrl()
    {
        return ImageUrl;
    }
    public String getmNewsData()
    {
        return NewsData;
    }
    public void setmHeading(String heading)
    {
        this.Heading = heading;
    }
    public void setmDate(String date)
    {
        this.Date = date;
    }
    public void setmImageUrl(String imageUrl)
    {
        this.ImageUrl = imageUrl;
    }
    public void setmNewsData(String newsData)
    {
        this.NewsData = newsData;
    }
}
