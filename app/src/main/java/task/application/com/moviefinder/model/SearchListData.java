package task.application.com.moviefinder.model;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by shashank on 1/25/2017.
 */

public class SearchListData {


    int length;
    ArrayList<Result> results = new ArrayList<>();


    private static final String YEARQUERY = "Year";
    private static final String IMDBIDQUERY = "imdbID";
    private static final String TYPEQUERY = "Type";
    private static final String IMAGEQUERY = "Poster";
    private static final String TITLEQUERY = "Title";


    public ArrayList<Result> populateResult(JSONArray jsonArray) {
        length = jsonArray.length();
        for (int i = 0; i < length; i++) {
            Result result = new Result();
            setData(result, jsonArray, i);
            results.add(i, result);


        }
        return results;
    }

    public void setData(Result result, JSONArray jsonArray, int position) {
        String imdbid = jsonArray.optJSONObject(position).optString(IMDBIDQUERY);
        String type = jsonArray.optJSONObject(position).optString(TYPEQUERY);
        String title = jsonArray.optJSONObject(position).optString(TITLEQUERY);
        String year = jsonArray.optJSONObject(position).optString(YEARQUERY);
        String imageUrl = jsonArray.optJSONObject(position).optString(IMAGEQUERY);


        result.setTitle(title);
        result.setType(type);
        result.setYear(year);
        result.setImdbId(imdbid);
        result.setImageUrl(imageUrl);

    }


}
