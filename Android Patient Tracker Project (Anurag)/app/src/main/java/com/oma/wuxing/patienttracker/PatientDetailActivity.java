package com.oma.wuxing.patienttracker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class PatientDetailActivity extends ActionBarActivity {
    PatientDataSource datasource;

    private TextView patientNameTextView;
    private TextView telTextView;
    private TextView emailTextView;
    private TextView arrivalDateTextView;
    private TextView arrivalTimeTextView;
    private TextView diseaseTextView;
    private TextView medicationTextView;
    private TextView costTextView;

    private long patient_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        patientNameTextView = (TextView) findViewById(R.id.patient_name);
        telTextView = (TextView) findViewById(R.id.tel) ;
        emailTextView = (TextView) findViewById(R.id.email);
        arrivalDateTextView = (TextView) findViewById(R.id.arrival_date);
        arrivalTimeTextView = (TextView) findViewById(R.id.arrival_time);
        diseaseTextView = (TextView) findViewById(R.id.disease);
        medicationTextView = (TextView) findViewById(R.id.medication);
        costTextView = (TextView) findViewById(R.id.cost);
        
        Intent intent = getIntent();
        patient_id = intent.getLongExtra(PatientListActivity.SELECTED_PATIENT_ID, -1);

        datasource = new PatientDataSource(this);

        refreshView();
    }

    private void refreshView()
    {
        Patient patient = datasource.get(patient_id);

        patientNameTextView.setText(patient.patient_name);
        telTextView.setText(patient.tel);
        emailTextView.setText(patient.email);
        arrivalDateTextView.setText(patient.arrival_date);
        arrivalTimeTextView.setText(patient.arrival_time);
        diseaseTextView.setText(patient.disease);
        medicationTextView.setText(patient.medication);
        costTextView.setText(patient.cost);
    }

    public void gotoList(View v) {
        finish();
    }

    public void delete(View v) {
        datasource.delete(patient_id);

        finish();
    }

    public void edit(View v) {
        Intent intent = new Intent(this, PatientActivity.class);
        intent.putExtra(PatientListActivity.SELECTED_PATIENT_ID, patient_id);
        startActivityForResult(intent, PatientListActivity.ACTIVITY_EDIT_PATIENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (PatientListActivity.ACTIVITY_EDIT_PATIENT) : {
                refreshView();
                break;
            }
        }
    }
}
