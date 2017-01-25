package task.application.com.moviefinder.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.GetJsonData;
import task.application.com.moviefinder.model.MakeConnection;
import task.application.com.moviefinder.model.Movie;
import task.application.com.moviefinder.model.SearchListData;

public class SearchListActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        linearLayoutManager = new LinearLayoutManager(this);

        new MakeConnection("http://www.omdbapi.com/?s=godfather&plot=full&r=json"){
            @Override
            protected void onPostExecute(JSONObject distanceMatrixJson) {
            JSONObject jsonObject = distanceMatrixJson;
                SearchListData searchListData = new SearchListData();

                Log.d("jjhb", searchListData.populateResult(jsonObject.optJSONArray("Search")).get(0).getTitle()+"");

                RecyclerAdapter recyclerViewAdapter = new RecyclerAdapter(searchListData.populateResult(jsonObject.optJSONArray("Search")));
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);

            }}.execute();


    }
}
