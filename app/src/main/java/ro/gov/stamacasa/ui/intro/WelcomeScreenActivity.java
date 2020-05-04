package ro.gov.stamacasa.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.elements.ButtonWalkthrough;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.ui.form.FormActivity;
import ro.gov.stamacasa.ui.tools.ActivityHelper;

public class WelcomeScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ButtonWalkthrough profileBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        initViews();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.profileBt) {
            Intent intent = new Intent(this, FormActivity.class);
            intent.putExtra("firstRegistration", true);
            intent.putExtra("flow", FlowIds.REGISTRATION);

            new ActivityHelper().temporarilyDisableButtonClick(profileBt);

            startActivity(intent);
        }
    }

    private void initViews() {
        profileBt = findViewById(R.id.profileBt);
        profileBt.setOnClickListener(this);
    }
}
