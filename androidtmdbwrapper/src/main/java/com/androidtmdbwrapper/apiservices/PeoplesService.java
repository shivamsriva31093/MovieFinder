package com.androidtmdbwrapper.apiservices;

import com.androidtmdbwrapper.model.people.PeopleDetails;
import com.androidtmdbwrapper.model.people.PeopleMovieCredits;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shivam.srivastava on 12/30/2017.
 */

public interface PeoplesService {
    @GET("person/{person_id}")
    Observable<PeopleDetails> getPeople(
            @Path("person_id") Integer personId,
            @Query("language") String ISO639_1_language
    );

    @GET("person/{person_id}/movie_credits")
    Observable<PeopleMovieCredits> getMovieCredits(
            @Path("person_id") Integer personId,
            @Query("language") String ISO639_1_language
    );
}
