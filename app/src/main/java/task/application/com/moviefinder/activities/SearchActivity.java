package task.application.com.moviefinder.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.victor.loading.rotate.RotateLoading;

import org.json.JSONObject;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.api_model.MakeConnection;
import task.application.com.moviefinder.api_model.Result;
import task.application.com.moviefinder.api_model.SearchListData;
import task.application.com.moviefinder.api_model.URLify;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();
    private ArrayList<Result> resultArray;
    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private ImageButton searchAction;
    private LinearLayout logoContainer;
    private ImageView logo;
    private RotateLoading progressView;
    private SearchListData searchListData;
    private static final String EMPTYQUERY = "Empty Query";
    private static final String SEARCH = "Searching for ";
    private static final String SPACE = " ";
    private static final String URLSPACE = "%20";
    private static final String SEARCHQUERY = "searchQuery";
    private static final String BUNDLE = "bundle";
    private static final String QUERY = "query";
    private static final String QUERYTYPE = "queryType";
    private static final String JSON = "jsonObject";
    private static final String RESPONSE = "Response";
    private static final String EMPTYSEARCH = "No Search Results for ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpToolbar();
        logo = (ImageView)findViewById(R.id.logo);
        logoContainer = (LinearLayout)findViewById(R.id.logo_container);
        progressView = (RotateLoading) findViewById(R.id.progressView);
        setUpSearchBox();

    }

    private void setUpToolbar() {
    }

    private void setUpSearchBox() {
        searchBox = (TextInputLayout) findViewById(R.id.searchbox);
        searchTerm = (EditText) findViewById(R.id.searchterm);
        moreOptions = (Spinner) findViewById(R.id.optionsMenu);
        searchAction = (ImageButton) findViewById(R.id.search_action);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.searchbox_extra_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moreOptions.setAdapter(adapter);
        moreOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchTerm.setHint("Search " + parent.getSelectedItem().toString());
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
                    attemptToSearch();
                }
                return false;
            }
        });

        searchAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToSearch();
            }
        });


    }

    private void attemptToSearch() {
        searchTerm.setError(null);
        final String query = searchTerm.getText().toString();
        final String queryType = moreOptions.getSelectedItem().toString();
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
            if (!isNetworkConnected()) {
                createAlertForNoInternet();
            } else {
                String paramquery = query.replaceAll(SPACE, URLSPACE);
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
                        if (jsonObject== null || jsonObject.optString(RESPONSE).equals("False")) {
                            Toast.makeText(getApplicationContext(), EMPTYSEARCH + query
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
                Toast.makeText(this, SEARCH + query, Toast.LENGTH_SHORT).show();

            }

        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createAlertForNoInternet() {

        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void showProgress(boolean show) {

        if(show) {
            logo.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
            progressView.start();
        } else {
            progressView.setVisibility(View.GONE);
            progressView.stop();
            logo.setVisibility(View.VISIBLE);
        }
    }
}
