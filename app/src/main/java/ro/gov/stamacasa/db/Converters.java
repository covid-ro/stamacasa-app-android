package ro.gov.stamacasa.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Converters {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static String fromHashMapStringArray(HashMap<Integer, List<Integer>> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }

    @TypeConverter
    public static HashMap<Integer, List<Integer>> fromMapStringArray(String value) {
        Type listType = new TypeToken<HashMap<Integer, List<Integer>>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static HashMap<Integer, String> fromMapString(String value) {
        Type listType = new TypeToken<HashMap<Integer, String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromHashMapString(HashMap<Integer, String> map) {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        return json;
    }
}
