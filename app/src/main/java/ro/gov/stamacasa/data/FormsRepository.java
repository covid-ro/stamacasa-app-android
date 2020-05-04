package ro.gov.stamacasa.data;

import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ro.gov.stamacasa.data.pojo.forminput.EvaluationFormInput;
import ro.gov.stamacasa.data.pojo.formsections.Answer;
import ro.gov.stamacasa.data.pojo.formsections.Flow;
import ro.gov.stamacasa.data.pojo.formsections.FlowSection;
import ro.gov.stamacasa.data.pojo.formsections.FormSections;
import ro.gov.stamacasa.data.pojo.formsections.Question;
import ro.gov.stamacasa.db.FormSectionsDatabase;
import ro.gov.stamacasa.db.evaluationform.EvaluationFormDao;
import ro.gov.stamacasa.retrofit.RetrofitService;
import ro.gov.stamacasa.retrofit.model.request.UserData;
import ro.gov.stamacasa.retrofit.model.response.form.WsResponseForm;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.tools.SectionsIds;

public class FormsRepository {

    private FormSections formSections;
    private EvaluationFormDao evaluationDao;
    private static FormsRepository instance;
    public static int symptomsQuestionId;

    private FormsRepository(Context context) {
        FormSectionsDatabase database = FormSectionsDatabase.getInstance(context.getApplicationContext());
        evaluationDao = database.formDao();
        populateForms(context);
    }

    public static synchronized FormsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new FormsRepository(context);
        }
        return instance;
    }

    public List<Integer> getQuestionIdsForSection(String id) {
        List<Integer> questionIds = new ArrayList<>();
        for (Flow flow : formSections.getData().getFlows()) {
            if (flow.getFlow_id().equals(FlowIds.REGISTRATION)) {
                for (FlowSection flowSection : flow.getFlow_sections()) {
                    if (flowSection.getSection_id().equals(id)) {
                        for (Question question : flowSection.getQuestions()) {
                            questionIds.add(question.getQuestion_id());
                        }
                    }
                }
            }
        }
        return questionIds;
    }

    public void addEvaluationForm(EvaluationFormInput input) {
        evaluationDao.insert(input);
    }

    public Single<List<EvaluationFormInput>> getAllEvaluationForms() {
        return evaluationDao.getAllEvaluationsRx();
    }

    public Single<List<EvaluationFormInput>> getLatestEvaluationForUser(long uid, int amount) {
        return evaluationDao.getEvaluationForUser(uid, amount);
    }


    public Single<List<Long>> getLatestEvaluationTimestampsById(long uid, int amount) {
        return evaluationDao.getEvaluationsTimestampsById(uid, amount);
    }

    public FormSections getProfileForms() {
        return formSections;
    }

    public Flow getEvaluationForm() {
        for (Flow flow : formSections.getData().getFlows()) {
            if (flow.getFlow_id().equals(FlowIds.EVALUATION)) {
                return flow;
            }
        }
        return null;
    }

    public Single<WsResponseForm> postUserEvaluation(UserData userData) {
        return RetrofitService.getInstance().getInterface().postForm(userData);
    }

    private void populateForms(Context context) {
        try {
            formSections = JsonProvider.getFormSections(context);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<Answer> getSymptoms() {
        ArrayList<Answer> symptoms = new ArrayList<>();
        for (Flow flow : formSections.getData().getFlows()) {
            if (flow.getFlow_id().equals(FlowIds.EVALUATION)) {
                for (FlowSection section : flow.getFlow_sections()) {
                    if (section.getSection_id().equals(SectionsIds.SYMPTOMS)) {
                        for (Question question : section.getQuestions()) {
                            symptomsQuestionId = question.getQuestion_id();
                            symptoms.addAll(question.getQuestion_answers());
                        }
                    }
                }
            }
        }
        return symptoms;
    }

    public Question getQuestionById(int id) {
        if (formSections != null)
            if (formSections.getData().getFlows() != null)
                for (Flow flow : formSections.getData().getFlows()) {
                    if (flow.getFlow_sections() != null)
                        for (FlowSection section : flow.getFlow_sections()) {
                            if (section.getQuestions() != null)
                                for (Question question : section.getQuestions()) {
                                    if (question.getQuestion_id() == id) {
                                        return question;
                                    }
                                }
                        }
                }
        return null;
    }
}
