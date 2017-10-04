package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by fl0 on 13/07/2017.
 */

public class SwiperCell extends Cell<Integer> implements ViewPager.OnPageChangeListener {


    private final PagerAdapter _adapter;
    private Integer _selectedIndex;
    private ViewPager _pager;


    public SwiperCell(int tag, FormFragment form, PagerAdapter adapter1) {
        this(tag, form, adapter1, 0);
    }

    public SwiperCell(int tag, FormFragment form, PagerAdapter adapter1, int selectedIndex) {
        super(tag, form);
        _adapter = adapter1;
        _selectedIndex = selectedIndex;
    }

    @Override
    protected View getView(Context context) {

        RelativeLayout cell = new RelativeLayout(context);

        _pager = new ViewPager(context);
        cell.addView(_pager, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        TabLayout tabLayout = new TabLayout(context);// width, height
//        tabLayout.setupWithViewPager(pager);
//        RelativeLayout.LayoutParams tlparams =  new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tlparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        tlparams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        cell.addView(tabLayout, tlparams);

        _pager.setAdapter(_adapter);
        _pager.setCurrentItem(_selectedIndex);
        _pager.addOnPageChangeListener(this);

        return applyBackgroundColor(cell);
    }

    @Override
    public void setValue(Integer value) {
        _selectedIndex = value;
        if(_pager != null) {
            _pager.setCurrentItem(_selectedIndex);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //ignore
    }

    @Override
    public void onPageSelected(int position) {
        _selectedIndex = position;
        notifyChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //ignore
    }
}
