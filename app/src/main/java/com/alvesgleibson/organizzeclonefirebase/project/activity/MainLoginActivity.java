package com.alvesgleibson.organizzeclonefirebase.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView viewText, txtBalance, txtName, txtRenda, txtDespesa;
    private DatabaseReference myDatabaseReference = SettingInstanceFirebase.getInstanceFirebaseDatabase();
    private FirebaseAuth myFirebaseAuth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
    private DatabaseReference referenceUserData;
    private ValueEventListener valueEventListenerUser;

    private Double rendaGeral = 0.0, despesaGeral = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        viewText = findViewById(R.id.txtDataExibir);
        txtName = findViewById(R.id.txtNameUser);
        txtBalance = findViewById(R.id.txtRendaUser);
        txtRenda = findViewById(R.id.txtRendaGeral);
        txtDespesa = findViewById(R.id.txtDespesaUser);

        //Irá setar logo ao execultar o programa a data atual;
        viewText.setText(DateCustom.dateShowUser());


        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        showInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.pag_init, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.logoffB:{
                myFirebaseAuth.signOut();
                finish();
                break;
            }
        }




        return super.onOptionsItemSelected(item);
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
        referenceUserData = myDatabaseReference.child("Users").child( emailId );

       valueEventListenerUser =  referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );
                Double amountAll, varDespesa, varRenda;
                varDespesa = user.getExpenseAll();
                varRenda = user.getIncomeAll();
                amountAll = varRenda - varDespesa;


                txtDespesa.setText( String.format("R$ -%.2f", varDespesa.doubleValue()) );
                txtRenda.setText(String.format("R$ %.2f", varRenda.doubleValue()) );



                txtBalance.setText( String.format("R$ %.2f",+amountAll));
                txtName.setText( user.getName() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        referenceUserData.removeEventListener( valueEventListenerUser );
    }
}