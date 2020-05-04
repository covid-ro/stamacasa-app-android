package ro.gov.stamacasa.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.ui.BaseActivity;

public abstract class AboutBaseActivity extends BaseActivity implements View.OnClickListener {

    protected void initViews() {
        findViewById(R.id.nText2).setOnClickListener(this);
    }

    protected void openGovBrowser() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.about_site))));
    }
}
