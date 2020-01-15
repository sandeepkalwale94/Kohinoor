package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 30-12-2017.
 */

public class ProfileData {
    protected String Name;
    protected String Email;
    protected String PhotoUrl;
    protected String AboutMeText;
    protected String DOB;
    protected String Address;

    public ProfileData()
    {

    }
    public ProfileData(String name, String email, String photoUrl, String aboutmetext, String dob, String address)
    {
        this.Name = name;
        this.Email = email;
        this.PhotoUrl = photoUrl;
        this.AboutMeText = aboutmetext;
        this.DOB = dob;
        this.Address = address;
    }
    public String getName()
    {
        return Name;
    }
    public String getEmail()
    {
        return Email;
    }
    public String getPhotoUrl()
    {
        return PhotoUrl;
    }
    public String getAboutMeText()
    {
        return AboutMeText;
    }
    public String getDOB()
    {
        return DOB;
    }
    public String getAddress()
    {
        return Address;
    }
    public void setName(String name)
    {
        this.Name = name;
    }
    public void setEmail(String email)
    {
        this.Email = email;
    }
    public void setPhotoUrl(String photoUrl)
    {
        this.PhotoUrl = photoUrl;
    }
    public  void setAboutMeText(String aboutMeText)
    {
        this.AboutMeText = aboutMeText;
    }
    public void setDOB(String dob)
    {
        this.DOB = dob;
    }
    public void setAddress(String address)
    {
        this.Address = address;
    }
}
