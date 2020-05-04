package ro.gov.stamacasa.ui.main.frags;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.exitstable.ExitsTableCv;
import ro.gov.stamacasa.customviews.symptomstable.SymptomsTableView;
import ro.gov.stamacasa.customviews.symptomstable.adapter.TableViewAdapter;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.tools.SharedPref;
import ro.gov.stamacasa.ui.DataLoadListener;
import ro.gov.stamacasa.ui.personalhistory.PersonalHistoryActivity;
import ro.gov.stamacasa.ui.exit.NewExitActivity;
import ro.gov.stamacasa.ui.form.FormActivity;
import ro.gov.stamacasa.ui.tools.ActivityHelper;

public class FragmentMy extends Fragment implements View.OnClickListener, DataLoadListener {

    private View mView;
    private Button completeFormBt;
    private long userId;
    private TableViewAdapter mAdapter;
    private SymptomsTableView mSymptomsTableView;
    private ExitsTableCv mExitsTableCv;

    public static FragmentMy newInstance() {
        return new FragmentMy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my, container, false);
        initViews();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSymptomsTableView.startDisposables();
        mExitsTableCv.startDisposables();
        userId = SharedPref.getInstance(getContext()).getLong(getContext(), "adminUid");
        setUpAdapter();
        setSymptomsTableData();
        setExitsTableData();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSymptomsTableView.pauseDisposables();
        mExitsTableCv.pauseDisposables();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.completeFormBt:
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("flow", FlowIds.EVALUATION);
                intent.putExtra("userId", userId);
                startActivity(intent);
                new ActivityHelper().temporarilyDisableButtonClick(completeFormBt);
                break;

            case R.id.addExitBt:
                Intent intent1 = new Intent(getContext(), NewExitActivity.class);
                intent1.putExtra("userId", userId);
                startActivity(intent1);
                new ActivityHelper().temporarilyDisableButtonClick(completeFormBt);
                break;

            case R.id.seeHitoryBt:
                Intent intent2 = new Intent(getContext(), PersonalHistoryActivity.class);
                intent2.putExtra("userId", userId);
                startActivity(intent2);
                new ActivityHelper().temporarilyDisableButtonClick(completeFormBt);
        }
    }

    private void initViews() {
        completeFormBt = mView.findViewById(R.id.completeFormBt);
        Button addExitBt = mView.findViewById(R.id.addExitBt);
        mSymptomsTableView = mView.findViewById(R.id.symptomsTable);
        mExitsTableCv = mView.findViewById(R.id.exitsTable);
        Button seeHistoryBt = mView.findViewById(R.id.seeHitoryBt);

        completeFormBt.setOnClickListener(this);
        seeHistoryBt.setOnClickListener(this);
        addExitBt.setOnClickListener(this);
    }

    void setUpAdapter() {
        mAdapter = new TableViewAdapter(getContext());
        mSymptomsTableView.setAdapter(mAdapter);
        mSymptomsTableView.setRowHeaderWidth(getResources().getDisplayMetrics().widthPixels / 2);
        mSymptomsTableView.setHasFixedWidth(false);
    }

    void setSymptomsTableData() {
        mSymptomsTableView.displayData(2, mAdapter, userId);
        mSymptomsTableView.setDataLoadListener(this);
    }

    void setExitsTableData() {
        mExitsTableCv.getData(userId, 3);
        mExitsTableCv.setDataLoadListener(this);
    }

    @Override
    public void symptomsLoadComplete(boolean loaded) {
        if (loaded) {
            mSymptomsTableView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void exitsLoadComplete(boolean loaded) {
        if (loaded) {
            mExitsTableCv.setVisibility(View.VISIBLE);
        }
    }
}