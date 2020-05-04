package ro.gov.stamacasa.data.pojo.forminput;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "evaluation_form")
public class EvaluationFormInput {

    public EvaluationFormInput() {

    }

    @Ignore
    public EvaluationFormInput(
            long userId, HashMap<Integer,
            List<Integer>> answersMap, long timestamp,
            HashMap<Integer, String> userInput) {

        this.userInput = userInput;
        this.userId = userId;
        this.answersMap = answersMap;
        this.timestamp = timestamp;
    }

    @Ignore
    public EvaluationFormInput(
            long userId, HashMap<Integer,
            List<Integer>> answersMap, long timestamp
            ) {

        this.userId = userId;
        this.answersMap = answersMap;
        this.timestamp = timestamp;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long userId;
    private HashMap<Integer, List<Integer>> answersMap;
    private HashMap<Integer, String> userInput;
    private long timestamp;

    public HashMap<Integer, String> getUserInput() {
        return userInput;
    }

    public void setUserInput(HashMap<Integer, String> userInput) {
        this.userInput = userInput;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public HashMap<Integer, List<Integer>> getAnswersMap() {
        return answersMap;
    }

    public void setAnswersMap(HashMap<Integer, List<Integer>> answersMap) {
        this.answersMap = answersMap;
    }

    @NonNull
    @Override
    public String toString() {
        return "user id : " + userId + " answers : " + answersMap + " input : " + userInput + " timestamp : " + timestamp;
    }
}
