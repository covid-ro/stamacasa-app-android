package ro.gov.stamacasa.ui.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.tools.SetAlarm;
import ro.gov.stamacasa.ui.BaseActivity;

public abstract class SettingsBaseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    protected void initViews() {
        ((Switch) findViewById(R.id.nAlarm20)).setChecked(isAlarm20set());
        ((Switch) findViewById(R.id.nAlarm20)).setOnCheckedChangeListener(this);
    }

    protected void manageAlarm20(Boolean nFlag) {
        if (nFlag) {
            new SetAlarm(this).set20alarm();
        } else {
            new SetAlarm(this).unset20alarm();
        }

        saveAlarm20state(nFlag);
    }

    protected boolean isAlarm20set() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        return settings.getBoolean("alarm20", false);
    }

    protected void saveAlarm20state(Boolean nFlag) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("alarm20", nFlag);
        editor.apply();
    }
}
