package ro.gov.stamacasa.customviews.personalprofile;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.gov.stamacasa.R;

public class PersonalProfileSectionCv extends LinearLayout {

    private View mView;
    private TextView mTitle;
    private String title;
    private PersonalProfileSectionTypes mType;
    private HashMap<String, ArrayList<String>> mData;
    private Map<String, String> mPersonalData;
    private LinearLayout mMainLayout;

    public PersonalProfileSectionCv(Context context, String title, PersonalProfileSectionTypes type, HashMap<String, ArrayList<String>> data) {
        super(context);
        Log.d("PersonalCv", " data is " + data);
        this.title = title;
        this.mData = data;
        this.mType = type;
        init();
    }

    public PersonalProfileSectionCv(Context context, String title, PersonalProfileSectionTypes type, Map<String, String> data) {
        super(context);
        this.title = title;
        this.mPersonalData = data;
        this.mType = type;
        init();
    }

    public PersonalProfileSectionCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.cv_personal_profile_section, this);
        mTitle = mView.findViewById(R.id.sectionTitle);
        mMainLayout = mView.findViewById(R.id.sectionLayout);

        setSectionTitle();
        inflateData();
    }

    public void setTitle(String title) {
        this.title = title;
        setSectionTitle();
    }

    public void setType(PersonalProfileSectionTypes type) {
        this.mType = type;
    }

    private void setSectionTitle() {
        if (title != null)
            mTitle.setText(title);
    }

    private void inflateData() {
        switch (mType) {
            case PERSONAL_DATA:
                inflatePersonalDataType();
                break;

            case OTHER:
                inflateOtherDataType();
                break;
        }
    }

    private void inflatePersonalDataType() {
        int i = 0;
        for (Map.Entry<String, String> entry : mPersonalData.entrySet()) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setLayoutParams(getParamsWrapWrap());
            layout.setOrientation(HORIZONTAL);

            if (i == 0) {
                layout.setPadding(0, 24, 0, 0);
            } else {
                layout.setPadding(0, 2, 0, 0);
            }

            addTextViewsToPersonalDataLayout(entry, layout);
            mMainLayout.addView(layout);
            i++;
        }
    }

    private void addTextViewsToPersonalDataLayout(Map.Entry<String, String> entry, LinearLayout layout) {
        TextView key = new TextView(getContext());
        key.setLayoutParams(getParamsWrapWrap());
        key.setTextColor(getResources().getColor(R.color.bodyTextColor));
        key.setText(entry.getKey() + ": ");

        TextView value = new TextView(getContext());
        value.setLayoutParams(getParamsWrapWrap());
        value.setTextColor(getResources().getColor(R.color.bodyTextColor));
        value.setText(entry.getValue());
        value.setPadding(8, 0, 0, 0);

        layout.addView(key);
        layout.addView(value);
    }

    private void inflateOtherDataType() {
        for (Map.Entry<String, ArrayList<String>> entry : mData.entrySet()) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setPadding(0, 24, 0, 0);
            layout.setLayoutParams(getParamsWrapWrap());
            layout.setOrientation(VERTICAL);

            addTextViewToOtherData(entry, layout);
            mMainLayout.addView(layout);
        }
    }

    private void addTextViewToOtherData(Map.Entry<String, ArrayList<String>> entry, LinearLayout layout) {
        TextView key = new TextView(getContext());
        key.setLayoutParams(getParamsWrapWrap());
        key.setTextSize(11);
        key.setTextColor(getResources().getColor(R.color.bodyTextColor));
        key.setTypeface(key.getTypeface(), Typeface.ITALIC);
        key.setText(entry.getKey());
        layout.addView(key);

        int i = 0;

        TextView value = new TextView(getContext());
        value.setLayoutParams(getParamsMatchWrap());
        value.setPadding(0, 4, 0, 0);

        StringBuilder stringBuilder = new StringBuilder();

        for (String answer : entry.getValue()) {
            value.setLayoutParams(getParamsWrapWrap());
            value.setTextColor(getResources().getColor(R.color.bodyTextColor));
            if (i > 0) {
                stringBuilder.append(", ").append(answer);
            } else if (i == 0) {
                stringBuilder.append(answer);
            }
            i++;
        }

        String finalString = stringBuilder.toString();
        value.setText(finalString);
        layout.addView(value);
    }

    private LayoutParams getParamsWrapWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private ViewGroup.LayoutParams getParamsMatchWrap() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
