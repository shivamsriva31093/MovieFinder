package task.application.com.moviefinder.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.Result;

/**
 * Created by shashank on 1/25/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Result> data = new ArrayList<>();
    private OnItemClickListener onItemClickHandler;

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;


      public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView icon;
        private TextView year;
        private TextView type;
        private TextView header;
        private CircleImageView imageView;


        private int Holder_Id;

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();


            onItemClickHandler.onItemClick(view,adapterPosition);

        }


        public ViewHolder(View v, int viewType) {
            super(v);

            if (viewType == TYPE_ITEM) {
                title = (TextView)v.findViewById(R.id.title);
                type = (TextView)v.findViewById(R.id.type);
                year = (TextView)v.findViewById(R.id.year);
                imageView = (CircleImageView) v.findViewById(R.id.item_image);

                Holder_Id = 1;


            } else {
                header = (TextView)v.findViewById(R.id.header);

                Holder_Id = 0;
            }

            v.setOnClickListener(this);
            v.setClickable(true);


        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View v,int position);
    }



    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view

        // set the view's size, margins, paddings and layout parameters

        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler, parent, false);
            ViewHolder vh = new ViewHolder(v, TYPE_ITEM);
            return vh;
        }
        if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
            ViewHolder vh = new ViewHolder(v, TYPE_HEADER);
            return vh;
        }

        return null;
    }

    public RecyclerAdapter(Context context,ArrayList<Result> dataset, OnItemClickListener clickHandler)  {

        this.context = context;
        data = dataset;
        onItemClickHandler = clickHandler;



    }
    public void onBindViewHolder(ViewHolder v, int position) {
        if (v.Holder_Id == 1) {
            v.year.setText(data.get(position -1 ).getYear());
            v.type.setText(data.get(position -1).getType());
            v.title.setText(data.get(position- 1).getTitle());
            Picasso.with(context).load(data.get(position - 1).getImageUrl()).into(v.imageView);

        } else {

            // Similarly we set the resources for header view
            //v.icon.setImageResource(imagedata);
            v.header.setText(data.size()+" results returned");

        }

        // v.menuName.setText(data.get(position));
        //v.icon.setImageResource(imagedata.get(position));


    }


    public int getItemCount() {
        return data.size()+1;

    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }



}

