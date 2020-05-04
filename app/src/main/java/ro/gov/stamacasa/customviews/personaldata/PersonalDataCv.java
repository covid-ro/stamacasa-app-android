package ro.gov.stamacasa.customviews.personaldata;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.answer.AnswerInputCv;
import ro.gov.stamacasa.ui.createprofile.LocationDialogFragment;
import ro.gov.stamacasa.ui.createprofile.LocationListener;
import ro.gov.stamacasa.ui.createprofile.adapter.Location;
import ro.gov.stamacasa.ui.tools.ActivityHelper;

public class PersonalDataCv extends LinearLayout implements LocationListener {

    private static final String REGEX_LETTERS_AND_SPACES = "^[a-zA-Z][a-zA-Z\\s]*$";
    private static final String REGEX_NUMBERS = "[0-9]+";

    private String countyName;
    private String cityName;

    View view;
    AnswerInputCv name;
    AnswerInputCv phoneNumber;
    AnswerInputCv county;
    AnswerInputCv city;
    AnswerInputCv gender;
    AnswerInputCv age;

    public PersonalDataCv(Context context) {
        super(context);
        init(context);
    }

    public PersonalDataCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.cv_personal_data_questions, this);

        initViews();
    }

    private void initViews() {
        name = view.findViewById(R.id.nameEt);
        phoneNumber = view.findViewById(R.id.phoneEt);
        county = view.findViewById(R.id.county);
        city = view.findViewById(R.id.city);
        age = view.findViewById(R.id.ageEt);
        gender = view.findViewById(R.id.genderEt);

        handleGenderSpinner();
        handleCountyPopUp();
        handleCityPopUp();
    }

    private void handleGenderSpinner() {
        gender.setClickable(true);
        gender.setFocusable(false);
        gender.displaySpinner(getContext().getResources().getStringArray(R.array.gender));
    }

    private void handleCountyPopUp() {
        county.setClickable(true);
        county.setFocusable(false);
        county.getTextInputEditText().setOnClickListener(view -> {
            new ActivityHelper().temporarilyDisableButtonClick(county);
            showDialog(R.id.county);
        });
    }

    private void handleCityPopUp() {
        city.setClickable(true);
        city.setFocusable(false);
        city.getTextInputEditText().setOnClickListener(view -> {
            new ActivityHelper().temporarilyDisableButtonClick(county);
            showDialog(R.id.city);
        });
    }

    public String getName() {
        if (name.getTextInputEditText().getText() != null) {
            String text = name.getTextInputEditText().getText().toString();
            if (isValidLetters(text)) {
                name.getTextInputLayout().setErrorEnabled(false);
                return text;
            }
        }
        name.getTextInputLayout().setError(getResources().getText(R.string.error_name));
        return null;
    }

    public String getGender() {
        String selectedItem = (String) gender.getSpinner().getSelectedItem();

        if (!selectedItem.equals(getContext().getResources().getString(R.string.gender))) {
            gender.getTextInputLayout().setErrorEnabled(false);
            return selectedItem;
        }
        gender.getTextInputLayout().setErrorIconDrawable(null);
        gender.getTextInputLayout().setError(getResources().getText(R.string.error_gender));
        return null;
    }

    public String getPhoneNumber() {
        if (phoneNumber.getTextInputEditText().getText() != null) {
            String number = phoneNumber.getTextInputEditText().getText().toString();
            if (isValidNumbers(number, "phoneNumber")) {
                phoneNumber.getTextInputLayout().setErrorEnabled(false);
                return number;
            }
        }
        phoneNumber.getTextInputLayout().setError(getResources().getText(R.string.error_phone));
        return null;
    }

    public String getCounty() {
        if (countyName != null) {
            county.getTextInputLayout().setErrorEnabled(false);
            return countyName;
        }
        county.getTextInputLayout().setError(getResources().getText(R.string.error_county));
        return null;
    }

    public String getCity() {
        if (cityName != null) {
            city.getTextInputLayout().setErrorEnabled(false);
            return cityName;
        }
        city.getTextInputLayout().setError(getResources().getText(R.string.error_city));
        return null;
    }

    public String getAge() {
        if (age.getTextInputEditText().getText() != null) {
            String text = age.getTextInputEditText().getText().toString();
            if (isValidNumbers(text, "age")) {
                age.getTextInputLayout().setErrorEnabled(false);
                return text;
            }
        }
        age.getTextInputLayout().setError(getResources().getText(R.string.error_age));
        return null;
    }

    private boolean isValidLetters(String text) {
        if (text != null) {
            if (text.length() > 3) {
                return text.matches(REGEX_LETTERS_AND_SPACES);
            }
        }
        return false;
    }

    private boolean isValidNumbers(String text, String type) {
        if (text != null) {
            if (type.equals("phoneNumber")) {
                if (text.length() == 10) {
                    return text.matches(REGEX_NUMBERS);
                }
            } else if (type.equals("age")) {
                if (text.length() > 0 && text.length() < 3) {
                    return text.matches(REGEX_NUMBERS);
                }
            } else {
                if (text.length() > 0) {
                    return text.matches(REGEX_NUMBERS);
                }
            }
        }
        return false;
    }

    @Override
    public void locationSelected(String location, Location locationSection) {
        switch (locationSection) {
            case CITY:
                cityName = location;
                city.getTextInputEditText().setText(location);
                break;

            case COUNTY:
                countyName = location;
                county.getTextInputEditText().setText(location);
                city.getTextInputEditText().setText("");
                break;
        }
    }

    public void showDialog(int viewId) {
        FragmentActivity fragmentActivity = (FragmentActivity) getContext();
        FragmentManager fm = fragmentActivity.getSupportFragmentManager();
        LocationDialogFragment dialogView = null;

        switch (viewId) {
            case R.id.county:
                dialogView = new LocationDialogFragment(this);
                break;

            case R.id.city:
                if (countyName != null) {
                    dialogView = new LocationDialogFragment(this, countyName);
                } else {
                    Snackbar.make(this, R.string.county_first, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
        if (dialogView != null)
            dialogView.show(fm, "LocationDialog");
    }
}
