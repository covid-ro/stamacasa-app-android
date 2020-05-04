package ro.gov.stamacasa.customviews.forms.answer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ro.gov.stamacasa.R;

@SuppressLint("ViewConstructor")
public class FollowUpInputNumericCv extends LinearLayout {

    View mView;
    LinearLayout mLinearLayout;
    TextView mTitle;
    EditText mUserInput;
    UserInputListener mUserInputListener;

    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms

    public FollowUpInputNumericCv(Context context, UserInputListener listener) {
        super(context);
        mUserInputListener = listener;
        mView = LayoutInflater.from(context).inflate(R.layout.cv_followup_input_numeric, this);

        initViews();
    }

    public void initViews() {
        mLinearLayout = mView.findViewById(R.id.followUpInputLayout);
        mTitle = mView.findViewById(R.id.nTitle);
        mUserInput = mView.findViewById(R.id.nReason);
        mUserInput = mView.findViewById(R.id.nReason);

        mUserInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 1) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mUserInputListener.onUserEnteredInput(s.toString());
                        }
                    }, DELAY);
                }
            }
        });
    }

    public void setHint(String text) {
        mTitle.setText(text);
    }
}
