package com.example.moviesapp.data.network;

import com.example.moviesapp.utils.Constants;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
   private static ApiClient instance = null;
    private ApiInterface apiInterface;

    //singleton to take only one instance
    public static ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;

    }


    // here we build our call
    private ApiClient() {
        //for debug the response
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //to add api key to every request
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    HttpUrl url = request.url()
                            .newBuilder()
                            .addQueryParameter(Constants.API_KEY_NAME, Constants.API_KEY_VALUE)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);

                })
                .addInterceptor(logging).build();


        // here we build the call
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);


    }


    public Call<MoviesResponseBody> getCurrentMovies(int page) {
        return apiInterface.getCurrentMovies(Constants.API_KEY_VALUE,page);

    }



    public Call<CastsResponseBody> getCast(Long movieId) {
        return apiInterface.getCast(movieId);

    }


    //get calories require to parameters we attach it into one hashmap
    public Call<MoviesResponseBody> getTopRated(int page) {


        return apiInterface.getTopRated(Constants.API_KEY_VALUE,page);


    }





}
