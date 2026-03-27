package com.group5.movieticket.adapter;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.movieticket.R;
import com.group5.movieticket.data.model.Theater;
import java.util.ArrayList;
import java.util.List;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder> {
    public interface OnTheaterClickListener { void onTheaterClick(Theater theater); }

    private List<Theater> theaters = new ArrayList<>();
    private final OnTheaterClickListener listener;

    public TheaterAdapter(OnTheaterClickListener listener) { this.listener = listener; }

    public void setTheaters(List<Theater> list) {
        this.theaters = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new TheaterViewHolder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_theater, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder h, int pos) { h.bind(theaters.get(pos)); }

    @Override public int getItemCount() { return theaters.size(); }

    class TheaterViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvRating, tvCity, tvPhone;
        TheaterViewHolder(View v) {
            super(v);
            tvName    = v.findViewById(R.id.tv_theater_name);
            tvAddress = v.findViewById(R.id.tv_theater_address);
            tvRating  = v.findViewById(R.id.tv_theater_rating);
            tvCity    = v.findViewById(R.id.tv_theater_city);
            tvPhone   = v.findViewById(R.id.tv_theater_phone);
        }
        void bind(Theater t) {
            tvName.setText(t.getName());
            tvAddress.setText(t.getAddress());
            tvRating.setText("⭐ " + t.getRating());
            tvCity.setText(t.getCity());
            tvPhone.setText(t.getPhone());
            itemView.setOnClickListener(v -> listener.onTheaterClick(t));
        }
    }
}