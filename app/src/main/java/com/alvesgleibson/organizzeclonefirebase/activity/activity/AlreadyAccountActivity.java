package com.alvesgleibson.organizzeclonefirebase.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.activity.setting.SettingInstanceFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class AlreadyAccountActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_account);

        etEmailLogin = findViewById(R.id.varLoginEmail);
        etPasswordLogin = findViewById(R.id.varLoginPassword);

    }

    public void loginFirebase(View view){

        String emailPar, passwordPar;
        emailPar = etEmailLogin.getText().toString();
        passwordPar =  etPasswordLogin.getText().toString();
        fieldEmptyAndLogin( emailPar,passwordPar );

    }

    public void fieldEmptyAndLogin(String email, String password) {
        String emailM, passwordM;
        emailM = email;
        passwordM = password;

        if (!emailM.isEmpty()) {

            if (!passwordM.isEmpty()) {

                loginUser(email, password);

            } else {
                Toast.makeText(AlreadyAccountActivity.this, "input anything Password", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(AlreadyAccountActivity.this, "input anything email", Toast.LENGTH_SHORT).show();
        }

    }

    public void loginUser(String email, String password) {

        auth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    userAuthSuccess();

                } else {
                    String loginErrorMessage = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        loginErrorMessage = "Email Invalid or not registered";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        loginErrorMessage = "Password Invalid";
                    }catch (Exception e ){
                        loginErrorMessage = "Problem login";
                        e.printStackTrace();
                    }
                    Toast.makeText(AlreadyAccountActivity.this, loginErrorMessage, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void userAuthSuccess(){
        startActivity( new Intent(AlreadyAccountActivity.this, MainLoginActivity.class));
        finish();
    }

}//end program