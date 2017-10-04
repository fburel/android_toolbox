package f10.net.androidtoolbox.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import f10.net.androidtoolbox.listAdapter.ViewHolder;
import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 25/07/2017.
 */

public class ActivityDialog extends AlertDialog {

    private ActivityDialogListener _listener;

    private ActivityDialog(@NonNull Context context, ArrayList<ActivityItem> items, ActivityDialogListener listener, int column, String title)
    {
        super(context);

        // Prepare grid view
        GridView gridView = new GridView(context);
        gridView.setAdapter(new ActivityItemAdapter(items, listener, this));
        gridView.setNumColumns(column);
        setView(gridView);
        setTitle(title);

    }

    public interface ActivityDialogListener {
        void onActivitySelected(int activityTag);
    }

    public static class ActivityItem{
        public final int tag;
        public final String title;
        public final Drawable icon;

        public ActivityItem(int tag, String title, Drawable icon) {
            this.tag = tag;
            this.title = title;
            this.icon = icon;
        }
    }

    private static class ActivityItemAdapter extends BaseAdapter implements ViewHolder.CallBack {

        private final ArrayList<ActivityItem> _items;
        private final ActivityDialogListener _listener;
        private final Dialog _dialog;


        public ActivityItemAdapter(ArrayList<ActivityItem> items, ActivityDialogListener activityDialogListener, Dialog dialog) {
            _items = items;
            _listener = activityDialogListener;
            _dialog = dialog;
        }

        @Override
        public int getCount() {
            return _items.size();
        }

        @Override
        public Object getItem(int i) {
            return _items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View cell = view;

            if(cell == null)
            {
                cell = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item_view, null);
                ViewHolder vh = new ViewHolder(cell, this);
                registerView(cell, vh);
                cell.setTag(vh);
            }

            ActivityItem item = (ActivityItem) getItem(i);

            ViewHolder vh = (ViewHolder) cell.getTag();

            vh.setPosition(i);

            onBindViewHolder(vh, item);

            return cell;
        }

        private void registerView(View cell, ViewHolder vh) {
            vh.registerView("imageView", cell.findViewById(R.id.imageView));
            vh.registerView("textView", cell.findViewById(R.id.textView));

        }

        private void onBindViewHolder(ViewHolder vh, ActivityItem item) {
            ((ImageView) vh.getView("imageView")).setImageDrawable(item.icon);
            ((TextView) vh.getView("textView")).setText(item.title);
        }

        @Override
        public void onClick(int position, View sender) {
            if(_listener != null) {
                _listener.onActivitySelected(_items.get(position).tag);
            }
            _dialog.dismiss();
        }
    }

    public static class Builder {

        private final Context _context;
        private ActivityDialogListener _activityDialogListener;
        private String _title = "Actitvities";
        private ArrayList<ActivityItem> _items = new ArrayList<>();
        private int _columnNumber = 3;

        public Builder(Context context) {
            _context = context;
        }

        public Builder addActivity(int tag, int actionNameRes, int iconRes){
            _items.add(new ActivityItem(tag, _context.getString(actionNameRes), _context.getDrawable(iconRes)));
            return this;
        }

        public Builder addActivity(int tag, String action, int iconRes){
            _items.add(new ActivityItem(tag, action, _context.getDrawable(iconRes)));
            return this;
        }

        public Builder addActivity(ActivityItem item){
            _items.add(item);
            return this;
        }

        public Builder addActivities(Collection<? extends ActivityItem> items){
            _items.addAll(items);
            return this;
        }

        public Builder addActivity(int tag, int actionNameRes, Drawable icone){
            _items.add(new ActivityItem(tag, _context.getString(actionNameRes), icone));
            return this;
        }

        public Builder addActivity(int tag, String action, Drawable icone){
            _items.add(new ActivityItem(tag, action, icone));
            return this;
        }

        public Builder setActivityDialogListener(ActivityDialogListener activityDialogListener) {
            _activityDialogListener = activityDialogListener;
            return this;
        }

        public Builder setTitle(String title) {
            _title = title;
            return this;
        }

        public Builder setNumberOfColumn(int number) {
            _columnNumber = number;
            return this;
        }

        public Dialog create()
        {
            return new ActivityDialog(_context, _items, _activityDialogListener, _columnNumber, _title);
        }

        public Dialog show()
        {
            Dialog dialog = this.create();

            dialog.show();

            return dialog;
        }
    }
}
