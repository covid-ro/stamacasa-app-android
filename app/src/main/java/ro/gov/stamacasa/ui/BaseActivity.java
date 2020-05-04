package ro.gov.stamacasa.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import ro.gov.stamacasa.R;
import ro.gov.stamacasa.tools.SharedPref;
import ro.gov.stamacasa.ui.about.AboutActivity;
import ro.gov.stamacasa.ui.main.MainScreenActivity;
import ro.gov.stamacasa.ui.myprofile.MyProfileActivity;
import ro.gov.stamacasa.ui.othersprofile.OthersProfilesActivity;
import ro.gov.stamacasa.ui.personalhistory.PersonalHistoryActivity;
import ro.gov.stamacasa.ui.settings.SettingsActivity;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected Toolbar mToolbar;
    protected NavigationView navigationView;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigation();
        initToolbar();
        initDrawer();
    }


    /**
     * Init menu left
     */
    protected void initNavigation() {
        mDrawer = findViewById(R.id.main_drawer_layout);

        navigationView = findViewById(R.id.main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Init top toolbar
     */
    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar2);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Init menu drawer trigger
     */
    private void initDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
        int color = getResources().getColor(getResources().getIdentifier("colorPrimary", "color", getPackageName()));
        mDrawerToggle.getDrawerArrowDrawable().setColor(color);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem nItem) {
        Intent mIntent = null;

        switch (nItem.getItemId()) {
            case R.id.nMenuPrincipal:
                mIntent = new Intent(this, MainScreenActivity.class);
                break;

            case R.id.nMenuProfil:
                mIntent = new Intent(this, MyProfileActivity.class);
                break;

            case R.id.nMenuProfile:
                mIntent = new Intent(this, OthersProfilesActivity.class);
                break;

            case R.id.nMenuIstoric:
                mIntent = new Intent(this, PersonalHistoryActivity.class);
                mIntent.putExtra("userId", SharedPref.getInstance(this).getLong(this, "adminUid"));
                break;

            case R.id.nSetari:
                mIntent = new Intent(this, SettingsActivity.class);
                break;

            case R.id.nDespre:
                mIntent = new Intent(this, AboutActivity.class);
                break;
        }

        if (mIntent!=null) {
            startActivity(mIntent);
        }

        DrawerLayout drawer = findViewById(R.id.main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
