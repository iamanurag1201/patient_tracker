package com.oma.wuxing.patienttracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wuxing on 12/25/2014.
 */
public class SearchOption {
    public String patient_name;
    public String arrival_date_from;
    public String arrival_time_from;
    public String arrival_date_to;
    public String arrival_time_to;
    public String disease;
    public String medication;

    public SearchOption()
    {
        patient_name = "";
        arrival_date_from = "";
        arrival_time_from = "";
        arrival_date_to = "";
        arrival_time_to = "";
        disease = "";
        medication = "";
    }

    public String arrival_datetime_from() {
        return Patient.toDateTime(arrival_date_from, arrival_time_from);
    }

    public String arrival_datetime_to() {
        return Patient.toDateTime(arrival_date_to, arrival_time_to, "23:59:00");
    }

}
