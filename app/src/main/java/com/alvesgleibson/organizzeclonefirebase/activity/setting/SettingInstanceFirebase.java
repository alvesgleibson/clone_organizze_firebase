package com.alvesgleibson.organizzeclonefirebase.activity.setting;

import com.google.firebase.auth.FirebaseAuth;

public class SettingInstanceFirebase {

    private static FirebaseAuth authFirebase;

    public static FirebaseAuth firebaseAuthMethod(){

        if(authFirebase == null){
            authFirebase = FirebaseAuth.getInstance();
        }
        return authFirebase;


    }


}
