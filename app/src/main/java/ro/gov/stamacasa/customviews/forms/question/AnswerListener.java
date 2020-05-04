package ro.gov.stamacasa.customviews.forms.question;

import android.view.View;

import java.util.List;
import java.util.Map;

import ro.gov.stamacasa.customviews.forms.answer.AnswerOptionCv;
import ro.gov.stamacasa.data.pojo.formsections.Answer;

public interface AnswerListener {

    //so that we can change the view color and select status from question cv
    void answerSelected(Answer answer, View view, AnswerOptionCv cv);

    void answerDeselected(Answer answer, View view, AnswerOptionCv cv);

    void followUpUserInputEntered(Integer key, String value);

}
