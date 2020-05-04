package ro.gov.stamacasa.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewStub;

import androidx.viewpager.widget.ViewPager;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.ui.BaseActivity;

import static ro.gov.stamacasa.ui.main.RedirectTab.MY;
import static ro.gov.stamacasa.ui.main.RedirectTab.OTHER;

public class MainScreenActivity extends BaseActivity {

    private ViewPager vpPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        ((ViewStub) findViewById(R.id.nIncludeMainActivity)).inflate();

        initNavigation();

        vpPager = findViewById(R.id.vpPager);
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

        checkForRedirect(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkForRedirect(intent);
    }

    private void checkForRedirect(Intent intent) {
        String redirectTab = intent.getStringExtra("redirectTab");

        if (redirectTab != null) {
            redirect(redirectTab);
        } else {
            //change this to maintain last tab shown instead of main
            vpPager.setCurrentItem(0);
        }
    }

    private void redirect(String tab) {
        switch (tab) {
            case OTHER:
                vpPager.setCurrentItem(1);
                break;

            case MY:
                //No case yet, my set to default
                break;

            default:
                vpPager.setCurrentItem(0);
                break;
        }
    }
}
