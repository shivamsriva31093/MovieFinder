package task.application.com.colette.ui.utility;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;
import java.util.WeakHashMap;

import task.application.com.colette.ui.discover.DiscoverActivity;

/**
 * Created by sHIVAM on 11/3/2017.
 */

public class SplashDataHelper {
    private static WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data;
    private static WeakHashMap<DiscoverActivity.QueryType, int[]> dataPages;

    public SplashDataHelper() {

    }

    public static WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> getData() {
        return data;
    }

    public static void setData(WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data) {
        SplashDataHelper.data = data;
    }

    public static WeakHashMap<DiscoverActivity.QueryType, int[]> getDataPages() {
        return dataPages;
    }

    public static void setDataPages(WeakHashMap<DiscoverActivity.QueryType, int[]> dataPages) {
        SplashDataHelper.dataPages = dataPages;
    }
}
