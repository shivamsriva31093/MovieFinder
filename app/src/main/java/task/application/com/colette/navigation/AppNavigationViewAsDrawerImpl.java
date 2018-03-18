/*
 * Copyright (c) 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package task.application.com.colette.navigation;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import task.application.com.colette.R;
import task.application.com.colette.util.CustomTypefaceSpan;
import task.application.com.colette.util.FontCache;
import task.application.com.colette.util.LogUtils;

/**
 * This is the implementation of {@link AppNavigationView} using a {@link DrawerLayout}. This
 * extends {@link AppNavigationViewAbstractImpl} so only UI specific methods are implemented.
 */
public class AppNavigationViewAsDrawerImpl extends AppNavigationViewAbstractImpl
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = AppNavigationViewAsDrawerImpl.class.getName();

    // Delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // Fade in and fade out durations for the main content when switching between
    // different Activities of the app through the Nav Drawer
    private static final int MAIN_CONTENT_FADEOUT_DURATION = 250;

    private DrawerLayout mDrawerLayout;

    // A Runnable that we should execute when the navigation drawer finishes its closing animation
    private Runnable mDeferredOnDrawerClosedRunnable;

    private NavigationView mNavigationView;

    private Handler mHandler;

    private NavigationDrawerStateListener mNavigationDrawerStateListener;

    public AppNavigationViewAsDrawerImpl(NavigationDrawerStateListener listener) {
        mNavigationDrawerStateListener = listener;
    }

    @Override
    public void displayNavigationItems(final NavigationModel.NavigationItemEnum[] items) {
        createNavDrawerItems(items);
        setSelectedNavDrawerItem(mSelfItem);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
        NavigationModel.NavigationItemEnum item = NavigationModel.NavigationItemEnum.getById(menuItem.getItemId());
        onNavDrawerItemClicked(item);
        return true;
    }

    @Override
    public void setUpView() {
        mHandler = new Handler();

        mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        mNavigationView = (NavigationView) mActivity.findViewById(R.id.nav_view);

        if (mSelfItem == NavigationModel.NavigationItemEnum.INVALID) {
            // do not show a nav drawer
            mDrawerLayout = null;
            return;
        }

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                // run deferred action, if we have one
                if (mDeferredOnDrawerClosedRunnable != null) {
                    mDeferredOnDrawerClosedRunnable.run();
                    mDeferredOnDrawerClosedRunnable = null;
                }
                mNavigationDrawerStateListener.onNavDrawerStateChanged(false, false);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mNavigationDrawerStateListener.onNavDrawerStateChanged(true, false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                mNavigationDrawerStateListener.onNavDrawerStateChanged(isNavDrawerOpen(),
                        newState != DrawerLayout.STATE_IDLE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mNavigationDrawerStateListener.onNavDrawerSlide(slideOffset);
            }
        });

//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    }

    @Override
    public void showNavigation() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void createNavDrawerItems(NavigationModel.NavigationItemEnum[] items) {
        if (mNavigationView != null) {
            Menu menu = mNavigationView.getMenu();
            for (int i = 0; i < items.length; i++) {
                MenuItem item = menu.findItem(items[i].getId());
                if (item != null) {
                    item.setVisible(true);
                    item.setIcon(items[i].getIcon());
                    item.setTitle(items[i].getTitleResource());
                } else {
                    LogUtils.LOGE(TAG, "Menu Item for navigation item with title " +
                            (items[i].getTitleResource() != 0 ? mActivity.getResources().getString(
                                    items[i].getTitleResource()) : "") + "not found");
                }
            }

            for (int i = 0; i < menu.size(); i++) {
                MenuItem mi = menu.getItem(i);
                applyFontToSubMenu(mi);
                applyFontToMenuItem(mi);
            }

            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void applyFontToSubMenu(MenuItem item) {
        SubMenu subMenu = item.getSubMenu();
        if (subMenu != null && subMenu.size() > 0) {
            for (int i = 0; i < subMenu.size(); i++) {
                MenuItem subMenuItem = subMenu.getItem(i);
                applyFontToMenuItem(subMenuItem);
            }
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = FontCache.getTypeface("Nunito-SemiBold.ttf", mActivity);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    /**
     * Sets up the given navdrawer item's appearance to the selected state. Note: this could also be
     * accomplished (perhaps more cleanly) with state-based layouts.
     */
    private void setSelectedNavDrawerItem(NavigationModel.NavigationItemEnum item) {
        if (mNavigationView != null &&
                mNavigationView.getMenu().findItem(item.getId()) != null &&
                item != NavigationModel.NavigationItemEnum.INVALID) {
            mNavigationView.getMenu().findItem(item.getId()).setChecked(true);
        }
    }


    public boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeNavDrawer() {
        if (isNavDrawerOpen()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    private void onNavDrawerItemClicked(final NavigationModel.NavigationItemEnum item) {
        if (item == mSelfItem) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            setSelectedNavDrawerItem(item);
            return;
        }

        if (isSpecialItem(item)) {
            itemSelected(item);
        } else {
            // Launch the target Activity after a short delay, to allow the close animation to play
            mHandler.postDelayed(() -> itemSelected(item), NAVDRAWER_LAUNCH_DELAY);

            // Change the active item on the list so the user can see the item changed
            setSelectedNavDrawerItem(item);
            // Fade out the main content
            View mainContent = mActivity.findViewById(R.id.main_content);
            if (mainContent != null) {
                mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
//                mainContent.animate().alpha(1).setDuration(250);
            }
        }

        // The navigation menu is accessible from some screens, via swiping, without a drawer
        // layout, eg MapActivity when accessed from SessionDetailsActivity.
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private boolean isSpecialItem(NavigationModel.NavigationItemEnum item) {
        return item == NavigationModel.NavigationItemEnum.SHARE || item == NavigationModel.NavigationItemEnum.SEND;
    }

    public interface NavigationDrawerStateListener {

        void onNavDrawerStateChanged(boolean isOpen, boolean isAnimating);

        void onNavDrawerSlide(float slideOffset);
    }
}
