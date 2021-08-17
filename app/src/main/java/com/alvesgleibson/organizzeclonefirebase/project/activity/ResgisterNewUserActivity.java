package com.alvesgleibson.organizzeclonefirebase.project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.entities.User;
import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class ResgisterNewUserActivity extends AppCompatActivity {

    private EditText name, email, password;
    private Button buttonCreate;
    private FirebaseAuth auth;
    private User myUserClass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister_new_user);

        name = findViewById(R.id.ettPersonName);
        email = findViewById(R.id.ettPersonEmail);
        password = findViewById(R.id.ettPersonPassword);
        buttonCreate = findViewById(R.id.btCreateAccount2);


    }


        public void onCreateNewUser(View view) {

            String nameConvert = name.getText().toString();
            String emailConvert = email.getText().toString();
            String passwordConvert = password.getText().toString();


            isCheckFieldInput(nameConvert, emailConvert, passwordConvert);

        }



        public void createUserMed() {
            auth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
            auth.createUserWithEmailAndPassword(myUserClass.getEmail(), myUserClass.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        finish();
                    }else {
                        String myError = "";
                        try {

                            throw task.getException();

                        }catch ( FirebaseAuthWeakPasswordException e){
                            myError = "Input one password Strong that contains number, letters e special characters ";
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            myError = "Email address is malformed";
                        }catch (FirebaseAuthUserCollisionException e ){
                            myError = "There already exists an account with the given email address";
                        }catch (Exception e ){
                            myError = "Problems for Create User";
                            e.printStackTrace();
                        }


                        Toast.makeText(getApplicationContext(), myError , Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }

        public void isCheckFieldInput(String name, String email, String password){

            if (!name.isEmpty()) {

                if (!email.isEmpty()) {

                    if (!password.isEmpty()) {

                        myUserClass = new User(name, email, password);

                        createUserMed();


                    }

                    else {
                        Toast.makeText(getApplicationContext(), "input anything Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "input anything email", Toast.LENGTH_SHORT).show();
                }


            } else {
                Toast.makeText(getApplicationContext(), "input anything name", Toast.LENGTH_SHORT).show();
            }


        }







}