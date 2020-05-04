package ro.gov.stamacasa.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import ro.gov.stamacasa.ui.main.frags.FragmentMy;
import ro.gov.stamacasa.ui.main.frags.FragmentOthers;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int cam_cate_sa_fie_boss = 2;

    MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return cam_cate_sa_fie_boss;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentMy.newInstance();
            case 1:
                return FragmentOthers.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Formularele Mele";
            case 1:
                return "Alte Persoane";
            default:
                return "The New Guy";
        }
    }
}