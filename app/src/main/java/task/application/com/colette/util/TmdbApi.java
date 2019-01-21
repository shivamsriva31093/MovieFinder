package task.application.com.colette.util;

import com.androidtmdbwrapper.Tmdb;

import okhttp3.OkHttpClient;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.remote.tmdb.LiveNetworkMonitor;
import task.application.com.colette.remote.tmdb.NetworkMonitor;

/**
 * Created by sHIVAM on 2/20/2017.
 */

public class TmdbApi extends Tmdb {

    private static final OkHttpClient client = new OkHttpClient();
    private static TmdbApi instance;
    public static final String API_KEY = "api_key";
    private final String apiKey;
    private NetworkMonitor networkMonitor;

    private TmdbApi(String apiKey, NetworkMonitor networkMonitor) {
        super(apiKey, ApplicationClass.OMDB_API_KEY);
        this.apiKey = apiKey;
        this.networkMonitor = networkMonitor;
    }

    String getApiKey() {
        return apiKey;
    }

    @Override
    protected void configOkHttpClient(OkHttpClient.Builder builder) {
        builder.addInterceptor(new CustomInterceptor(this, networkMonitor));
//        builder.addInterceptor(chain -> {
//            if(networkMonitor.isConnected()) {
//                return chain.proceed(chain.request());
//            } else {
//                throw new NoNetworkException();
//            }
//        });
    }

    @Override
    protected synchronized OkHttpClient okHttpClient() {
        if (getOkHttpClient() == null) {
            OkHttpClient.Builder builder = client.newBuilder();
            setOkHttpClient(builder.build());
        }
        return getOkHttpClient();
    }

    public static TmdbApi getApiClient(String apiKey) {
        if (instance == null)
            instance = new TmdbApi(apiKey, new LiveNetworkMonitor(ApplicationClass.getInstance()));
        return instance;
    }
}
