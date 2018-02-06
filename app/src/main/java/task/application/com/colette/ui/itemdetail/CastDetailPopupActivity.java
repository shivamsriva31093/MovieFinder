package task.application.com.colette.ui.itemdetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.androidtmdbwrapper.enums.MediaType;
import com.androidtmdbwrapper.model.core.BaseMediaData;
import com.androidtmdbwrapper.model.credits.MediaCredit;
import com.androidtmdbwrapper.model.people.Cast;
import com.androidtmdbwrapper.model.people.PeopleDetails;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import task.application.com.colette.ApplicationClass;
import task.application.com.colette.R;
import task.application.com.colette.ui.utility.widgets.CollapsibleTextView;
import task.application.com.colette.ui.utility.widgets.GeneralTextView;
import task.application.com.colette.util.TmdbApi;

/**
 * Created by shivam.srivastava on 12/27/2017.
 */

public class CastDetailPopupActivity extends Activity {

    private static final String TAG = CastDetailPopupActivity.class.getSimpleName();

    private GeneralTextView castName;
    private CircleImageView castImage;
    private CollapsibleTextView description;
    private RecyclerView popularMovies;
    private MediaCredit creditData;
    private PeopleDetails peopleDetails;
    private CompositeDisposable disposables = new CompositeDisposable();
    private RVAdapter rvAdapter;
    private ScrollView mainContent;
    private AVLoadingIndicatorView progressItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpWindow();
        setContentView(R.layout.activity_castdetail);
        initUiComponents();
    }

    private void showUi(List<Cast> cast) {
        if (peopleDetails != null) {
            castName.setText(peopleDetails.getName());
            Picasso.with(this)
                    .load("https://image.tmdb.org/t/p/w500" + creditData.getProfilePath())
                    .placeholder(R.drawable.credit1)
                    .error(R.drawable.credit1)
                    .into(castImage);
            description.setText(peopleDetails.getBiography());
            if (cast != null && !cast.isEmpty()) {
                rvAdapter.updateData(cast);
            }
        }
    }

    private void initUiComponents() {
        mainContent = findViewById(R.id.main_content);
        castName = mainContent.findViewById(R.id.cast_name);
        castImage = mainContent.findViewById(R.id.circleImageView);
        description = mainContent.findViewById(R.id.desc);
        popularMovies = mainContent.findViewById(R.id.recView);
        progressItem = findViewById(R.id.progress);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        popularMovies.setItemViewCacheSize(50);
        popularMovies.setDrawingCacheEnabled(true);
        popularMovies.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        popularMovies.setLayoutManager(layoutManager);
        rvAdapter = new RVAdapter(new ArrayList<Cast>());
        popularMovies.setAdapter(rvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("castData") && creditData == null && peopleDetails == null) {
            creditData = (MediaCredit) getIntent().getParcelableExtra("castData");
            showLoading(true);
            Observable<PeopleDetails> people = TmdbApi.getApiClient(ApplicationClass.API_KEY)
                    .peoplesService().getPeople(creditData.getId(), "en-US")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());
            Disposable subscribe = people.subscribe(peopleDetails1 -> {
                peopleDetails = peopleDetails1;
                TmdbApi.getApiClient(ApplicationClass.API_KEY)
                        .peoplesService()
                        .getMovieCredits(peopleDetails1.getId(), "en-US")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(peopleMovieCredits -> {
                            showUi(peopleMovieCredits.getCast());
                            showLoading(false);
                        });
            }, throwable -> {
                throwable.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            });
            disposables.add(subscribe);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    private void showLoading(boolean status) {
        if (status) {
            mainContent.setVisibility(View.GONE);
            progressItem.setVisibility(View.VISIBLE);
        } else {
            progressItem.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);
        }
    }

    private void setUpWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND
        );

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.35f;
        getWindow().setAttributes(params);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if (height > width) {
            getWindow().setLayout((int) (width * .9), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .7), (int) (height * .9));
        }
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<Cast> credits;

        public RVAdapter(List<Cast> credits) {
            this.credits = credits;
        }

        @Override
        public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(CastDetailPopupActivity.this).inflate(R.layout.fragment_image_slider, parent, false);
            return new RVAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RVAdapter.ViewHolder holder, int position) {
            Picasso picasso = Picasso.with(CastDetailPopupActivity.this);
            TextDrawable drawable1 = TextDrawable.builder()
                    .buildRoundRect("A", Color.RED, 30);

            picasso.load("https://image.tmdb.org/t/p/w500"
                    + credits.get(position).getPosterPath())
                    .placeholder(R.drawable.credit1)
                    .error(R.drawable.credit1)
                    .into(holder.creditImage);

            holder.creditTitle.setText(credits.get(position).getTitle());

        }

        @Override
        public int getItemCount() {
            return credits.size();
        }

        public void updateData(List<Cast> list) {
            this.credits = list;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView creditImage;
            private TextView creditTitle;
            private TextView creditDesignation;

            public ViewHolder(View itemView) {
                super(itemView);
                CardView parentCard = itemView.findViewById(R.id.parent_cardview);
                CardView childCard = parentCard.findViewById(R.id.child_cardview);
                ConstraintLayout constraintLayout = childCard.findViewById(R.id.parent_cl);

                childCard.setElevation(0);
                parentCard.setCardBackgroundColor(ContextCompat.getColor(CastDetailPopupActivity.this, R.color.transparent));
                childCard.setCardBackgroundColor(ContextCompat.getColor(CastDetailPopupActivity.this, R.color.transparent));
                constraintLayout.setBackgroundColor(ContextCompat.getColor(CastDetailPopupActivity.this, R.color.transparent));

                creditImage = (CircleImageView) constraintLayout.findViewById(R.id.credit_image);
                creditTitle = (TextView) constraintLayout.findViewById(R.id.credit_title);
                creditDesignation = (TextView) constraintLayout.findViewById(R.id.credit_design);

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) creditTitle.getLayoutParams();
                layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                creditTitle.setLayoutParams(layoutParams);

                itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(CastDetailPopupActivity.this, SearchItemDetailActivity.class);
                    Bundle bundle = new Bundle();
                    BaseMediaData clickedItem = new BaseMediaData();
                    clickedItem.setId(credits.get(getAdapterPosition()).getId());
                    bundle.putParcelable("clickedItem", clickedItem);
                    bundle.putSerializable("filtering_type", MediaType.MOVIES);
                    intent.putExtra("searchItem", bundle);
                    startActivity(intent);
                });
            }

        }
    }
}
