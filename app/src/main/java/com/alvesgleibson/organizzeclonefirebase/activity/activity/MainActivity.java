package com.alvesgleibson.organizzeclonefirebase.activity.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_login_screen_main);

    }


    public void registerUserNew(View view){
        startActivity( new Intent(this, ResgisterNewUserActivity.class));
    }

    public void alreadyAccount(View view){
        startActivity( new Intent(this, AlreadyAccountActivity.class));
    }


}