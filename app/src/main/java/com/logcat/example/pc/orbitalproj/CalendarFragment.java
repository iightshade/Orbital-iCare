package com.logcat.example.pc.orbitalproj;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarFragment extends Fragment implements MonthLoader.MonthChangeListener, WeekView.EmptyViewClickListener, WeekView.EventClickListener {

    private WeekView mWeekView;
    private List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    private ArrayList<Boolean> medicationDays;

    FirebaseAuth userAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;

    String userId;

    int count=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = userAuth.getCurrentUser();

        userId = firebaseUser.getUid();
        userReference = firebaseDatabase.getReference(userId);

        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mWeekView = (WeekView) mView.findViewById(R.id.weekView);

        //set listener for month change
        mWeekView.setMonthChangeListener(this);

        // Set up empty view click listener.
        mWeekView.setEmptyViewClickListener(this);

        mWeekView.setOnEventClickListener(this);

        //return view
        return mView;
    }



    @Override
    public List<WeekViewEvent> onMonthChange(final int newYear, final int newMonth) {

        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            count++;

                            Medication temp = snapshot.getValue(Medication.class);

                            medicationDays=temp.getMedicationDays();

                            for (Integer i=0 ; i<medicationDays.size() ; i++){

                                List<WeekViewEvent> loopEvents = new ArrayList<WeekViewEvent>();

                                if (medicationDays.get(i)){

                                    int tempUse = i + 2;

                                    Calendar startTime = Calendar.getInstance();
                                    startTime.set(Calendar.DAY_OF_WEEK, tempUse );
                                    startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp.getMedicationHour()));
                                    startTime.set(Calendar.MINUTE, Integer.parseInt(temp.getMedicationMinute()));
                                    startTime.set(Calendar.MONTH, newMonth - 1);
                                    startTime.set(Calendar.YEAR, newYear);
                                    Calendar endTime = (Calendar) startTime.clone();
                                    endTime.add(Calendar.MINUTE, 30 );
                                    endTime.set(Calendar.MONTH, newMonth - 1);




                                    WeekViewEvent mEvent = new WeekViewEvent(1, temp.getMedicationTitle(), startTime, endTime);

                                    loopEvents.add(mEvent);
                                    for (WeekViewEvent pEvent : loopEvents) {
                                        if (eventMatches(pEvent, newYear, newMonth)) {
                                            events.add(pEvent);

                                }
                            }
                                }
                            }
                        }

                        if (count==dataSnapshot.getChildrenCount()){
                            mWeekView.notifyDatasetChanged();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }


        return matchedEvents;

    }

    @Override
    public void onEmptyViewClicked(Calendar time) {

        Intent intent = new Intent(getActivity(), MedicationEdit.class);
        startActivity(intent);


        /*// Set the new event with duration one hour.
        Calendar endTime = (Calendar) time.clone();
        endTime.add(Calendar.HOUR, 1);

        // Create a new event.
        WeekViewEvent event = new WeekViewEvent(20, "New event", time, endTime);
        mNewEvents.add(event);*/

        // Refresh the week view. onMonthChange will be called again.
        //mWeekView.notifyDatasetChanged();
    }




    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month- 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }
}