package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminMainActivity extends AppCompatActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);
        setTheme(R.style.AppTheme2);

        Button adminSignOutButton = (Button) findViewById(R.id.adminSignOutButton);
        Button adminAccessButton = (Button) findViewById(R.id.adminAccessButton);
        EditText usersNameEditText = (EditText) findViewById(R.id.usersNameEditText);

        adminSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(AdminMainActivity.this , LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
