package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.core.AppendToResponse;
import com.androidtmdbwrapper.model.tv.ExternalIds;
import com.androidtmdbwrapper.model.tv.TvInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sHIVAM on 5/27/2017.
 */

public interface TvService {
    @GET("tv/{tv_id}")
    Observable<TvInfo> summary(
            @Path("tv_id") int tmdbId,
            @Query("append_to_response") AppendToResponse appendToResponse
    );

    @GET("tv/{tv_id}/external_ids")
    Observable<ExternalIds> getExternalIds(
            @Path("tv_id") int tvId
    );
}
