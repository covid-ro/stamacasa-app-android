package ro.gov.stamacasa.ui.main.frags;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.UsersRepository;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.ui.othersprofile.OthersProfilesActivity;
import ro.gov.stamacasa.ui.form.FormActivity;
import ro.gov.stamacasa.ui.main.frags.adapter.OtherProfilesRvAdapter;

public class FragmentOthers extends Fragment implements OtherProfilesRvAdapter.ClickListener, View.OnClickListener {

    private View mView;
    private Button mButton;
    private RecyclerView usersList;
    private OtherProfilesRvAdapter adapter;
    private CompositeDisposable disposable;

    public static FragmentOthers newInstance() {
        return new FragmentOthers();
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        disposable = new CompositeDisposable();
        initList();
    }

    @Override
    public void onUserClick(UserProfile userProfile) {
        Intent intent = new Intent(getContext(), OthersProfilesActivity.class);
        intent.putExtra("userId", userProfile.getId());
        startActivity(intent);
    }

    @Override
    public void onMenuClick(UserProfile userProfile) {
        Intent intent = new Intent(getActivity(), FormActivity.class);
        intent.putExtra("flow", FlowIds.EVALUATION);
        intent.putExtra("userId", userProfile.getId());
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_others, container, false);
        initViews();
        return mView;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profileBt) {
            Intent intent = new Intent(getActivity(), FormActivity.class);
            intent.putExtra("flow", FlowIds.REGISTRATION);
            startActivity(intent);
        }
    }

    private void initViews() {
        usersList = mView.findViewById(R.id.otherProfilesRv);
        usersList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mButton = mView.findViewById(R.id.profileBt);
        mButton.setOnClickListener(this);
    }

    private void initList() {
        disposable.add(UsersRepository.getInstance(getContext()).getAllUserProfiles(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayUsers));
    }

    private void displayUsers(List<UserProfile> users) {
        adapter = new OtherProfilesRvAdapter(getActivity(), users, this);
        usersList.setAdapter(adapter);
    }
}