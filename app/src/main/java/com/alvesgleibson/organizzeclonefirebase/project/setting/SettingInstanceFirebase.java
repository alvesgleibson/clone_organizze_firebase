package com.alvesgleibson.organizzeclonefirebase.project.setting;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingInstanceFirebase {

    private static FirebaseAuth authFirebase;
    private static DatabaseReference varDatabaseReference;

    public static FirebaseAuth getInstanceFirebaseAuthMethod(){

        if(authFirebase == null){
            authFirebase = FirebaseAuth.getInstance();
        }
        return authFirebase;
    }

    public static DatabaseReference getInstanceFirebaseDatabase(){
        if (varDatabaseReference == null){
            varDatabaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return varDatabaseReference;
    }

}
