package com.oma.wuxing.patienttracker;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;
import java.util.List;


public class PatientListActivity extends ActionBarActivity {
    public final static String SELECTED_PATIENT_ID = "com.oma.wuxing.SELECTED_PATIENT_ID";
    public final static int ACTIVITY_ADD_PATIENT = 1;
    public final static int ACTIVITY_EDIT_PATIENT = 2;
    public final static int ACTIVITY_DETAIL_PATIENT = 3;
    public final static int ACTIVITY_SEARCH = 4;

    private PatientDataSource datasource;
    private ListView listview;

    private static SearchOption searchOption;
    public static void setSearchOption(SearchOption so) {
        searchOption = so;
    }
    public static SearchOption getSearchOption() { return searchOption; }

    private List<Patient> patients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listview = (ListView) findViewById(R.id.list);

        datasource = new PatientDataSource(this);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(view.getContext(), PatientDetailActivity.class);
                long patient_id = patients.get(position).id;
                intent.putExtra(SELECTED_PATIENT_ID, patient_id);
                startActivityForResult(intent, ACTIVITY_DETAIL_PATIENT);
            }
        });

        initList();
    }

    public void initList()
    {
        patients = datasource.search(searchOption);

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Patient> adapter = new ArrayAdapter<Patient>(this,
                android.R.layout.simple_list_item_1, patients);

        listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_patient) {
            Intent intent = new Intent(this, PatientActivity.class);
            startActivityForResult(intent, ACTIVITY_ADD_PATIENT);
            return true;
        }

        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, ACTIVITY_SEARCH);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (ACTIVITY_ADD_PATIENT) : {
                initList();
                break;
            }
            case (ACTIVITY_DETAIL_PATIENT): {
                initList();
                break;
            }
            case (ACTIVITY_SEARCH) : {
                initList();
                break;
            }
        }
    }
}
