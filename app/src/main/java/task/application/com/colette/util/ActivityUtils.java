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

package task.application.com.colette.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import task.application.com.colette.R;
import task.application.com.colette.ui.utility.widgets.GeneralTextView;

/**
 * Utility methods for activities management.
 */
public class ActivityUtils {

    /**
     * Enables back navigation for activities that are launched from the NavBar. See {@code
     * AndroidManifest.xml} to find out the parent activity names for each activity.
     *
     * @param intent
     */
    public static void createBackStack(Activity activity, Intent intent) {
        TaskStackBuilder builder = TaskStackBuilder.create(activity);
        builder.addNextIntentWithParentStack(intent);
        builder.startActivities();
    }

    public static void startNewActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static void showBottomSheetMessage(String message, Context context, @DrawableRes int imgSrc, long delay, DialogInterface.OnDismissListener listener) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View sheet = LayoutInflater.from(context).inflate(R.layout.bottomsheet_layout, null);
        bottomSheetDialog.setContentView(sheet);

        GeneralTextView msgView = (GeneralTextView) sheet.findViewById(R.id.msg);
        ImageView msgLogo = (ImageView) sheet.findViewById(R.id.msg_logo);
        if(imgSrc != -1)    msgLogo.setImageDrawable(ContextCompat.getDrawable(context, imgSrc));
        msgView.setText(message);
        if (listener != null) {
//            bottomSheetDialog.setCanceledOnTouchOutside(true);
            bottomSheetDialog.setOnDismissListener(listener);
        }
        bottomSheetDialog.show();
        if (delay > 0) {
            new Handler().postDelayed(bottomSheetDialog::dismiss, delay);
        }
    }


}
