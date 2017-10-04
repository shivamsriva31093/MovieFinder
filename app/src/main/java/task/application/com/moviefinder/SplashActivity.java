package task.application.com.moviefinder;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import task.application.com.moviefinder.ui.discover.DiscoverActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, DiscoverActivity.class));
            finish();
        }, 2000);
    }
}
