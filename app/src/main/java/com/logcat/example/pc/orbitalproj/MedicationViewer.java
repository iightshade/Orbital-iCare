package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MedicationViewer extends AppCompatActivity{

    TextView medicationTitleText;
    TextView medicationDescriptionText;
    ImageView medicationImage;
    TextView timeText;
    TextView datesText;
    Button editButton;
    Button returnButton;
    Intent intent;
    Medication medication;

    private FirebaseAuth userAuth;
    private StorageReference storageReference;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_viewer_layout);

        userAuth = FirebaseAuth.getInstance();
        userId = userAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference(userId);

        medicationTitleText = (TextView)findViewById(R.id.medicationTitleText);
        medicationDescriptionText = (TextView)findViewById(R.id.medicationDescriptionText);
        medicationImage = (ImageView) findViewById(R.id.medicationImage) ;
        timeText = (TextView) findViewById(R.id.timeText);
        datesText = (TextView) findViewById((R.id.datesText)) ;

        editButton = (Button)findViewById(R.id.editButton);
        returnButton = (Button)findViewById(R.id.returnButton);

        intent = getIntent();
        medication = intent.getParcelableExtra("Medicine");

        medicationTitleText.setText(medication.getMedicationTitle());
        medicationDescriptionText.setText(medication.getMedicationDescription());

        String medicationId = medication.getMedicationId();
        StorageReference importReference = storageReference.child(medicationId + ".jpg");

        importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(MedicationViewer.this)
                        .load(downloadUrl)
                        .dontAnimate()
                        .into(medicationImage);
            }
        });


        int numHours = Integer.parseInt(medication.getMedicationHour());
        int numMinutes = Integer.parseInt(medication.getMedicationMinute());
        setTimeText(numHours, numMinutes);


        ArrayList<Boolean> medicationDays;
        medicationDays = medication.getMedicationDays();
        boolean[] daysChecked = new boolean[medicationDays.size()];
        for(int i = 0; i < medicationDays.size(); i++)
        {
            daysChecked[i] = medicationDays.get(i);
        }
        setDatesText(daysChecked);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MedicationViewer.this , MedicationEdit.class);
                intent.putExtra("Medicine", medication);
                startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MedicationViewer.this , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setTimeText(int numHour, int numMinute) {

        Boolean isMorning = true;
        String hourString;
        String minuteString;

        if (numHour >= 12) {
            isMorning = false;
        }

        if (numHour > 12) {
            numHour = (numHour - 12);
        }

        if (numHour < 10) {
            hourString = "0" + Integer.toString(numHour);
        } else {
            hourString = Integer.toString(numHour);
        }

        if (numMinute < 10) {
            minuteString = "0" + Integer.toString(numMinute);
        } else {
            minuteString = Integer.toString(numMinute);
        }

        timeText.setText(hourString + ":" + minuteString);

        if (isMorning == true) {
            timeText.append(" AM");
        } else {
            timeText.append(" PM");
        }
    }

    private void setDatesText(boolean[] dates){
        datesText.setText("");
        for(int i =0; i<7; i++){
            if(dates[i] == true){
                switch (i){
                    case 0:datesText.append("Mon ");    continue;
                    case 1:datesText.append("Tues ");   continue;
                    case 2:datesText.append("Wed ");    continue;
                    case 3:datesText.append("Thurs ");  continue;
                    case 4:datesText.append("Fri ");    continue;
                    case 5:datesText.append("Sat ");    continue;
                    case 6:datesText.append("Sun ");    continue;
                }
            }
        }
        if(datesText.getText().toString().equals("")){
            datesText.setText("Enter dates here.");
        }
    }

}
