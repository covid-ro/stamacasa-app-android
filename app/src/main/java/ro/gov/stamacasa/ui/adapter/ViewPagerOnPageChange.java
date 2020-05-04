package ro.gov.stamacasa.ui.adapter;


import androidx.viewpager.widget.ViewPager;

import ro.gov.stamacasa.customviews.forms.elements.ProgressTabCv;

public class ViewPagerOnPageChange implements ViewPager.OnPageChangeListener {

    private ProgressTabCv tabCv;

    public ViewPagerOnPageChange(ProgressTabCv tabCv) {
        this.tabCv = tabCv;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tabCv.setTabCompleted(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
