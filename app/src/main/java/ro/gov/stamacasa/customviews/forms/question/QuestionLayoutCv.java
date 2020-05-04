package ro.gov.stamacasa.customviews.forms.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.FormAnswerListener;
import ro.gov.stamacasa.customviews.forms.answer.AnswerOptionCv;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.data.pojo.formsections.Question;
import ro.gov.stamacasa.tools.InputTypes;
import ro.gov.stamacasa.tools.QuestionTypes;

public class QuestionLayoutCv extends LinearLayout implements AnswerListener {

    private Question question;
    private LabelQuestionCv questionLabel;
    private LinearLayout questionL;
    private View selectedView;
    private FormAnswerListener mFormAnswerListener;
    private boolean inflateHidden;
    private HashMap<Integer, String> followUpUserInput = new HashMap<>();

    //for multiple choice
    List<Answer> selectedAnswers = new ArrayList<>();

    //for single choice
    Answer selectedAnswer;

    public QuestionLayoutCv(Context context, Question question, FormAnswerListener listener, boolean inflateHidden) {
        super(context);
        this.question = question;
        this.inflateHidden = inflateHidden;
        this.mFormAnswerListener = listener;
        init();
    }

    public QuestionLayoutCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void followUpUserInputEntered(Integer key, String value) {
        followUpUserInput.put(key, value);
    }

    @Override
    public void answerDeselected(Answer answer, View view, AnswerOptionCv cv) {
        switch (question.getQuestion_type()) {

            case QuestionTypes.MULTIPLE_CHOICE:
                handleAnswerDeselectionMultipleChoice(answer);
                break;

            case QuestionTypes.SINGLE_CHOICE:
                //deselection handled with answerSelected callback
                break;
        }
    }

    @Override
    public void answerSelected(Answer answer, View view, AnswerOptionCv cv) {
        switch (question.getQuestion_type()) {
            case QuestionTypes.MULTIPLE_CHOICE:
                handleAnswerSelectionMultipleChoice(answer, view, cv);
                break;

            case QuestionTypes.SINGLE_CHOICE:
                handleAnswerSelectionSingleChoice(answer, view, cv);
                break;
        }
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cv_question_layout, this);
        questionL = view.findViewById(R.id.questionLayout);

        if (question != null)
            inflateQuestionCv(question);
    }

    private void inflateQuestionCv(Question question) {
        if (!question.isQuestion_hidden() || inflateHidden) {
            if (question.getQuestion_text() != null) {
                questionLabel = new LabelQuestionCv(getContext(), question.getQuestion_text());
                questionL.addView(questionLabel);
                inflateAnswersCv(question.getQuestion_answers(), question.getQuestion_type());
            }
        }
    }

    private void inflateAnswersCv(List<Answer> questionAnswer, String questionType) {
        AnswerOptionCv answerOptionCv;

        if (questionAnswer != null) {
            for (Answer answer : questionAnswer) {
                if (answer.getAnswer_decision().getAnswer_input() != null) {
                    answerOptionCv = getViewForAnswerInput(answer, questionType);
                } else {
                    answerOptionCv = new AnswerOptionCv(getContext(), this, answer, questionType);
                }
                questionL.addView(answerOptionCv);
            }
        }
    }

    private AnswerOptionCv getViewForAnswerInput(Answer answer, String questionType) {
        AnswerOptionCv answerOptionCv = new AnswerOptionCv(getContext(), this, answer, questionType);

        switch (answer.getAnswer_decision().getAnswer_input()) {
            case InputTypes.NUMERIC:
                answerOptionCv.expandNumericInputView(answer.getAnswer_decision().getAnswer_hint());
                break;

            case InputTypes.TEXT_AREA:
                answerOptionCv.expandTextInputView(answer.getAnswer_decision().getAnswer_hint());
                break;
        }
        return answerOptionCv;
    }

    private void handleAnswerSelectionMultipleChoice(Answer answer, View view, AnswerOptionCv cv) {
        cv.selectView(view);
        selectedAnswers.add(answer);
        mFormAnswerListener.onAnswerAdded(question, selectedAnswers);
    }

    private void handleAnswerSelectionSingleChoice(Answer answer, View view, AnswerOptionCv cv) {
        if (selectedView == null) {
            cv.selectAnswer(view);
        } else {
            cv.deselectAnswer(selectedView);
            cv.selectAnswer(view);
        }
        selectedView = view;
        selectedAnswer = answer;
        ArrayList<Answer> selectedAnswer = new ArrayList<>();
        selectedAnswer.add(answer);
        mFormAnswerListener.onAnswerAdded(question, selectedAnswer);
    }

    private void handleAnswerDeselectionMultipleChoice(Answer answer) {
        List<Answer> selectedAnswers = new ArrayList<>();
        if (this.selectedAnswers != null) {
            for (Answer questionAnswer : this.selectedAnswers) {
                if (questionAnswer.getAnswer_id() != answer.getAnswer_id()) {
                    selectedAnswers.add(questionAnswer);
                }
            }
            this.selectedAnswers.clear();
            this.selectedAnswers.addAll(selectedAnswers);
            mFormAnswerListener.onAnswerAdded(question, selectedAnswers);
        }
    }

    public void setQuestionError() {
        if (questionLabel != null)
            questionLabel.setError();
    }


    public Question getQuestion() {
        return question;
    }

    public Answer getSelectedAnswer() {
        return selectedAnswer;
    }

    public List<Answer> getSelectedAnswers() {
        return selectedAnswers;
    }

    public HashMap<Integer, String> getUserFollowUpInput() {
        return followUpUserInput;
    }

}
