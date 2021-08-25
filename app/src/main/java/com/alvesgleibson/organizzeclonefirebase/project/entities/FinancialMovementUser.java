package com.alvesgleibson.organizzeclonefirebase.project.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alvesgleibson.organizzeclonefirebase.project.helper.Base64Custom;
import com.alvesgleibson.organizzeclonefirebase.project.helper.DateCustom;
import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class FinancialMovementUser {

    private String category, date, description,type,idMovientacao;
    private Double inputValueUser;

    


    public FinancialMovementUser(){

    }


    public FinancialMovementUser(String category, String date, String description, String type, Double inputValueUser) {
        this.category = category;
        this.date = date;
        this.description = description;
        this.type = type;
        this.inputValueUser = inputValueUser;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveMovementIncomeFirebase(){
        DatabaseReference reference = SettingInstanceFirebase.getInstanceFirebaseDatabase();
        FirebaseAuth auth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
        String ss = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());
        reference.child( "Financial Movement" )
                 .child( ss )
                 .child(DateCustom.getDateCurrentWithoutDay( this.date ))
                 .push()
                 .setValue( this );
}


    public String getIdMovientacao() {
        return idMovientacao;
    }

    public void setIdMovientacao(String idMovientacao) {
        this.idMovientacao = idMovientacao;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return inputValueUser;
    }

    public void setValue(Double inputValueUser) {
        this.inputValueUser = inputValueUser;
    }
}
