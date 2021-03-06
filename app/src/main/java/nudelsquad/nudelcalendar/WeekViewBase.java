package nudelsquad.nudelcalendar;

import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.*;
import com.alamkanak.weekview.WeekView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Marco on 27.04.2016.
 */
public class WeekViewBase extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
        private static final int TYPE_DAY_VIEW = 1;
        private static final int TYPE_THREE_DAY_VIEW = 2;
        private static final int TYPE_WEEK_VIEW = 3;
        private int mWeekViewType = TYPE_THREE_DAY_VIEW;
        private com.alamkanak.weekview.WeekView mWeekView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.week_fragment, container, false);
        // Get a reference for the week view in the layout.
        mWeekView = (com.alamkanak.weekview.WeekView) rootView.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional



        setupDateTimeInterpreter(false);
        mWeekView.notifyDatasetChanged();

        /*
        if (mWeekViewType != TYPE_WEEK_VIEW) {
            mWeekViewType = TYPE_WEEK_VIEW;
            mWeekView.setNumberOfVisibleDays(7);

            // Lets change some dimensions to best fit the view.
            mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
            mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        }
        */
        return rootView;
    }


            /**
             * Set up a date time interpreter which will show short date values when in week view and long
             * date values otherwise.
             * @param shortDate True if the date values should be short.
             */
        private void setupDateTimeInterpreter(final boolean shortDate) {
            mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
                @Override
                public String interpretDate(Calendar date) {
                    //SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                    //String weekday = weekdayNameFormat.format(date.getTime());
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM", Locale.getDefault());

                    // All android api level do not have a standard way of getting the first letter of
                    // the week day name. Hence we get the first char programmatically.
                    // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                    //if (shortDate)
                      //  weekday = String.valueOf(weekday.charAt(0));

                    //return weekday.toUpperCase() + format.format(date.getTime());
                    //Log.e("Date", "" + format.format((date.getTime())));
                    return format.format(date.getTime());
                }

                @Override
                public String interpretTime(int hour) {
                    if (hour < 10)
                        return "0" + hour + ":00";
                    return hour + ":00";
                }
            });
        }

        protected String getEventTitle(Calendar time) {
            return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        public void onEventClick(WeekViewEvent event, RectF eventRect) {                //go to Event

            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, new TaskLookView(), "NewFragmentTag");
            ft.commit();
        }

        @Override
        public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
            Toast.makeText(rootView.getContext(), "is needed?", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmptyViewLongPress(Calendar time) {
            Toast.makeText(rootView.getContext(), "is needed?", Toast.LENGTH_SHORT).show();
        }

        public com.alamkanak.weekview.WeekView getWeekView() {
            return mWeekView;
        }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(Color.RED);
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(Color.BLUE);
        events.add(event);

        return events;
    }
}
