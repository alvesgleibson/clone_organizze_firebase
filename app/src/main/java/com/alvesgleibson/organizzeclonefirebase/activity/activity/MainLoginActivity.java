package com.alvesgleibson.organizzeclonefirebase.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alvesgleibson.organizzeclonefirebase.R;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }

    public void addIncome(View view){
        Toast.makeText(this, "Income", Toast.LENGTH_SHORT).show();

    }
    public void addExpenses(View view){
        Toast.makeText(this, "Expense", Toast.LENGTH_SHORT).show();
    }


}