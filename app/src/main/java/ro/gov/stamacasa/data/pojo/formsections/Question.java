package ro.gov.stamacasa.data.pojo.formsections;

import java.util.List;

public class Question {

    private int question_id;
    private int question_order;
    private boolean question_hidden;
    private String question_type;
    private String question_text;

    private List<Answer> question_answers;

    public int getQuestion_id() {
        return question_id;
    }

    public int getQuestion_order() {
        return question_order;
    }

    public boolean isQuestion_hidden() {
        return question_hidden;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public List<Answer> getQuestion_answers() {
        return question_answers;
    }

}
