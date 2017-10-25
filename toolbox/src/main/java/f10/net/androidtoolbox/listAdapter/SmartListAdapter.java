package f10.net.androidtoolbox.listAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by fl0 on 09/05/2017.
 */

public abstract class SmartListAdapter<T> extends BaseSmartAdapter  {

    private List<T> _data;

    public interface DataProvider<T> {
        List<T> getElements();
    }

    private final DataProvider _provider;

    public SmartListAdapter(DataProvider provider, EventListener listener) {
        super(listener);
         _provider = provider;
        _data = provider.getElements();
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public T getItem(int i) {
        return _data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View cell = view;

        if(cell == null)
        {
            cell = LayoutInflater.from(viewGroup.getContext()).inflate(getCellResource(), null);
            ViewHolder vh = new ViewHolder(cell, this);
            registerView(cell, vh);
            cell.setTag(vh);
        }

        T item = getItem(i);

        ViewHolder vh = (ViewHolder) cell.getTag();

        vh.setPosition(i);

        onBindViewHolder(vh, item);

        return cell;
    }


    @Override
    public void notifyDataSetChanged() {
        _data = _provider.getElements();
        super.notifyDataSetChanged();
    }

    protected abstract int getCellResource();

    protected abstract void registerView(View cell, ViewHolder vh);

    protected abstract void onBindViewHolder(ViewHolder vh, T item);

}
