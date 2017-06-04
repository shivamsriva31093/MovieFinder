package task.application.com.moviefinder.ui.utility;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtmdbwrapper.model.credits.MediaCredit;
import com.androidtmdbwrapper.model.credits.MediaCreditCast;
import com.androidtmdbwrapper.model.credits.MediaCreditCrew;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.R;

public class ImageSlider<T extends MediaCredit> extends Fragment {
    private String FRAG_TAG;
    public static final String CAST_FRAG = "castFragment";
    public static final String CREW_FRAG = "crewFragment";
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    public ImageSlider() {
        // Required empty public constructor
    }

    public static <E extends MediaCredit> ImageSlider<E> newInstance() {
        return new ImageSlider<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.FRAG_TAG = getTag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test", "inside frag");
        View rootView = inflater.inflate(R.layout.imageslider, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.credit_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return true;
            }
        };
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setLayoutManager(layoutManager);
        rvAdapter = new RVAdapter(Collections.<T>emptyList());
        recyclerView.setAdapter(rvAdapter);
    }

    public void updateImageSliderView(List<T> list) {
        rvAdapter.updateData(list);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
        private List<T> credits;

        public RVAdapter(List<T> credits) {
            this.credits = credits;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image_slider, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Picasso picasso = Picasso.with(getActivity());
            picasso.setIndicatorsEnabled(true);
            picasso.load("https://image.tmdb.org/t/p/w500"
                    + credits.get(position).getProfilePath())
                    .placeholder(R.drawable.creditplaceholder)
                    .error(R.drawable.creditplaceholder)
                    .into(holder.creditImage);

            holder.creditTitle.setText(credits.get(position).getName());
            if (credits.get(0) instanceof MediaCreditCrew) {
                holder.creditDesignation.setText(((MediaCreditCrew) credits.get(position)).getJob());
            } else {
                holder.creditDesignation.setText(((MediaCreditCast) credits.get(position)).getCharacter());
            }
        }

        @Override
        public int getItemCount() {
            return credits.size();
        }

        public void updateData(List<T> list) {
            this.credits = list;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView creditImage;
            private TextView creditTitle;
            private TextView creditDesignation;

            public ViewHolder(View itemView) {
                super(itemView);
                creditImage = (CircleImageView) itemView.findViewById(R.id.credit_image);
                creditTitle = (TextView) itemView.findViewById(R.id.credit_title);
                creditDesignation = (TextView) itemView.findViewById(R.id.credit_design);
            }
        }
    }

}
