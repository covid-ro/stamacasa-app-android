package ro.gov.stamacasa.customviews.personalprofile;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.data.FormsRepository;
import ro.gov.stamacasa.data.UsersRepository;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.data.pojo.formsections.Flow;
import ro.gov.stamacasa.data.pojo.formsections.FlowSection;
import ro.gov.stamacasa.data.pojo.formsections.FormSections;
import ro.gov.stamacasa.data.pojo.formsections.Question;
import ro.gov.stamacasa.data.pojo.userprofile.UserProfile;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.tools.SectionsIds;

public class PersonalProfileCv extends LinearLayout {

    private long userId = -1;
    private LinearLayout mainLayout;
    private View mView;
    private CompositeDisposable mDisposable;
    private HashMap<String, String> userMap;
    private UserProfile userProfile;
    private FormSections mFormSections;

    public PersonalProfileCv(Context context, long userId) {
        super(context);
        this.userId = userId;
        init();
    }

    public PersonalProfileCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        this.mView = LayoutInflater.from(getContext()).inflate(R.layout.cv_personal_profile, this);
        mainLayout = mView.findViewById(R.id.personalProfileLayout);
        if (userId != -1)
            populateLayout();
    }

    public void startDisposables() {
        this.mDisposable = new CompositeDisposable();
    }

    public void pauseDisposables() {
        mDisposable.clear();
    }

    public void setUserId(long userId) {
        this.userId = userId;
        populateLayout();
    }

    private void populateLayout() {
        if (mDisposable != null)
            getUserData();
    }

    private void getUserData() {
        mDisposable.add(UsersRepository.getInstance(getContext()).getUserById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData, this::onError));
    }


    private void setData(UserProfile userProfile) {
        this.userProfile = userProfile;

        populateForms();
    }

    //CONSIDER SENDING TO BACK THREAD
    private void populateForms() {
        mFormSections = FormsRepository.getInstance(getContext()).getProfileForms();

        for (Flow flow : mFormSections.getData().getFlows()) {
            if (flow.getFlow_id().equals(FlowIds.REGISTRATION)) {
                for (FlowSection section : flow.getFlow_sections()) {
                    if (section.getSection_id().equals(SectionsIds.PERSONAL_DATA)) {
                        inflatePersonalDataSection(section);
                    } else {
                        inflateOtherDataSection(section);
                    }
                }
            }
        }
    }

    private void onError(Throwable t) {
        Log.d("PersonalProfileCv", "on error" + t.getLocalizedMessage());
        t.printStackTrace();
    }

    private void inflatePersonalDataSection(FlowSection section) {
        HashMap<String, String> personalDataMap = new HashMap<>();

        if (userProfile != null) {
            personalDataMap.put(getResources().getString(R.string.name), userProfile.getName());
            personalDataMap.put(getResources().getString(R.string.age), Integer.toString(userProfile.getAge()));
            personalDataMap.put(getResources().getString(R.string.phone), userProfile.getPhoneNumber());
            personalDataMap.put(getResources().getString(R.string.city), userProfile.getCity());
            personalDataMap.put(getResources().getString(R.string.county), userProfile.getCounty());
            personalDataMap.put(getResources().getString(R.string.gender), userProfile.getGender());
        }

        mainLayout.addView(new PersonalProfileSectionCv(getContext(),
                section.getSection_name(),
                PersonalProfileSectionTypes.PERSONAL_DATA,
                personalDataMap));
    }

    private void inflateOtherDataSection(FlowSection section) {
        HashMap<String, ArrayList<String>> questionsMap = new HashMap<>();

        for (Question question : section.getQuestions()) {
            for (Map.Entry<Integer, List<Integer>> userEntry : userProfile.getQuestionAnswers().entrySet()) {
                Log.d("PersonalProfile", "key : " + userEntry.getKey().getClass() + " " + userEntry.getKey());
                if (question.getQuestion_id() == userEntry.getKey()) {
                    ArrayList<String> answersText = new ArrayList<>();
                    if (userEntry.getValue() != null)
                        for (Integer userAnswer : userEntry.getValue()) {
                            for (Answer answer : question.getQuestion_answers()) {
                                if (userAnswer.equals(answer.getAnswer_id())) {
                                    answersText.add(answer.getAnswer_text());
                                }
                                questionsMap.put(question.getQuestion_text(), answersText);
                            }
                        }
                }
            }
        }

        if (questionsMap.size() > 0) {
            mainLayout.addView(new PersonalProfileSectionCv(getContext(),
                    section.getSection_name(),
                    PersonalProfileSectionTypes.OTHER,
                    questionsMap));
        }
    }

    public void removeAllLayouts() {
        mainLayout.removeAllViews();
    }
}
