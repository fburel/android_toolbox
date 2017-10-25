package f10.net.androidtoolboxdemo.fragments;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import f10.net.androidtoolbox.Logger.Log;
import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.collections.SmartList;
import f10.net.androidtoolbox.interfaces.SortDescripor;
import f10.net.androidtoolbox.listAdapter.SmartListFragment;
import f10.net.androidtoolbox.listAdapter.ViewHolder;
import f10.net.androidtoolboxdemo.R;

/**
 * Created by fl0 on 25/10/2017.
 */

public class RevealCellListFragment extends SmartListFragment<String> implements ViewHolder.CallBack {
    private static final String TEXT_VIEW_FIELD = "TEXT_VIEW_FIELD";
    private static final String BUTTON_1 = "BUTTON_1";
    private static final String BUTTON_2 = "BUTTON_2";
    private static final String BUTTON_3 = "BUTTON_3";



    @Override
    public int getCellLayout() {
        return R.layout.swipe_reveal_cell;
    }

    @Override
    public void bindViews(View cell, ViewHolder vh) {
        vh.registerView(TEXT_VIEW_FIELD, cell.findViewById(R.id.textView));
        vh.registerView(BUTTON_1, cell.findViewById(R.id.button_action_1));
        vh.registerView(BUTTON_2, cell.findViewById(R.id.button_action_2));
        vh.registerView(BUTTON_3, cell.findViewById(R.id.button_action_3));

        vh.registerOnClickListener(BUTTON_1, this);
        vh.registerOnClickListener(BUTTON_2, this);
        vh.registerOnClickListener(BUTTON_3, this);
    }

    @Override
    public void bindViewHolder(ViewHolder vh, String item) {
        ((TextView)(vh.getView(TEXT_VIEW_FIELD))).setText(item);
    }

    @Override
    protected void refreshDataSet(Promise<Boolean> completion) {
        completion.succeeds(true);
    }

    @Override
    public List<String> getElements() {
        SmartList<String> strs = new SmartList<>(new String[]{"Rouen", "Paris", "Chicago", "Budapest", "Buccarest", "Prague", "Tokio","Pekin", "Moscow", "St Petersbourg", "Kiev", "Bratislava", "Rome", "Belgrade", "Barcelone", "Los Angeles", "San Fransisco", "Las Vegas", "Tijuana", "Lisbone", "Porto", "Venise", "Lubljana", "Sarajevo", "Bruxelles", "Liege", "Amsterdam"});
        strs.sort(new SortDescripor<String>() {
            @Override
            public boolean isBefore(String item1, String item2) {
                return item1.compareTo(item2) < 0;
            }
        });
        return strs;
    }

    @Override
    public void onSelect(String element) {

    }

    @Override
    public void onClick(int position, View sender) {
        if(sender.getId() == R.id.button_action_1)
        {
            Log.d("demo", String.format("%s action 1", getElements().get(position)));
        }
        else if(sender.getId() == R.id.button_action_2)
        {
            Log.d("demo", String.format("%s action 2", getElements().get(position)));
        }
        if(sender.getId() == R.id.button_action_3)
        {
            Log.d("demo", String.format("%s action 3", getElements().get(position)));
        }
    }
}
