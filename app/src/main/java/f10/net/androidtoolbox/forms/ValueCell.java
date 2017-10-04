package f10.net.androidtoolbox.forms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import f10.net.androidtoolbox.R;

/**
 * Created by fl0 on 25/07/2017.
 */

public abstract class ValueCell<T> extends Cell<T> {

    private class ViewHolder {

        private final TextView textLabel;
        private final TextView valueLabel;

        private ViewHolder(TextView textLabel, TextView valueLabel) {
            this.textLabel = textLabel;
            this.valueLabel = valueLabel;
        }
    }

    private ViewHolder _holder;
    private String _text;
    private T _value;

    public ValueCell(int tag, FormFragment form, String label, T value) {
        super(tag, form);
        _text = label;
        _value = value;
    }



    @Override
    protected View getView(Context context) {

        View cell = LayoutInflater.from(context).inflate(R.layout.forms_cell_value, null);

        _holder = new ViewHolder(
                (TextView) cell.findViewById(R.id.hintTextView),
                (TextView) cell.findViewById(R.id.valueTextView)
        );

        _holder.textLabel.setText(_text);
        _holder.valueLabel.setText(onUpdatingValueText(_value));

        return applyBackgroundColor(cell);
    }

    protected abstract String onUpdatingValueText(T value);

    @Override
    public void setValue(T value) {
        _value = value;
        _holder.valueLabel.setText(onUpdatingValueText(value));
    }
}
