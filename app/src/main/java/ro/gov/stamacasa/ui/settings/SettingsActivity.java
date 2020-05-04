package ro.gov.stamacasa.ui.settings;

import android.os.Bundle;
import android.view.ViewStub;
import android.widget.CompoundButton;

import ro.gov.stamacasa.R;

public class SettingsActivity extends SettingsBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        ((ViewStub) findViewById(R.id.nIncludeSettings)).inflate();
        initViews();
    }

    @Override
    public void onCheckedChanged(CompoundButton nView, boolean nIsChecked) {
        switch (nView.getId()) {
            case R.id.nAlarm20:  manageAlarm20(nIsChecked); break;
        }
    }
}
