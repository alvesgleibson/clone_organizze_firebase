package com.alvesgleibson.organizzeclonefirebase.activity.setting;

import com.google.firebase.auth.FirebaseAuth;

public class SettingInstanceFirebase {

    private static FirebaseAuth authFirebase;

    public static FirebaseAuth getInstanceFirebaseAuthMethod(){

        if(authFirebase == null){
            authFirebase = FirebaseAuth.getInstance();
        }
        return authFirebase;


    }


}
