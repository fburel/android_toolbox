package f10.net.androidtoolbox.forms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by fl0 on 27/06/2017.
 */

public class ListPickerCell extends PickerDialogCell<Integer> implements DialogInterface.OnClickListener{

    private final String[] _values;

    public ListPickerCell(int tag, FormFragment form, String label, Integer value, String[] values) {
        super(tag, form, label, value);
        _values = values;
    }

    @Override
    protected Dialog onCreateDialog(Context c, Integer value) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setItems(_values, this);
        return builder.create();
    }

    @Override
    protected String onUpdatingValueText(Integer value) {
        return _values[value];
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        setValue(i, true);
    }
}
