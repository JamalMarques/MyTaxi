package com.mytaxi.app.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Size;

public class TextHelper {

    public static String textToUpperCase(@NonNull @Size(min = 1) String text){
        return text.substring(0,1).toUpperCase()+text.substring(1);
    }
}
