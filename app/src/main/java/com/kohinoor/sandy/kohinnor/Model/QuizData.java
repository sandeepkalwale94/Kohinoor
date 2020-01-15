package com.kohinoor.sandy.kohinnor.Model;

/**
 * Created by Dell on 11-01-2018.
 */

public class QuizData {

    public String mQuetion;
    public String mAnswer;
    public String mOption1;
    public String mOption2;
    public String mOption3;
    public String mOption4;
    //public int mQuetionCount;

    public QuizData()
    {

    }

    public QuizData(String quetion, String answer, String option1,String option2,String option3,String option4/*, int quetioncount*/) {
        this.mQuetion = quetion;
        this.mAnswer = answer;
        this.mOption1 = option1;
        this.mOption2 = option2;
        this.mOption3 = option3;
        this.mOption4 = option4;
      //  this.mQuetionCount = quetioncount;

    }

    public String getmQuetion() {
        return mQuetion;
    }

    public String getmAnswer() {
        return mAnswer;
    }

    public String getmOption1() {
        return mOption1;
    }

    public String getmOption2() {
        return mOption2;
    }

    public String getmOption3() {
        return mOption3;
    }

    public String getmOption4() {
        return mOption4;
    }

    /*public int getmQuetionCount() {
        return mQuetionCount;
    }*/
}
