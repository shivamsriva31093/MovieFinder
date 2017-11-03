package task.application.com.moviefinder.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.androidtmdbwrapper.model.mediadetails.MediaBasic;

import java.util.ArrayList;
import java.util.WeakHashMap;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.ui.discover.DiscoverActivity;
import task.application.com.moviefinder.ui.utility.SplashDataHelper;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setPresenter(new SplashPresenter(this));
        presenter.getInitialData("en", 1, null);
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setData(WeakHashMap<DiscoverActivity.QueryType, ArrayList<? extends MediaBasic>> data, WeakHashMap<DiscoverActivity.QueryType, int[]> pages) {
        SplashDataHelper.setData(data);
        SplashDataHelper.setDataPages(pages);
    }

    @Override
    public void stopSplash() {
        final Intent intent = new Intent(SplashActivity.this, DiscoverActivity.class);
        startActivity(intent);
        finish();
//        new Handler().postDelayed(() -> {
//
//        }, 500);
    }
}
