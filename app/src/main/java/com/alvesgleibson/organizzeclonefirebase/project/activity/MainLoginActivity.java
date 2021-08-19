package com.alvesgleibson.organizzeclonefirebase.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.entities.User;
import com.alvesgleibson.organizzeclonefirebase.project.helper.Base64Custom;
import com.alvesgleibson.organizzeclonefirebase.project.helper.DateCustom;
import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainLoginActivity extends AppCompatActivity {
    private TextView viewText, txtBalance, txtName;
    private String nameUser;
    private Double numberAllUser;
    private DatabaseReference myDatabaseReference = SettingInstanceFirebase.getInstanceFirebaseDatabase();
    private FirebaseAuth myFirebaseAuth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        viewText = findViewById(R.id.txtDataExibir);
        txtName = findViewById(R.id.txtNameUser);
        txtBalance = findViewById(R.id.txtBalanceUser);

        //Irá setar logo ao execultar o programa a data atual;
        viewText.setText(DateCustom.dateShowUser());
        showInformation();

    }

    public void addIncome(View view){
        startActivity( new Intent(this, IncomeActivity.class));

    }
    public void addExpenses(View view){
        startActivity( new Intent(this, ExpenseActivity.class));
    }

    public void beforeClick(View view){

        Toast.makeText(this, "Data Selecionada Firebase: "+DateCustom.dateFirebase(0), Toast.LENGTH_SHORT).show();
        viewText.setText(DateCustom.dateShowUser());
    }

    public void afterClick(View view){

        Toast.makeText(this, "Data Selecionada Firebase: "+DateCustom.dateFirebase(1), Toast.LENGTH_SHORT).show();
        viewText.setText(DateCustom.dateShowUser());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(){
        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        DatabaseReference referenceUserData = myDatabaseReference.child("Users").child( emailId );

        referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );

                txtBalance.setText( "R$ "+user.getGolAll());
                txtName.setText( user.getName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}