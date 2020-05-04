package ro.gov.stamacasa.ui.tools;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import ro.gov.stamacasa.R;

public class ActivityHelper {

    public View getCustomTabViewProfile(Context nContext) {
        return LayoutInflater.from(nContext).inflate(R.layout.cv_tab_view_custom_create_profile, null);
    }


    public void inflateContinueButton(LinearLayout layout, Context context, View.OnClickListener listener) {
        if (layout != null) {

            Button button = new Button(context);
            button.setId(R.id.continueBtId);
            button.setBackgroundColor(context.getResources().getColor(R.color.colorButtonMain));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, (int) context.getResources().getDimension(R.dimen.continueButtonMarginTop), 0, 0);

            button.setLayoutParams(params);
            button.setText(context.getResources().getString(R.string.continue_button));

            button.setOnClickListener(listener);

            layout.addView(button);
        }
    }

    public void temporarilyDisableButtonClick(View view) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 2000);
    }
}
