package com.alvesgleibson.organizzeclonefirebase.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class ExpenseActivity extends AppCompatActivity {

    private TextInputEditText ettInputUserExpenseDate2, ettInputUserExpenseDescription2, ettInputUserExpenseCategory2;
    private EditText ettInputUserExpenseValue2;
    private DatabaseReference myDatabaseReference = SettingInstanceFirebase.getInstanceFirebaseDatabase(), referenceUserData;
    private FirebaseAuth myFirebaseAuth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
    private Double expenseAll, generalValue;
    private ValueEventListener valueEventListenerDespesas;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        ettInputUserExpenseDate2 = findViewById(R.id.ettInputUserExpenseDate);
        ettInputUserExpenseCategory2 = findViewById(R.id.ettInputUserExpenseCategory);
        ettInputUserExpenseDescription2 = findViewById(R.id.ettInputUserExpenseDescription);
        ettInputUserExpenseValue2 = findViewById(R.id.ettInputUserExpenseValue);

        ettInputUserExpenseDate2.setText( DateCustom.getDateCurrent() );

        recoveryIncomeAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        recoveryIncomeAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveExpenseMovement(View view) {

        validValueField();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void validValueField() {

        String valueUserInput = ettInputUserExpenseValue2.getText().toString();
        String dateUser = ettInputUserExpenseDate2.getText().toString();
        String category = ettInputUserExpenseCategory2.getText().toString();
        String descriptionUser = ettInputUserExpenseDescription2.getText().toString();

        if (!valueUserInput.isEmpty()) {
            if (!dateUser.isEmpty()) {
                if (!category.isEmpty()) {
                    if (!descriptionUser.isEmpty()) {
                        Double par = Double.parseDouble(valueUserInput);

                        FinancialMovementUser movementUser = new FinancialMovementUser(category, dateUser, descriptionUser, "d", par);
                        Double updatedExpense = par + expenseAll;

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setCancelable( false );
                        builder.setTitle("Salvar Dispesa?");
                        builder.setMessage("Deseja realmente salvar essa dispesa? \n\n"+"Valor: R$ -"+par+"\nCategoria: "+category+"\nDescrição: " +descriptionUser+"\nCom data: "+dateUser);
                        builder.setIcon(android.R.drawable.ic_menu_save);
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                updateExpenseFirebase( updatedExpense );
                                movementUser.saveMovementIncomeFirebase();

                                clearFields();
                            }
                        }).setNegativeButton("Não", null);
                        builder.create().show();




                    } else {
                        Toast.makeText(this, "Informe valor para Descrição", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Informe valor para Categoria", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Informe valor para Data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Informe valor R$ para Cadastra", Toast.LENGTH_SHORT).show();
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void recoveryIncomeAll() {

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child(emailId);

        valueEventListenerDespesas = referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                expenseAll = user.getExpenseAll();
                generalValue = user.getGolAll();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateExpenseFirebase(Double expense) {

        String emailId = Base64Custom.encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child(emailId);
        referenceUserData.child("expenseAll").setValue(expense);
        Double valueSum = (generalValue - expense);
        referenceUserData.child("golAll").setValue(valueSum);



    }

    public void clearFields(){


        Toast.makeText(this, "Despesa cadastrada com sucesso!!!", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceUserData.removeEventListener( valueEventListenerDespesas );
    }
}
