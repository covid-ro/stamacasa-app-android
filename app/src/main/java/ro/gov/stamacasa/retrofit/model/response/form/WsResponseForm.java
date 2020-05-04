package ro.gov.stamacasa.retrofit.model.response.form;

import androidx.annotation.NonNull;

public class WsResponseForm {
    private Meta meta;
    private Data data;

    public Meta getMeta() {
        return meta;
    }

    public Data getData() {
        return data;
    }

    @NonNull
    @Override
    public String toString() {
        return "rating is" + getData().getEvaluation().getRating() + " and message is " + data.getEvaluation().getMessage();
    }
}
