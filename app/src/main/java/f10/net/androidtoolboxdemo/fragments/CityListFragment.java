package f10.net.androidtoolboxdemo.fragments;

import android.view.View;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;

import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.collections.SmartList;
import f10.net.androidtoolbox.interfaces.SortDescripor;
import f10.net.androidtoolbox.listAdapter.SmartListFragment;
import f10.net.androidtoolbox.listAdapter.ViewHolder;

/**
 * Created by fl0 on 25/10/2017.
 */

public class CityListFragment extends SmartListFragment<String> {

    private static final String TEXT_VIEW_FIELD = "TEXT_VIEW_FIELD";

    @Override
    public int getCellLayout() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void bindViews(View cell, ViewHolder vh) {
        vh.registerView(TEXT_VIEW_FIELD, cell.findViewById(android.R.id.text1));
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
    public String getTitle() {
        return "Cities";
    }
}
