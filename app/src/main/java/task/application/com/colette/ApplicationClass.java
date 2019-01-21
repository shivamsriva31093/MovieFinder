package task.application.com.colette;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sHIVAM on 2/19/2017.
 */

public class ApplicationClass extends Application {

    private static ApplicationClass instance;
    public static final String API_KEY = BuildConfig.TMDB_API_KEY;
    public static final String OMDB_API_KEY = BuildConfig.OMDB_API_KEY;

    public ApplicationClass() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }

    public static synchronized ApplicationClass getInstance() {
        return instance;
    }
}
