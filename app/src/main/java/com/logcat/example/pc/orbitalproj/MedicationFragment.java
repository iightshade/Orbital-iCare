package com.logcat.example.pc.orbitalproj;

/**
 * Created by PC on 6/2/2017.
 */


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MedicationFragment extends Fragment {

    private static TextView medicalTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        medicalTextView = (TextView)view.findViewById(R.id.medicalTextView);

        final Button changeButton = (Button)view.findViewById(R.id.changeButton);

        changeButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        medicalTextView.setText("Changed");
                    }
                }
        );
        return view;

    }


}