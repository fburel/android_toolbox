package f10.net.androidtoolboxdemo.fragments;

import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Comparator;
import java.util.List;

import f10.net.androidtoolbox.ServiceLocator;
import f10.net.androidtoolbox.asynclib.Promise;
import f10.net.androidtoolbox.collections.SmartList;
import f10.net.androidtoolbox.interfaces.SortDescripor;
import f10.net.androidtoolbox.listAdapter.SmartListFragment;
import f10.net.androidtoolbox.listAdapter.ViewHolder;
import f10.net.androidtoolbox.navigation.BundleBuilder;
import f10.net.androidtoolboxdemo.IServices.ICityRepository;
import f10.net.androidtoolboxdemo.SQLite.City;
import f10.net.androidtoolboxdemo.Segue.SegueEvents;

/**
 * Created by fl0 on 25/10/2017.
 */

public class CityListFragment extends SmartListFragment<City> {

    private static final String TEXT_VIEW_FIELD = "TEXT_VIEW_FIELD";

    @Override
    public void onStart() {
        super.onStart();
        setRightButtonItem("Add", android.R.drawable.ic_menu_add);
        onRefresh();
    }

    @Override
    protected void onRightButtonItemClicked() {
        EventBus.getDefault().post(SegueEvents.AddCity);
    }

    @Override
    public int getCellLayout() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    public void bindViews(View cell, ViewHolder vh) {
        vh.registerView(TEXT_VIEW_FIELD, cell.findViewById(android.R.id.text1));
    }

    @Override
    public void bindViewHolder(ViewHolder vh, City item) {
        ((TextView)(vh.getView(TEXT_VIEW_FIELD))).setText(item.getName());
    }

    @Override
    protected void refreshDataSet(Promise<Boolean> completion) {
        completion.succeeds(true);
    }

    @Override
    public List<City> getElements() {
        return ServiceLocator.get(ICityRepository.class).getAllCities();
    }

    @Override
    public void onSelect(City element) {
        EventBus.getDefault().post(SegueEvents.CitySelected.addBundle(BundleBuilder
                .withSerializable("CitySelected", element)
                .createBundle())
        );
    }

    @Override
    public String getTitle() {
        return "Cities";
    }
}
