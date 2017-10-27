package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.Serializable;

/**
 * Created by fl0 on 11/05/2017.
 */

public abstract class Cell<T> {


    public static final String CELL_BACKGROUND_COLOR = "CELL_BACKGROUND_COLOR";
    public static final String CELL_BACKGROUND_DRAWABLE = "CELL_BACKGROUND_DRAWABLE";


    private final int tag;
    private final FormFragment form;
    private Bundle configuration;

    public Cell(int tag, FormFragment form) {
        this.tag = tag;
        this.form = form;
        this.configuration = new Bundle();
    }

    protected abstract View getView(Context context);

    protected void notifyChanged(T newValue) {

        form.onCellDataChanged(this, newValue);
    }

    protected int getConfigAsInt(String key, int defaultValue)
    {
        if(configuration != null)
            return configuration.getInt(key, defaultValue);
        else
            return defaultValue;
    }

    protected String getConfigAsString(String key, String defaultValue)
    {
        if(configuration != null)
            return configuration.getString(key, defaultValue);
        else
            return defaultValue;
    }

    public int getTag() {
        return tag;
    }

    protected View applyBackgroundColor(View cell)
    {
        if(getConfigAsInt(CELL_BACKGROUND_COLOR, 0) != 0)
        {
            cell.setBackgroundColor(getConfigAsInt(CELL_BACKGROUND_COLOR, 0));
        }
        else if(getConfigAsInt(CELL_BACKGROUND_DRAWABLE, 0) != 0)
        {
            Drawable backgroung = ContextCompat.getDrawable(cell.getContext(), getConfigAsInt(CELL_BACKGROUND_DRAWABLE, 0));
            cell.setBackground(backgroung);
        }

        return cell;
    }


    /* extra args */

    public void putConfigExtra(String key, byte value) {
        this.configuration.putByte(key, value);
    }

    public void putConfigExtra(String key, int value) {
        this.configuration.putInt(key, value);
    }

    public void putConfigExtra(String key, long value) {
        this.configuration.putLong(key, value);
    }

    public void putConfigExtra(String key, double value) {
        this.configuration.putDouble(key, value);
    }

    public void putConfigExtra(String key, float value) {
        this.configuration.putFloat(key, value);
    }

    public void putConfigExtra(String key, String value) {
        this.configuration.putString(key, value);
    }

    public void putConfigExtra(String key, Serializable value) {
        this.configuration.putSerializable(key, value);
    }

    protected void onLoosingFocus(Context c)
    {

    }

    protected void onGainingFocus(Context c)
    {

    }

    public abstract void setValue(T value);
}
