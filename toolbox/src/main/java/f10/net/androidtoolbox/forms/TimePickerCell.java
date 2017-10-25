package f10.net.androidtoolbox.forms;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by fl0 on 08/06/2017.
 */

public class TimePickerCell extends PickerDialogCell<TimePickerCell.TimeComponent> implements TimePickerDialog.OnTimeSetListener {

    public static class TimeComponent {
        public final int hours;
        public final int minutes;

        public TimeComponent(int hours, int minutes) {
            this.hours = hours;
            this.minutes = minutes;
        }
    }

    public TimePickerCell(int tag, FormFragment form, String label, TimeComponent current) {
        super(tag, form, label, current);
    }

    @Override
    protected Dialog onCreateDialog(Context c, TimeComponent value) {

        return new TimePickerDialog(c, this, value.hours, value.minutes, DateFormat.is24HourFormat(c));
    }

    @Override
    protected String onUpdatingValueText(TimeComponent value) {
        return String.format("%02d:%02d", value.hours, value.minutes);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        setValue(new TimeComponent(hour, min), true);
    }
}

