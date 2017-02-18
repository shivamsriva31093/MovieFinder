package com.androidtmdbwrapper.model.core;

/**
 * Created by sHIVAM on 2/18/2017.
 */

public class AppendToResponse {
    private final AppendToResponse[] items;

    public AppendToResponse(AppendToResponse... items) {
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
