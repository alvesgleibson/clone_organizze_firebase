package com.alvesgleibson.organizzeclonefirebase.project.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
    private Double incomeAll, generalValue;

    private DatabaseReference referenceUserData;
    private ValueEventListener valueEventListenerReceitas;




    @RequiresApi(api = Build.VERSION_CODES.O)
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
    @Override
    protected void onStart() {
        super.onStart();
        recoveryIncomeAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveIncomeMovement(View view){

        validValueField();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validValueField(){

        String valueUserInput = eTInputUser.getText().toString();
        String dateUser = txtDate.getText().toString();
        String category = txtCategory.getText().toString();
        String descriptionUser = txtDescription.getText().toString();

        if ( !valueUserInput.isEmpty() ){
            if ( !dateUser.isEmpty()){
                if ( !category.isEmpty()){
                    if ( !descriptionUser.isEmpty()){
                        Double par = Double.parseDouble( valueUserInput );

                        FinancialMovementUser movementUser = new FinancialMovementUser( category, dateUser, descriptionUser, "r", par );
                        Double updatedIncome = par + incomeAll;

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable( false );
                        builder.setTitle("Salvar Receita?");
                        builder.setMessage("Deseja realmente salvar receita \n\n"+"Valor: R$ "+par+"\nCategoria :"+category+"\nDescrição: " +descriptionUser+"\nCom data: "+dateUser);
                        builder.setIcon( R.drawable.ic_save_alert_dialog_24);
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateIncomeFirebase( updatedIncome );
                                movementUser.saveMovementIncomeFirebase( );

                                clearFields();
                            }
                        }).setNegativeButton("Não", null);
                        builder.create().show();



                    }else {
                        Toast.makeText(this, "Informe valor para Descrição", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Informe valor para Categoria", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Informe valor para Data", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Informe valor R$ para Cadastra", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoveryIncomeAll(){

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child( emailId );

        valueEventListenerReceitas = referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );
                incomeAll = user.getIncomeAll();
                generalValue = user.getGolAll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateIncomeFirebase(Double income){

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child( emailId );
        referenceUserData.child("incomeAll").setValue( income );
        Double valueSum = (generalValue + income);
        referenceUserData.child("golAll").setValue( valueSum );


    }

    public void clearFields(){

        Toast.makeText(this, "Receita cadastrado com sucesso!!!", Toast.LENGTH_SHORT).show();
        finish();

    }

    public void Alert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        referenceUserData.removeEventListener( valueEventListenerReceitas );
    }
}