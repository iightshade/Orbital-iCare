package com.logcat.example.pc.orbitalproj;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        String activityString = getIntent().getStringExtra("position");
        if (activityString == null) {
            fragment = new MedicationFragment();

        } else if (activityString.equals("Calendar3Day")) {
            Bundle bundle = new Bundle();
            bundle.putString("message", "Calendar3Day");
            //set Fragmentclass Arguments
            fragment = new CalendarMainFragment();
            fragment.setArguments(bundle);

        } else if (activityString.equals("CalendarDay")) {
            Bundle bundle = new Bundle();
            bundle.putString("message", "CalendarDay");
            //set Fragmentclass Arguments
            fragment = new CalendarMainFragment();
            fragment.setArguments(bundle);

        } else if (activityString.equals("CalendarWeek")) {
            Bundle bundle = new Bundle();
            bundle.putString("message", "CalendarWeek");
            //set Fragmentclass Arguments
            fragment = new CalendarMainFragment();
            fragment.setArguments(bundle);
        }


        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.medication:
                                fragment = new MedicationFragment();
                                break;
                            case R.id.calendar:
                                fragment = new CalendarMainFragment();
                                break;
                            case R.id.userInfo:
                                fragment = new UserinfoFragment();
                                break;
                            case R.id.settings:
                                fragment = new SettingsFragment();
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}