package task.application.com.moviefinder.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.MakeConnection;
import task.application.com.moviefinder.model.Result;
import task.application.com.moviefinder.model.SearchListData;
import task.application.com.moviefinder.model.URLify;
import task.application.com.moviefinder.util.RecyclerAdapter;

public class SearchListActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private SearchListData searchListData;
    private ArrayList<Result> resultArray;
    private JSONObject json;
    private String jsonObject;
    private CharSequence searchQuery;
    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private ImageButton searchAction;
    private NewtonCradleLoading progressView;
    private RelativeLayout recViewParent;
    private static final String IMDBID = "imdbId";
    private static final String DISCONNECT = "No Internet Connection";
    private static final String SEARCHQUERY = "searchQuery";
    private static final String BUNDLE = "bundle";
    private static final String QUERY = "query";
    private static final String QUERYTYPE = "queryType";
    private static final String SEARCH = "Search";
    private static final String JSON = "jsonObject";
    private static final String EMPTYQUERY = "emptyQuery";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SEARCHQUERY)) {
                searchQuery = savedInstanceState.getCharSequence(SEARCHQUERY);
            }
        } else {
            if (getIntent().hasExtra(BUNDLE)) {
                searchQuery = getIntent().getBundleExtra(BUNDLE).getCharSequence(QUERY);
            }
        }
        if (!isNetworkConnected()) {
            createAlertForNoInternet();
        } else {
            setUpSearchBox();
            Intent intent = getIntent();
            jsonObject = intent.getStringExtra(JSON);

            try {
                json = new JSONObject(jsonObject);
                searchListData = new SearchListData();
                resultArray = searchListData.populateResult(json.optJSONArray("Search"));
                setRecyclerView(resultArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void setUpSearchBox() {
        searchBox = (TextInputLayout) findViewById(R.id.searchbox);
        searchTerm = (EditText) findViewById(R.id.searchterm);
        searchTerm.setText(searchQuery);
        moreOptions = (Spinner) findViewById(R.id.optionsMenu);
        searchAction = (ImageButton) findViewById(R.id.search_action);
        progressView = (NewtonCradleLoading) findViewById(R.id.progressView);
        searchTerm.setFocusable(false);
        InputMethodManager inputMethodManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchTerm.getWindowToken(), 0);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchbox_extra_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreOptions.setAdapter(adapter);
        moreOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTerm.setHint(SEARCH+" " + parent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setSelection(0);
            }
        });
        searchTerm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchTerm.setFocusableInTouchMode(true);
                searchTerm.requestFocus();
                return false;
            }
        });

        searchTerm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (!isNetworkConnected()) {
                        createAlertForNoInternet();
                    } else {
                        attemptToSearch();
                    }

                }
                return false;
            }
        });

        searchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (!isNetworkConnected()) {
                        createAlertForNoInternet();
                    } else {
                        attemptToSearch();
                    }


            }
        });


    }

    private void attemptToSearch() {
        searchTerm.setError(null);
        final String query = searchTerm.getText().toString();
        final String queryType = moreOptions.getSelectedItem().toString();
        Log.d("querytype", queryType);
        View focusView = null;
        boolean cancel = false;

        if (TextUtils.isEmpty(query)) {
            searchTerm.setError(EMPTYQUERY);
            focusView = searchTerm;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            String paramquery = query.replaceAll(" ", "%20");
            URLify urLify = new URLify(paramquery, "s");
            String url = urLify.addParameter(queryType);
            showProgress(true);
            new MakeConnection(url) {
                @Override
                protected void onPostExecute(JSONObject object) {
                    JSONObject jsonObject = object;
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(QUERY, query);
                    bundle.putCharSequence(QUERYTYPE, queryType);
                    if (jsonObject==null && jsonObject.optString("Response").equals("False")) {
                        Toast.makeText(getApplicationContext(), "No search results for " + query
                                , Toast.LENGTH_SHORT).show();
                        showProgress(false);

                    } else {

                        Intent intent = new Intent(getBaseContext(), SearchListActivity.class);
                        intent.putExtra(JSON, jsonObject + "");
                        intent.putExtra(BUNDLE, bundle);
                        showProgress(false);
                        startActivity(intent);
                    }


                }
            }.execute();


            Toast.makeText(this, "Searching for " + query, Toast.LENGTH_SHORT).show();
        }

    }


    public void setRecyclerView(ArrayList<Result> arrayList) {
        recViewParent = (RelativeLayout) findViewById(R.id.activity_search_list);
        recyclerView = (RecyclerView) recViewParent.findViewById(R.id.recView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setRecyclerListener(arrayList);

        recyclerView.setAdapter(recyclerAdapter);

    }

    public void setRecyclerListener(final ArrayList<Result> arrayList) {
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), arrayList,
                new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {




                        if (!isNetworkConnected()) {
                            createAlertForNoInternet();

                        } else {
                                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                                    intent.putExtra(IMDBID, arrayList.get(position - 1).getImdbId());
                                    startActivity(intent);

                        }





                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence("searchQuery", searchQuery);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.setFocusable(false);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void createAlertForNoInternet() {

        Toast.makeText(this, DISCONNECT, Toast.LENGTH_SHORT).show();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void showProgress(boolean show) {

        if(show) {
            recyclerView.setVisibility(View.GONE);
            recViewParent.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            progressView.setVisibility(View.VISIBLE);
            progressView.start();
        } else {
            progressView.setVisibility(View.GONE);
            progressView.stop();
            recViewParent.setBackgroundColor(getResources().getColor(android.R.color.background_light));
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
