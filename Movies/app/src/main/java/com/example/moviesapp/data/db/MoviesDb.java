package com.example.moviesapp.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.moviesapp.Model.MoviesModel;
// Movies Db
@Database(entities = {MoviesModel.class}, version = 1, exportSchema = false)
public abstract class MoviesDb extends RoomDatabase {
    public static final String DATABASE_NAME = "moviesDb";
    public abstract Dao moviesDao();

}
