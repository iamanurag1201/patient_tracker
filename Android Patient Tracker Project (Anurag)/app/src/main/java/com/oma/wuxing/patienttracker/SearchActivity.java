package com.oma.wuxing.patienttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class SearchActivity extends ActionBarActivity {

    private EditText patientNameEditText;
    private EditText arrivalDateFromEditText;
    private EditText arrivalTimeFromEditText;
    private EditText arrivalDateToEditText;
    private EditText arrivalTimeToEditText;
    private EditText diseaseEditText;
    private EditText medicationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        patientNameEditText = (EditText) findViewById(R.id.patient_name);
        arrivalDateFromEditText = (EditText) findViewById(R.id.arrival_date_from);
        arrivalTimeFromEditText = (EditText) findViewById(R.id.arrival_time_from);
        arrivalDateToEditText = (EditText) findViewById(R.id.arrival_date_to);
        arrivalTimeToEditText = (EditText) findViewById(R.id.arrival_time_to);
        diseaseEditText = (EditText) findViewById(R.id.disease);
        medicationEditText = (EditText) findViewById(R.id.medication);

        refreshView();
    }

    public void clear(View v) {
        SearchOption option = new SearchOption();

        PatientListActivity.setSearchOption(option);

        finish();
    }

    public void search(View v) {
        SearchOption option = new SearchOption();

        option.patient_name = patientNameEditText.getText().toString();
        option.arrival_date_from = arrivalDateFromEditText.getText().toString();
        option.arrival_time_from = arrivalTimeFromEditText.getText().toString();
        option.arrival_date_to = arrivalDateToEditText.getText().toString();
        option.arrival_time_to = arrivalTimeToEditText.getText().toString();
        option.disease = diseaseEditText.getText().toString();
        option.medication = medicationEditText.getText().toString();

        PatientListActivity.setSearchOption(option);

        finish();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public int edit_id = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Get result
            final Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            String new_date = String.format("%1$td-%1$tm-%1$tY", c);

            // Set string to control
            SearchActivity parent = (SearchActivity) getActivity();
            EditText arrival_date = (EditText) parent.findViewById(edit_id);
            arrival_date.setText(new_date);
        }
    }

    public void showDatePickerDialog1(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.edit_id = R.id.arrival_date_from;
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showDatePickerDialog2(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.edit_id = R.id.arrival_date_to;
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        public int edit_id = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Get result
            final Calendar c = Calendar.getInstance();
            c.set(0, 0, 0, hourOfDay, minute);
            String new_time = String.format("%1$tI:%1$tM", c);

            // Set string to control
            SearchActivity parent = (SearchActivity) getActivity();
            EditText arrival_time = (EditText) parent.findViewById(edit_id);
            arrival_time.setText(new_time);
        }
    }

    public void showTimePickerDialog1(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.edit_id = R.id.arrival_time_from;
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showTimePickerDialog2(View v) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.edit_id = R.id.arrival_time_to;
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void refreshView()
    {
        SearchOption so = PatientListActivity.getSearchOption();

        if (so != null) {
            patientNameEditText.setText(so.patient_name);
            arrivalDateFromEditText.setText(so.arrival_date_from);
            arrivalDateToEditText.setText(so.arrival_date_to);
            arrivalTimeFromEditText.setText(so.arrival_time_from);
            arrivalTimeToEditText.setText(so.arrival_time_to);
            diseaseEditText.setText(so.disease);
            medicationEditText.setText(so.medication);
        }
    }
}
