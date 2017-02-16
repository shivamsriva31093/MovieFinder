package com.androidtmdbwrapper.model.core;

import android.os.Parcel;
import android.os.Parcelable;

import com.androidtmdbwrapper.model.movies.BasicMovieInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sHIVAM on 2/14/2017.
 */

public class SearchResult implements Parcelable, Iterable<BasicMovieInfo> {
    @JsonProperty("page")
    private int page;
    @JsonProperty("results")
    private List<BasicMovieInfo> results = Collections.EMPTY_LIST;
    @JsonProperty("total_results")
    private int totalResults;
    @JsonProperty("total_pages")
    private int totalPages;

    public SearchResult() {
    }

    protected SearchResult(Parcel in) {
        page = in.readInt();
        totalResults = in.readInt();
        totalPages = in.readInt();
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel in) {
            return new SearchResult(in);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<BasicMovieInfo> getResults() {
        return results;
    }

    public void setResults(List<BasicMovieInfo> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
    }

    @Override
    public Iterator<BasicMovieInfo> iterator() {
        return results.iterator();
    }
}
