package ro.gov.stamacasa.ui.othersprofile;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.personalprofile.PersonalProfileCv;
import ro.gov.stamacasa.ui.BaseActivity;
import ro.gov.stamacasa.ui.main.MainScreenActivity;

import static ro.gov.stamacasa.ui.main.RedirectTab.OTHER;

public class OthersProfilesActivity extends BaseActivity {

    private long userId;
    private PersonalProfileCv personalProfileCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);
        ((ViewStub) findViewById(R.id.nIncludeOthersProfiles)).inflate();
        initNavigation();

        userId = getIntent().getLongExtra("userId", -1);
        personalProfileCv = findViewById(R.id.personalData);
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalProfileCv.startDisposables();
        handleDataDisplay();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        userId = intent.getLongExtra("userId", -1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        personalProfileCv.pauseDisposables();
    }

    private void handleDataDisplay() {
        personalProfileCv.removeAllLayouts();
        if (userId != -1) {
            personalProfileCv.setUserId(userId);
        } else {
            Intent intent = new Intent(this, MainScreenActivity.class);
            intent.putExtra("redirectTab", OTHER);
            startActivity(intent);
        }
    }
}
