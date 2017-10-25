package f10.net.androidtoolbox.forms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by fl0 on 20/07/2017.
 */

public class EditableTextCell extends PickerDialogCell<String> {


    public EditableTextCell(int tag, FormFragment form, String label, String value) {
        super(tag, form, label, value);
    }

    @Override
    protected Dialog onCreateDialog(Context c, String value) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        final EditText input = new EditText(c);
        input.setText(value);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setValue(input.getText().toString(), true);
                dialog.cancel();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    @Override
    protected String onUpdatingValueText(String value) {
        return value;
    }
}
