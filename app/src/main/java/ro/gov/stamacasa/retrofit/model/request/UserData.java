package ro.gov.stamacasa.retrofit.model.request;

public class UserData {
    Profile profile;
    EvaluationForm form;

    public UserData(Profile profile, EvaluationForm form) {
        this.profile = profile;
        this.form = form;
    }

    public Profile getProfile() {
        return profile;
    }

    public EvaluationForm getForm() {
        return form;
    }
}
