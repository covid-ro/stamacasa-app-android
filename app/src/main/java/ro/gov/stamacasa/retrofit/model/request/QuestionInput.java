package ro.gov.stamacasa.retrofit.model.request;

import java.util.List;

public class QuestionInput {
    private int question_id;
    private List<String> answers;

    public int getQuestionId() {
        return question_id;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public QuestionInput(int questionId, List<String> answers) {
        this.question_id = questionId;
        this.answers = answers;
    }
}
