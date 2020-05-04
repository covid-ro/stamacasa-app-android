package ro.gov.stamacasa.customviews.forms.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ro.gov.stamacasa.R;

public class ProgressTabCv extends LinearLayout {

    LinearLayout tabLayout;
    View view;
    List<TabItemCv> tabs = new ArrayList<>();

    public ProgressTabCv(Context context) {
        super(context);
        init(context);
        initViews();
    }

    public ProgressTabCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        initViews();
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.cv_progress_tabs, this);
    }

    private void initViews() {
        tabLayout = view.findViewById(R.id.tabLl);
    }

    public void addTabs(int number) {
        tabs.clear();
        tabLayout.setWeightSum(number);

        for (int i = 0; i < number; i++) {
            TabItemCv tabItemCv = new TabItemCv(getContext());
            LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
            if (i == 0) {
                tabItemCv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            } else {
                params.setMargins(36, 0, 0, 0);
                tabItemCv.setBackgroundColor(getResources().getColor(R.color.progressTabUnselected));
            }
            tabItemCv.setLayoutParams(params);
            tabs.add(tabItemCv);
            tabLayout.addView(tabItemCv);
        }
    }

    public void setTabCompleted(int position) {
        if (position < tabs.size())
            tabs.get(position).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }
}
