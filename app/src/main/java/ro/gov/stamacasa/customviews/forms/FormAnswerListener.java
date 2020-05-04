package ro.gov.stamacasa.customviews.forms;

import java.util.List;

import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.data.pojo.formsections.Question;

public interface FormAnswerListener {
    void onAnswerAdded(Question question, List<Answer> answers);
    void onAnswerRemoved(Question question, List<Answer> answers);
}
