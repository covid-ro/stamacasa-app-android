package ro.gov.stamacasa.customviews.forms.question;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ro.gov.stamacasa.R;

public class LabelQuestionCv extends ConstraintLayout {

    private TextView questionLabelTv;
    private View view;
    private String questionLabel;

    public LabelQuestionCv(Context context, String questionLabel) {
        super(context);
        this.questionLabel = questionLabel;
        init(context);
    }

    public LabelQuestionCv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cv_label_question, this);
        initViews();
    }

    private void initViews() {
        questionLabelTv = view.findViewById(R.id.questionLabelTv);

        if (questionLabel != null) {
            questionLabelTv.setText(questionLabel);
        }
    }

    public void setQuestionLabel(String label) {
        questionLabelTv.setText(label);
    }

    public void setError(){
        questionLabelTv.setBackgroundColor(getContext().getResources().getColor(R.color.errorColor));
    }
}
