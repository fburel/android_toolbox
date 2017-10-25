package f10.net.androidtoolbox.listAdapter;

import android.view.View;

import java.util.HashMap;

import f10.net.androidtoolbox.collections.SmartList;
import f10.net.androidtoolbox.interfaces.Executor;

/**
 * Created by fl0 on 24/07/2017.
 */

public class ViewHolder implements View.OnClickListener {

    public interface CallBack
    {
        void onClick(int position, View sender);
    }

    private int position;
    private final HashMap<String, View> views;
    private final CallBack _callback;
    private final View _cell;
    private final HashMap<View, SmartList<CallBack>> _extraCallbacks;

    public ViewHolder(View cell) {
        this(cell, null);
    }

    public ViewHolder(View cell, CallBack callBack) {
        views = new HashMap<>();
        cell.setOnClickListener(this);
        _cell = cell;
        _callback = callBack;
        _extraCallbacks = new HashMap<>();
    }

    public void registerView(String label, View v)
    {
        views.put(label, v);
    }

    public View getView(String label) {
        return views.get(label);
    }

    @Override
    public void onClick(final View view) {
        if(view == _cell &&_callback != null) {
            _callback.onClick(position, view);
        }
        else if(_extraCallbacks.containsKey(view))
        {
            _extraCallbacks.get(view).forEach(new Executor<CallBack>() {
                @Override
                public void executeWith(CallBack object) {
                    object.onClick(position, view);
                }
            });
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void registerOnClickListener(String viewKey, CallBack callback) {
        View v = getView(viewKey);
        v.setOnClickListener(this);
        if(!_extraCallbacks.containsKey(v))
        {
            _extraCallbacks.put(v, new SmartList<CallBack>());
        }
        _extraCallbacks.get(v).add(callback);
    }
}