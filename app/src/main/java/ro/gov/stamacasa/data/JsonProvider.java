package ro.gov.stamacasa.data;

import android.content.Context;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.pojo.formsections.FormSections;
import ro.gov.stamacasa.data.pojo.userprofile.Counties;

class JsonProvider {

    static Counties getCounties(Context context) throws UnsupportedEncodingException {
        InputStream in = context.getApplicationContext().getResources().openRawResource(R.raw.localitati_simplu);
        Reader reader = new InputStreamReader(in, "UTF-8");

        return new Gson().fromJson(reader, Counties.class);
    }


    static FormSections getFormSections(Context context) throws UnsupportedEncodingException {
        InputStream in = context.getApplicationContext().getResources().openRawResource(R.raw.json_stam_acasa_alternativa);
        Reader reader = new InputStreamReader(in, "UTF-8");

        return new Gson().fromJson(reader, FormSections.class);
    }
}
