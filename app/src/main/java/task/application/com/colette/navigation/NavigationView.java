package task.application.com.colette.navigation;

import android.app.Activity;

/**
 * Created by sHIVAM on 6/13/2017.
 */

public interface NavigationView {
    void activityReady(Activity activity, NavigationModel.NavigationItemEnum self);

    void setUpView();

    void updateNavigationItems();

    void displayNavigationItems(NavigationModel.NavigationItemEnum[] items);

    void itemSelected(NavigationModel.NavigationItemEnum item);

    void showNavigation();
}
