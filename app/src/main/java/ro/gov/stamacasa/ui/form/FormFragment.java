package ro.gov.stamacasa.ui.form;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.FormAnswerListener;
import ro.gov.stamacasa.customviews.forms.question.LabelQuestionCv;
import ro.gov.stamacasa.customviews.forms.question.QuestionLayoutCv;
import ro.gov.stamacasa.customviews.personaldata.PersonalDataCv;
import ro.gov.stamacasa.data.FormsRepository;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.data.pojo.formsections.FlowSection;
import ro.gov.stamacasa.data.pojo.formsections.Question;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.tools.QuestionTypes;
import ro.gov.stamacasa.ui.createprofile.CompleteProfileActivity;
import ro.gov.stamacasa.ui.main.MainScreenActivity;
import ro.gov.stamacasa.ui.main.RedirectTab;
import ro.gov.stamacasa.ui.tools.ActivityHelper;

import static ro.gov.stamacasa.tools.SectionsIds.PERSONAL_DATA;

public class FormFragment extends Fragment implements View.OnClickListener, FormAnswerListener {

    private FlowSection flowSection;
    private View view;
    private LinearLayout profileSectionLl;
    private LinearLayout buttonLayout;
    private ActivityHelper helper = new ActivityHelper();
    private List<QuestionLayoutCv> questionLayouts = new ArrayList<>();
    private Map<Integer, List<Answer>> allAnswers = new HashMap<>();
    private String flowId;
    private PersonalDataCv personalDataCv;
    private int tabNr;
    private int totalTabs;
    private FormActivityBase base;
    private boolean firstRegistration;

    FormFragment(FlowSection section, int tabNr, String flowId, int totalTabs, boolean firstRegistration) {
        this.tabNr = tabNr;
        flowSection = section;
        this.flowId = flowId;
        this.firstRegistration = firstRegistration;
        this.totalTabs = totalTabs;
    }

    FormFragment(FlowSection section, int tabNr, String flowId, int totalTabs) {
        this.tabNr = tabNr;
        flowSection = section;
        this.flowId = flowId;
        this.totalTabs = totalTabs;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.continueBtId) {
            if (base != null) {
                if (questionLayouts.size() > 0) {
                    if (checkIfMandatoryAnswersCompleted()) {
                        if (tabNr == totalTabs - 1) {
                            view.setEnabled(false);
                            view.setBackgroundColor(getResources().getColor(R.color.transparentGray));
                            handleCompleteFlow();
                        } else {
                            base.setTab(tabNr + 1);
                        }
                    }
                } else {
                    if (flowSection.getSection_id().equals(PERSONAL_DATA)) {
                        if (updateUserData())
                            base.setTab(tabNr + 1);
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_section_fragment, container, false);

        initViews();
        inflateCvs();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null)
            base = (FormActivityBase) getActivity();
    }

    private void initViews() {
        profileSectionLl = view.findViewById(R.id.profileSectionLl);
        buttonLayout = view.findViewById(R.id.buttonLayout);
    }

    @Override
    public void onAnswerAdded(Question question, List<Answer> answers) {
        allAnswers.put(question.getQuestion_id(), answers);
        checkForFollowUpQuestions(question, answers);
    }

    @Override
    public void onAnswerRemoved(Question question, List<Answer> answers) {
        allAnswers.remove(question.getQuestion_id());
    }

    private void inflateCvs() {
        if (flowSection != null)
            if (flowSection.getSection_name() != null) {

                //inflates section label
                profileSectionLl.addView(new LabelQuestionCv(getContext(), flowSection.getSection_name()));

                //inflates section questions
                if (flowSection.getSection_id().equals(PERSONAL_DATA)) {
                    inflatePersonalDataQuestions();
                    helper.inflateContinueButton(profileSectionLl, getContext(), this);
                    return;
                }

                if (flowSection.getQuestions() != null)
                    for (Question question : flowSection.getQuestions()) {
                        QuestionLayoutCv questionLayoutCv = new QuestionLayoutCv(getContext(), question, this, false);
                        if (!question.isQuestion_hidden()) {
                            questionLayouts.add(questionLayoutCv);
                            profileSectionLl.addView(questionLayoutCv);
                        }
                    }
                helper.inflateContinueButton(buttonLayout, getContext(), this);
            }
    }

    private void inflatePersonalDataQuestions() {
        personalDataCv = new PersonalDataCv(getContext());
        personalDataCv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        profileSectionLl.addView(personalDataCv);
    }

    private boolean checkIfMandatoryAnswersCompleted() {
        for (QuestionLayoutCv questionLayoutCv : questionLayouts) {
            Question question = questionLayoutCv.getQuestion();
            if (question != null)
                if (question.getQuestion_type().equals(QuestionTypes.SINGLE_CHOICE)) {
                    if (questionLayoutCv.getQuestion() != null) {
                        if (!checkForAnswer(questionLayoutCv.getQuestion().getQuestion_id())) {
                            showQuestionError(questionLayoutCv);
                            return false;
                        }
                    }
                }
        }
        if (base != null)
            base.addQuestionsAndAnswers(transformAnswersToStringList(allAnswers));

        saveFormData();
        return true;
    }

    private void showQuestionError(QuestionLayoutCv questionLayoutCv) {
        questionLayoutCv.setQuestionError();
        Snackbar.make(view, getResources().getString(R.string.question_error), Snackbar.LENGTH_SHORT).show();
    }

    private boolean checkForAnswer(int id) {
        return allAnswers.containsKey(id);
    }

    private HashMap<Integer, List<Integer>> transformAnswersToStringList(Map<Integer, List<Answer>> map) {
        HashMap<Integer, List<Integer>> answersMap = new HashMap<>();
        for (Map.Entry<Integer, List<Answer>> entry : allAnswers.entrySet()) {
            answersMap.put(entry.getKey(), getAnswerIds(entry.getValue()));
        }
        return answersMap;
    }

    private boolean updateUserData() {
        String age = personalDataCv.getAge();
        String phoneNumber = personalDataCv.getPhoneNumber();
        String county = personalDataCv.getCounty();
        String city = personalDataCv.getCity();
        String name = personalDataCv.getName();
        String gender = personalDataCv.getGender();

        if (age != null && phoneNumber != null && county != null && city != null && name != null && gender != null) {
            if (base != null) {
                base.setUserPersonalData(name, phoneNumber, county, city, Integer.parseInt(age), gender);
            }
            return true;
        }
        return false;
    }

    //using this method we can save all the answers to the form questions.
    //it is called after the data has already been validated.
    private void saveFormData() {
        for (QuestionLayoutCv questionLayoutCv : questionLayouts) {
            switch (questionLayoutCv.getQuestion().getQuestion_type()) {
                case QuestionTypes.MULTIPLE_CHOICE:
                    checkForUserInputMultipleChoice(questionLayoutCv);
                    break;

                case QuestionTypes.SINGLE_CHOICE:
                    checkForUserInputSingleChoice(questionLayoutCv);
                    break;
            }
        }
        if (base != null) {
            base.addQuestionsAndAnswers(transformAnswersToStringList(allAnswers));
        }
    }

    private void checkForUserInputMultipleChoice(QuestionLayoutCv questionLayoutCv) {
        if (questionLayoutCv.getUserFollowUpInput() != null) {
            for (Answer answer : questionLayoutCv.getSelectedAnswers()) {
                for (Map.Entry<Integer, String> entry : questionLayoutCv.getUserFollowUpInput().entrySet()) {
                    if (answer.getAnswer_id() == entry.getKey()) {
                        base.addUserInput(entry);
                    }
                }
            }
        }
    }

    private void checkForUserInputSingleChoice(QuestionLayoutCv questionLayoutCv) {
        if (questionLayoutCv.getUserFollowUpInput() != null) {
            for (Map.Entry<Integer, String> entry : questionLayoutCv.getUserFollowUpInput().entrySet()) {
                if (questionLayoutCv.getSelectedAnswer().getAnswer_id() == (entry.getKey())) {
                    base.addUserInput(entry);
                }
            }
        }
    }

    private void checkForFollowUpQuestions(Question question, List<Answer> answers) {
        QuestionLayoutCv questionLayoutCv = null;
        for (Answer answer : answers) {
            if (answer.getAnswer_decision().getAnswer_question_id() != 0) {
                Question nextQuestion = FormsRepository.getInstance(getContext()).getQuestionById(answer.getAnswer_decision().getAnswer_question_id());
                boolean alreadyExistent = false;
                for (QuestionLayoutCv layout : questionLayouts) {
                    if (layout.getQuestion() != null && nextQuestion != null)
                        if (layout.getQuestion().getQuestion_id() == nextQuestion.getQuestion_id()) {
                            alreadyExistent = true;
                            break;
                        }
                }
                if (!alreadyExistent) {
                    questionLayoutCv = new QuestionLayoutCv(getContext(), nextQuestion, this, true);
                }
            }
        }
        if (questionLayoutCv != null) {
            questionLayouts.add(questionLayoutCv);
            profileSectionLl.addView(questionLayoutCv);
        }
        updateQuestionLayouts(question, answers);
    }

    private void updateQuestionLayouts(Question question, List<Answer> answers) {
        QuestionLayoutCv layoutToBeRemoved = null;

        for (Answer answer : question.getQuestion_answers()) {
            if (!answers.contains(answer)) {
                if (answer.getAnswer_decision().getAnswer_question_id() != 0) {
                    for (QuestionLayoutCv layout : questionLayouts) {
                        if (layout.getQuestion() != null)
                            if (layout.getQuestion().getQuestion_id() == answer.getAnswer_decision().getAnswer_question_id()) {
                                profileSectionLl.removeView(layout);
                                layoutToBeRemoved = layout;
                            }
                    }
                }
            }
        }
        if (layoutToBeRemoved != null) {
            questionLayouts.remove(layoutToBeRemoved);
        }
    }

    private ArrayList<Integer> getAnswerIds(List<Answer> answers) {
        ArrayList<Integer> answerIds = new ArrayList<>();
        for (Answer answer : answers) {
            answerIds.add(answer.getAnswer_id());
        }
        return answerIds;
    }

    private void handleCompleteFlow() {
        switch (flowId) {
            case FlowIds.REGISTRATION:
                base.addUserToDatabase();
                handleFirstRegistration();
                break;

            case FlowIds.EVALUATION:
                base.addEvaluationFormToDatabase();
                break;
        }
    }

    private void handleFirstRegistration() {
        if (firstRegistration) {
            Intent intent = new Intent(getActivity(), CompleteProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            if (getActivity() != null) {
                getActivity().finish();
            } else {
                Intent intent = new Intent(getActivity(), MainScreenActivity.class);
                intent.putExtra("redirectTab", RedirectTab.OTHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
