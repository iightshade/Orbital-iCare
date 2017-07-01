package com.logcat.example.pc.orbitalproj;


import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MedicationViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Medication> medicationArrayList;
    private LayoutInflater layoutInflater;

    private static FirebaseAuth userAuth;
    private static StorageReference storageReference;
    private static String userId;

    public MedicationViewAdapter(Activity context, List<Medication> medicationArrayList) {
        this.context = context;
        this.medicationArrayList = medicationArrayList;
        layoutInflater = LayoutInflater.from(context);

        userAuth = FirebaseAuth.getInstance();
        userId = userAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference(userId);
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
        String medicationId;

        if ((position + 1) == medicationArrayList.size()) {

            view = layoutInflater.inflate(R.layout.medication_add_new, parent, false);
            return view;

        } else {

            if (convertView == null) {

                view = layoutInflater.inflate(R.layout.medication_frame, parent, false);
                view.setTag(R.id.medicationImage, view.findViewById(R.id.medicationImage));
                view.setTag(R.id.medicationMainTitle, view.findViewById(R.id.medicationMainTitle));

            } else {
                view = convertView;
            }

            medicationImage = (ImageView) view.getTag(R.id.medicationImage);
            medicationMainTitle = (TextView) view.getTag(R.id.medicationMainTitle);

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

            medicationMainTitle.setText(medication.getMedicationTitle());

            return view;
        }
    }
}