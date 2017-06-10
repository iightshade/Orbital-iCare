package com.logcat.example.pc.orbitalproj;


import android.os.Parcel;
import android.os.Parcelable;

public class Medication extends Object implements Parcelable{

    private String medicationId;
    private String medicationTitle;
    private String medicationDescription;

    public Medication(String medicationId, String medicationTitle, String medicationDescription){
        this.medicationId = medicationId;
        this.medicationTitle = medicationTitle;
        this.medicationDescription = medicationDescription;
    }

    public Medication(){

    }

    protected Medication(Parcel in) {
        medicationId = in.readString();
        medicationTitle = in.readString();
        medicationDescription = in.readString();
    }


    public String getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(String medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationTitle() {
        return medicationTitle;
    }

    public void setMedicationTitle(String medicationTitle) {
        this.medicationTitle = medicationTitle;
    }

    public String getMedicationDescription() {
        return medicationDescription;
    }

    public void setMedicationDescription(String medicationDescription) {
        this.medicationDescription = medicationDescription;
    }

    public static final Creator<Medication> CREATOR = new Creator<Medication>() {
        @Override
        public Medication createFromParcel(Parcel in) {
            return new Medication(in);
        }

        @Override
        public Medication[] newArray(int size) {
            return new Medication[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicationId);
        dest.writeString(medicationTitle);
        dest.writeString(medicationDescription);
    }
}
