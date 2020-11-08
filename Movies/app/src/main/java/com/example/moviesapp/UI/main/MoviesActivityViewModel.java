package com.example.moviesapp.UI.main;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.moviesapp.Model.MoviesModel;
import com.example.moviesapp.data.db.MoviesDb;
import com.example.moviesapp.data.network.ApiClient;
import com.example.moviesapp.data.network.MoviesResponseBody;
import com.example.moviesapp.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivityViewModel extends AndroidViewModel {
    //list for main movies home
    MutableLiveData<List<MoviesModel>> mTopRatedMoviesList = new MutableLiveData<>();

    //first call list
    MutableLiveData<List<MoviesModel>> mMoviesList = new MutableLiveData<>();

    //list for cached movies
    MutableLiveData<List<MoviesModel>> mMoviesCacheList = new MutableLiveData<>();
    // list for load more movies when scrolling scrolling
    MutableLiveData<List<MoviesModel>> mLoadmore = new MutableLiveData<>();
    // live data to observe any error
    MutableLiveData<Throwable> error = new MutableLiveData<>();



    boolean check = false;


    public MoviesActivityViewModel(@NonNull Application application) {
        super(application);
    }

    // build the data base
    MoviesDb moviesDB = Room.databaseBuilder(getApplication(), MoviesDb.class, MoviesDb.DATABASE_NAME).allowMainThreadQueries().build();







    public List<MoviesModel> getCacheMoviesList() {
        return moviesDB.moviesDao().getCashedMoviesList();

    }

    private void deleteCatchMoviesList() {

        moviesDB.moviesDao().deleteCachedMovies();
    }

    private void catchMoviesList(List<MoviesModel> catchedlist) {
        moviesDB.moviesDao().addMoviesList(catchedlist);
    }




    //call for getting  Top rated movies List
    public void getTopRatedList(int page) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getTopRated(page);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {
                mTopRatedMoviesList.setValue(response.body().getResults());
                Log.v("Url", call.request().url().toString());

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                error.setValue(t);
            }
        });

    }

    // Call for Top Movies (Home)
    public void getCurrentMoviesList(int page) {
        Call<MoviesResponseBody> popularMovies = ApiClient.getInstance().getCurrentMovies(page);
        popularMovies.enqueue(new Callback<MoviesResponseBody>() {
            @Override
            public void onResponse(Call<MoviesResponseBody> call, Response<MoviesResponseBody> response) {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                if (Utils.isInternetAvailable(getApplication())) {// is net is available than store data in DB or show old data
                    if (!check) {
                        if (response.body().getResults() != null) {
                            deleteCatchMoviesList();

                            catchMoviesList(response.body().getResults());// store in ca
                            check = true;
                            mMoviesList.setValue(response.body().getResults());
                        }
                    }
                    mLoadmore.setValue(response.body().getResults());
                    Log.v("Url", call.request().url().toString());

                } else {
                    mMoviesCacheList.setValue(getCacheMoviesList());
                }

            }

            @Override
            public void onFailure(Call<MoviesResponseBody> call, Throwable t) {
                mMoviesCacheList.setValue(getCacheMoviesList());
                error.setValue(t);
            }
        });

    }



}
