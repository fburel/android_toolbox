package f10.net.androidtoolbox.forms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by fl0 on 08/06/2017.
 */

public abstract class PickerDialogCell<T> extends ValueCell<T> implements DialogInterface.OnDismissListener {

    private Dialog _dialog;
    private T _value;
    private String _text;

    public PickerDialogCell(int tag, FormFragment form, String label, T value) {
        super(tag, form, label, value);
        _text = label;
        _value = value;
    }

    @Override
    protected void onGainingFocus(Context c) {
        super.onGainingFocus(c);

        _dialog = onCreateDialog(c, _value);
        _dialog.setTitle(_text);
        _dialog.setOnDismissListener(this);
        _dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        _dialog = null;
    }

    protected abstract Dialog onCreateDialog(Context c, T value);

    protected void setValue(T value, boolean notifyForm)
    {
        _value = value;
        super.setValue(value);
        if(notifyForm) {
            notifyChanged(value);
        }
    }
}
