package ro.gov.stamacasa.data.pojo.userprofile;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "users_profile")
public class UserProfile {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private boolean isAdmin;
    private String name;
    private String phoneNumber;
    private String county;
    private String city;
    private int age;
    private String gender;
    private HashMap<Integer, List<Integer>> questionAnswers;
    private HashMap<Integer, String> userInput;
    private long lastFormDate;

    @Ignore
    public UserProfile(String name, String phoneNumber, String county,
                       String city, int age, HashMap<Integer, List<Integer>> questionAnswers,
                       String gender, HashMap<Integer, String> userInput) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.county = county;
        this.city = city;
        this.age = age;
        this.gender = gender;
        this.userInput = userInput;
        this.questionAnswers = questionAnswers;
    }

    public UserProfile() {

    }

    @Ignore
    public UserProfile(boolean isAdmin, String name, String phoneNumber, String county, String city, int age) {
        this.isAdmin = isAdmin;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.county = county;
        this.city = city;
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getLastFormDate() {
        return lastFormDate;
    }

    public void setLastFormDate(long lastFormDate) {
        this.lastFormDate = lastFormDate;
    }

    public HashMap<Integer, List<Integer>> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(HashMap<Integer, List<Integer>> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public HashMap<Integer, String> getUserInput() {
        return userInput;
    }

    public void setUserInput(HashMap<Integer, String> userInput) {
        this.userInput = userInput;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @NonNull
    @Override
    public String toString() {
        return " name " + name + " question answers " + questionAnswers;
    }
}
