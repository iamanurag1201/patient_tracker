package com.oma.wuxing.patienttracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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


public class PatientActivity extends ActionBarActivity {
    PatientDataSource datasource;

    private EditText patientNameEditText;
    private EditText telEditText;
    private EditText emailEditText;
    private EditText arrivalDateEditText;
    private EditText arrivalTimeEditText;
    private EditText diseaseEditText;
    private EditText medicationEditText;
    private EditText costEditText;

    private long patient_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        patientNameEditText = (EditText) findViewById(R.id.patient_name);
        telEditText = (EditText) findViewById(R.id.tel) ;
        emailEditText = (EditText) findViewById(R.id.email);
        arrivalDateEditText = (EditText) findViewById(R.id.arrival_date);
        arrivalTimeEditText = (EditText) findViewById(R.id.arrival_time);
        diseaseEditText = (EditText) findViewById(R.id.disease);
        medicationEditText = (EditText) findViewById(R.id.medication);
        costEditText = (EditText) findViewById(R.id.cost);

        Intent intent = getIntent();
        patient_id = intent.getLongExtra(PatientListActivity.SELECTED_PATIENT_ID, -1);

        if (patient_id != -1) {
            setTitle(getString(R.string.edit_patient));
            datasource = new PatientDataSource(this);
            Patient patient = datasource.get(patient_id);

            patientToView(patient);
        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            PatientActivity parent = (PatientActivity) getActivity();
            EditText arrival_date = (EditText) parent.findViewById(R.id.arrival_date);
            arrival_date.setText(new_date);
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
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
            PatientActivity parent = (PatientActivity) getActivity();
            EditText arrival_time = (EditText) parent.findViewById(R.id.arrival_time);
            arrival_time.setText(new_time);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void gotoList(View v) {
        finish();
    }

    public void save(View v) {
        boolean valid = true;
        Patient patient = new Patient();

        patient.patient_name = patientNameEditText.getText().toString();
        if (patient.patient_name.equals("")) {
            patientNameEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.tel = telEditText.getText().toString();
        if (patient.tel.equals("")) {
            telEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.email = emailEditText.getText().toString();
        if (patient.email.equals("")) {
            emailEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.arrival_date = arrivalDateEditText.getText().toString();
        if (patient.arrival_date.equals("")) {
            arrivalDateEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.arrival_time = arrivalTimeEditText.getText().toString();
        if (patient.arrival_time.equals("")) {
            arrivalTimeEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.disease = diseaseEditText.getText().toString();
        if (patient.disease.equals("")) {
            diseaseEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.medication = medicationEditText.getText().toString();
        if (patient.medication.equals("")) {
            medicationEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        patient.cost = costEditText.getText().toString();
        if (patient.cost.equals("")) {
            costEditText.setError(getString(R.string.err_required));
            valid = false;
        }

        if (!valid)
            return;

        PatientDataSource dataSource = new PatientDataSource(this);
        if (patient_id != -1) {
            patient.id = patient_id;
            dataSource.update(patient);
        }
        else {
            long new_patient_id = dataSource.insert(patient);
        }

        finish();
    }

    private void patientToView(Patient patient)
    {
        patientNameEditText.setText(patient.patient_name);
        telEditText.setText(patient.tel);
        emailEditText.setText(patient.email);
        arrivalDateEditText.setText(patient.arrival_date);
        arrivalTimeEditText.setText(patient.arrival_time);
        diseaseEditText.setText(patient.disease);
        medicationEditText.setText(patient.medication);
        costEditText.setText(patient.cost);
    }
}
