package ro.gov.stamacasa.ui.form;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.elements.CustomViewPager;
import ro.gov.stamacasa.customviews.forms.elements.ProgressTabCv;
import ro.gov.stamacasa.data.FormsRepository;
import ro.gov.stamacasa.data.UsersRepository;
import ro.gov.stamacasa.data.pojo.forminput.EvaluationFormInput;
import ro.gov.stamacasa.data.pojo.formsections.Flow;
import ro.gov.stamacasa.data.pojo.formsections.FlowSection;
import ro.gov.stamacasa.data.pojo.formsections.FormSections;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.retrofit.model.request.EvaluationForm;
import ro.gov.stamacasa.retrofit.model.request.HealthStatus;
import ro.gov.stamacasa.retrofit.model.request.Profile;
import ro.gov.stamacasa.retrofit.model.request.QuestionAnswer;
import ro.gov.stamacasa.retrofit.model.request.QuestionInput;
import ro.gov.stamacasa.retrofit.model.request.UserData;
import ro.gov.stamacasa.tools.EvaluationWorkManager;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.tools.SectionsIds;
import ro.gov.stamacasa.tools.SharedPref;
import ro.gov.stamacasa.ui.adapter.ProfilePagerAdapter;
import ro.gov.stamacasa.ui.adapter.ViewPagerOnPageChange;


@SuppressLint("Registered")
public class FormActivityBase extends AppCompatActivity {

    static final String TAG = "FormActivityBase";
    protected CustomViewPager pager;
    protected ProgressTabCv mProgressTabs;
    protected ProgressBar mProgressBar;
    protected ProfilePagerAdapter mPagerAdapter;
    protected ViewPagerOnPageChange mViewPagerListener;
    protected FormSections formSections;
    protected TextView headerTv;
    protected TextView headerBodyTv;

    protected long userId;
    protected Flow flow;
    protected String flowId;
    protected boolean isFirstRegistrationProcess;
    protected  Observer<WorkInfo> observer;
    protected UUID requestId;

    protected UserProfile mUserProfile;
    protected HashMap<Integer, List<Integer>> questionAnswers = new HashMap<>();
    protected HashMap<Integer, String> userInput = new HashMap<>();
    protected EvaluationFormInput evaluationFormInput;
    private final int BACKOFF_MILLIS = 3000;

    protected CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flowId = getIntent().getStringExtra("flow");
        userId = getIntent().getLongExtra("userId", -1);
        mUserProfile = new UserProfile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDisposable = new CompositeDisposable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisposable.clear();
        if (observer!=null && requestId != null){
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(requestId).removeObserver(observer);
            observer = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (observer!=null && requestId != null){
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(requestId).removeObserver(observer);
        }
    }

    protected void getStarted() {
        if (flowId != null) {
            switch (flowId) {
                case FlowIds.REGISTRATION:
                    formSections = FormsRepository.getInstance(this).getProfileForms();
                    isFirstRegistrationProcess = getIntent().getBooleanExtra("firstRegistration", false);
                    setUpCustomTabsRegistration();
                    break;

                case FlowIds.EVALUATION:
                    flow = FormsRepository.getInstance(this).getEvaluationForm();
                    setUpCustomTabsEvaluation();
                    break;
            }
            initHeader();
        }
    }

    protected void setUpCustomTabsRegistration() {
        if (formSections != null) {
            mViewPagerListener = new ViewPagerOnPageChange(mProgressTabs);
            pager.addOnPageChangeListener(mViewPagerListener);
            try {
                for (Flow flow : formSections.getData().getFlows()) {
                    if (flow.getFlow_id().equals(FlowIds.REGISTRATION)) {
                        inflateProgressTabs(flow.getFlow_sections().size());
                    }
                    int i = 0;
                    for (FlowSection flowSection : flow.getFlow_sections()) {
                        mPagerAdapter.addFragment(new FormFragment(flowSection, i, FlowIds.REGISTRATION, flow.getFlow_sections().size(), isFirstRegistrationProcess));
                        mPagerAdapter.notifyDataSetChanged();
                        i++;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "tabs are null " + e.getMessage());
            }
        }
    }

    protected void setUpCustomTabsEvaluation() {
        if (flow != null) {
            mViewPagerListener = new ViewPagerOnPageChange(mProgressTabs);
            pager.addOnPageChangeListener(mViewPagerListener);
            try {
                if (flow.getFlow_id().equals(FlowIds.EVALUATION)) {
                    inflateProgressTabs(flow.getFlow_sections().size());
                }
                int i = 0;
                for (FlowSection flowSection : flow.getFlow_sections()) {
                    mPagerAdapter.addFragment(new FormFragment(flowSection, i, FlowIds.EVALUATION, flow.getFlow_sections().size()));
                    mPagerAdapter.notifyDataSetChanged();
                    i++;
                }
            } catch (Exception e) {
                Log.e(TAG, "tabs are null " + e.getMessage());
            }
        }
    }

    public void setUserPersonalData(String name, String phoneNumber, String county, String city, int age, String gender) {
        mUserProfile.setName(name);
        mUserProfile.setGender(gender);
        mUserProfile.setAge(age);
        mUserProfile.setCity(city);
        mUserProfile.setCounty(county);
        mUserProfile.setPhoneNumber(phoneNumber);
    }

    public void addUserToDatabase() {
        new Thread(() -> {
            if (mUserProfile != null) {
                mUserProfile.setQuestionAnswers(questionAnswers);
                if (userInput.size() > 0) {
                    mUserProfile.setUserInput(userInput);
                }
                UsersRepository.getInstance(this).addUserProfile(mUserProfile, this);
            }
        }).start();
    }

    public void addEvaluationFormToDatabase() {
        mProgressTabs.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            if (questionAnswers != null && userId != -1) {
                long timestamp = new Date().getTime();
                evaluationFormInput = new EvaluationFormInput(userId, questionAnswers, timestamp);
                if (userInput.size() > 0) {
                    evaluationFormInput.setUserInput(userInput);
                }
                saveForm();
                prepareServerUserData(evaluationFormInput);
            }
        }).start();
    }

    @WorkerThread
    private void addLastFormToUser(long userId, long timestamp) {
        UsersRepository.getInstance(this).updateUserLastForm(userId, timestamp);
    }

    @SuppressLint("CheckResult")
    @WorkerThread
    private void prepareServerUserData(EvaluationFormInput evaluationFormInput) {
        mDisposable.add(UsersRepository.getInstance(getApplicationContext()).getUserById(evaluationFormInput.getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> { sendDataToWorkManager(createUserData(user)); }, this::onErrorReceived));
    }

    private UserData createUserData(UserProfile user) {
        UserData userData = new UserData(
                new Profile(user.getAge(), new HealthStatus(SectionsIds.HEALTH_STATUS, getFilteredQAsForProfile(user.getQuestionAnswers()))),
                new EvaluationForm(getQuestionAnswers(evaluationFormInput.getAnswersMap()), getQuestionInput(evaluationFormInput.getUserInput())));
        Log.d("FormActivityBase", "new user data created : " + new Gson().toJson(userData, UserData.class));
        return userData;
    }

    @WorkerThread
    private void sendDataToWorkManager(UserData userData) {
        WorkManager mWorkManager = WorkManager.getInstance(this);

        Gson gson = new Gson();
        Data.Builder data = new Data.Builder();
        data.putString(EvaluationWorkManager.USER_DATA, gson.toJson(userData));
        data.putString(EvaluationWorkManager.EVALUATION_FORM, gson.toJson(evaluationFormInput));

        Constraints.Builder builder = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED);

        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(EvaluationWorkManager.class)
                .setInputData(data.build())
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .setConstraints(builder.build()).build();

        mWorkManager.enqueue(mRequest);
        monitorWork(mRequest);
    }

    private void monitorWork(OneTimeWorkRequest request) {
        requestId = request.getId();
        checkConnectivity();

        observer = workInfo -> {
            if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                mProgressTabs.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                showOnlyPositiveButtonDialog(workInfo.getOutputData().getString(EvaluationWorkManager.EVALUATION_MESSAGE), getResources().getString(R.string.alert_form_title));
            } else if (workInfo != null && workInfo.getRunAttemptCount() > 1) {
                showOnlyPositiveButtonDialog(getResources().getString(R.string.server_overload), getResources().getString(R.string.alert_form_title));
            }
        };

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(requestId).observe(this, observer);
    }

    private void saveForm() {
        new Thread(() -> {
            if (evaluationFormInput != null) {
                FormsRepository.getInstance(this).addEvaluationForm(evaluationFormInput);
                addLastFormToUser(userId, evaluationFormInput.getTimestamp());
            }
        }).start();
    }

    private void onErrorReceived(Throwable throwable) {
        throwable.printStackTrace();
    }

    private List<QuestionAnswer> getQuestionAnswers(HashMap<Integer, List<Integer>> map) {
        List<QuestionAnswer> questionAnswers = new ArrayList<>();
        if (map != null && map.size() > 0) {
            for (Map.Entry<Integer, List<Integer>> entry : map.entrySet()) {
                QuestionAnswer questionAnswer = new QuestionAnswer(entry.getKey(), entry.getValue());
                questionAnswers.add(questionAnswer);
            }
        }
        return questionAnswers;
    }

    private List<QuestionInput> getQuestionInput(HashMap<Integer, String> map) {
        List<QuestionInput> questionAnswers = new ArrayList<>();
        if (map != null && map.size() > 0) {
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                QuestionInput questionInput = new QuestionInput(entry.getKey(), Collections.singletonList(entry.getValue()));
                questionAnswers.add(questionInput);
            }
        }
        return questionAnswers;
    }

    private List<QuestionAnswer> getFilteredQAsForProfile(HashMap<Integer, List<Integer>> map) {
        List<Integer> questionIds = FormsRepository.getInstance(this).getQuestionIdsForSection(SectionsIds.HEALTH_STATUS);
        HashMap<Integer, List<Integer>> newMap = new HashMap<>();
        if (questionIds != null) {
            for (Integer id : questionIds) {
                if (map.containsKey(id)) {
                    newMap.put(id, map.get(id));
                }
            }
        }
        return getQuestionAnswers(newMap);
    }

    public void initHeader() {
        switch (flowId) {
            case FlowIds.EVALUATION:
                headerBodyTv.setText(getResources().getString(R.string.form_intro));
                headerTv.setText(getResources().getString(R.string.form));
                break;

            case FlowIds.REGISTRATION:
                headerBodyTv.setText(getResources().getString(R.string.create_profile_intro));
                handleAdminOrOtherUserRegistration();
                break;
        }
    }

    private void handleAdminOrOtherUserRegistration() {
        if (!checkIfAdmin()) {
            mUserProfile.setAdmin(true);
            headerTv.setText(R.string.your_profile);
        } else {
            mUserProfile.setAdmin(false);
            headerTv.setText(R.string.other_profile);
        }
    }

    private boolean checkIfAdmin() {
        long firstUserId = SharedPref.getInstance(this).getLong(this, "adminUid");
        return firstUserId != -1;
    }

    public void setTab(int i) {
        pager.setCurrentItem(i);
    }

    private void inflateProgressTabs(int numberOfTabs) {
        mProgressTabs.addTabs(numberOfTabs);
    }

    public void addQuestionsAndAnswers(Map<Integer, List<Integer>> answers) {
        questionAnswers.putAll(answers);
    }

    public void addUserInput(Map.Entry<Integer, String> input) {
        userInput.put(input.getKey(), input.getValue());
    }

    private void showOnlyPositiveButtonDialog(String message, String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> finish())
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void checkConnectivity() {
        if (!isConnected()) {
            mProgressTabs.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            showOnlyPositiveButtonDialog(getResources().getString(R.string.no_connectivity_evaluation), getResources().getString(R.string.alert_form_title));
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}
