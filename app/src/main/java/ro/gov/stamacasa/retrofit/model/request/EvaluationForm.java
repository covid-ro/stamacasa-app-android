package ro.gov.stamacasa.retrofit.model.request;

import java.util.List;

public class EvaluationForm {
    private List<QuestionAnswer> question_answers;
    private List<QuestionInput> question_input;

    public EvaluationForm(List<QuestionAnswer> questionAnswers, List<QuestionInput> question_input) {
        this.question_answers = questionAnswers;
        this.question_input = question_input;
    }
}
