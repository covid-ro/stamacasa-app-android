package ro.gov.stamacasa.ui.exit;

import android.app.TimePickerDialog;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.UsersRepository;
import ro.gov.stamacasa.data.pojo.exit.ExitForm;
import ro.gov.stamacasa.ui.BaseActivity;

public abstract class NewExitBaseActivity extends BaseActivity implements View.OnClickListener {

    protected Calendar mCalendar = Calendar.getInstance();
    protected int mHour, mMinute;
    protected TimePickerDialog mTimePicker;
    protected EditText mReason, mStartHour, mEndHour;
    protected View mTag1, mTag2;
    protected TextView mAnswerNo, mAnswerYes;
    protected Boolean mWasInDanger = null;
    protected long userId;

    protected void initViews() {

        mTag1 = findViewById(R.id.nTag1);
        mTag2 = findViewById(R.id.nTag2);

        mAnswerNo = findViewById(R.id.nAnswerNo);
        mAnswerYes = findViewById(R.id.nAnswerYes);

        mReason = findViewById(R.id.nReason);
        mStartHour = findViewById(R.id.nStartHour);
        mEndHour = findViewById(R.id.nEndHour);

        mStartHour.setOnClickListener(this);
        mEndHour.setOnClickListener(this);
        mAnswerNo.setOnClickListener(this);
        mAnswerYes.setOnClickListener(this);
        findViewById(R.id.nSubmit).setOnClickListener(this);
    }


    /**
     * Thre is one correct answer and one flag that retain the status as boolean
     *
     * @param nFlag - the flag!
     */
    protected void mValidateSingleChoice(Boolean nFlag) {

        mWasInDanger = nFlag;

        if (mWasInDanger) {
            mAnswerNo.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_not_selected));
            mAnswerYes.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_selected));
        } else {
            mAnswerNo.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_selected));
            mAnswerYes.setBackground(getResources().getDrawable(R.drawable.rounded_corners_answer_not_selected));
        }
    }

    /**
     * Calculate current hour and minute as startup point in picker time
     */
    protected void mCalculateCurrentHour() {
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
    }

    /**
     * Show native time picker
     *
     * @param nTextView - textview where we should put the hour and minute from the picker
     */
    protected void mShowPicker(TextView nTextView) {
        mCalculateCurrentHour();

        mTimePicker = new TimePickerDialog(this, (view, hourOfDay, minute) -> nTextView.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
        mTimePicker.show();
    }

    /**
     * Validate form
     */
    protected boolean mValidateForm() {

        boolean mValidate = false;
        mTag1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mTag2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (mReason != null && mReason.getText() != null && mReason.getText().length() > 0) {
            mValidate = true;
        } else if (mReason != null) {
            Snackbar.make(mReason, getString(R.string.exit_title_error), Snackbar.LENGTH_SHORT).show();
            mTag1.setBackgroundColor(getResources().getColor(R.color.errorColor));
            return false;
        }

        if (mStartHour != null && mStartHour.getText() != null && mStartHour.getText().length() > 0) {
            mValidate = true;
        } else if (mStartHour != null) {
            Snackbar.make(mStartHour, getString(R.string.exit_start_hour_error), Snackbar.LENGTH_SHORT).show();
            mTag1.setBackgroundColor(getResources().getColor(R.color.errorColor));
            return false;
        }

        if (mEndHour != null && mEndHour.getText() != null && mEndHour.getText().length() > 0) {
            mValidate = true;
        } else if (mEndHour != null) {
            Snackbar.make(mEndHour, getString(R.string.exit_end_hour_error), Snackbar.LENGTH_SHORT).show();
            mTag1.setBackgroundColor(getResources().getColor(R.color.errorColor));
            return false;
        }

        if (mWasInDanger == null) {
            Snackbar.make(mAnswerNo, getString(R.string.exit_was_in_danger), Snackbar.LENGTH_SHORT).show();
            mTag2.setBackgroundColor(getResources().getColor(R.color.errorColor));
            return false;
        }

        if (mValidate) {
            saveData();

        }

        return mValidate;
    }

    private void saveData() {
        new Thread(() -> {
            ExitForm mExitForm = new ExitForm(userId, mReason.getText().toString(), mStartHour.getText().toString(), mEndHour.getText().toString(), mWasInDanger);
            UsersRepository.getInstance(this).addUserExit(mExitForm);
            runOnUiThread(() -> {
                Snackbar.make(mAnswerNo, getString(R.string.exit_saved), Snackbar.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> finish(), 1000);
            });
        }).start();
    }
}
