package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MedicationViewer extends AppCompatActivity{

    TextView medicationTitleTextView;
    TextView medicationDescriptionTextView;
    Button editButton;
    Button returnButton;
    Intent intent;
    Medication medication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_dialog_layout);

        medicationTitleTextView = (TextView)findViewById(R.id.medicationTitleTextView);
        medicationDescriptionTextView = (TextView)findViewById(R.id.medicationDescriptionTextView);
        editButton = (Button)findViewById(R.id.editButton);
        returnButton = (Button)findViewById(R.id.returnButton);

        intent = getIntent();
        medication = intent.getParcelableExtra("Medicine");

        medicationTitleTextView.setText(medication.getMedicationTitle());
        medicationDescriptionTextView.setText(medication.getMedicationDescription());

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

}
