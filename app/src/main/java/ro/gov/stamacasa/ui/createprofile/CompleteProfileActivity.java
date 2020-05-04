package ro.gov.stamacasa.ui.createprofile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.customviews.forms.elements.ButtonWalkthrough;
import ro.gov.stamacasa.tools.FlowIds;
import ro.gov.stamacasa.ui.form.FormActivity;
import ro.gov.stamacasa.ui.main.MainScreenActivity;

public class CompleteProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ButtonWalkthrough profileBt;
    ButtonWalkthrough addExtraProfileBt;
    Button continueBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        initViews();
    }

    private void initViews() {
        profileBt = findViewById(R.id.profileBt);
        addExtraProfileBt = findViewById(R.id.addExtraProfileBt);
        continueBt = findViewById(R.id.continueBt);

        profileBt.setOnClickListener(this);
        addExtraProfileBt.setOnClickListener(this);
        continueBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.profileBt:
                //profile button clicked
                break;

            case R.id.continueBt:
                Intent intent = new Intent(this, MainScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.addExtraProfileBt:
                Intent intentProfile = new Intent(this, FormActivity.class);
                intentProfile.putExtra("flow", FlowIds.REGISTRATION);
                intentProfile.putExtra("firstRegistration", true);
                intentProfile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentProfile);
                break;
        }
    }
}
