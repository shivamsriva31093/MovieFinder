package task.application.com.moviefinder.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.MakeConnection;
import task.application.com.moviefinder.model.Result;
import task.application.com.moviefinder.model.SearchListData;
import task.application.com.moviefinder.model.URLify;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getName();
    private ArrayList<Result> resultArray ;
    private TextInputLayout searchBox;
    private EditText searchTerm;
    private Spinner moreOptions;
    private ImageButton searchAction;
    private SearchListData searchListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpToolbar();
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
                searchTerm.setHint("Search "+parent.getSelectedItem().toString());
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
        String query = searchTerm.getText().toString();
        String queryType = moreOptions.getSelectedItem().toString();
        View focusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(query)) {
            searchTerm.setError("Empty Query");
            focusView = searchTerm;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            URLify urLify = new URLify(query,"s");
            String url = urLify.addParameter();
            new MakeConnection(url){
                @Override
                protected void onPostExecute(JSONObject object) {
                    JSONObject jsonObject = object;

                    Intent intent = new Intent(getBaseContext(),SearchListActivity.class);
                    intent.putExtra("jsonObject",jsonObject+"");
                    startActivity(intent);

                }}.execute();


            Toast.makeText(this, "Searching for "+query, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if(view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int)ev.getRawX(), (int)ev.getRawY())) {
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
}
