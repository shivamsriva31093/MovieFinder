package task.application.com.moviefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.api_model.DetailData;
import task.application.com.moviefinder.api_model.MakeConnection;
import task.application.com.moviefinder.api_model.Result;
import task.application.com.moviefinder.api_model.URLify;

public class DetailActivity extends AppCompatActivity {

    private String imdbId;
    private TextView genreView;
    private TextView plotView;
    private TextView imdbView;
    private TextView rtView;
    private ImageView imageView;
    private TextView dateView;
    private Toolbar toolbar;
    private TextView votesView;
    private TextView directorView;
    private TextView actorsView;
    private TextView yearView;
    private View detailView;

    private AVLoadingIndicatorView progressView;

    private static final String TYPE_IMDBID = "i";
    private static final String IMDBID = "imdbId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        imdbId = intent.getStringExtra(IMDBID);
        instantiateViews();

        downloadJson();

    }

    public void instantiateViews() {
        genreView = (TextView) findViewById(R.id.genre);
        plotView = (TextView) findViewById(R.id.plot);
        imdbView = (TextView) findViewById(R.id.imdbrating);
        rtView = (TextView) findViewById(R.id.rtrating);
        imageView = (ImageView) findViewById(R.id.detailimage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        dateView = (TextView) findViewById(R.id.releasedate);
        votesView = (TextView) findViewById(R.id.imdbvotes);
        directorView = (TextView) findViewById(R.id.directorview);
        actorsView = (TextView) findViewById(R.id.actorview);
        yearView = (TextView) findViewById(R.id.year);
        detailView = findViewById(R.id.include_detail_view);
        progressView = (AVLoadingIndicatorView) findViewById(R.id.progressView);
    }

    public void downloadJson() {
        URLify urLify = new URLify(imdbId, TYPE_IMDBID);
        String url = urLify.addParameter("All");
        showProgress(true);
        new MakeConnection(url) {
            @Override
            protected void onPostExecute(JSONObject object) {
                JSONObject jsonObject = object;

                DetailData detailData = new DetailData();
                Result result = detailData.detailResult(jsonObject);
                showProgress(false);
                detailData.populateViews(getApplicationContext(), toolbar, actorsView, directorView, yearView, genreView,
                        plotView, imdbView, rtView, dateView, votesView, imageView, result);

            }
        }.execute();

    }

    private void showProgress(boolean show) {

        if(show) {
            detailView.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            progressView.show();
        } else {
            progressView.hide();
            progressView.setVisibility(View.GONE);
            detailView.setVisibility(View.VISIBLE);
        }
    }
}