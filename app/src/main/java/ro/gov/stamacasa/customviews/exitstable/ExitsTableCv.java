package ro.gov.stamacasa.customviews.exitstable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.UsersRepository;
import ro.gov.stamacasa.data.pojo.exit.ExitForm;
import ro.gov.stamacasa.ui.DataLoadListener;

public class ExitsTableCv extends ConstraintLayout implements ExitsTableAdapter.ClickListener {

    private View mView;
    private RecyclerView mRecyclerView;
    private ExitsTableAdapter mAdapter;
    private CompositeDisposable mDisposable;
    private TextView title;
    private DataLoadListener mDataLoadListener;
    private boolean dataLoaded;

    public ExitsTableCv(Context context) {
        super(context);
        init();
    }

    public ExitsTableCv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onFormClick(ExitForm exitForm) {

    }

    public void setDataLoadListener(DataLoadListener loadListener) {
        this.mDataLoadListener = loadListener;
    }

    private void init() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.cv_exits_list, this);
        mRecyclerView = mView.findViewById(R.id.exitsList);
        title = mView.findViewById(R.id.exitsHistoryTv);
        setupList();
    }

    private void setupList() {
        mAdapter = new ExitsTableAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getData(long userId, int amount) {
        mDisposable.add(UsersRepository.getInstance(getContext()).getUserExitsById(userId, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData, this::onError));
    }

    private void setData(List<ExitForm> forms) {
        if (forms.size() > 0) {
            mAdapter.setData(forms);
            if (title != null) {
                title.setVisibility(VISIBLE);
            }
            dataLoaded = true;
        } else {
            dataLoaded = false;
        }
        announceListeners();
    }

    private void announceListeners() {
        if (mDataLoadListener != null) {
            mDataLoadListener.exitsLoadComplete(dataLoaded);
        }
    }

    private void onError(Throwable t) {
        t.printStackTrace();
    }

    public void pauseDisposables() {
        mDisposable.clear();
    }

    public void startDisposables() {
        mDisposable = new CompositeDisposable();
    }
}
