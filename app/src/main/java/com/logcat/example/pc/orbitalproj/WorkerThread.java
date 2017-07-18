package com.logcat.example.pc.orbitalproj;


import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

public class WorkerThread implements Runnable{
    private String command;
    private StorageReference importReference;
    private Context context;
    private ImageView medicationImage;

    public WorkerThread(StorageReference importReference, Context context, ImageView medicationImage, String s){
        this.importReference = importReference;
        this.context = context;
        this.medicationImage = medicationImage;
        this.command=s;
    }

    @Override
    public void run() {

        importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(context.getApplicationContext())
                        .load(downloadUrl)
                        .dontAnimate()
                        .into(medicationImage);
            }
        });

    }

    @Override
    public String toString(){
        return this.command;
    }
}
