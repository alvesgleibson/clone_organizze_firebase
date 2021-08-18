package com.alvesgleibson.organizzeclonefirebase.project.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.entities.FinancialMovementUser;
import com.alvesgleibson.organizzeclonefirebase.project.helper.DateCustom;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

public class IncomeActivity extends AppCompatActivity {

    private TextInputEditText txtDate, txtDescription, txtCategory;
    private EditText eTInputUser;
    private DatabaseReference databaseReference;
   // private FinancialMovementUser movementUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        txtDate = findViewById(R.id.tieDate);
        txtCategory = findViewById(R.id.tieCategory);
        txtDescription = findViewById(R.id.tieDescription);
        eTInputUser = findViewById(R.id.txtUserIncome);

        txtDate.setText(DateCustom.getDateCurrent());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveIncomeMovement(View view){

        validValueField();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public FinancialMovementUser validValueField(){

        String date = txtDate.getText().toString();
        String category = txtCategory.getText().toString();
        String description = txtDescription.getText().toString();
        String s = eTInputUser.getText().toString();



        if ( !date.isEmpty() ){
            if ( !category.isEmpty()){
                if ( !description.isEmpty()){
                    if ( !s.isEmpty()){

                        FinancialMovementUser movementUser = new FinancialMovementUser( category, date, description, "r", Double.parseDouble( s ));

                        movementUser.saveMovementIncomeFirebase( );

                    }else {
                        Toast.makeText(this, "Informe valor para Cadastrar", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Informe valor para Descrição", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Informe valor para Categoria", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Informe valor para data", Toast.LENGTH_SHORT).show();
        }


        return null;
    }



}