package f10.net.androidtoolbox.viewPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fl0 on 30/05/2017.
 */

public abstract class LayoutViewPagerAdapter<T> extends PagerAdapter {

    final private ArrayList<T> _items = new ArrayList<T>();

    protected final void addItem(T item)
    {
        _items.add(item);
    }

    protected final T getItem(int position)
    {
        return _items.get(position);
    }

    @Override
    public final int getCount() {
        return _items.size();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        View layout = onCreateLayout(collection, getItem(position));
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    protected abstract View onCreateLayout(ViewGroup parent, T item);
}
