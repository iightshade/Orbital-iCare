package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserInfoEdit extends AppCompatActivity{

    FirebaseAuth userAuth;
    FirebaseUser firebaseUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userInfoReference;

    String userId;
    UserInfo userInfo;

    String userName;
    String userIC;
    String userAge;
    String userSex;
    String userAllergies;
    String userChronic;
    String userOther;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.userinfo_edit);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userInfoReference = firebaseDatabase.getReference(userId + "info");

        Button saveUserInfoButton = (Button) findViewById(R.id.saveUserInfoButton);
        Button cancelUserInfoButton = (Button) findViewById(R.id.cancelUserInfoButton);
        final EditText nameEditText = (EditText) findViewById(R.id.nameTextView);
        final EditText icEditText = (EditText) findViewById(R.id.icTextView);
        final EditText ageEditText = (EditText) findViewById(R.id.ageTextView);
        final EditText sexEditText = (EditText) findViewById(R.id.sexTextView);
        final EditText allergiesEditText = (EditText) findViewById(R.id.allergiesTextView);
        final EditText chronicEditText = (EditText) findViewById(R.id.chronicTextView);
        final EditText otherEditText = (EditText) findViewById(R.id.otherTextView);

        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("UserInfo");

        nameEditText.setText(userInfo.getUserName());
        icEditText.setText(userInfo.getUserIC());
        ageEditText.setText(userInfo.getUserAge());
        sexEditText.setText(userInfo.getUserSex());
        allergiesEditText.setText(userInfo.getUserAllergies());
        chronicEditText.setText(userInfo.getUserChronic());
        otherEditText.setText(userInfo.getUserOther());

        saveUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = nameEditText.getText().toString();
                userIC = icEditText.getText().toString();
                userAge = ageEditText.getText().toString();
                userSex = sexEditText.getText().toString();
                userAllergies = allergiesEditText.getText().toString();
                userChronic = chronicEditText.getText().toString();
                userOther = otherEditText.getText().toString();

                userInfo = new UserInfo(
                        userName,
                        userIC,
                        userAge,
                        userSex,
                        userAllergies,
                        userChronic,
                        userOther
                );

                userInfoReference.setValue(userInfo);

                Toast.makeText(getApplicationContext(), "User information saved", Toast.LENGTH_SHORT).show();

                UserInfoEdit.super.onBackPressed();
                finish();
            }
        });

        cancelUserInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoEdit.super.onBackPressed();
            }
        });
    }
}
