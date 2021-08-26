package com.alvesgleibson.organizzeclonefirebase.project.entities;

import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String name, email, password, idCode;

    private Double incomeAll = 0.0, expenseAll = 0.0, golAll = 0.0;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void saveUserFirebase(){

        DatabaseReference database = SettingInstanceFirebase.getInstanceFirebaseDatabase();
        database.child( "Users" )
                .child( this.idCode )
                .setValue( this );


    }

    public Double getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(Double incomeAll) {
        this.incomeAll = incomeAll;
    }

    public Double getExpenseAll() {
        return expenseAll;
    }

    public void setExpenseAll(Double expenseAll) {
        this.expenseAll = expenseAll;
    }

    public Double getGolAll() {
        return golAll;
    }

    public void setGolAll(Double golAll) {
        this.golAll = golAll;
    }

    @Exclude
    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
