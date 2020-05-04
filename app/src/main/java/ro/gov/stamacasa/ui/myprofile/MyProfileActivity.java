package ro.gov.stamacasa.ui.myprofile;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.Button;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.personalprofile.PersonalProfileCv;
import ro.gov.stamacasa.tools.SharedPref;
import ro.gov.stamacasa.ui.BaseActivity;

public class MyProfileActivity extends BaseActivity {

    private long userId;
    protected PersonalProfileCv mPersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        ((ViewStub) findViewById(R.id.nIncludeMyProfile)).inflate();

        userId = SharedPref.getInstance(this).getLong(this, "adminUid");
        initNavigation();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPersonalProfile.startDisposables();
        if (userId != -1)
            mPersonalProfile.setUserId(userId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPersonalProfile.pauseDisposables();
    }

    private void initViews() {
        mPersonalProfile = findViewById(R.id.personalData);
    }
}
