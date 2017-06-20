package com.logcat.example.pc.orbitalproj;


import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class Medication extends Object implements Parcelable {

    private String medicationId;
    private String medicationTitle;
    private String medicationDescription;
    private String medicationHour;
    private String medicationMinute;
    private ArrayList<Boolean> medicationDays;


    public Medication(String medicationId,
                      String medicationTitle,
                      String medicationDescription,
                      String medicationHour,
                      String medicationMinute,
                      ArrayList<Boolean> medicationDays ) {
        this.medicationId = medicationId;
        this.medicationTitle = medicationTitle;
        this.medicationDescription = medicationDescription;
        this.medicationHour = medicationHour;
        this.medicationMinute = medicationMinute;
        this.medicationDays = medicationDays;
    }

    public Medication() {

    }

    protected Medication(Parcel in) {
        medicationId = in.readString();
        medicationTitle = in.readString();
        medicationDescription = in.readString();
        medicationHour = in.readString();
        medicationMinute = in.readString();
        medicationDays = (ArrayList<Boolean>) in.readSerializable();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(medicationId);
        out.writeString(medicationTitle);
        out.writeString(medicationDescription);
        out.writeString(medicationHour);
        out.writeString(medicationMinute);
        out.writeSerializable(medicationDays);
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

    public String getMedicationHour() {
        return medicationHour;
    }

    public void setMedicationHour(String medicationHour) {
        this.medicationHour = medicationHour;
    }

    public String getMedicationMinute() {
        return medicationMinute;
    }

    public void setMedicationMinute(String medicationMinute) {
        this.medicationMinute = medicationMinute;
    }

    public ArrayList<Boolean> getMedicationDays() {
        return medicationDays;
    }

    public void setMedicationDays(ArrayList<Boolean> medicationDays) {
        this.medicationDays = medicationDays;
    }

    @Override
    public int describeContents() {
        return 0;
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
}



