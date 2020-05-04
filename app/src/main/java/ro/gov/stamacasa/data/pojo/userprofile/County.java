package ro.gov.stamacasa.data.pojo.userprofile;

import java.util.List;

public class County {
    private String auto;
    private String nume;
    private List<City> localitati;

    public String getAuto() {
        return auto;
    }

    public String getName() {
        return nume;
    }

    public List<City> getCities() {
        return localitati;
    }

}
