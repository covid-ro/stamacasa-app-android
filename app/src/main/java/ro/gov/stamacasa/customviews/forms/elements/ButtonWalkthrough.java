package ro.gov.stamacasa.customviews.forms.elements;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import ro.gov.stamacasa.R;

public class ButtonWalkthrough extends RelativeLayout {

    private TextView labelTv;
    private ImageView labelIc;
    private RelativeLayout iconL;
    private ConstraintLayout buttonL;

    private String labelText;
    private int labelIcon;

    private Context context;
    private View view;

    public ButtonWalkthrough(Context context) {
        super(context);
    }

    public ButtonWalkthrough(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cv_progress_button_splashscreen, this);

        initViews();
        assignValues(attrs);
    }

    public ButtonWalkthrough(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initViews() {
        labelTv = view.findViewById(R.id.labelTv);
        labelIc = view.findViewById(R.id.labelIc);
        buttonL = view.findViewById(R.id.progressBtSplashscreenCl);
        iconL = view.findViewById(R.id.labelRl);

    }

    private void assignValues(AttributeSet attributeSet) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.ButtonWalkthrough,
                0, 0);
        try {
            labelText = a.getString(R.styleable.ButtonWalkthrough_labelText);
            labelIcon = a.getResourceId(R.styleable.ButtonWalkthrough_labelIcon, 0);
        } finally {
            a.recycle();
            assignValuesToViews();
        }
    }

    private void assignValuesToViews() {

        labelTv.setText(labelText);
        labelIc.setImageResource(labelIcon);

    }

    private void assignSelectedColors() {
        buttonL.setBackgroundColor(getResources().getColor(R.color.backgroundBtProgressSelected));
        iconL.setBackgroundColor(getResources().getColor(R.color.backgroundIconProgressSelected));
    }

    private void assignNonSelectedColors() {
        buttonL.setBackgroundColor(getResources().getColor(R.color.backgroundBtProgressUnselected));
        iconL.setBackgroundColor(getResources().getColor(R.color.backgroundIconBtProgressUnselected));
    }
}
