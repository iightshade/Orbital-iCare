package com.logcat.example.pc.orbitalproj;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

public class SettingsFragment extends Fragment {

    private Button signOut, revokeGoogleAccess, deleteAccountButton;
    private GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar); // need to fix
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar); //need to fix

        signOut = (Button) mView.findViewById(R.id.sign_out);
        revokeGoogleAccess = (Button) mView.findViewById(R.id.revokeGoogleAccess);
        deleteAccountButton = (Button) mView.findViewById(R.id.deleteFirebaseAccountButton);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String providerId = user.getProviders().get(0);

        if (providerId.equals("google.com")) {
            revokeGoogleAccess.setVisibility(View.VISIBLE);
            deleteAccountButton.setVisibility(View.GONE);
        } else {
            deleteAccountButton.setVisibility(View.VISIBLE);
            revokeGoogleAccess.setVisibility(View.GONE);
        }

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (providerId.equals("password")) {
                    signOut();
                } else {
                    signOutGoogle();
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        });


        revokeGoogleAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEverything();
                //placed into the end of removeEverything function
                /*revokeAccess();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }*/
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeEverything();

            }
        });

        return mView;
    }


    @Override
    public void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();

    }


    //sign out method
    public void signOut() {

        FirebaseAuth.getInstance().signOut();

    }


    private void signOutGoogle() {
        // Firebase sign out
        FirebaseAuth.getInstance().signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });
    }

    private boolean removeEverything(){

        FirebaseUser firebaseUser;
        String userId;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(userId);
        final DatabaseReference databaseInfoReference = FirebaseDatabase.getInstance().getReference(userId + "info");
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference(userId);
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AppTheme2));
        builder.setTitle("Delete account?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Deleting");
                progressDialog.show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int keyCounter = 0;
                        for (DataSnapshot categoriesSnapShot : dataSnapshot.getChildren()) {

                            Intent intent = new Intent(getActivity(), NotificationBroadcast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), keyCounter, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            pendingIntent.cancel();

                            keyCounter++;
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (databaseReference != null) {
                    databaseReference.removeValue();
                }

                if(databaseInfoReference !=null){
                    databaseInfoReference.removeValue();
                }

                if (storageReference != null) {
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                            if(deleteAccountButton.getVisibility() == View.VISIBLE) {
                                deleteEmailAccount();
                            }else{
                                deleteGoogleAccount();
                            }
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Account deleted", Toast.LENGTH_SHORT).show();
                            if(deleteAccountButton.getVisibility() == View.VISIBLE) {
                                deleteEmailAccount();
                            }else{
                                deleteGoogleAccount();
                            }
                        }
                    });
                }
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

    private void deleteEmailAccount(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        signOut();

        firebaseUser.delete();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void deleteGoogleAccount(){
        revokeAccess();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}


