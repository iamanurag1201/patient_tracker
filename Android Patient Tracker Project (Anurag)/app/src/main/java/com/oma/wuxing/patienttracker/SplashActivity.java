package com.oma.wuxing.patienttracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class SplashActivity extends Activity {

    //A ProgressDialog object
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void startApp(View v)
    {
        Intent intent = new Intent(this, PatientListActivity.class);
        startActivity(intent);
    }
}
