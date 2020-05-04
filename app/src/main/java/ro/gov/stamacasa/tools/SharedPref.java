package ro.gov.stamacasa.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPref {

    private static SharedPref instance;
    private static SharedPreferences sharedPref;

    private SharedPref() {

    }

    public static synchronized SharedPref getInstance(Context context){
        if (instance == null) {
            instance = new SharedPref();
            sharedPref = context.getApplicationContext().getSharedPreferences("StamAcasa", Context.MODE_PRIVATE);
        }

        return instance;
    }

    public void addLong(Context context, String key, long value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(Context context, String key) {
        Log.d("SharedPref", "admin user id is " + sharedPref.getLong(key, -1));
       return sharedPref.getLong(key, -1);
    }
}

