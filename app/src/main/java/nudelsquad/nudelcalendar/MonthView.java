package nudelsquad.nudelcalendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sathearo on 27.04.2016.
 */
public class MonthView extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.month_fragment, container, false);

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);


        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.calendarFragment, caldroidFragment, "NewFragmentTag");
        ft.commit();

        caldroidFragment.refreshView();

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                String month = (String) android.text.format.DateFormat.format("MM", date);
                String year = (String) android.text.format.DateFormat.format("yyyy", date);
                String day = (String) android.text.format.DateFormat.format("dd", date);
                String selectedDate = day + "-" + month + "-" + year;
                MainActivity.myBundle.putString("selectedDate", String.valueOf(selectedDate));

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_frame, new DayList(), "NewFragmentTag");
                ft.commit();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                //String text = "month: " + month + " year: " + year;
                //Toast.makeText(rootView.getContext(), text,
                //Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                //Toast.makeText(rootView.getContext(),
                //      "Long click " + "Date = " + date,
                //      Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                //Toast.makeText(rootView.getContext(),
                //"Caldroid view is created",
                //Toast.LENGTH_SHORT).show();
            }

        };

        caldroidFragment.setCaldroidListener(listener);

        return rootView;
    }


    ;

}
