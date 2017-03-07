package com.androidtmdbwrapper.model.core;

import com.androidtmdbwrapper.enums.AppendToResponseItem;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public class AppendToResponse {
    private final AppendToResponseItem[] items;

    public AppendToResponse(AppendToResponseItem... items) {
        this.items = items;
    }

    @Override
    public String toString() {
        if (items != null && items.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < items.length; i++) {
                sb.append(items[i]);
                if (i < items.length)
                    sb.append(',');
            }
            return sb.toString();
        }
        return "";
    }
}
