package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MedicationEdit extends AppCompatActivity{

    FirebaseAuth userAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    String userId;

    String medicationId;

    EditText medicationTitleTextView;
    EditText medicationDescriptionTextView;
    Medication medicationInEdit;
    Button saveButton;
    Button cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_edit);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();

        userId = firebaseUser.getUid();
        userReference = firebaseDatabase.getReference(userId);

        medicationTitleTextView = (EditText)findViewById(R.id.medicationTitleTextView);
        medicationDescriptionTextView = (EditText)findViewById(R.id.medicationDescriptionTextView);
        saveButton = (Button)findViewById(R.id.saveButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);

        Intent intent = getIntent();
        medicationInEdit = intent.getParcelableExtra("Medicine");

        if(medicationInEdit != null) {
            medicationTitleTextView.setText(medicationInEdit.getMedicationTitle());
            medicationDescriptionTextView.setText(medicationInEdit.getMedicationDescription());
            medicationId = medicationInEdit.getMedicationId();
        }else{
            medicationId = userReference.push().getKey();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicationInEdit = new Medication(
                        medicationId,
                        medicationTitleTextView.getText().toString(),
                        medicationDescriptionTextView.getText().toString());
                userReference.child(medicationId).setValue(medicationInEdit);
                Intent intent = new Intent(MedicationEdit.this , MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
