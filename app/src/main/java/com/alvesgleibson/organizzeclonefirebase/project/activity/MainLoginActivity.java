package com.alvesgleibson.organizzeclonefirebase.project.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alvesgleibson.organizzeclonefirebase.R;
import com.alvesgleibson.organizzeclonefirebase.project.adapter.AdapterHome;
import com.alvesgleibson.organizzeclonefirebase.project.entities.FinancialMovementUser;
import com.alvesgleibson.organizzeclonefirebase.project.entities.User;
import com.alvesgleibson.organizzeclonefirebase.project.helper.Base64Custom;
import com.alvesgleibson.organizzeclonefirebase.project.helper.DateCustom;
import com.alvesgleibson.organizzeclonefirebase.project.setting.SettingInstanceFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alvesgleibson.organizzeclonefirebase.project.helper.Base64Custom.encodeBase64;


public class MainLoginActivity extends AppCompatActivity {

    private TextView viewText, txtBalance, txtName, txtRenda, txtDespesa;
    private DatabaseReference myDatabaseReference = SettingInstanceFirebase.getInstanceFirebaseDatabase();
    private FirebaseAuth myFirebaseAuth = SettingInstanceFirebase.getInstanceFirebaseAuthMethod();
    private DatabaseReference referenceUserData;
    private DatabaseReference referenceUserDatass;
    private DatabaseReference referenceExcluirValor =  myDatabaseReference;
    private ValueEventListener valueEventListenerUser;
    private List<FinancialMovementUser> movimentacaoList = new ArrayList<>();
    private static String monthYearFirebase;
    private int positionAdapter;
    private Double varDespesa, varRenda, rendaGeral = 0.0, despesaGeral = 0.0, varTemp;
    private FinancialMovementUser financialMovementUser;
    private ValueEventListener valueEventListenerMovimentacao;
    private RecyclerView myRecyclerView;
    private AdapterHome adapterHome;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        viewText = findViewById(R.id.txtDataExibir);
        txtName = findViewById(R.id.txtNameUser);
        txtBalance = findViewById(R.id.txtRendaUser);
        txtRenda = findViewById(R.id.txtRendaGeral);
        txtDespesa = findViewById(R.id.txtDespesaUser);
        myRecyclerView = findViewById(R.id.rvPrincipal);


        //Irá setar logo ao execultar o programa a data atual;
        monthYearFirebase = (DateCustom.dateFirebase(3));
        viewText.setText( DateCustom.dateShowUser());


        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);



        RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager( this);
        myRecyclerView.setLayoutManager( myLayoutManager );

        myRecyclerView.setHasFixedSize( true );
        adapterHome = new AdapterHome(movimentacaoList, this);

        myRecyclerView.setAdapter( adapterHome);
        swipe();


    }

    public void swipe(){

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.END | ItemTouchHelper.START;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                positionAdapter = viewHolder.getAdapterPosition();
                financialMovementUser = movimentacaoList.get( positionAdapter );

                deleteItemRecycler();

            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(myRecyclerView);

    }

    public void deleteItemRecycler(){

        String msn;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Excluir Transação?");
        if (financialMovementUser.getType() == "r"|| financialMovementUser.getType().equals("r")){
            msn = "Deseja Realmente excluir essa Receita? \n\n"+"Categoria: "+financialMovementUser.getCategory()+"\nDescrição: "+financialMovementUser.getDescription();

        }else{
            msn = "Deseja Realmente excluir essa Despesa? \n\n"+"Categoria: "+financialMovementUser.getCategory()+"\nDescrição: "+financialMovementUser.getDescription();

        }
        builder.setMessage(msn);
        builder.setIcon( R.drawable.ic_delete_24);
        builder.setCancelable(false);
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String emailId = encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());

                referenceExcluirValor.child("Financial Movement").child( emailId ).child(monthYearFirebase).child(financialMovementUser.getIdMovientacao()).removeValue();
                adapterHome.notifyItemRemoved(positionAdapter );

                updateBalanceAfterDelete();
                Toast.makeText(MainLoginActivity.this, "Transição Excluida com sucesso", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adapterHome.notifyDataSetChanged();
                Toast.makeText(MainLoginActivity.this, "Cancelado a exclusão", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateBalanceAfterDelete(){
        String emailId = encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child( emailId );

        if (financialMovementUser.getType() == "r" || financialMovementUser.getType().equals("r")){

            varRenda -= financialMovementUser.getValue();
            referenceUserData.child("incomeAll").setValue(varRenda);

        }else if (financialMovementUser.getType() == "d" || financialMovementUser.getType().equals("d")){
            varDespesa -= financialMovementUser.getValue();
            referenceUserData.child("expenseAll").setValue(varDespesa);
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buscarTrasacao(){

        String emailId = encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserDatass =  myDatabaseReference.child("Financial Movement").child( emailId ).child(monthYearFirebase);


        valueEventListenerMovimentacao = referenceUserDatass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                movimentacaoList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    FinancialMovementUser sa = snapshot1.getValue( FinancialMovementUser.class );
                   sa.setIdMovientacao(snapshot1.getKey() );

                    movimentacaoList.add( sa );
                }
                adapterHome.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void beforeClick(View view){


        monthYearFirebase = DateCustom.dateFirebase(0);
        referenceUserDatass.removeEventListener( valueEventListenerMovimentacao );
        buscarTrasacao();
        viewText.setText(DateCustom.dateShowUser());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void afterClick(View view){

        monthYearFirebase = DateCustom.dateFirebase(1);
        referenceUserDatass.removeEventListener( valueEventListenerMovimentacao );
        buscarTrasacao();
        viewText.setText(DateCustom.dateShowUser());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showInformation(){
        String emailId = encodeBase64(myFirebaseAuth.getCurrentUser().getEmail());
        referenceUserData = myDatabaseReference.child("Users").child( emailId );

       valueEventListenerUser =  referenceUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue( User.class );
                    try {
                        Double amountAll;
                        varDespesa = user.getExpenseAll();
                        varRenda = user.getIncomeAll();
                        amountAll = varRenda - varDespesa;

                        txtDespesa.setText( String.format("R$ -%.2f", varDespesa.doubleValue()) );
                        txtRenda.setText(String.format("R$ %.2f", varRenda.doubleValue()) );

                        txtBalance.setText( String.format("R$ %.2f",+amountAll));
                        txtName.setText( user.getName() );

                    }catch (Exception e){
                        e.printStackTrace();
                    }



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
        referenceUserDatass.removeEventListener( valueEventListenerMovimentacao );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        showInformation();
        buscarTrasacao();
    }

}