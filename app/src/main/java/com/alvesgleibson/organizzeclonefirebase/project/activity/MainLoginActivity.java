package com.alvesgleibson.organizzeclonefirebase.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alvesgleibson.organizzeclonefirebase.R;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }

    public void addIncome(View view){
        startActivity( new Intent(this, IncomeActivity.class));

    }
    public void addExpenses(View view){
        startActivity( new Intent(this, ExpenseActivity.class));
    }


}