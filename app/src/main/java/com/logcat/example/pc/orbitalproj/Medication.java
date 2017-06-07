package com.logcat.example.pc.orbitalproj;



public class Medication extends Object{

    String mainTitle;
    String subTitle;

    public Medication(String mainTitle, String subTitle){
        this.mainTitle = mainTitle;
        this.subTitle = subTitle;
    }

    public Medication(){

    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
