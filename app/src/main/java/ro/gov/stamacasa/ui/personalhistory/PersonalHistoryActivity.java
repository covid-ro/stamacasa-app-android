package ro.gov.stamacasa.ui.personalhistory;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.exitstable.ExitsTableCv;
import ro.gov.stamacasa.customviews.symptomstable.SymptomsTableView;
import ro.gov.stamacasa.customviews.symptomstable.adapter.TableViewAdapter;
import ro.gov.stamacasa.ui.BaseActivity;
import ro.gov.stamacasa.ui.DataLoadListener;

public class PersonalHistoryActivity extends BaseActivity implements DataLoadListener {

    private long userId;
    private TableViewAdapter mAdapter;
    private SymptomsTableView mSymptomsTableView;
    private ExitsTableCv mExitsTableCv;
    private TextView noDataLoadedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);  // this one first
        super.onCreate(savedInstanceState);

        ((ViewStub) findViewById(R.id.nIncludeMyHistory)).inflate();

        initNavigation();
        initViews();
    }

    private void initViews() {
        mSymptomsTableView = findViewById(R.id.symptomsTable);
        mExitsTableCv = findViewById(R.id.exitsTable);
        noDataLoadedTv = findViewById(R.id.noDataTv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSymptomsTableView.startDisposables();
        mExitsTableCv.startDisposables();
        userId = getIntent().getLongExtra("userId", -1);

        setUpAdapter();
        setSymptomsTableData();
        setExitsTableData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSymptomsTableView.pauseDisposables();
        mExitsTableCv.pauseDisposables();
    }

    @Override
    public void symptomsLoadComplete(boolean loaded) {
        if (loaded) {
            mSymptomsTableView.setVisibility(View.VISIBLE);
        } else {
            mSymptomsTableView.setVisibility(View.GONE);
        }
        showHideNoDataTv();
    }

    @Override
    public void exitsLoadComplete(boolean loaded) {
        if (loaded) {
            mExitsTableCv.setVisibility(View.VISIBLE);
        } else {
            mExitsTableCv.setVisibility(View.GONE);
        }
        showHideNoDataTv();
    }

    void setUpAdapter() {
        mAdapter = new TableViewAdapter(this);
        mSymptomsTableView.setAdapter(mAdapter);
        mSymptomsTableView.setRowHeaderWidth(getResources().getDisplayMetrics().widthPixels / 3);
        mSymptomsTableView.setHasFixedWidth(false);
    }

    //-1 shows all columns from the database
    void setSymptomsTableData() {
        mSymptomsTableView.displayData(-1, mAdapter, userId);
        mSymptomsTableView.setDataLoadListener(this);
    }

    void setExitsTableData() {
        mExitsTableCv.getData(userId, -1);
        mExitsTableCv.setDataLoadListener(this);
    }

    private void showHideNoDataTv() {
        if (mExitsTableCv.getVisibility() == View.GONE && mSymptomsTableView.getVisibility() == View.GONE) {
            noDataLoadedTv.setVisibility(View.VISIBLE);
        } else {
            noDataLoadedTv.setVisibility(View.INVISIBLE);
        }
    }
}
