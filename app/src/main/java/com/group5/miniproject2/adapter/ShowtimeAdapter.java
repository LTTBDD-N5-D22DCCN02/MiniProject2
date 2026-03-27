package com.group5.miniproject2.adapter;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.miniproject2.R;
import com.group5.miniproject2.data.model.ShowtimeDetail;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeAdapter extends RecyclerView.Adapter<ShowtimeAdapter.VH> {
    public interface OnShowtimeClickListener { void onShowtimeClick(ShowtimeDetail s); }

    private List<ShowtimeDetail> list = new ArrayList<>();
    private final OnShowtimeClickListener listener;

    public ShowtimeAdapter(OnShowtimeClickListener l) { this.listener = l; }

    public void setShowtimes(List<ShowtimeDetail> data) {
        this.list = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_showtime, p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) { h.bind(list.get(pos)); }
    @Override public int getItemCount() { return list.size(); }

    class VH extends RecyclerView.ViewHolder {
        TextView tvTime, tvTheater, tvRoom, tvType, tvPrice, tvSeats, tvMovie;
        VH(View v) {
            super(v);
            tvTime    = v.findViewById(R.id.tv_showtime_time);
            tvTheater = v.findViewById(R.id.tv_showtime_theater);
            tvRoom    = v.findViewById(R.id.tv_showtime_room);
            tvType    = v.findViewById(R.id.tv_showtime_type);
            tvPrice   = v.findViewById(R.id.tv_showtime_price);
            tvSeats   = v.findViewById(R.id.tv_showtime_seats);
            tvMovie   = v.findViewById(R.id.tv_showtime_movie);
        }
        void bind(ShowtimeDetail s) {
            tvTime.setText(s.showTime);
            tvTheater.setText(s.theaterName);
            tvRoom.setText("Phòng " + s.roomNumber);
            tvType.setText(s.screenType);
            tvPrice.setText(String.format("%,.0f đ", s.price));
            tvSeats.setText(s.availableSeats + " ghế trống");
            if (tvMovie != null) tvMovie.setText(s.movieTitle);
            itemView.setOnClickListener(v -> listener.onShowtimeClick(s));
        }
    }
}