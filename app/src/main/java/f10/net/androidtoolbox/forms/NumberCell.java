package f10.net.androidtoolbox.forms;

import android.app.Dialog;
import android.content.Context;

import f10.net.androidtoolbox.views.NumberPickerDialog;


/**
 * Created by fl0 on 17/05/2017.
 */

public class NumberCell extends PickerDialogCell<Integer> implements NumberPickerDialog.OnNewValueListener {


    public static final String CONFIG_PICKER_VALUE_DISPLAY_FORMAT = "CONFIG_PICKER_VALUE_DISPLAY_FORMAT";
    private final int _min;
    private final int _max;

    private NumberCell(int tag, FormFragment form, String label, int value, int max, int min) {
        super(tag, form, label, value);
       _min = min; _max = max;
    }

    @Override
    protected Dialog onCreateDialog(Context c, Integer value) {
        NumberPickerDialog dialog = new NumberPickerDialog(c, value);
        dialog.setOnNewValueListener(this);
        dialog.setMaxValue(_max);
        dialog.setMinValue(_min);
        return dialog;
    }

    @Override
    public void onNewValueSelected(int newValue) {
        setValue(newValue, true);
    }

    @Override
    protected String onUpdatingValueText(Integer value) {
        return String.format(getConfigAsString(CONFIG_PICKER_VALUE_DISPLAY_FORMAT, "%d"), value);
    }

    public static class Builder {
        int _tag;
        FormFragment _fragment;
        private int _min = 0;
        private int _max = 100;
        private int _current = 0;
        private String _text = "";

        public Builder(int tag, FormFragment formFragment) {
            _tag = tag; _fragment = formFragment;
        }

        public NumberCell.Builder setMinimum(int min) {
            _min = min;
            return this;
        }

        public NumberCell.Builder setMaximum(int max) {
            _max = max;
            return this;
        }

        public NumberCell.Builder setCurrentValue(int value) {
            _current = value;
            return this;
        }

        public NumberCell.Builder setText(String text) {
            _text = text;
            return this;
        }

        public Cell build() {
            return new NumberCell(_tag, _fragment, _text, _current, _max, _min);
        }
    }
}
