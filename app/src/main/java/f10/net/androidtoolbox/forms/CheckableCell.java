package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 19/07/2017.
 */

public class CheckableCell extends Cell<Boolean> {

    private String _hint;
    private boolean _value;
    private ViewHolder _holder;

    private CheckableCell(int tag, FormFragment form, String hint, boolean value) {
        super(tag, form);
        _hint = hint;
        _value = value;
    }

    protected View getView(Context context)
    {

        View cell = LayoutInflater.from(context).inflate(R.layout.forms_cell_checkable, null);

        _holder = new ViewHolder(cell);
        _holder.label.setText(_hint);
        _holder.imageView.setImageResource(_value ? R.drawable.checkmark : 0);

        return applyBackgroundColor(cell);
    }

    @Override
    public void setValue(Boolean value) {
        _value = value;
        if(_holder != null){
            _holder.imageView.setImageResource(_value ? R.drawable.checkmark : 0);
        }
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


        public Builder(int tag, FormFragment formFragment) {
            _tag = tag; _fragment = formFragment;
        }

        public CheckableCell.Builder setHint(String hint) {
            _hint = hint;
            return this;
        }

        public CheckableCell.Builder setValue(boolean value) {
            _value = value;
            return this;
        }

        public Cell build() {

            CheckableCell cell = new CheckableCell(_tag, _fragment, _hint, _value);

            return cell;
        }
    }

    private class ViewHolder {
        public final TextView label;
        public final ImageView imageView;

        public ViewHolder(View cell) {

            imageView = (ImageView) cell.findViewById(R.id.image);
            label = (TextView) cell.findViewById(R.id.label);;
        }
    }
}