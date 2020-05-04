package ro.gov.stamacasa.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import io.reactivex.schedulers.Schedulers;
import ro.gov.stamacasa.BuildConfig;
import ro.gov.stamacasa.R;
import ro.gov.stamacasa.retrofit.RetrofitService;
import ro.gov.stamacasa.retrofit.model.response.versioning.WsResponse;
import ro.gov.stamacasa.tools.SharedPref;
import ro.gov.stamacasa.ui.main.MainScreenActivity;

public class SplashScreenActivity extends AppCompatActivity {

    final Handler mHandler = new Handler();
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkVersion();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Check with WS for latest app version
     */
    private void checkVersion() {
        RetrofitService.getInstance().getInterface().getVersion().observeOn(Schedulers.io()).subscribeOn(Schedulers.io()).subscribe(this::getVersionSuccess,this::getVersionError);
    }

    /**
     * Ws request error
     * @param nError - Throwable
     */
    void getVersionError(Throwable nError) {
        mDelay();
    }

    /**
     * Ws request success
     * @param mWsResponse - ws success response message
     */
    private void getVersionSuccess(WsResponse mWsResponse) {
        if (!mWsResponse.data.android.allowedversions.contains(BuildConfig.VERSION_NAME)) {
            Snackbar.make(findViewById(R.id.nContainer), getString(R.string.version_not_ok), Snackbar.LENGTH_SHORT).show();
        }
        mDelay();
    }

    /**
     * Give snackbar a chance to open :)
     */
    private void mDelay() {
        mHandler.postDelayed(this::mRedirect, 500);
    }

    /**
     * Go where you have to go! :)
     */
    private void mRedirect() {
        long userId = SharedPref.getInstance(this).getLong(this, "adminUid");

        if (userId==-1){
            startActivity(new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class));
        } else {
            startActivity(new Intent(SplashScreenActivity.this, MainScreenActivity.class));
        }

        finish();
    }
}