package ro.gov.stamacasa.ui.main.frags;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.ui.othersprofile.OthersProfilesActivity;
import ro.gov.stamacasa.ui.personalhistory.PersonalHistoryActivity;
import ro.gov.stamacasa.ui.exit.NewExitActivity;
import ro.gov.stamacasa.ui.form.FormActivity;

public class MenuDialogFragmentForOthers extends DialogFragment implements View.OnClickListener {

    private View mView;
    private UserProfile mUserProfile;
    private Activity mActivity;
    private Intent mIntent;

    public MenuDialogFragmentForOthers(Activity nActivity, UserProfile nUserProfile) {
        this.mUserProfile = nUserProfile;
        this.mActivity = nActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_menu_others, container, false);

        initViews();

        return mView;
    }

    private void initViews() {
        mView.findViewById(R.id.detailsTv).setOnClickListener(this);
        mView.findViewById(R.id.evaluationTv).setOnClickListener(this);
        mView.findViewById(R.id.exitsTv).setOnClickListener(this);
        mView.findViewById(R.id.historyTv).setOnClickListener(this);
    }

    @Override
    public void onClick(View nView) {

        switch (nView.getId()) {
            case R.id.detailsTv:
                redirectDetailsTv();
                break;
            case R.id.evaluationTv:
                redirectEvaluationTv();
                break;
            case R.id.exitsTv:
                redirectExitsTv();
                break;
            case R.id.historyTv:
                redirectHistoryTv();
                break;
        }

        if (mIntent != null) {
            mActivity.startActivity(mIntent);
            dismiss();
        }
    }

    private void redirectDetailsTv() {
        mIntent = new Intent(mActivity, OthersProfilesActivity.class);
        mIntent.putExtra("userId", mUserProfile.getId());
    }

    private void redirectEvaluationTv() {
        mIntent = new Intent(mActivity, FormActivity.class);
        mIntent.putExtra("userId", mUserProfile.getId());
        mIntent.putExtra("flow", FlowIds.EVALUATION);
    }

    private void redirectExitsTv() {
        mIntent = new Intent(mActivity, NewExitActivity.class);
        mIntent.putExtra("userId", mUserProfile.getId());
    }

    private void redirectHistoryTv() {
        mIntent = new Intent(mActivity, PersonalHistoryActivity.class);
        mIntent.putExtra("userId", mUserProfile.getId());
    }
}
