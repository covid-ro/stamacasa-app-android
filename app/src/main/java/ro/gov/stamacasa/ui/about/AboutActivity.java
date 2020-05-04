package ro.gov.stamacasa.ui.about;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import ro.gov.stamacasa.R;

public class AboutActivity extends AboutBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        //inflate de needed layout
        ((ViewStub) findViewById(R.id.nIncludeAbout)).inflate();
        initViews();
    }

    @Override
    public void onClick(View nView) {
        switch (nView.getId()) {
            case R.id.nText2 : openGovBrowser(); break;
        }
    }
}
