package f10.net.androidtoolbox.navigation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import f10.net.androidtoolbox.R;


/**
 * Created by fl0 on 05/05/2017.
 */

/**
 * A frameLayout activity with an optionnal side drawer for horizontal navigation. Horizontal navigation is best serve when navigating from a DrawerActivity to another
 */
public abstract class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle _drawerToggle;
    private DrawerLayout _drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // Create the UI
        View v = onCreateMainView(savedInstanceState);

        _drawer = new DrawerLayout(this);
        _drawer.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        addContentView(_drawer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        // prep drawer if needed
        NavigationView navigationView = new NavigationView(this);
        Menu menu = navigationView.getMenu();

        if (prepareSideMenu(menu)) {

            navigationView.setNavigationItemSelectedListener(this);
            _drawer.addView(navigationView, new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.START));

            // set the header
            View header = getNavigationHeaderView(LayoutInflater.from(this));
            if(header != null) {
                navigationView.addHeaderView(header);
            }

            TypedValue a = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.colorBackground, a, true);
            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                navigationView.setBackgroundColor(a.data);
            }

            // notify when drawer opens / closes
            _drawerToggle = onCreateDrawerToggle(this, _drawer);
            _drawer.addDrawerListener(_drawerToggle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    protected abstract View onCreateMainView(Bundle savedInstanceState);

    protected View getNavigationHeaderView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.side_menu_demo_header, null);
    }

    protected DrawerToggle onCreateDrawerToggle(DrawerActivity activity, DrawerLayout drawer) {
        return new DrawerToggle(activity, drawer);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        if(_drawerToggle != null && _drawer.isDrawerOpen(Gravity.START))
        {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if(_drawerToggle != null) _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (_drawerToggle != null && _drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        onSideMenuItemSelected(item);

        _drawer.closeDrawers();

        return true;
    }

    // Must override

    protected abstract boolean prepareSideMenu(Menu menu);

    protected abstract void onSideMenuItemSelected(MenuItem element);

    protected String getDrawerTitle()
    {
        return "Menu";
    }

}
