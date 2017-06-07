package com.logcat.example.pc.orbitalproj;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MedicationViewAdapter extends BaseAdapter{

    private Activity context;
    private List<Medication> medicationArrayList;
    private LayoutInflater layoutInflater;

    public MedicationViewAdapter(Activity context, List<Medication> medicationArrayList){
        this.context = context;
        this.medicationArrayList = medicationArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    public MedicationViewAdapter() {
    }

    @Override
    public int getCount() {
        return medicationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return medicationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        TextView medicationMainTitle;
        TextView medicationSubTitle;

        if(convertView == null){

            view = layoutInflater.inflate(R.layout.medication_frame, parent, false);
            view.setTag(R.id.medicationMainTitle, view.findViewById(R.id.medicationMainTitle));
            view.setTag(R.id.medicationSubTitle, view.findViewById(R.id.medicationSubTitle));

        }else{
            view = convertView;
        }

        medicationMainTitle = (TextView)view.getTag(R.id.medicationMainTitle);
        medicationSubTitle = (TextView)view.getTag(R.id.medicationSubTitle);

        Medication medication = (Medication)getItem(position);

        medicationMainTitle.setText(medication.getMainTitle());
        medicationSubTitle.setText(medication.getSubTitle());

        return view;
    }
}
