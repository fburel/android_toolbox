package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 11/05/2017.
 */

public class SwitchCell extends Cell<Boolean> implements View.OnClickListener {

    private String _hint;
    private boolean _value;
    private SwitchCompat _checkBox;

    public static final String SWITCH_THUMB_DRAWABLE_RES = "SWITCH_THUMB_DRAWABLE_RES";
    public static final String SWITCH_TRACK_DRAWABLE_RES = "SWITCH_TRACK_DRAWABLE_RES";


    private SwitchCell(int tag, FormFragment form, String hint, boolean value) {
        super(tag, form);
        _hint = hint;
        _value = value;
    }

    protected View getView(Context context)
    {

        View cell = LayoutInflater.from(context).inflate(R.layout.forms_cell_checkbox, null);

        TextView label = (TextView) cell.findViewById(R.id.label);
        _checkBox = (SwitchCompat) cell.findViewById(R.id.checkbox);

        label.setText(_hint);
        _checkBox.setChecked(_value);

        _checkBox.setOnClickListener(this);

        if(getConfigAsInt(SWITCH_THUMB_DRAWABLE_RES, 0) != 0) _checkBox.setThumbResource(getConfigAsInt(SWITCH_THUMB_DRAWABLE_RES, 0));
        if(getConfigAsInt(SWITCH_TRACK_DRAWABLE_RES, 0) != 0) _checkBox.setTrackResource(getConfigAsInt(SWITCH_TRACK_DRAWABLE_RES, 0));


        return applyBackgroundColor(cell);
    }

    @Override
    public void setValue(Boolean value) {
        _value = value;
        if(_checkBox != null){
            _checkBox.setChecked(_value);
        }
    }

    @Override
    public void onClick(View view) {
        _value = _checkBox.isChecked();
        notifyChanged(_value);
    }

    @Override
    protected void onGainingFocus(Context c) {
        setValue(!_value);
        notifyChanged(_value);
    }

    public static class Builder {
        int _tag;
        FormFragment _fragment;
        private String _hint = "";
        private boolean _value = false;
        private int _track = 0;
        private int _thumb = 0;

        public Builder(int tag, FormFragment formFragment) {
            _tag = tag; _fragment = formFragment;
        }

        public SwitchCell.Builder setHint(String hint) {
            _hint = hint;
            return this;
        }

        public SwitchCell.Builder setValue(boolean value) {
            _value = value;
            return this;
        }

        public SwitchCell.Builder setTrackDrawable(int ressource) {
            _track = ressource;
            return this;
        }

        public SwitchCell.Builder setThumbDrawable(int ressource) {
            _thumb = ressource;
            return this;
        }

        public Cell build() {

            SwitchCell cell = new SwitchCell(_tag, _fragment, _hint, _value);
            cell.putConfigExtra(SWITCH_TRACK_DRAWABLE_RES, _track);
            cell.putConfigExtra(SWITCH_THUMB_DRAWABLE_RES, _thumb);

            return cell;
        }
    }

}
