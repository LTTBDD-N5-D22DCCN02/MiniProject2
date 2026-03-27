package com.group5.miniproject2.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.group5.miniproject2.R;
import com.group5.miniproject2.data.model.Movie;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies != null ? movies : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemCount() { return movies.size(); }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivPoster;
        private final TextView tvTitle, tvGenre, tvDuration, tvRating, tvAgeRating;

        MovieViewHolder(View v) {
            super(v);
            ivPoster   = v.findViewById(R.id.iv_poster);
            tvTitle    = v.findViewById(R.id.tv_title);
            tvGenre    = v.findViewById(R.id.tv_genre);
            tvDuration = v.findViewById(R.id.tv_duration);
            tvRating   = v.findViewById(R.id.tv_rating);
            tvAgeRating= v.findViewById(R.id.tv_age_rating);
        }

        void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvGenre.setText(movie.getGenre());
            tvDuration.setText(movie.getDuration() + " phút");
            tvRating.setText(String.valueOf(movie.getRating()));
            tvAgeRating.setText(movie.getAgeRating());

            Glide.with(itemView.getContext())
                    .load(movie.getPosterUrl())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivPoster);

            itemView.setOnClickListener(v -> listener.onMovieClick(movie));
        }
    }
}