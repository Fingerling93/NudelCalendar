package nudelsquad.nudelcalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Marco on 04.05.2016.
 */
public class CreateTaskView extends Fragment {
    private View rootView;
    private EditText taskName;
    private EditText taskDate;
    private EditText color2;
    private CheckBox remind;
    private DatePickerDialog  eventDatePickerDialog;
    private DateFormat dateFormater;
    private ColorPickerDialog colPicker;
    int color = Color.parseColor("#33b5e5");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.create_task_fragement, container, false);
        findViewsById();
        dateFormater = new SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN);

        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colPicker = new ColorPickerDialog(rootView.getContext(), color);
                colPicker.setAlphaSliderVisible(true);
                colPicker.setHexValueEnabled(true);
                colPicker.setTitle("Farbe auswählen");
                colPicker.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int i) {
                        color = i;
                        color2.setText("#" + Integer.toHexString(i));
                        color2.setBackgroundColor(i);
                    }
                });
                colPicker.show();
            }
        });

        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                eventDatePickerDialog = new DatePickerDialog(rootView.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        taskDate.setText(dateFormater.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                eventDatePickerDialog.show();
            }
        });

        Button addTaskButton = (Button) rootView.findViewById(R.id.addTaskBtn);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskName.toString().isEmpty() || taskDate.toString().isEmpty()){
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(rootView.getContext());
                    alert1.setMessage("Missing required data!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert2 = alert1.create();
                    alert2.setTitle("ALERT");
                    alert2.show();
                } else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(rootView.getContext());
                    alert1.setMessage("Save Task??!")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Add function to save task into Database
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert3 = alert1.create();
                    alert3.setTitle("ADD TASK");
                    alert3.show();
                }
            }
        });

        Button discard = (Button) rootView.findViewById(R.id.discardBtn);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_frame, new CreateEventView(), "NewFragmentTag");
                ft.commit();
            }
        });

        return rootView;
    }

    private void findViewsById(){
        taskName = (EditText) rootView.findViewById(R.id.taskName);
        taskDate = (EditText) rootView.findViewById(R.id.taskDate);
        taskDate.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.calendar), null);
        taskDate.setInputType(InputType.TYPE_NULL);
        taskDate.requestFocus();
        color2 = (EditText) rootView.findViewById(R.id.editText2);
        color2.setInputType(InputType.TYPE_NULL);
        color2.requestFocus();
        remind = (CheckBox) rootView.findViewById(R.id.reminder);
    }

}
