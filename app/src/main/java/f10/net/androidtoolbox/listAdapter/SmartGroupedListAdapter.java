package f10.net.androidtoolbox.listAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fl0 on 24/07/2017.
 */

public abstract class SmartGroupedListAdapter<T> extends BaseSmartAdapter {

    public interface DataProvider<T> {

        int getNumberOfSections();

        int getNumberOfElements(int section);

        T getElement(int section, int position);

        String getSectionTitle(int i);
    }

    private final DataProvider<T> _provider;

    private final ArrayList<Integer> _sectionHeaderPosition = new ArrayList<Integer>();

    protected SmartGroupedListAdapter(DataProvider<T> provider, EventListener<T> listener) {
        super(listener);
        _provider = provider;
    }

    @Override
    public boolean isEnabled(int position) {
        return _sectionHeaderPosition.contains(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        _sectionHeaderPosition.clear();
        int count = 0;
        for(int i = 0 ; i < _provider.getNumberOfSections(); i++) {
            _sectionHeaderPosition.add(count);
            count++ ;
            count +=  _provider.getNumberOfElements(i);
        }
        return count;
    }

    @Override
    public T getItem(int i) {

        int sectionIdx = findSectionIndex(i);

        int sectionHeaderPosition = _sectionHeaderPosition.get(sectionIdx);

        return sectionHeaderPosition == i ? null : _provider.getElement(sectionIdx, i - sectionHeaderPosition -1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return _sectionHeaderPosition.contains(position) ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View cell = convertView;

        if(_sectionHeaderPosition.contains(position)){

            String title = _provider.getSectionTitle(_sectionHeaderPosition.indexOf(position));

            if(cell == null) {
                cell = onCreateSectionHeaderView(parent.getContext());
            }

            onSetSectionHeaderTitle(cell, title);

            return cell;

        } else {

            if(cell == null)
            {
                cell = LayoutInflater.from(parent.getContext()).inflate(getCellResource(), null);

                ViewHolder vh = new ViewHolder(cell, this);
                registerView(cell, vh);
                cell.setTag(vh);
            }

            T item = getItem(position);

            ViewHolder vh = (ViewHolder) cell.getTag();

            vh.setPosition(position);

            onBindViewHolder(vh, item);

            return cell;
        }
    }

    public void onSetSectionHeaderTitle(View header, String title) {

        TextView textView = (TextView) header.findViewById(android.R.id.text1);

        textView.setText(title);

    }

    public View onCreateSectionHeaderView(Context context) {
       return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
    }

    private int findSectionIndex(int position) {

        for(int i = _sectionHeaderPosition.size(); i > 0 ; i--) {

            int value = _sectionHeaderPosition.get(i - 1);

            if(position >= value) {
                return i - 1;
            }
        }

        return -1;
    }

    public abstract void onBindViewHolder(ViewHolder vh, T item);

    public abstract void registerView(View cell, ViewHolder vh);

    public abstract int getCellResource();

}
