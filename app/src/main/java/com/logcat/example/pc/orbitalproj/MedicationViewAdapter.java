package com.logcat.example.pc.orbitalproj;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicationViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Medication> medicationArrayList;
    private LayoutInflater layoutInflater;

    private static FirebaseAuth userAuth;
    private static StorageReference storageReference;
    private static String userId;

    private Calendar calendar = Calendar.getInstance();
    private static Integer currentYear, currentMonth, currentDay, currentDate;

    int counter;


    public MedicationViewAdapter(Activity context, List<Medication> medicationArrayList) {
        this.context = context;
        this.medicationArrayList = medicationArrayList;
        layoutInflater = LayoutInflater.from(context);

        userAuth = FirebaseAuth.getInstance();
        userId = userAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference(userId);

        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        currentDate = currentYear * 10000 + currentMonth * 100 + currentDay;

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
        final ImageView medicationImage;
        final TextView medicationMainTitle;
        final TextView expiredTextView;
        String medicationId;
        Integer endYear, endMonth, endDay, endDate;


        if (convertView == null) {

            view = layoutInflater.inflate(R.layout.medication_frame, parent, false);
            view.setTag(R.id.medicationImageAdapter, view.findViewById(R.id.medicationImageAdapter));
            view.setTag(R.id.medicationMainTitleAdapter, view.findViewById(R.id.medicationMainTitleAdapter));
            view.setTag(R.id.expiredTextViewAdapter, view.findViewById(R.id.expiredTextViewAdapter));

        } else {
            view = convertView;
        }

        medicationImage = (ImageView) view.getTag(R.id.medicationImageAdapter);
        medicationMainTitle = (TextView) view.getTag(R.id.medicationMainTitleAdapter);
        expiredTextView = (TextView) view.getTag(R.id.expiredTextViewAdapter);

        Medication medication = (Medication) getItem(position);
        medicationId = medication.getMedicationId();

        StorageReference importReference = storageReference.child(medicationId + ".jpg");

        importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context.getApplicationContext())
                        .load(downloadUrl)
                        .dontAnimate()
                        .into(medicationImage);
            }
        });

        //glideMultiThreading(importReference, context, medicationImage);

        medicationMainTitle.setText(medication.getMedicationTitle());

        endDay = medication.getMedicationEndDay();
        endMonth = medication.getMedicationEndMonth();
        endYear = medication.getMedicationEndYear();
        endDate = endYear * 10000 + endMonth * 100 + endDay;

        if (currentDate > endDate) {
            medicationImage.setAlpha(0.2f);
            medicationMainTitle.setAlpha(0.2f);
            expiredTextView.setVisibility(View.VISIBLE);
        }

        return view;
    }

    /*private static void glideMultiThreading(StorageReference importReference, Context context, ImageView medicationImage) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Runnable worker = new WorkerThread(importReference, context, medicationImage, "");
        executor.execute(worker);
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }*/
}
