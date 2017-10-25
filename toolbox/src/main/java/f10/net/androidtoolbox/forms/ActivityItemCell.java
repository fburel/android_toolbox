package f10.net.androidtoolbox.forms;

import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;

import java.util.List;

import f10.net.androidtoolbox.views.ActivityDialog;


/**
 * Created by fl0 on 27/07/2017.
 */

public class ActivityItemCell extends PickerDialogCell<Integer> implements ActivityDialog.ActivityDialogListener {

    private final List<ActivityDialog.ActivityItem> _items;

    private final SparseArray<String> _texts = new SparseArray<>();

    public ActivityItemCell(int tag, FormFragment form, String label, Integer value, List<ActivityDialog.ActivityItem> items) {
        super(tag, form, label, value);
        _items = items;
        for(ActivityDialog.ActivityItem item : _items){
            _texts.put(item.tag, item.title);
        }
    }

    @Override
    protected Dialog onCreateDialog(Context c, Integer value) {

        return new ActivityDialog.Builder(c)
                .addActivities(_items)
                .setActivityDialogListener(this)
                .setNumberOfColumn(3)
                .create();
    }

    @Override
    protected String onUpdatingValueText(Integer value) {
        return _texts.get(value);
    }

    @Override
    public void onActivitySelected(int activityTag) {
        setValue(activityTag, true);
    }
}
