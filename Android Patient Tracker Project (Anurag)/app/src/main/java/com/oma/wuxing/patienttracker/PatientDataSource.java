package com.oma.wuxing.patienttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxing on 12/25/2014.
 */
public class PatientDataSource {
    private SQLiteDatabase db;
    private PatientDbHelper dbHelper;

    public static abstract class Data implements BaseColumns {
        public static final String TABLE_NAME = "patient";
        public static final String COLUMN_NAME_PATIENT_NAME = "patient_name";
        public static final String COLUMN_NAME_TEL = "tel";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_ARRIVAL_DATE = "arrival_date";
        public static final String COLUMN_NAME_ARRIVAL_TIME = "arrival_time";
        public static final String COLUMN_NAME_ARRIVAL_DATETIME = "arrival_datetime"; // for search
        public static final String COLUMN_NAME_DISEASE = "disease";
        public static final String COLUMN_NAME_MEDICATION = "medication";
        public static final String COLUMN_NAME_COST = "cost";
        public static final Uri CONTENT_URI = Uri.parse("content://patient/list");
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_PATIENTS =
            "CREATE TABLE " + Data.TABLE_NAME + " (" +
                    Data._ID + " INTEGER PRIMARY KEY," +
                    Data.COLUMN_NAME_PATIENT_NAME + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_TEL + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_ARRIVAL_DATE + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_ARRIVAL_TIME + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_ARRIVAL_DATETIME + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_DISEASE + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_MEDICATION + TEXT_TYPE + COMMA_SEP +
                    Data.COLUMN_NAME_COST + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_PATIENTS =
            "DROP TABLE IF EXISTS " + Data.TABLE_NAME;

    public class PatientDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 2;
        public static final String DATABASE_NAME = "patients.db";

        public PatientDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_PATIENTS);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_PATIENTS);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public PatientDataSource(Context context) {
        dbHelper = new PatientDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(Patient patient)
    {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_NAME_PATIENT_NAME, patient.patient_name);
        values.put(Data.COLUMN_NAME_TEL, patient.tel);
        values.put(Data.COLUMN_NAME_EMAIL, patient.email);
        values.put(Data.COLUMN_NAME_ARRIVAL_DATE, patient.arrival_date);
        values.put(Data.COLUMN_NAME_ARRIVAL_TIME, patient.arrival_time);
        values.put(Data.COLUMN_NAME_ARRIVAL_DATETIME, patient.arrival_datetime());
        values.put(Data.COLUMN_NAME_DISEASE, patient.disease);
        values.put(Data.COLUMN_NAME_MEDICATION, patient.medication);
        values.put(Data.COLUMN_NAME_COST, patient.cost);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(Data.TABLE_NAME, null, values);

        return newRowId;
    }

    public boolean delete(long _id)
    {
        // Define 'where' part of query.
        String selection = Data._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(_id) };
        // Issue SQL statement.
        db.delete(Data.TABLE_NAME, selection, selectionArgs);

        return true;
    }

    public boolean update(Patient patient)
    {
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Data.COLUMN_NAME_PATIENT_NAME, patient.patient_name);
        values.put(Data.COLUMN_NAME_TEL, patient.tel);
        values.put(Data.COLUMN_NAME_EMAIL, patient.email);
        values.put(Data.COLUMN_NAME_ARRIVAL_DATE, patient.arrival_date);
        values.put(Data.COLUMN_NAME_ARRIVAL_TIME, patient.arrival_time);
        values.put(Data.COLUMN_NAME_ARRIVAL_DATETIME, patient.arrival_datetime());
        values.put(Data.COLUMN_NAME_DISEASE, patient.disease);
        values.put(Data.COLUMN_NAME_MEDICATION, patient.medication);
        values.put(Data.COLUMN_NAME_COST, patient.cost);

        // Which row to update, based on the ID
        String selection = Data._ID + " = ?";
        String[] selectionArgs = { String.valueOf(patient.id) };

        int count = db.update(
                Data.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return true;
    }

    public List<Patient> search(SearchOption so) {
        List<Patient> patients = new ArrayList<Patient>();

        ArrayList<String> selections = new ArrayList<String>();
        ArrayList<String> selectionArgs = new ArrayList<String>();

        if (so != null) {
            if (!so.patient_name.equals("")) {
                selections.add("(" + Data.COLUMN_NAME_PATIENT_NAME + " LIKE ?)");
                selectionArgs.add("%" + so.patient_name + "%");
            }

            if (!so.disease.equals("")) {
                selections.add("(" + Data.COLUMN_NAME_DISEASE + " LIKE ?)");
                selectionArgs.add("%" + so.disease + "%");
            }

            if (!so.medication.equals("")) {
                selections.add("(" + Data.COLUMN_NAME_MEDICATION + " LIKE ?)");
                selectionArgs.add("%" + so.medication + "%");
            }

            if (!so.arrival_datetime_from().equals("")) {
                selections.add("(" + Data.COLUMN_NAME_ARRIVAL_DATETIME + " >= ?)");
                selectionArgs.add(so.arrival_datetime_from());
            }

            if (!so.arrival_datetime_to().equals("")) {
                selections.add("(" + Data.COLUMN_NAME_ARRIVAL_DATETIME + " <= ?)");
                selectionArgs.add(so.arrival_datetime_to());
            }
        }

        String selection = TextUtils.join(" AND ", selections.toArray());
        String[] args = new String[selectionArgs.size()];
        args = selectionArgs.toArray(args);

        String[] projection = { Data._ID, Data.COLUMN_NAME_PATIENT_NAME };
        Cursor cursor = db.query(Data.TABLE_NAME,
                projection, selection, args, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Patient patient = cursorToPatient(cursor);
            patients.add(patient);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return patients;
    }

    public Patient get(long id) {
        Patient patient = new Patient();

        String[] projection = {
                Data._ID,
                Data.COLUMN_NAME_PATIENT_NAME,
                Data.COLUMN_NAME_TEL,
                Data.COLUMN_NAME_EMAIL,
                Data.COLUMN_NAME_ARRIVAL_DATE,
                Data.COLUMN_NAME_ARRIVAL_TIME,
                Data.COLUMN_NAME_DISEASE,
                Data.COLUMN_NAME_MEDICATION,
                Data.COLUMN_NAME_COST
        };
        String selection = Data._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(Data.TABLE_NAME,
                projection, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            patient.id = cursor.getLong(0);
            patient.patient_name = cursor.getString(1);
            patient.tel = cursor.getString(2);
            patient.email = cursor.getString(3);
            patient.arrival_date = cursor.getString(4);
            patient.arrival_time = cursor.getString(5);
            patient.disease = cursor.getString(6);
            patient.medication = cursor.getString(7);
            patient.cost = cursor.getString(8);
            break;
        }
        // make sure to close the cursor
        cursor.close();
        return patient;
    }

    private Patient cursorToPatient(Cursor cursor) {
        Patient patient = new Patient();
        patient.id = cursor.getLong(0);
        patient.patient_name = cursor.getString(1);
        return patient;
    }
}
