package task.application.com.moviefinder.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.GetJsonData;
import task.application.com.moviefinder.model.MakeConnection;
import task.application.com.moviefinder.model.Movie;
import task.application.com.moviefinder.model.Result;
import task.application.com.moviefinder.model.SearchListData;

public class SearchListActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private SearchListData searchListData;
    private ArrayList<Result> resultArray;
    private JSONObject json;
    private String jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        Intent intent = getIntent();
        jsonObject = intent.getStringExtra("jsonObject");

        try {
            json= new JSONObject(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        searchListData = new SearchListData();
        resultArray = searchListData.populateResult(json.optJSONArray("Search"));
        setRecyclerView(resultArray);



    }

    public void setRecyclerView(ArrayList<Result> arrayList){
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerListener(arrayList);

        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerListener(final ArrayList<Result> arrayList){
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), arrayList,
                new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v,int position) {


                        Intent intent = new Intent(getBaseContext(),DetailActivity.class);
                        intent.putExtra("imdbId",arrayList.get(position-1).getImdbId());
                        startActivity(intent);



                    }
                });
    }


    public void downloadJson(){

        new MakeConnection("http://www.omdbapi.com/?s=godfather&plot=full&r=json"){
            @Override
            protected void onPostExecute(JSONObject object) {
                JSONObject jsonObject = object;
                searchListData = new SearchListData();
                resultArray = searchListData.populateResult(jsonObject.optJSONArray("Search"));
                setRecyclerView(resultArray);

            }}.execute();

    }
}
