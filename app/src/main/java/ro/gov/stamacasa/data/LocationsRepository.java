package ro.gov.stamacasa.data;

import android.content.Context;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ro.gov.stamacasa.data.pojo.userprofile.City;
import ro.gov.stamacasa.data.pojo.userprofile.Counties;
import ro.gov.stamacasa.data.pojo.userprofile.County;

public class LocationsRepository {

    private static LocationsRepository instance;

    private Counties counties;

    private LocationsRepository(Context context) {
        try {
            counties = JsonProvider.getCounties(context);
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public static synchronized LocationsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new LocationsRepository(context);
        }
        return instance;
    }

    public List<String> getCountiesList() {
        List<String> countiesNames = new ArrayList<>();

        for (County county : counties.getJudete()) {
            countiesNames.add(county.getName());
        }
        return countiesNames;
    }

    public List<String> getCityList(String countyName) {
        List<String> cityNames = new ArrayList<>();

        for (County county : counties.getJudete()) {
            if (county.getName().equals(countyName)) {
                for (City city : county.getCities()) {
                    cityNames.add(city.getName());
                }
                break;
            }
        }
        return cityNames;
    }
}
