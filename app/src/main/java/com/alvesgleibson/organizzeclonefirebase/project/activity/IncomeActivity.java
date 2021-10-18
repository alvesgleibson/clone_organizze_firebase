package com.alvesgleibson.organizzeclonefirebase.project.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.entities.FinancialMovementUser;
import com.alvesgleibson.organizzeclonefirebase.project.entities.User;
import com.alvesgleibson.organizzeclonefirebase.project.helper.Base64Custom;
import com.alvesgleibson.organizzeclonefirebase.project.helper.DateCustom;
import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class IncomeActivity extends AppCompatActivity {

    private TextInputEditText txtDate, txtDescription, txtCategory;
    private EditText eTInputUser;
    private DatabaseReference myDatabaseReference = SettingInstanceFirebase.getInstanceFirebaseDatabase();
    private FirebaseAuth myFirebaseAuth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
    private Double incomeAll = 0.0, expenseAll = 0.0, generalValue = 0.0, sett = 0.0, updatedIncome = 0.0, par = 0.0;

    private DatabaseReference referenceUserData;
    private ValueEventListener valueEventListenerReceitas;
    private RadioButton incomeRadioB, expenseRadioB;
    private RadioGroup radioGroupPro;
    private String st = "", pa ="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        txtDate = findViewById(R.id.tieDate);
        txtCategory = findViewById(R.id.tieCategory);
        txtDescription = findViewById(R.id.tieDescription);
        eTInputUser = findViewById(R.id.txtUserIncome);
        incomeRadioB = findViewById(R.id.radioButtonReceitas);
        expenseRadioB = findViewById(R.id.radioButtonDespesas);
        radioGroupPro = findViewById(R.id.radioGroup);

        txtDate.setText(DateCustom.getDateCurrent());


    }



    @Override
    protected void onStart() {
        super.onStart();
        recoveryIncomeAll();
    }

    public void saveIncomeMovement(View view){

        validValueField();

    }


    public void validValueField(){
        String valueUserInput = eTInputUser.getText().toString();
        String dateUser = txtDate.getText().toString();
        String category = txtCategory.getText().toString();
        String descriptionUser = txtDescription.getText().toString();

        if ( !valueUserInput.isEmpty() ){
            if ( !dateUser.isEmpty()){
                if ( !category.isEmpty()){
                    if ( !descriptionUser.isEmpty()){

                            par = Double.parseDouble( valueUserInput );

                            String title = "", menssage = "";
                            incomeRadioB.setActivated( true );

                            if (incomeRadioB.isChecked()){
                                st = "r";
                                title = "Salvar Receita?";
                                menssage = "Deseja realmente salvar essa receita \n\n"+"Valor: R$ "+par+"\nCategoria :"+category+"\nDescrição: " +descriptionUser+"\nCom data: "+dateUser;
                                pa = "Receita cadastrado com sucesso!!!";
                                sett = incomeAll;
                            }
                            else if(expenseRadioB.isChecked()){
                                st = "d";
                                title = "Salvar Despesa?";
                                menssage = "Deseja realmente salvar essa Despesa? \n\n"+"Valor: R$ -"+par+"\nCategoria :"+category+"\nDescrição: " +descriptionUser+"\nCom data: "+dateUser;
                                pa = "Despesa cadastrada com sucesso!!!";
                                sett = expenseAll;
                            }

                        Log.i("alvesss", "UpdateIncome:validFields Depois "+ updatedIncome+" par "+par+" sett "+sett);

                            FinancialMovementUser movementUser = new FinancialMovementUser( category, dateUser, descriptionUser, st, par );

                            updatedIncome = par + sett;

                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setCancelable( false );

                            builder.setTitle( title );
                            builder.setMessage(menssage);
                            builder.setIcon( R.drawable.ic_save_alert_dialog_24);
                            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    updateIncomeFirebase( updatedIncome );
                                    movementUser.saveMovementIncomeFirebase( );

                                    clearFields( pa );
                                }
                            }).setNegativeButton("Não", null);
                            builder.create().show();

                    }else {
                        Toast.makeText(this, "Informe Uma Descrição", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Informe Uma Categoria", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Informe Uma Data", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Informe valor R$ ", Toast.LENGTH_SHORT).show();
        }


    }

    public void recoveryIncomeAll(){

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child( emailId );

        valueEventListenerReceitas = referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );
                generalValue = user.getGolAll();

                if (st == "r" || st.equals("r")) {
                    incomeAll = user.getIncomeAll();

                }else if (st == "d" || st.equals("d")){
                    expenseAll = user.getExpenseAll();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateIncomeFirebase(Double income){

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());

        String varFire = "";

        if (st == "r" || st.equals("r")) {
            varFire = "incomeAll";

        }else if (st == "d" || st.equals("d")){
            varFire =  "expenseAll";

        }
        referenceUserData = myDatabaseReference.child("Users").child( emailId );
        referenceUserData.child(varFire).setValue( income );
        Double valueSum = (generalValue + income);
        referenceUserData.child("golAll").setValue( valueSum );


    }

    public void clearFields( String pa){

        Toast.makeText(this, pa, Toast.LENGTH_SHORT).show();
        finish();

    }



    @Override
    protected void onStop() {
        super.onStop();
        referenceUserData.removeEventListener( valueEventListenerReceitas );
    }
}