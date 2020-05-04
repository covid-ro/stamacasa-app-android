package ro.gov.stamacasa.ui.exit;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.tools.SharedPref;

public class NewExitActivity extends NewExitBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        ((ViewStub) findViewById(R.id.nIncludeExit)).inflate();
        userId = getIntent().getLongExtra("userId", -1);

        initViews();
    }

    @Override
    public void onClick(View nView) {

        switch (nView.getId()) {
            case R.id.nStartHour :
            case R.id.nEndHour :
                mShowPicker((TextView) nView); break;
            case R.id.nSubmit: mValidateForm(); break;
            case R.id.nAnswerNo: mValidateSingleChoice(false); break;
            case R.id.nAnswerYes: mValidateSingleChoice(true); break;
        }
    }
}
