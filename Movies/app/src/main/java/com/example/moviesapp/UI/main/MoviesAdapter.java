package com.example.moviesapp.UI.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviesapp.Model.MoviesModel;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ActivityMainBinding;

import com.example.moviesapp.databinding.ListItemMoviesBinding;
import com.example.moviesapp.utils.Constants;


import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Context mContext;
    private OnItemClicked onItemClicked;
    MoviesActivityViewModel mViewModel;
    ActivityMainBinding mActivityMainBinding;

    // constructor to get adapter needs from main activity
    public MoviesAdapter(Context mContext, MoviesActivityViewModel mainActivityViewModel,
                         ActivityMainBinding mActivityMainBinding, OnItemClicked onItemClicked) {
        this.mContext = mContext;
        this.onItemClicked = onItemClicked;
        mViewModel = mainActivityViewModel;
        this.mActivityMainBinding = mActivityMainBinding;
    }

    // movies list for recycler view
    List<MoviesModel> mMoviesList = new ArrayList<>();


    @NonNull
    @Override
    //inflate the view form data binding
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemMoviesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_movies, parent, false);
        return new ViewHolder(binding);

    }


    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder holder, int position) {
        MoviesModel moviesModel = mMoviesList.get(position);
        holder.binding.tvAvgRate.setText(moviesModel.getVoteAverage() + "");



        // if no title get original title
        if (!moviesModel.getTitle().isEmpty()) {
            holder.binding.tvTitle.setText(moviesModel.getTitle());

        } else {

            holder.binding.tvTitle.setText(moviesModel.getOriginalTitle());
        }

        // if no image get the poster
        if (!TextUtils.isEmpty(moviesModel.getBackdropPath())) {
            glideloader(moviesModel.getBackdropPath(), holder);

        } else {

            glideloader(moviesModel.getPosterPath(), holder);
        }
// Null protection
        try {
            holder.binding.tvReleaseDate.setText(releaseYear(moviesModel.getReleaseDate()));
        } catch (Exception e) {
            holder.binding.tvReleaseDate.setText("Unknown");

        }
    }

    // method to get only year from the date
    private String releaseYear(String date) {
        return date.substring(0, 4);
    }

    void glideloader(String original, ViewHolder holder) {
        // to apply rounded image
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(mContext)
                .applyDefaultRequestOptions(requestOptions)
                .load(Constants.IMAGE_BASE_URL + original)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.binding.pbMovies.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.binding.pbMovies.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.binding.ivPhoto);

    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ListItemMoviesBinding binding;

        public ViewHolder(@NonNull ListItemMoviesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;



            //on item click
            itemView.setOnClickListener(v -> onItemClicked.onListItemCLicked(mMoviesList.get(getAdapterPosition())));
        }

    }

    // add movies to the adapter
    public void addMoviesList(List<MoviesModel> moviesList) {
        this.mMoviesList = moviesList;
        notifyDataSetChanged();
    }



    // load more to the exist list
    public void loadMore(List<MoviesModel> moviesList) {
        this.mMoviesList.addAll(moviesList);
        notifyDataSetChanged();
    }

    //clear the adapter
    public void clear() {
        this.mMoviesList.clear();
        notifyDataSetChanged();

    }


}
