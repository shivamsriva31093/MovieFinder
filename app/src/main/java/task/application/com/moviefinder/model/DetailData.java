package task.application.com.moviefinder.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shashank on 1/26/2017.
 */

public class DetailData {


    int length;
    private Result result ;


    public Result detailResult(JSONObject jsonObject){
        result = new Result();
        GetJsonData getJsonData = new GetJsonData(jsonObject);
        result.setTitle(getJsonData.getTitle());
        result.setPlot(getJsonData.getPlot());
        result.setImdRating(getJsonData.getImdbRating());
        result.setRtRating(getJsonData.getTomatoRating());
        result.setGenre(getJsonData.getGenre());
        result.setImageUrl(getJsonData.getImageUrl());

        return result;

    }


}
