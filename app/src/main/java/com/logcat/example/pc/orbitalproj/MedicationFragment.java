package com.logcat.example.pc.orbitalproj;

/**
 * Created by PC on 6/2/2017.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MedicationFragment extends Fragment {

    public MedicationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medication, container, false);
    }
}