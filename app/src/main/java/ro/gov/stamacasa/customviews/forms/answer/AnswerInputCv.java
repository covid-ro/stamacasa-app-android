package ro.gov.stamacasa.customviews.forms.answer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ro.gov.stamacasa.R;

public class AnswerInputCv extends FrameLayout {

    private View view;
    private FrameLayout layout;
    private Spinner mSpinner;
    private int inputType = -1;

    private String answerLabel;
    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;

    public AnswerInputCv(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);

        view = inflater.inflate(R.layout.cv_answer_input, this);

        init(context, attrs);
        initViews();
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet,
                R.styleable.AnswerInputCv, 0, 0);
        try {
            answerLabel = typedArray.getString(R.styleable.AnswerInputCv_label);
            inputType = typedArray.getInt(R.styleable.AnswerInputCv_android_inputType, 0);
        } finally {
            typedArray.recycle();
        }
    }

    private void initViews() {
        textInputEditText = view.findViewById(R.id.answerEt);
        textInputLayout = view.findViewById(R.id.inputLayout);
        layout = view.findViewById(R.id.inputLl);
        mSpinner = view.findViewById(R.id.dropdown_menu);

        if (inputType != -1)
            textInputEditText.setInputType(inputType);

        if (answerLabel != null) {
            setAnswerHint(answerLabel);
        }
    }

    public TextInputEditText getTextInputEditText() {
        return textInputEditText;
    }

    public TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    public FrameLayout getLayout() {
        return layout;
    }

    public void setFocusable(boolean focusable) {
        textInputEditText.setFocusable(focusable);
    }

    public void setClickable(boolean clickable) {
        textInputEditText.setClickable(clickable);
    }

    public void setAnswerHint(String label) {
        textInputLayout.setHint(label);
    }

    public void displaySpinner(String[] options) {
        mSpinner.setVisibility(VISIBLE);
        populateSpinner(options);
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    private void populateSpinner(String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.my_spinner_style, options) {

            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                ((TextView) v).setPadding(0, 0, 0, 0);
                ((TextView) v).setTextSize(18);
                return v;
            }

            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setPadding(40, 20, 0, 20);
                return v;
            }
        };
        mSpinner.setAdapter(adapter);
    }
}
