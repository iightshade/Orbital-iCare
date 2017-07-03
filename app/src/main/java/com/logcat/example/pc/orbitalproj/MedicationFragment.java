package com.logcat.example.pc.orbitalproj;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class MedicationFragment extends Fragment {

    FirebaseAuth userAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    StorageReference storageReference;

    String userId;

    Medication medication;
    Medication temp;
    ArrayList<Medication> tempList;

    GridView medicationGridView;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();

        userId = firebaseUser.getUid();
        userReference = firebaseDatabase.getReference(userId);
        storageReference = FirebaseStorage.getInstance().getReference(userId);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medication, container, false);
        medicationGridView = (GridView) view.findViewById(R.id.medicationGridView);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tempList = new ArrayList<Medication>();
                tempList.clear();

                for (DataSnapshot categoriesSnapShot : dataSnapshot.getChildren()) {

                    temp = categoriesSnapShot.getValue(Medication.class);

                    tempList.add(temp);

                }

                Medication addNewMedication = null;
                tempList.add(addNewMedication);

                MedicationViewAdapter adapter = new MedicationViewAdapter(getActivity(), tempList);
                medicationGridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    ;

    @Override
    public void onStart() {
        super.onStart();
        medicationGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (tempList.size() == (position + 1)) {
                    Intent intent = new Intent(getActivity(), MedicationEdit.class);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(getActivity(), MedicationViewer.class);
                    medication = tempList.get(position);
                    intent.putExtra("Medicine", medication);
                    startActivity(intent);

                }
            }
        });

        medicationGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                if(tempList.size() == (position + 1)){
                    return false;
                }

                final PopupMenu popupMenu = new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.medication_popup, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().toString().equals("Delete")) {
                            ConfirmDelete(position);
                            popupMenu.dismiss();
                        }

                        if(item.getTitle().toString().equals("Edit")){
                            Intent intent = new Intent(getActivity() , MedicationEdit.class);
                            medication = tempList.get(position);
                            intent.putExtra("Medicine", medication);
                            startActivity(intent);
                        }

                        return true;
                    }
                });

                popupMenu.show();
                return true;
            }
        });


    }

    private boolean ConfirmDelete(Integer position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme2));
        builder.setTitle("Delete medication?");
        final String dialogMessage = tempList.get(position).getMedicationTitle();
        final String medicationId = tempList.get(position).getMedicationId();
        builder.setMessage(dialogMessage);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading");
                progressDialog.show();

                userReference.child(medicationId).removeValue();
                storageReference.child(medicationId + ".jpg").delete().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Medication deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Medication deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.dismiss();
            }
        });

        builder.show();

        return true;

    }


}