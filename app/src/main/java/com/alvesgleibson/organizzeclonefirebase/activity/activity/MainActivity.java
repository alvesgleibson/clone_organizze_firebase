package com.alvesgleibson.organizzeclonefirebase.activity.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.activity.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_login_screen_main);


    }

    @Override
    protected void onStart() {
        super.onStart();
        isCheckUserLogged();

    }

    public void registerUserNew(View view){
        startActivity( new Intent(this, ResgisterNewUserActivity.class));
    }

    public void alreadyAccount(View view){
        startActivity( new Intent(this, AlreadyAccountActivity.class));
    }

    public void isCheckUserLogged(){

        auth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
        //auth.signOut();
        if (auth.getCurrentUser() != null){
            startActivity( new Intent(this, MainLoginActivity.class));
            finish();
        }

    }


}