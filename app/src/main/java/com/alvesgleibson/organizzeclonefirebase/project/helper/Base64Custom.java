package com.alvesgleibson.organizzeclonefirebase.project.helper;


import android.os.Build;

import java.util.Base64;

import androidx.annotation.RequiresApi;

public class Base64Custom {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encodeBase64(String st) {
        return Base64.getEncoder().encodeToString(st.getBytes()).replaceAll("\\n|\\r", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decodeBase64(String st) {
        
        return new String(Base64.getDecoder().decode(st.getBytes()));
    }

}
