package f10.net.androidtoolboxdemo;

import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import f10.net.androidtoolbox.navigation.PushPopActivity;
import f10.net.androidtoolboxdemo.fragments.CityListFragment;
import f10.net.androidtoolboxdemo.fragments.RevealCellListFragment;

/**
 * Created by fl0 on 25/10/2017.
 */

public class MainActivity extends PushPopActivity {

    @Override
    protected void askForPermissionsIfNeeded() {
        // we don't need anything here
    }

    @Override
    public Class<? extends Fragment> getStartActivityClass() {
        return RevealCellListFragment.class;
    }

    @Override
    protected boolean prepareSideMenu(Menu menu) {
        return false; // no side menu... yet...
    }

    @Override
    protected void onSideMenuItemSelected(MenuItem element) {
        // ignore
    }
}
