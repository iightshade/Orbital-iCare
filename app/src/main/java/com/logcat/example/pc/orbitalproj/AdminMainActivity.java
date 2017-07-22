package com.logcat.example.pc.orbitalproj;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {

    FirebaseAuth userAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference generalDatabaseReference;
    DatabaseReference userReference;

    String userId;
    String usersNRIC;
    Medication medication;
    ArrayList<Medication> tempList;
    ArrayList<String> stringList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main_activity);
        setTheme(R.style.AppTheme2);

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();
        generalDatabaseReference = firebaseDatabase.getReference();

        Button adminSignOutButton = (Button) findViewById(R.id.adminSignOutButton);
        Button adminAccessButton = (Button) findViewById(R.id.adminAccessButton);
        final Button adminAddNewButton = (Button) findViewById(R.id.adminAddNewButton);
        final EditText usersNameEditText = (EditText) findViewById(R.id.usersNameEditText);
        final ListView adminListView = (ListView) findViewById(R.id.adminListView);

        adminAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersNRIC = usersNameEditText.getText().toString();
                generalDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot categoriesSnapShot : dataSnapshot.getChildren()) {

                            if (categoriesSnapShot.getKey().endsWith("info")) {
                                if (categoriesSnapShot.getValue(UserInfo.class).getUserIC().trim().equalsIgnoreCase(usersNRIC)) {

                                    userId = categoriesSnapShot.getKey().substring(0, categoriesSnapShot.getKey().length() - 4);

                                    userReference = firebaseDatabase.getReference(userId);

                                    userReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            tempList = new ArrayList<Medication>();
                                            tempList.clear();
                                            stringList = new ArrayList<String>();
                                            stringList.clear();

                                            for (DataSnapshot categoriesSnapShot : dataSnapshot.getChildren()) {

                                                tempList.add(categoriesSnapShot.getValue(Medication.class));
                                                stringList.add(categoriesSnapShot.getValue(Medication.class).getMedicationTitle());

                                            }

                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AdminMainActivity.this, android.R.layout.simple_list_item_1, stringList);
                                            adminListView.setAdapter(arrayAdapter);

                                            adminListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent = new Intent(AdminMainActivity.this, AdminMedicationEdit.class);
                                                    medication = tempList.get(position);
                                                    intent.putExtra("Medicine", medication);
                                                    intent.putExtra("UserId", userId);
                                                    startActivity(intent);
                                                }
                                            });

                                            adminAddNewButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(AdminMainActivity.this, AdminMedicationEdit.class);
                                                    medication = null;
                                                    intent.putExtra("Medicine", medication);
                                                    intent.putExtra("UserId", userId);
                                                    startActivity(intent);
                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        adminSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(AdminMainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
