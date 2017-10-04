package f10.net.androidtoolbox.listAdapter;

import android.view.View;
import android.widget.BaseAdapter;

/**
 * Created by fl0 on 24/07/2017.
 */

public abstract class BaseSmartAdapter extends BaseAdapter implements ViewHolder.CallBack {




    public interface EventListener<T> {
        void onSelect(T element);
    }

    private final EventListener _listener;

    protected BaseSmartAdapter(EventListener listener) {
        _listener = listener;
    }

    public void onClick(int position, View view) {
        if(_listener != null)
        {
            _listener.onSelect(getItem(position));
        }
    }

}
