package com.alvesgleibson.organizzeclonefirebase.project.helper;

import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustom {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    public static String getDateCurrent(){
        return sdf.format( new Date());
    }

    public static String getDateCurrentWithoutDay(String date){

        String returnDate[] = date.split("/");
        String day = returnDate[0];
        String month = returnDate [1];
        String year = returnDate[2];

        String monthYear = (month + year);

        return monthYear;
    }


}
