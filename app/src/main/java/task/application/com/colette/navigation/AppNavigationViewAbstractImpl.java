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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

import task.application.com.colette.R;
import task.application.com.colette.navigation.NavigationModel.NavigationItemEnum;
import task.application.com.colette.navigation.NavigationModel.NavigationQueryEnum;
import task.application.com.colette.navigation.NavigationModel.NavigationUserActionEnum;
import task.application.com.colette.util.ActivityUtils;

/**
 * This abstract class implements both {@link UpdatableView} and {@link AppNavigationView}, without
 * any specific UI implementation details. This uses the {@link } for getting its data and processing user actions. Some methods which are UI
 * specific are left abstract. Extend this class for full navigation functionality.
 */
public abstract class AppNavigationViewAbstractImpl implements
        UpdatableView<NavigationModel, NavigationModel.NavigationQueryEnum, NavigationModel.NavigationUserActionEnum>,
        AppNavigationView {

    protected Activity mActivity;
    protected NavigationItemEnum mSelfItem;
    private UpdatableView.UserActionListener mUserActionListener;

    @Override
    public void displayData(final NavigationModel model,
                            final NavigationModel.NavigationQueryEnum query) {
        switch (query) {
            case LOAD_ITEMS:
                displayNavigationItems(model.getItems());
                break;
        }
    }

    @Override
    public void displayErrorMessage(final NavigationModel.NavigationQueryEnum query) {
        switch (query) {
            case LOAD_ITEMS:
                // No error message displayed
                break;
        }
    }

    @Override
    public void activityReady(Activity activity, NavigationItemEnum self) {
        mActivity = activity;
        mSelfItem = self;

        setUpView();

        NavigationModel model = new NavigationModel(getContext());
        PresenterImpl presenter = new PresenterImpl(model, this,
                NavigationUserActionEnum.values(), NavigationQueryEnum.values());
        presenter.loadInitialQueries();
        addListener(presenter);
    }

    @Override
    public void updateNavigationItems() {
        mUserActionListener.onUserAction(NavigationUserActionEnum.RELOAD_ITEMS, null);
    }

    @Override
    public abstract void displayNavigationItems(final NavigationItemEnum[] items);

    @Override
    public abstract void setUpView();

    @Override
    public abstract void showNavigation();

    @Override
    public void itemSelected(final NavigationItemEnum item) {
        switch (item) {

            case SHARE:
                startShareIntent();
                break;

            case SEND:
                Intent sendIntent = createEmailIntent(
                        "moveupdev@gmail.com",
                        "Feedback:",
                        ""
                );
                mActivity.startActivity(sendIntent);
                break;

            case FAVORITES:
                if (item.getClassToLaunch() != null) {
                    Intent intent = new Intent(mActivity, item.getClassToLaunch());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(intent);
                    if (item.isFinish()) {
                        mActivity.finish();
                    }
                }
                break;

            default:
                if (item.getClassToLaunch() != null) {
                    ActivityUtils.createBackStack(mActivity,
                            new Intent(mActivity, item.getClassToLaunch()));
                    if (item.isFinish()) {
                        mActivity.finish();
                    }
                }
                break;
        }
    }

    private void startShareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out this app: " +
                "https://play.google.com/store/apps/details?id=task.application.com.colette"
        );
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(
                Intent.createChooser(
                        sendIntent, mActivity.getResources().getText(R.string.app_title)
                )
        );
    }

    public Intent createEmailIntent(final String toEmail,
                                    final String subject,
                                    final String message) {
        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);
        sendTo.setData(uri);

        List<ResolveInfo> resolveInfos =
                mActivity.getPackageManager().queryIntentActivities(sendTo, 0);

        // Emulators may not like this check...
        if (!resolveInfos.isEmpty()) {
            return sendTo;
        }

        // Nothing resolves send to, so fallback to send...
        Intent send = new Intent(Intent.ACTION_SEND);

        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_EMAIL,
                new String[]{toEmail});
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, message);

        return Intent.createChooser(send, R.string.app_title + "");
    }

    @Override
    public void displayUserActionResult(final NavigationModel model,
                                        final NavigationModel.NavigationUserActionEnum userAction, final boolean success) {
        switch (userAction) {
            case RELOAD_ITEMS:
                displayNavigationItems(model.getItems());
                break;
        }
    }

    @Override
    public Uri getDataUri(final NavigationModel.NavigationQueryEnum query) {
        // This feature has no Uri
        return null;
    }

    @Override
    public Context getContext() {
        return mActivity;
    }

    @Override
    public void addListener(final UpdatableView.UserActionListener listener) {
        mUserActionListener = listener;
    }
}
