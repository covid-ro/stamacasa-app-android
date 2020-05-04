package ro.gov.stamacasa.retrofit.model.request;

import java.util.List;

public class QuestionAnswer {
    private int question_id;
    private List<Integer> answers;

    public int getQuestionId() {
        return question_id;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public QuestionAnswer(int questionId, List<Integer> answers) {
        this.question_id = questionId;
        this.answers = answers;
    }
}
