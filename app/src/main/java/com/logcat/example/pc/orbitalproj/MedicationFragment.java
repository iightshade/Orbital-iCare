package com.logcat.example.pc.orbitalproj;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.logcat.example.pc.orbitalproj.R.layout.add_new_medication;
import static com.logcat.example.pc.orbitalproj.R.layout.medication_edit;


public class MedicationFragment extends Fragment {

    FirebaseAuth userAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    String userId;
    String medicationIdTest;
    Medication medicationId;
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

        /*
        medicationIdTest = userReference.push().getKey();
        temp = new Medication(medicationIdTest, "So much", "medication");
        tempList= new ArrayList<Medication>();
        tempList.add(temp);
        userReference.child(medicationIdTest).setValue(temp);
        */

        //For adding some items to database, to be removed and referenced into 'add medication'

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_medication,container, false);
        medicationGridView = (GridView)view.findViewById(R.id.medicationGridView);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                tempList = new ArrayList<Medication>();
                tempList.clear();

                for(DataSnapshot categoriesSnapShot : dataSnapshot.getChildren()) {

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

                    Intent intent = new Intent(getActivity(), MedicationDialog.class);
                    medicationId = tempList.get(position);
                    intent.putExtra("Medicine", medicationId);
                    startActivity(intent);

                }
            }
        });

    }

}