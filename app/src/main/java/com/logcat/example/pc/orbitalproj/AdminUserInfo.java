package com.logcat.example.pc.orbitalproj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by PC on 7/24/2017.
 */

public class AdminUserInfo extends AppCompatActivity {

    String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppTheme2);
        setContentView(R.layout.admin_userinfo);
        getSupportActionBar().setTitle("User Info");

        Intent intent = getIntent();
        userId = intent.getStringExtra("UserId");

        FirebaseDatabase firebaseDatabase;
        DatabaseReference userInfoReference;


        firebaseDatabase = FirebaseDatabase.getInstance();
        userInfoReference = firebaseDatabase.getReference(userId + "info");

        final TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        final TextView icTextView = (TextView) findViewById(R.id.icTextView);
        final TextView ageTextView = (TextView) findViewById(R.id.ageTextView);
        final TextView sexTextView = (TextView) findViewById(R.id.sexTextView);
        final TextView allergiesTextView = (TextView) findViewById(R.id.allergiesTextView);
        final TextView chronicTextView = (TextView) findViewById(R.id.chronicTextView);
        final TextView otherTextView = (TextView) findViewById(R.id.otherTextView);
        Button editUserInfoButton = (Button) findViewById(R.id.editUserInfoButton);

        userInfoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                if(userInfo != null) {
                    nameTextView.setText(userInfo.getUserName());
                    icTextView.setText(userInfo.getUserIC());
                    ageTextView.setText(userInfo.getUserAge());
                    sexTextView.setText(userInfo.getUserSex());
                    allergiesTextView.setText(userInfo.getUserAllergies());
                    chronicTextView.setText(userInfo.getUserChronic());
                    otherTextView.setText(userInfo.getUserOther());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        editUserInfoButton.setText("Return");

        editUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

