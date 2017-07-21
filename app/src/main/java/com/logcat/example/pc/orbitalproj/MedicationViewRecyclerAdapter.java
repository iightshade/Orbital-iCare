package com.logcat.example.pc.orbitalproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;


public class MedicationViewRecyclerAdapter extends RecyclerView.Adapter<MedicationViewRecyclerAdapter.ViewHolder> {

    private Activity context;
    private List<Medication> medicationArrayList;
    private LayoutInflater layoutInflater;

    private static FirebaseAuth userAuth;
    private static StorageReference storageReference;
    private static String userId;

    private Calendar calendar = Calendar.getInstance();
    private static Integer currentYear, currentMonth, currentDay, currentDate;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView medicationImage;
        TextView medicationMainTitle;
        TextView expiredTextView;

        public ViewHolder(View view) {
            super(view);

            medicationImage = (ImageView) view.findViewById(R.id.medicationImageAdapter);
            medicationMainTitle = (TextView) view.findViewById(R.id.medicationMainTitleAdapter);
            expiredTextView = (TextView) view.findViewById(R.id.expiredTextViewAdapter);

        }
    }

    public MedicationViewRecyclerAdapter(Activity context, List<Medication> medicationArrayList) {
        this.context = context;
        this.medicationArrayList = medicationArrayList;

        userAuth = FirebaseAuth.getInstance();
        userId = userAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference(userId);

        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        currentDate = currentYear * 10000 + currentMonth * 100 + currentDay;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medication_frame, parent, false);

        return new ViewHolder(inflatedView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Medication theMedication = medicationArrayList.get(position);
        holder.medicationMainTitle.setText(theMedication.getMedicationTitle());

        String medicationId = theMedication.getMedicationId();
        StorageReference importReference = storageReference.child(medicationId + ".jpg");
        importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context.getApplicationContext())
                        .load(downloadUrl)
                        .dontAnimate()
                        .into(holder.medicationImage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

}
