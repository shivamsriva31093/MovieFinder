package task.application.com.moviefinder.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shashank on 1/25/2017.
 */

public class SearchListData {


    int length;



    ArrayList<Result> results = new ArrayList<>();

    public ArrayList<Result> populateResult(JSONArray jsonArray){
        length = jsonArray.length();
        for(int i=0; i<length; i++){
            Result result = new Result();
            String type = jsonArray.optJSONObject(i).optString("Type");
            String title = jsonArray.optJSONObject(i).optString("Title");
            String year = jsonArray.optJSONObject(i).optString("Year");

            result.setTitle(title);
            result.setType(type);
            result.setYear(year);
            results.add(i,result);


        }
        return results;
    }
}
