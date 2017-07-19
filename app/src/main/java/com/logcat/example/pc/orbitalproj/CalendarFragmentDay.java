package com.logcat.example.pc.orbitalproj;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarFragmentDay extends Fragment implements MonthLoader.MonthChangeListener, WeekView.EmptyViewClickListener, WeekView.EventClickListener {

    private WeekView mWeekView;
    private List<WeekViewEvent> newEvents= new ArrayList<WeekViewEvent>();
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

        mWeekView.setNumberOfVisibleDays(1);

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

                    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

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

                    if (count==dataSnapshot.getChildrenCount()){
                        mWeekView.notifyDatasetChanged();
                    }


                    LocalDate endDate=new LocalDate(temp.getMedicationEndYear(),temp.getMedicationEndMonth()+1,temp.getMedicationEndDay()+1);
                    LocalDate startDate=new LocalDate(temp.getMedicationStartYear(),temp.getMedicationStartMonth()+1,temp.getMedicationStartDay());

                    for (WeekViewEvent event : events) {
                        Calendar dateTime = event .getStartTime();
                        Calendar dateEndTime = event .getEndTime();
                        Calendar monCal = getFirstDay(newMonth - 1, newYear, dateTime.get(Calendar.DAY_OF_WEEK));
                        int hday = dateTime.get(Calendar.HOUR_OF_DAY);
                        int mday = dateTime.get(Calendar.MINUTE);
                        int ehday = dateEndTime.get(Calendar.HOUR_OF_DAY);
                        int emday = dateEndTime.get(Calendar.MINUTE);

                        for (LocalDate date = new LocalDate(monCal); date.isBefore(endDate); date = date.plusDays(7)) {

                            List<WeekViewEvent> looperEvents = new ArrayList<WeekViewEvent>();

                            Calendar startTime = Calendar.getInstance();
                            startTime.set(Calendar.MONTH, newMonth - 1);
                            startTime.set(Calendar.DAY_OF_MONTH, date.getDayOfMonth());
                            startTime.set(Calendar.YEAR, newYear);
                            startTime.set(Calendar.HOUR_OF_DAY, hday);
                            startTime.set(Calendar.MINUTE, mday);
                            startTime.set(Calendar.SECOND, 0);
                            startTime.set(Calendar.MILLISECOND, 0);

                            Calendar endTime = (Calendar) startTime.clone();
                            endTime.set(Calendar.HOUR_OF_DAY, ehday);
                            endTime.set(Calendar.MINUTE, emday - 1);
                            endTime.set(Calendar.MONTH, newMonth - 1);
                            endTime.set(Calendar.SECOND, 59);
                            endTime.set(Calendar.MILLISECOND, 999);

                            WeekViewEvent newEvent = new WeekViewEvent(1, event .getName(), startTime, endTime);

                            looperEvents.add(newEvent);
                            for (WeekViewEvent kEvent : looperEvents) {
                                if (eventMatches(kEvent, newYear, newMonth) && (new LocalDate(kEvent.getStartTime()).isAfter(startDate)) || new LocalDate(kEvent.getStartTime()).isEqual(startDate)){
                                    newEvents.add(kEvent);
                                }
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        for (WeekViewEvent event : newEvents) {
            if (eventMatches(event, newYear, newMonth)) {
                matchedEvents.add(event);
            }
        }


        return matchedEvents;
    }


    public static Calendar getFirstDay(int i2, int i, int weekday) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, i2);
        c.set(Calendar.YEAR, i);
        c.set(Calendar.DAY_OF_MONTH, 1);
        int day = c.get(Calendar.DAY_OF_WEEK);
        while (day != weekday) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            day = c.get(Calendar.DAY_OF_WEEK);
        }
        return c;
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {

        Intent intent = new Intent(getActivity(), MedicationEdit.class);
        intent.putExtra("Medicine", "CalendarFragmentDay");
        startActivity(intent);

    }




    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month- 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }
}