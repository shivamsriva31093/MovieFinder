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
    ArrayList<String> listId = new ArrayList<>();

    public ArrayList<Result> populateResult(JSONArray jsonArray){
        length = jsonArray.length();
        for(int i=0; i<length; i++){
            Result result = new Result();
            setData(result,jsonArray,i);
            results.add(i,result);


        }
        return results;
    }
    public void setData(Result result,JSONArray jsonArray,int position){
        String imdbid =  jsonArray.optJSONObject(position).optString("imdbID");
        String type = jsonArray.optJSONObject(position).optString("Type");
        String title = jsonArray.optJSONObject(position).optString("Title");
        String year = jsonArray.optJSONObject(position).optString("Year");
        String imageUrl = jsonArray.optJSONObject(position).optString("Poster");


        result.setTitle(title);
        result.setType(type);
        result.setYear(year);
        result.setImdbId(imdbid);
        result.setImageUrl(imageUrl);

    }

    public ArrayList<String> resultDetail(JSONArray jsonArray){
        length = jsonArray.length();
        for(int i=0; i<length; i++){
            Result result = new Result();
            String imdbid =  jsonArray.optJSONObject(i).optString("imdbID");

            URLify urLify = new URLify(imdbid,"i");
            String url = urLify.addParameter();

            listId.add(i,url);


        }
        return listId;
    }

}
