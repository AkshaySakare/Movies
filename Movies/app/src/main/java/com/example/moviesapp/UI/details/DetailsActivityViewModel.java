package com.example.moviesapp.UI.details;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.Model.CastModel;
import com.example.moviesapp.data.network.ApiClient;
import com.example.moviesapp.data.network.CastsResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivityViewModel extends ViewModel {
    // list for cast
    MutableLiveData<List<CastModel>> CastList = new MutableLiveData<>();

    // live data for error
    MutableLiveData<Throwable> error = new MutableLiveData<>();



    // Call for cast list
    public void getCastlist(Long MovieId) {
        Call<CastsResponseBody> cast = ApiClient.getInstance().getCast(MovieId);
        cast.enqueue(new Callback<CastsResponseBody>() {
            @Override
            public void onResponse(Call<CastsResponseBody> call, Response<CastsResponseBody> response) {
                CastList.postValue(response.body().getCast());
                Log.v("Url=", call.request().url().toString());
            }

            @Override
            public void onFailure(Call<CastsResponseBody> call, Throwable t) {


            }
        });


    }







}
