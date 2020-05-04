package ro.gov.stamacasa.retrofit.model.request;

import java.util.List;
import java.util.Map;

public class HealthStatus {
    String section_id;
    List<QuestionAnswer> question_answers;

    public HealthStatus(String section_id, List<QuestionAnswer> questionAnswers) {
        this.section_id = section_id;
        this.question_answers = questionAnswers;
    }

    public String getSection_id() {
        return section_id;
    }

    public List<QuestionAnswer> getQuestion_answers() {
        return question_answers;
    }
}
