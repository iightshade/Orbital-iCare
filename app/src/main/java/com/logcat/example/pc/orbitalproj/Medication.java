package com.logcat.example.pc.orbitalproj;


import android.os.Parcel;
import android.os.Parcelable;

public class Medication extends Object implements Parcelable {

    private String medicationId;
    private String medicationTitle;
    private String medicationDescription;
    private String medicationHour;
    private String medicationMinute;


    public Medication(String medicationId,
                      String medicationTitle,
                      String medicationDescription,
                      String medicationHour,
                      String medicationMinute) {
        this.medicationId = medicationId;
        this.medicationTitle = medicationTitle;
        this.medicationDescription = medicationDescription;
        this.medicationHour = medicationHour;
        this.medicationMinute = medicationMinute;
    }

    public Medication() {

    }


    protected Medication(Parcel in) {
        medicationId = in.readString();
        medicationTitle = in.readString();
        medicationDescription = in.readString();
        medicationHour = in.readString();
        medicationMinute = in.readString();
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(medicationId);
        dest.writeString(medicationTitle);
        dest.writeString(medicationDescription);
        dest.writeString(medicationHour);
        dest.writeString(medicationMinute);
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



