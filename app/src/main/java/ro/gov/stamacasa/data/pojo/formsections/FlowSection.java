package ro.gov.stamacasa.data.pojo.formsections;

import java.util.List;

public class FlowSection {

    private String section_id;
    private String section_name;
    private String section_text;
    private String section_next_id;

    public String getSection_text() {
        return section_text;
    }

    private List<Question> questions;

    public String getSection_id() {
        return section_id;
    }

    public String getSection_name() {
        return section_name;
    }

    public String getSection_next_id() {
        return section_next_id;
    }

    public List<Question> getQuestions() {
        return questions;
    }


}
