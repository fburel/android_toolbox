package f10.net.androidtoolbox.listAdapter;

import android.view.View;

import java.util.HashMap;

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

    public ViewHolder(View cell) {
        views = new HashMap<String, View>();
        cell.setOnClickListener(this);
        _callback = null;
    }

    public ViewHolder(View cell, CallBack callBack) {
        views = new HashMap<String, View>();
        cell.setOnClickListener(this);
        _callback = callBack;
    }

    public void registerView(String label, View v)
    {
        views.put(label, v);
    }

    public View getView(String label) {
        return views.get(label);
    }

    @Override
    public void onClick(View view) {
        if(_callback != null) {
            _callback.onClick(position, view);
        }
    }

    public void setPosition(int position) {
        this.position = position;
    }
}