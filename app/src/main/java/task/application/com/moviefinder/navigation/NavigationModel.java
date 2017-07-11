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

package task.application.com.moviefinder.navigation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.discover.DiscoverActivity;
import task.application.com.moviefinder.ui.favorites.FavoritesMediaActivity;
import task.application.com.moviefinder.ui.search.SearchActivity;

/**
 * Determines which items to show in the {@link AppNavigationView}.
 */
public class NavigationModel implements Model<NavigationModel.NavigationQueryEnum, NavigationModel.NavigationUserActionEnum> {

    private Context mContext;

    private NavigationItemEnum[] mItems;

    public NavigationModel(Context context) {
        mContext = context;
    }

    public NavigationItemEnum[] getItems() {
        return mItems;
    }

    @Override
    public NavigationQueryEnum[] getQueries() {
        return NavigationQueryEnum.values();
    }

    @Override
    public NavigationUserActionEnum[] getUserActions() {
        return NavigationUserActionEnum.values();
    }

    @Override
    public void deliverUserAction(final NavigationUserActionEnum action,
                                  @Nullable final Bundle args, final UserActionCallback callback) {
        switch (action) {
            case RELOAD_ITEMS:
                mItems = null;
                populateNavigationItems();
                callback.onModelUpdated(this, action);
                break;
        }
    }

    @Override
    public void requestData(final NavigationQueryEnum query,
                            final DataQueryCallback callback) {
        switch (query) {
            case LOAD_ITEMS:
                if (mItems != null) {
                    callback.onModelUpdated(this, query);
                } else {
                    populateNavigationItems();
                    callback.onModelUpdated(this, query);
                }
                break;
        }
    }

    private void populateNavigationItems() {

        NavigationItemEnum[] items = NavigationConfig.NAV_ITEMS;

        mItems = NavigationConfig.filterOutItemsDisabledInBuildConfig(items);
    }

    @Override
    public void cleanUp() {
        mContext = null;
    }

    /**
     * List of all possible navigation items.
     **/
    public enum NavigationItemEnum {
        FAVORITES(R.id.nav_favorite, R.string.favorites,
                R.drawable.heart_outline, FavoritesMediaActivity.class),
        SEARCH_HOME(R.id.nav_search, R.string.advanced_search,
                R.drawable.magnify, SearchActivity.class),
        DISCOVER_MOVIES(R.id.nav_recent, R.string.recent_movies,
                R.drawable.calendar, DiscoverActivity.class),
        IMDB_TOP_250(R.id.nav_top_250, R.string.imdb_top_250,
                R.drawable.trophy_variant_outline, null),
        SHARE(R.id.nav_share, R.string.share, R.drawable.ic_menu_share, null),
        SEND(R.id.nav_send, R.string.send, R.drawable.ic_menu_send, null),
        INVALID(),
        SEARCHLIST(),
        SEARCHDETAIL();

        private int id;
        private int titleResource;
        private int icon;
        private Class classToLaunch;
        private boolean finish;

        NavigationItemEnum() {
            this(12, 0, 0, null);
        }

        NavigationItemEnum(int id, int titleResource, int icon, Class classToLaunch) {
            this(id, titleResource, icon, classToLaunch, false);
        }

        NavigationItemEnum(int id, int titleResourse, int icon, Class classToLaunch, boolean finish) {
            this.id = id;
            this.titleResource = titleResourse;
            this.icon = icon;
            this.classToLaunch = classToLaunch;
            this.finish = finish;
        }

        public static NavigationItemEnum getById(int id) {
            NavigationItemEnum[] values = NavigationItemEnum.values();
            for (NavigationItemEnum item : values)
                if (item.getId() == id)
                    return item;
            return INVALID;
        }

        public int getId() {
            return id;
        }

        public int getTitleResource() {
            return titleResource;
        }

        public int getIcon() {
            return icon;
        }

        public Class getClassToLaunch() {
            return classToLaunch;
        }

        public boolean isFinish() {
            return finish;
        }
    }

    public enum NavigationQueryEnum implements QueryEnum {
        LOAD_ITEMS(0);

        private int id;

        NavigationQueryEnum(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String[] getProjection() {
            return new String[0];
        }
    }

    public enum NavigationUserActionEnum implements UserActionEnum {
        RELOAD_ITEMS(0);

        private int id;

        NavigationUserActionEnum(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }
    }
}
