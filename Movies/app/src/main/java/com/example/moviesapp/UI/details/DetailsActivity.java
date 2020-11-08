package com.example.moviesapp.UI.details;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.moviesapp.Model.CastModel;
import com.example.moviesapp.Model.MoviesModel;

import com.example.moviesapp.R;
import com.example.moviesapp.databinding.MovieDetailsBinding;
import com.example.moviesapp.utils.Constants;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailsActivity extends AppCompatActivity {
    MovieDetailsBinding movieDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieDetailsBinding = DataBindingUtil.setContentView(this, R.layout.movie_details);




        DetailsAdapter detailsAdapter = new DetailsAdapter(this);

        // initialize rv and adapter
        movieDetailsBinding.rvCast.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        movieDetailsBinding.rvCast.setAdapter(detailsAdapter);

        MoviesModel moviesObject = getIntent().getParcelableExtra(Constants.MOVIE_INTENT_KEY);
        // here we got the object from the main activity
        if (!moviesObject.getTitle().isEmpty())
            movieDetailsBinding.tvTitle.setText(moviesObject.getTitle());
        else {
            movieDetailsBinding.tvTitle.setText(moviesObject.getOriginalTitle());
        }


        movieDetailsBinding.tvDescription.setText(textHead("Description : " + moviesObject.getOverview(), 13));


        if (!TextUtils.isEmpty(moviesObject.getReleaseDate())) {
            movieDetailsBinding.tvReleaseDate.setText(textHead("Release Date : " + moviesObject.getReleaseDate(), 14));
        } else {
            movieDetailsBinding.tvReleaseDate.setText("Unkown");

        }
        movieDetailsBinding.tvAvgRate.setText(moviesObject.getVoteAverage() + "/10");
        Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + moviesObject.getPosterPath())
                .transform(new MultiTransformation(new BlurTransformation()))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        movieDetailsBinding.clDetailsBackground.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                });

        if (!TextUtils.isEmpty(moviesObject.getBackdropPath())) {
            glideloader(moviesObject.getBackdropPath());

        } else {
            glideloader(moviesObject.getPosterPath());
        }

        // view model instance
        DetailsActivityViewModel detailsActivityViewModel = ViewModelProviders.of(this).get(DetailsActivityViewModel.class);
        // call for the cast
        detailsActivityViewModel.getCastlist(moviesObject.getId());









        // observe any change in cast list
        detailsActivityViewModel.CastList.observe(this, new Observer<List<CastModel>>() {
            @Override
            public void onChanged(List<CastModel> castModels) {
                detailsAdapter.addCastList(castModels);


            }
        });
        //observe internet connection
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                // network
                detailsActivityViewModel.getCastlist(moviesObject.getId());

                Glide.with(getApplicationContext())
                        .load(Constants.IMAGE_BASE_URL + moviesObject.getPosterPath())
                        .transform(new MultiTransformation(new BlurTransformation()))
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                movieDetailsBinding.clDetailsBackground.setBackground(resource);
                            }
                        });


            }

            @Override
            public void onLost(Network network) {
                // network unavailable
            }
        };

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }


    }

    // to style head for descrption  etc
    private Spannable textHead(String text, int end) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(
                new ForegroundColorSpan(getResources().getColor(R.color.pink)), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(
                new RelativeSizeSpan(1.40f), 0, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;


    }

    private void glideloader(String imgurl) {
        // rounded image effect
        RequestOptions castCircleCrop = new RequestOptions();
        castCircleCrop = castCircleCrop.transforms(new CenterCrop(), new RoundedCorners(120));

        Glide.with(this)
                .applyDefaultRequestOptions(castCircleCrop)
                .load(Constants.IMAGE_BASE_URL + imgurl)
                .into(movieDetailsBinding.ivMovieImage);
    }


}
