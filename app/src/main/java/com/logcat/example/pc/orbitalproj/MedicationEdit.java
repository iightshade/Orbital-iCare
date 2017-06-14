package com.logcat.example.pc.orbitalproj;


import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
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
    ImageView medicationImage;
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
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        verifyStoragePermissions(this);

        Intent intent = getIntent();
        medicationInEdit = intent.getParcelableExtra("Medicine");

        if (medicationInEdit != null) {

            medicationTitleTextView.setText(medicationInEdit.getMedicationTitle());
            medicationDescriptionTextView.setText(medicationInEdit.getMedicationDescription());
            medicationId = medicationInEdit.getMedicationId();

            StorageReference importReference = storageReference.child(medicationId + ".jpg");

            importReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
            {
                @Override
                public void onSuccess(Uri downloadUrl)
                {
                    Glide.with(MedicationEdit.this)
                            .load(downloadUrl)
                            .dontAnimate()
                            .into(medicationImage);
                }
            });

        } else {
            medicationId = userReference.push().getKey();
        }

        medicationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                medicationInEdit = new Medication(
                        medicationId,
                        medicationTitleTextView.getText().toString(),
                        medicationDescriptionTextView.getText().toString());
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
                                    Toast.makeText(MedicationEdit.this, "file uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(MedicationEdit.this, "file failed", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MedicationEdit.this, "Failed\nPlease enter an image", Toast.LENGTH_SHORT).show();
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