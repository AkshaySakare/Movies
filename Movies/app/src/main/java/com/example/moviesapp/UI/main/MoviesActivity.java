package com.example.moviesapp.UI.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviesapp.Model.MoviesModel;
import com.example.moviesapp.R;
import com.example.moviesapp.UI.details.DetailsActivity;
import com.example.moviesapp.databinding.ActivityMainBinding;
import com.example.moviesapp.utils.Constants;

import java.util.List;

public class MoviesActivity extends AppCompatActivity {
    MoviesActivityViewModel viewModel;
    MoviesAdapter mainAdapter;
    ActivityMainBinding activityMainBinding;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModelInstance
        initViewModel();

        //dataBinding
        initBinding();

        //recyclerView Manger
        initAdapter();

        //navigation setup
        initNavigation();
        //refresh
        refresh();

        //observe on all lists
        observe();
        //load more when scroll in home  with top movies
        onScroll();

        //  set navigation categories   right data
        navigationOptions();

        //mainFirstCall
        viewModel.getCurrentMoviesList(1);

    }

    //getCatogeryList
    public void catogeryData( String catogeryName) {
        mainAdapter.clear();
        activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);

        viewModel.getTopRatedList( 1);

        activityMainBinding.dlMain.closeDrawer(Gravity.LEFT);
        activityMainBinding.tvHeadName.setText(catogeryName);

    }
    // make sure from user that he wanna close app and if he wasn't home back to home
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MoviesActivity.this);

        if (activityMainBinding.tvHeadName.getText().toString() != "Home") {
            mainAdapter.clear();
            home();
            activityMainBinding.tvHeadName.setText("Home");
        } else {
            builder.setTitle("Please confirm");
            builder.setMessage("Are you want to exit the app?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do something when user want to exit the app
                    // Let allow the system to handle the event, such as exit the app
                    MoviesActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do something when user want to exit the app
                    // Let allow the system to handle the event, such as exit the app
                }
            });


            // Create the alert dialog using alert dialog builder
            AlertDialog dialog = builder.create();

            // Finally, display the dialog when user press back button
            dialog.show();
        }
    }
    // when search closed take user to the right category
    public void returnToRightCatogery(String catogery) {
        switch (catogery) {
            case "Home":
                activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);

                mainAdapter.clear();
                home();
                activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                activityMainBinding.tvHeadName.setText(getResources().getString(R.string.home));
                break;


            case "TopRated":
                catogeryData(getResources().getString(R.string.toprated));
                break;




        }


    }

    void home() {
        for (int i = 1; i <= 9; i++) viewModel.getCurrentMoviesList(i);
    }

    void loadMore() {
        for (int i = 2; i <= 9; i++) viewModel.getCurrentMoviesList(i);
    }

    void navigationOptions() {
        activityMainBinding.nvMain.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.Home:
                    mainAdapter.clear();
                    activityMainBinding.rvPopularmovies.setVisibility(View.VISIBLE);

                    home();
                    activityMainBinding.dlMain.closeDrawer(GravityCompat.START);
                    activityMainBinding.tvHeadName.setText(getResources().getString(R.string.home));
                    break;

                case R.id.toprated:
                    catogeryData(getResources().getString(R.string.toprated));
                    break;



            }
            return true;
        });
        activityMainBinding.nvMain.bringToFront();
    }

    void initNavigation() {

        //btn for open navigationDrawer and set background to be transparent
        activityMainBinding.btnOpenDrawer.setBackgroundColor(Color.TRANSPARENT);
        activityMainBinding.btnOpenDrawer.setOnClickListener(v -> activityMainBinding.dlMain.openDrawer(Gravity.LEFT));
        //set the head for navigation drawer and
        View hView = activityMainBinding.nvMain.getHeaderView(0);
        ImageView nav_header = hView.findViewById(R.id.iv_header);
        Glide.with(this)
                .load(R.drawable.movies)
                .centerCrop()
                .into(nav_header);}

    void onScroll() {


        activityMainBinding.rvPopularmovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean check = false;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!check) {
                    loadMore();
                    check = true;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


    }

    void observe() {

        //get cached movies
        viewModel.mMoviesCacheList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });



        //get popular movies from the api
        viewModel.mTopRatedMoviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });




        viewModel.mTopRatedMoviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                moviesModels.remove(moviesModels.size() - 1);

                mainAdapter.addMoviesList(moviesModels);
            }
        });

        //top movies load more list

        viewModel.mMoviesList.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.addMoviesList(moviesModels);
            }
        });
        viewModel.mLoadmore.observe(this, new Observer<List<MoviesModel>>() {
            @Override
            public void onChanged(List<MoviesModel> moviesModels) {
                mainAdapter.loadMore(moviesModels);
            }
        });

        // observe error
        viewModel.error.observe(this, new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }







    void refresh() {
        activityMainBinding.refresh.setOnRefreshListener(() -> {
            returnToRightCatogery(activityMainBinding.tvHeadName.getText().toString());
            activityMainBinding.refresh.setRefreshing(false);
        });

    }

    void detailsScreenIntent(MoviesModel moviesModel) {
        Intent intent = new Intent(MoviesActivity.this, DetailsActivity.class);
        intent.putExtra(Constants.MOVIE_INTENT_KEY, moviesModel);
        startActivity(intent);

    }

    void initAdapter() {
        activityMainBinding.rvPopularmovies.setLayoutManager(new GridLayoutManager(this, 3));
        mainAdapter = new MoviesAdapter(this, viewModel, activityMainBinding, new OnItemClicked() {
            @Override
            public void onListItemCLicked(MoviesModel moviesModel) {

                detailsScreenIntent(moviesModel);

            }
        });
        activityMainBinding.rvPopularmovies.setAdapter(mainAdapter);

    }

    void  initViewModel() {

        viewModel = ViewModelProviders.of(this).get(MoviesActivityViewModel.class);

    }

    void initBinding(){

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    }
}


