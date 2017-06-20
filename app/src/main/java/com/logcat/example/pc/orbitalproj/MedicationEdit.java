package com.logcat.example.pc.orbitalproj;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class MedicationEdit extends AppCompatActivity {

    FirebaseAuth userAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    String userId;
    String medicationId;

    StorageReference storageReference;
    Uri uri;

    EditText medicationTitleTextView;
    EditText medicationDescriptionTextView;
    Medication medicationInEdit;
    String medicationTitle;
    String medicationDescription;
    ImageView medicationImage;
    TextView timeTextView;
    String medicationHour;
    String medicationMinute;
    int currentHour;
    int currentMinute;
    TextView datesTextView;
    boolean[] daysChecked;
    ArrayList<Boolean> medicationDays;
    Button saveButton;
    Button cancelButton;

    private static final int PICK_IMAGE_REQUEST = 123;

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_edit);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userReference = firebaseDatabase.getReference(userId);
        storageReference = FirebaseStorage.getInstance().getReference(userId);

        medicationTitleTextView = (EditText) findViewById(R.id.medicationTitleTextView);
        medicationDescriptionTextView = (EditText) findViewById(R.id.medicationDescriptionTextView);
        medicationImage = (ImageView) findViewById(R.id.medicationImage);
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        datesTextView = (TextView) findViewById(R.id.datesTextView);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        verifyStoragePermissions(this);

        Intent intent = getIntent();
        medicationInEdit = intent.getParcelableExtra("Medicine");

        if (medicationInEdit != null) {
            //if medication already exists, populate the view with the items.
            medicationTitleTextView.setText(medicationInEdit.getMedicationTitle());
            if(medicationInEdit.getMedicationDescription() == null){
                medicationDescriptionTextView.setText("");
            }else{
                medicationDescriptionTextView.setText(medicationInEdit.getMedicationDescription());
            }
            medicationTitleTextView.setText(medicationInEdit.getMedicationTitle());
            medicationId = medicationInEdit.getMedicationId();

            StorageReference importReference = storageReference.child(medicationId + ".jpg");

            importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Glide.with(MedicationEdit.this)
                            .load(downloadUrl)
                            .dontAnimate()
                            .into(medicationImage);
                }
            });

            medicationHour = medicationInEdit.getMedicationHour();
            medicationMinute = medicationInEdit.getMedicationMinute();
            currentHour = Integer.parseInt(medicationHour);
            currentMinute = Integer.parseInt(medicationMinute);
            setTimeTextView(currentHour, currentMinute);

            medicationDays = new ArrayList<Boolean>();
            medicationDays = medicationInEdit.getMedicationDays();
            daysChecked = new boolean[medicationDays.size()];
            for(int i = 0; i < medicationDays.size(); i++)
            {
                daysChecked[i] = medicationDays.get(i);
            }
            setDates(daysChecked);

        } else {
            //if a new medication is being added
            medicationId = userReference.push().getKey();
            medicationTitleTextView.setText("");
            medicationDescriptionTextView.setText("");
            currentHour = 0;
            currentMinute = 0;
            medicationHour = "0";
            medicationMinute = "0";
            daysChecked = new boolean[]{false, false, false, false, false, false, false};
            setDates(daysChecked);
            medicationDays = new ArrayList<Boolean>();
            for(int index =0; index < daysChecked.length; index++) {
                medicationDays.add(daysChecked[index]);
            }
        }

        medicationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(MedicationEdit.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                medicationHour = Integer.toString(hourOfDay);
                                medicationMinute = Integer.toString(minute);

                                currentHour = hourOfDay;
                                currentMinute = minute;

                                setTimeTextView(hourOfDay, minute);

                            }
                        }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });

        //temp placement for boolean position
        //first position represents monday, last position represents sunday

        datesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] daysOfWeek = getResources().getStringArray(R.array.daysinweek);
                AlertDialog.Builder builder = new AlertDialog.Builder(MedicationEdit.this);
                builder.setCancelable(false);
                builder.setTitle("Choose Days");
                builder.setMultiChoiceItems(daysOfWeek, daysChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            daysChecked[position] = true;
                        }
                        if(!isChecked){
                            daysChecked[position] = false;
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {

                        //Convert array of boolean into arraylist for the days which are selected
                        medicationDays = new ArrayList<Boolean>();
                        for(int index =0; index < daysChecked.length; index++) {
                            medicationDays.add(daysChecked[index]);
                        }

                        setDates(daysChecked);

                    }
                });
                builder.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medicationTitleTextView.getText().toString().isEmpty()){
                    Toast.makeText(MedicationEdit.this, "Enter a medication title", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    medicationTitle = medicationTitleTextView.getText().toString();
                }

                medicationDescription = medicationDescriptionTextView.getText().toString();

                if(datesTextView.getText().toString().equals("Enter dates here.")){
                    Toast.makeText(MedicationEdit.this, "Select days for medicine consumption", Toast.LENGTH_SHORT).show();
                    return;
                }

                medicationInEdit = new Medication(
                        medicationId,
                        medicationTitle,
                        medicationDescription,
                        medicationHour,
                        medicationMinute,
                        medicationDays);


                userReference.child(medicationId).setValue(medicationInEdit);

                if (uri != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(MedicationEdit.this);
                    progressDialog.setMessage("Uploading...");
                    progressDialog.show();

                    StorageReference exportReference = storageReference.child(medicationId + ".jpg");

                    StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = exportReference.putFile(uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MedicationEdit.this, "File uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MedicationEdit.this, "Upload failed", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    Intent intent = new Intent(MedicationEdit.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                } else {
                    Intent intent = new Intent(MedicationEdit.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                medicationImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Permission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void setTimeTextView(int numHour, int numMinute) {

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

        timeTextView.setText(hourString + ":" + minuteString);

        if (isMorning == true) {
            timeTextView.append(" AM");
        } else {
            timeTextView.append(" PM");
        }
    }

    private void setDates(boolean[] dates){
        datesTextView.setText("");
        for(int i =0; i<7; i++){
            if(dates[i] == true){
                switch (i){
                    case 0:datesTextView.append("Mon ");    continue;
                    case 1:datesTextView.append("Tues ");   continue;
                    case 2:datesTextView.append("Wed ");    continue;
                    case 3:datesTextView.append("Thurs ");  continue;
                    case 4:datesTextView.append("Fri ");    continue;
                    case 5:datesTextView.append("Sat ");    continue;
                    case 6:datesTextView.append("Sun ");    continue;
                }
            }
        }
        if(datesTextView.getText().toString().equals("")){
            datesTextView.setText("Enter dates here.");
        }
    }

}


/* FOR REFERENCE
    private void getUrl(StorageReference storageReference){

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Log.i("downlaod123 url is ", downloadUrl.toString());
                imageUrlString = downloadUrl.toString();
                Log.i("image url is", imageUrlString);
            }
        });

    }
*/