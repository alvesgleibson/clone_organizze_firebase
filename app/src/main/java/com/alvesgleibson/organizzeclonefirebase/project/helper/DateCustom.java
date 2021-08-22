package com.alvesgleibson.organizzeclonefirebase.project.helper;

import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCustom {

    private static SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy");


    private static Calendar cal = Calendar.getInstance();
    private static String months[] = {"Janeiro/","Fevereiro/","Março/","Abril/","Maio/","Junho/","Julho/","Agosto/","Setembro/","Outubro/","Novembro/","Dezembro/"};
    //Informação para o Firebase
    private static SimpleDateFormat sdf = new SimpleDateFormat("MMyyyy");
    //Informação para o sistema(mostrar para o usuário)
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("M");


    public static String getDateCurrent(){
        return sdf4.format( new Date());
    }

    public static String getDateCurrentWithoutDay(String date){

        String returnDate[] = date.split("/");
        String day = returnDate[0];
        String month = returnDate [1];
        String year = returnDate[2];

        String monthYear = (month + year);

        return monthYear;
    }

    public static String dateFirebase(int in){
        if (in == 0){
            cal.add(Calendar.MONTH, -1);
        }else if (in == 1){
            cal.add(Calendar.MONTH, 1);
        }else {
            cal.add(Calendar.MONTH, 0);
        }
        return sdf.format( cal.getTime() );
    }

    public static String dateShowUser(){

        int test = Integer.parseInt(sdf3.format(cal.getTime())) - 1;
        if (test > 11){
            test -= 10;
        }
        String returnDate = months[test] + sdf2.format(cal.getTime());

        return returnDate;
    }

}
