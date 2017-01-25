package task.application.com.moviefinder.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import task.application.com.moviefinder.R;
import task.application.com.moviefinder.model.Result;

/**
 * Created by shashank on 1/25/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    ArrayList<Result> data = new ArrayList<>();





    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView icon;
        private TextView year;
        private TextView type;

        private int Holder_Id;


        public ViewHolder(View v) {
            super(v);


                title = (TextView)v.findViewById(R.id.title);
                type = (TextView)v.findViewById(R.id.type);
                year = (TextView)v.findViewById(R.id.year);


                Holder_Id = 1;




        }


    }

    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view

        // set the view's size, margins, paddings and layout parameters


            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;



    }

    public RecyclerAdapter(ArrayList<Result> dataset)  {
        data = dataset;


    }
    public void onBindViewHolder(ViewHolder v, int position) {

            v.year.setText(data.get(position ).getYear());
            v.type.setText(data.get(position ).getType());
            v.title.setText(data.get(position).getTitle());




    }

    public int getItemCount() {
        return data.size();

    }


//    @Override
//    public int getItemViewType(int position) {
//        if (isPositionHeader(position))
//            return TYPE_HEADER;
//
//        return TYPE_ITEM;
//    }
//
//    private boolean isPositionHeader(int position) {
//        return position == 0;
//    }



}

