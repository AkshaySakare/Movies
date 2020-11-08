package com.example.moviesapp.data.db;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.moviesapp.Model.MoviesModel;

import java.util.List;
// Add Data in to Db Query
@androidx.room.Dao
public interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addMoviesList(List<MoviesModel> moviesList);

    //get cashed movies
    @Query("SELECT * FROM movies")
    List<MoviesModel> getCashedMoviesList();

    // we remove the old cached movies
    @Query("DELETE FROM movies")
    void deleteCachedMovies();


}
