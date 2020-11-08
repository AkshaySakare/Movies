
package com.example.moviesapp.data.network;

import com.example.moviesapp.Model.MoviesModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponseBody {
    @SerializedName("results")
    private List<MoviesModel> mResults;

    public List<MoviesModel> getResults() {
        return mResults;
    }

    public void setResults(List<MoviesModel> results) {
        mResults = results;
    }


}
