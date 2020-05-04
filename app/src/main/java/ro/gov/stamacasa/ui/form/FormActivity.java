package ro.gov.stamacasa.ui.form;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.ui.adapter.ProfilePagerAdapter;

public class FormActivity extends FormActivityBase {

    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), 0);

        initViews();
        initToolbar();
        getStarted();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void initViews() {
        pager = findViewById(R.id.pager);
        mProgressTabs = findViewById(R.id.progressTab);
        mProgressBar = findViewById(R.id.formPb);
        headerTv = findViewById(R.id.headerTv);
        headerBodyTv = findViewById(R.id.bodyTextTv);

        pager.setAdapter(mPagerAdapter);
        pager.setPageScrollEnabled(false);
    }
}

