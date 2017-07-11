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
    private Integer medicationStartYear;
    private Integer medicationStartMonth;
    private Integer medicationStartDay;
    private Integer medicationEndYear;
    private Integer medicationEndMonth;
    private Integer medicationEndDay;
    private ArrayList<Boolean> medicationDays;

    public Medication(String medicationId,
                      String medicationTitle,
                      String medicationDescription,
                      String medicationHour,
                      String medicationMinute,
                      ArrayList<Boolean> medicationDays,
                      Integer medicationStartYear,
                      Integer medicationStartMonth,
                      Integer medicationStartDay,
                      Integer medicationEndYear,
                      Integer medicationEndMonth,
                      Integer medicationEndDay) {
        this.medicationId = medicationId;
        this.medicationTitle = medicationTitle;
        this.medicationDescription = medicationDescription;
        this.medicationHour = medicationHour;
        this.medicationMinute = medicationMinute;
        this.medicationDays = medicationDays;
        this.medicationStartYear = medicationStartYear;
        this.medicationStartMonth = medicationStartMonth;
        this.medicationStartDay = medicationStartDay;
        this.medicationEndYear = medicationEndYear;
        this.medicationEndMonth = medicationEndMonth;
        this.medicationEndDay = medicationEndDay;

    }

    public Medication() {

    }

    private Medication(Parcel in) {
        medicationId = in.readString();
        medicationTitle = in.readString();
        medicationDescription = in.readString();
        medicationHour = in.readString();
        medicationMinute = in.readString();
        medicationDays = (ArrayList<Boolean>) in.readSerializable();
        medicationStartYear = in.readInt();
        medicationStartMonth = in.readInt();
        medicationStartDay = in.readInt();
        medicationEndYear = in.readInt();
        medicationEndMonth = in.readInt();
        medicationEndDay = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(medicationId);
        out.writeString(medicationTitle);
        out.writeString(medicationDescription);
        out.writeString(medicationHour);
        out.writeString(medicationMinute);
        out.writeSerializable(medicationDays);
        out.writeInt(medicationStartYear);
        out.writeInt(medicationStartMonth);
        out.writeInt(medicationStartDay);
        out.writeInt(medicationEndYear);
        out.writeInt(medicationEndMonth);
        out.writeInt(medicationEndDay);
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

    public Integer getMedicationStartYear() {
        return medicationStartYear;
    }

    public void setMedicationStartYear(Integer medicationStartYear) {
        this.medicationStartYear = medicationStartYear;
    }

    public Integer getMedicationStartMonth() {
        return medicationStartMonth;
    }

    public void setMedicationStartMonth(Integer medicationStartMonth) {
        this.medicationStartMonth = medicationStartMonth;
    }

    public Integer getMedicationStartDay() {
        return medicationStartDay;
    }

    public void setMedicationStartDay(Integer medicationStartDay) {
        this.medicationStartDay = medicationStartDay;
    }

    public Integer getMedicationEndYear() {
        return medicationEndYear;
    }

    public void setMedicationEndYear(Integer medicationEndYear) {
        this.medicationEndYear = medicationEndYear;
    }

    public Integer getMedicationEndMonth() {
        return medicationEndMonth;
    }

    public void setMedicationEndMonth(Integer medicationEndMonth) {
        this.medicationEndMonth = medicationEndMonth;
    }

    public Integer getMedicationEndDay() {
        return medicationEndDay;
    }

    public void setMedicationEndDay(Integer medicationEndDay) {
        this.medicationEndDay = medicationEndDay;
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



