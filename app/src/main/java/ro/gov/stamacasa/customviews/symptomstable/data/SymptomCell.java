package ro.gov.stamacasa.customviews.symptomstable.data;

public class SymptomCell {

    public SymptomCell(boolean hadSymptom) {
        this.hadSymptom = hadSymptom;
    }
    private boolean hadSymptom;

    public boolean getHadSymptom() {
        return hadSymptom;
    }
}

