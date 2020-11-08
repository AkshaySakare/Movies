package com.example.moviesapp.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponseBody> getTopRated(@Query("api_key") String key,@Query("page") int page);

    //get top rated movies
// currently play date
    @GET("movie/now_playing")
    Call<MoviesResponseBody> getCurrentMovies(@Query("api_key") String key, @Query("page") int page);


    //here we call for movie cast
    @GET("movie/{movie_id}/credits")
    Call<CastsResponseBody> getCast(@Path(value = "movie_id", encoded = true) Long movieId);



}
