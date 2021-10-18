package com.alvesgleibson.organizzeclonefirebase.project.helper;

import android.util.Base64;

public class Base64Custom {

    public static String encodeBase64(String st) {
        return Base64.encodeToString(st.getBytes(),0).replaceAll("\\n|\\r", "");
    }


    public static String decodeBase64(String st) {
        return new String(Base64.decode(st.getBytes(),0));
    }

}
