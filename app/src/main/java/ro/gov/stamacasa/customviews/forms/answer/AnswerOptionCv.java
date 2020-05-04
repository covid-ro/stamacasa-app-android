package ro.gov.stamacasa.customviews.forms.answer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.question.AnswerListener;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.tools.QuestionTypes;

public class AnswerOptionCv extends ConstraintLayout implements View.OnClickListener, UserInputListener {

    private TextView answerLabelTv;
    private LinearLayout extraQuestionsL;
    private View view;
    private boolean clicked;
    private AnswerListener listener;
    private Answer answer;
    private String question_type;

    public AnswerOptionCv(Context context, AnswerListener listener, Answer answer, String question_type) {
        super(context);
        this.listener = listener;
        this.answer = answer;
        this.question_type = question_type;
        init(context);
    }

    public AnswerOptionCv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cv_answer_option, this);
        initViews();
    }

    private void initViews() {
        answerLabelTv = view.findViewById(R.id.answerTv);
        extraQuestionsL = view.findViewById(R.id.extraQuestionsL);

        setAnswerLabel(answer.getAnswer_text());
        answerLabelTv.setOnClickListener(this);
    }

    public void setAnswerLabel(String label) {
        answerLabelTv.setText(label);
    }

    public void expandTextInputView(String hint) {
        FollowUpInputTextCv followUpInputTextCv = new FollowUpInputTextCv(getContext(), this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        followUpInputTextCv.setLayoutParams(params);
        followUpInputTextCv.setHint(hint);

        addViewToLayout(followUpInputTextCv);
    }

    public void expandNumericInputView(String hint) {
        FollowUpInputNumericCv followUpInputNumericCv = new FollowUpInputNumericCv(getContext(), this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        followUpInputNumericCv.setLayoutParams(params);
        followUpInputNumericCv.setHint(hint);

        addViewToLayout(followUpInputNumericCv);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.answerTv) {
            if (question_type.equals(QuestionTypes.MULTIPLE_CHOICE)) {
                if (!clicked) {
                    listener.answerSelected(answer, view, this);
                    selectAnswer(view);
                } else {
                    listener.answerDeselected(answer, view, this);
                    deselectAnswer(view);
                }
            } else if (question_type.equals(QuestionTypes.SINGLE_CHOICE)) {
                listener.answerSelected(answer, view, this);
            }
            handleFollowUpQuestionVisibility();
        }
    }

    public void addViewToLayout(View view) {
        if (extraQuestionsL != null) {
            extraQuestionsL.addView(view);
        }
    }

    public void selectView(View view) {
        if (!clicked) {
            selectAnswer(view);
            clicked = true;
        } else {
            deselectAnswer(view);
            clicked = false;
        }
    }

    public void selectAnswer(View view) {
        clicked = true;
        view.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_selected));
    }

    public void deselectAnswer(View view) {
        clicked = false;
        view.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_not_selected));
    }

    public void handleFollowUpQuestionVisibility() {
        if (clicked && extraQuestionsL.getVisibility() == GONE) {
            extraQuestionsL.setVisibility(VISIBLE);

        } else if (!clicked && extraQuestionsL.getVisibility() == VISIBLE) {
            extraQuestionsL.setVisibility(GONE);
        }
    }

    @Override
    public void onUserEnteredInput(String entry) {
        listener.followUpUserInputEntered(answer.getAnswer_id(), entry);
    }
}
