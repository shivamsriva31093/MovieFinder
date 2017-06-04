package task.application.com.moviefinder.ui.navdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.favorites.FavoritesMediaActivity;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationDrawerItemListeners {

    private static final int NAVDRAWER_LAUNCH_DELAY = 250;
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 250;
    private DrawerLayout drawer;
    private NavigationDrawerItemListeners listener;
    private FrameLayout frameLayout;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        frameLayout = (FrameLayout) findViewById(R.id.activity_content);
        listener = this;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        createNvDrawer();
    }

    @Override
    public void setContentView(View view) {
        frameLayout.addView(view);
    }

    protected void createNvDrawer() {

        handler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.isDrawerIndicatorEnabled();

        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void openDrawer(){
        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favorite:
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.itemSelected(new Intent(NavigationDrawerActivity.this, FavoritesMediaActivity.class));
                    }
                }, NAVDRAWER_LAUNCH_DELAY);
                break;
        }

        if (frameLayout != null) {
            frameLayout.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void itemSelected(Intent intent) {
        startActivity(intent);
        finish();
    }

}

interface NavigationDrawerItemListeners {
    void itemSelected(Intent intent);
}
