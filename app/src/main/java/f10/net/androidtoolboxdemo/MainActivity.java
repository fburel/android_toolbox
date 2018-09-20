package f10.net.androidtoolboxdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import f10.net.androidtoolbox.navigation.PushPopActivity;
import f10.net.androidtoolboxdemo.Segue.SegueEvents;
import f10.net.androidtoolboxdemo.fragments.CityListFragment;
import f10.net.androidtoolboxdemo.fragments.CityDetailFragments;

/**
 * Created by fl0 on 25/10/2017.
 */

public class MainActivity extends PushPopActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void askForPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    @Override
    public Class<? extends Fragment> getStartActivityClass() {
        return CityListFragment.class;
    }

    @Override
    protected boolean prepareSideMenu(Menu menu) {
        return false; // no side menu... yet...
    }

    @Override
    protected void onSideMenuItemSelected(MenuItem element) {
        // ignore
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SegueEvents event) {
        switch (event)
        {
            // When a city is selected, present the detail of said city (push animation)
            case CitySelected:
                push(new CityDetailFragments(), event.getBundle());
                break;
            // When a city is selected, present the detail of said city (push animation)
            case AddCity:
                presentModally(CityDetailFragments.class);
                // To present modally, add <activity android:name="f10.net.androidtoolbox.navigation.ModalActivity"> in your manifest
                break;
            case CityAdded:
                dismissModalActivity();
                break;
            case CityRemoved:
                pop();
                break;
        }
    };
}
