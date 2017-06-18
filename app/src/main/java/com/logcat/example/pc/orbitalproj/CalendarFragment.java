package com.logcat.example.pc.orbitalproj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarFragment extends Fragment implements MonthLoader.MonthChangeListener, WeekView.EmptyViewClickListener {

    private WeekView mWeekView;
    private ArrayList<WeekViewEvent> mNewEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_calendar, container, false);
        mWeekView = (WeekView) mView.findViewById(R.id.weekView);

        //set listener for month change
        mWeekView.setMonthChangeListener(this);

        mWeekView.setEmptyViewClickListener(this);

        // Initially, there will be no events on the week view because the user has not tapped on
        // it yet.
        mNewEvents = new ArrayList<WeekViewEvent>();

        //return view
        return mView;
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        //for loop firebase medication. Run through each medication date

            //for loop if repetition

        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }

        // Get the starting point and ending point of the specific medicine.
        Calendar startOfEvent = Calendar.getInstance();
        startOfEvent.set(Calendar.YEAR, startYearEvent);  //year== startyear
        startOfEvent.set(Calendar.MONTH, startMonthEvent - 1); //month == startmonth
        startOfEvent.set(Calendar.DATE, 1);
        startOfEvent.set(Calendar.HOUR_OF_DAY, 0);
        startOfEvent.set(Calendar.MINUTE, 0);
        startOfEvent.set(Calendar.SECOND, 0);
        startOfEvent.set(Calendar.MILLISECOND, 0);
        Calendar endOfEvent = (Calendar) startOfEvent.clone();
        endOfEvent.set(Calendar.YEAR,endYearEvent); //end Year== end of event
        endOfEvent.set(Calendar.MONTH, endMonthEvent); //endMonth== end of event
        endOfEvent.set(Calendar.DATE,endDateEvent);
        endOfEvent.set(Calendar.HOUR_OF_DAY, 23);
        endOfEvent.set(Calendar.MINUTE, 59);
        endOfEvent.set(Calendar.SECOND, 59);



        // Populate the week view with events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DATE,16);
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(3, "hihi", startTime, endTime);
        events.add(event);

        return events;
    }


    @Override
    public void onEmptyViewClicked(Calendar time) {

        Calendar endTime = (Calendar) time.clone();
        endTime.add(Calendar.HOUR, 1);

        // Create a new event.
        WeekViewEvent event = new WeekViewEvent(1, "New event", time, endTime);
        mNewEvents.add(event);

        // Refresh the week view. onMonthChange will be called again.
        mWeekView.notifyDatasetChanged();

    }

    private ArrayList<WeekViewEvent> getNewEvents(int year, int month) {

        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 0);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : mNewEvents) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);
            }
        }
        return events;
    }

}