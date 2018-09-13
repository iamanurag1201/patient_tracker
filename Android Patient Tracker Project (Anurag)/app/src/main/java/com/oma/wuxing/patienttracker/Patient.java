package com.oma.wuxing.patienttracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import org.w3c.dom.Comment;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wuxing on 12/25/2014.
 */
public final class Patient {
    public Patient() {
    }

    public long id;
    public String patient_name;
    public String tel;
    public String email;
    public String arrival_date;
    public String arrival_time;
    public String disease;
    public String medication;
    public String cost;

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return patient_name;
    }

    public String arrival_datetime() {
        return toDateTime(arrival_date, arrival_time);
    }

    public static String toDateTime(String date, String time)
    {
        return toDateTime(date, time, "00:00:00");
    }

    public static String toDateTime(String date, String time, String time_default)
    {
        Calendar cal = Calendar.getInstance();
        if (date.equals(""))
            return "";

        try {
            SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");
            cal.setTime(date_format.parse(date));// all done

            String new_date = String.format("%1$tY-%1$tm-%1$td", cal);
            if (!time.equals(""))
                return new_date + " " + time + ":00";
            else
                return new_date + " " + time_default;
        }
        catch (ParseException e) {
            return "";
        }
    }
}
