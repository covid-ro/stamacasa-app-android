package ro.gov.stamacasa.customviews.forms.elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import ro.gov.stamacasa.R;

public class TabItemCv extends View {

    View tabView;

    public TabItemCv(Context context) {
        super(context);
        init(context);
    }

    public TabItemCv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.cv_tab_view_custom_create_profile, null);
        tabView = view.findViewById(R.id.customTab);
    }

    public void setTabColor(int colorResource) {
        tabView.setBackgroundColor(colorResource);
    }
}
