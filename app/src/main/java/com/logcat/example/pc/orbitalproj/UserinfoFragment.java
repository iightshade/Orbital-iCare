package com.logcat.example.pc.orbitalproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class UserinfoFragment extends Fragment {


    public UserinfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_userinfo, container, false);

        FirebaseAuth userAuth;
        FirebaseUser firebaseUser;

        FirebaseDatabase firebaseDatabase;
        DatabaseReference userInfoReference;

        final String userId;

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userInfoReference = firebaseDatabase.getReference(userId + "info");

        final TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        final TextView icTextView = (TextView) view.findViewById(R.id.icTextView);
        final TextView ageTextView = (TextView) view.findViewById(R.id.ageTextView);
        final TextView sexTextView = (TextView) view.findViewById(R.id.sexTextView);
        final TextView allergiesTextView = (TextView) view.findViewById(R.id.allergiesTextView);
        final TextView chronicTextView = (TextView) view.findViewById(R.id.chronicTextView);
        final TextView otherTextView = (TextView) view.findViewById(R.id.otherTextView);
        Button editUserInfoButton = (Button) view.findViewById(R.id.editUserInfoButton);


        userInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                nameTextView.setText(userInfo.getUserName());
                icTextView.setText(userInfo.getUserIC());
                ageTextView.setText(userInfo.getUserAge());
                sexTextView.setText(userInfo.getUserSex());
                allergiesTextView.setText(userInfo.getUserAllergies());
                chronicTextView.setText(userInfo.getUserChronic());
                otherTextView.setText(userInfo.getUserOther());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        editUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfoParcel = new UserInfo(
                        nameTextView.getText().toString(),
                        icTextView.getText().toString(),
                        ageTextView.getText().toString(),
                        sexTextView.getText().toString(),
                        allergiesTextView.getText().toString(),
                        chronicTextView.getText().toString(),
                        otherTextView.getText().toString()
                        );

                Intent intent = new Intent(getActivity(), UserInfoEdit.class);
                intent.putExtra("UserInfo", userInfoParcel);
                startActivity(intent);
            }
        });

        return view;
    }
}