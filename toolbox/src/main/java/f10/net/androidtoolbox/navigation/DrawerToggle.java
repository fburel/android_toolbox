package f10.net.androidtoolbox.navigation;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.View;

/**
 * Created by fl0 on 14/06/2017.
 */

public class DrawerToggle extends ActionBarDrawerToggle {


    private final DrawerActivity _activity;

    public DrawerToggle(DrawerActivity activity, DrawerLayout drawerLayout) {
        super(activity, drawerLayout, 0, 0);
        _activity = activity;

    }

    /** Called when a drawer has settled in a completely closed state. */
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        _activity.getSupportActionBar().setTitle(_activity.getTitle()); // Change the title
        _activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }

    /** Called when a drawer has settled in a completely open state. */
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        _activity.getSupportActionBar().setTitle(_activity.getDrawerTitle());
        _activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
    }
}
