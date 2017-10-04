package f10.net.androidtoolbox.forms;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fl0 on 14/06/2017.
 */

public class DatePickerCell extends PickerDialogCell<Date> implements DatePickerDialog.OnDateSetListener {

    private long _min;
    private long _max;


    private DatePickerCell(int tag, FormFragment fragment, String text, long min, long max, Date current) {
        super(tag, fragment, text, current);
        _max = max; _min = min;
    }

    @Override
    protected Dialog onCreateDialog(Context c, Date current) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(current);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog picker = new DatePickerDialog(c, this, year, month, day);
        picker.getDatePicker().setMinDate(_min);
        picker.getDatePicker().setMaxDate(_max);
        return picker;

    }

    @Override
    protected String onUpdatingValueText(Date current) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(current);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        setValue(c.getTime(), true);
    }


    public static class Builder {
        int _tag;
        FormFragment _fragment;
        private long _min;
        private long _max;
        private Date _current;
        private String _text;

        public Builder(int tag, FormFragment formFragment) {
            _tag = tag; _fragment = formFragment;
        }

        public Builder setMinimumDate(Date date) {
            _min = date.getTime();
            return this;
        }

        public Builder setMaximumDate(Date date) {
            _max = date.getTime();
            return this;
        }

        public Builder setDefaultDate(Date date) {
            _current = date;
            return this;
        }

        public Builder setText(String text) {
            _text = text;
            return this;
        }

        public Cell build() {
            return new DatePickerCell(_tag, _fragment, _text, _min, _max, _current);
        }
    }
}
