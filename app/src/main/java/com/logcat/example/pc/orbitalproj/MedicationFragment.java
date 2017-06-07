package com.logcat.example.pc.orbitalproj;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MedicationFragment extends Fragment {

    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userReference;

    String userId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();

        userId = firebaseUser.getUid();
        userReference = firebaseDatabase.getReference(userId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view;
        view = inflater.inflate(R.layout.fragment_medication,container, false);
        GridView medicationGridView = (GridView)view.findViewById(R.id.medicationGridView);
        Medication temp = new Medication("So much", "MEDICATION");

        //Testing interface, to be removed.
        List<Medication> tempList= new ArrayList<Medication>();
        tempList.add(temp);
        MedicationViewAdapter adapter = new MedicationViewAdapter(getActivity(), tempList);
        medicationGridView.setAdapter(adapter);

        userReference.child("Medicine number").setValue(temp);

        //medicationGridView.setAdapter(new MedicationViewAdapter(this.getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}