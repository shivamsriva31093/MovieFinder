package task.application.com.moviefinder.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.DetailData;
import task.application.com.moviefinder.model.MakeConnection;
import task.application.com.moviefinder.model.SearchListData;
import task.application.com.moviefinder.model.URLify;

import static android.R.attr.data;

public class DetailActivity extends AppCompatActivity {

    private String imdbId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        imdbId= intent.getStringExtra("imdbId");

        Log.d("id",imdbId);
        downloadJson();

    }

    public void downloadJson(){
        URLify urLify = new URLify(imdbId,"i");
        String url = urLify.addParameter();
        new MakeConnection(url){
            @Override
            protected void onPostExecute(JSONObject object) {
                JSONObject jsonObject = object;
                TextView textView = (TextView)findViewById(R.id.genre);
                ImageView imageView = (ImageView)findViewById(R.id.detailimage);
                DetailData detailData = new DetailData();
                detailData.detailResult(jsonObject);
                Picasso.with(getApplicationContext()).load(detailData.detailResult(jsonObject).getImageUrl()).into(imageView);

                textView.setText( detailData.detailResult(jsonObject).getGenre());
            }}.execute();

    }
}
