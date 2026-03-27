package com.group5.miniproject2.adapter;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.group5.miniproject2.R;
import com.group5.miniproject2.data.model.TicketDetail;
import com.group5.miniproject2.utils.Constants;
import java.text.SimpleDateFormat;
import java.util.*;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.VH> {
    public interface OnTicketClickListener { void onTicketClick(TicketDetail t); }

    private List<TicketDetail> list = new ArrayList<>();
    private final OnTicketClickListener listener;

    public TicketAdapter(OnTicketClickListener l) { this.listener = l; }

    public void setTickets(List<TicketDetail> data) {
        this.list = data != null ? data : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_ticket, p, false));
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) { h.bind(list.get(pos)); }
    @Override public int getItemCount() { return list.size(); }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvMovie, tvCode, tvDate, tvTime, tvTheater, tvSeats, tvPrice, tvStatus, tvType;
        VH(View v) {
            super(v);
            ivPoster  = v.findViewById(R.id.iv_ticket_poster);
            tvMovie   = v.findViewById(R.id.tv_ticket_movie);
            tvCode    = v.findViewById(R.id.tv_ticket_code);
            tvDate    = v.findViewById(R.id.tv_ticket_date);
            tvTime    = v.findViewById(R.id.tv_ticket_time);
            tvTheater = v.findViewById(R.id.tv_ticket_theater);
            tvSeats   = v.findViewById(R.id.tv_ticket_seats);
            tvPrice   = v.findViewById(R.id.tv_ticket_price);
            tvStatus  = v.findViewById(R.id.tv_ticket_status);
            tvType    = v.findViewById(R.id.tv_ticket_type);
        }
        void bind(TicketDetail t) {
            tvMovie.setText(t.movieTitle);
            tvCode.setText("Mã: " + t.bookingCode);
            tvDate.setText(t.showDate);
            tvTime.setText(t.showTime);
            tvTheater.setText(t.theaterName + " - " + t.theaterCity);
            tvSeats.setText("Ghế: " + t.seatNumbers);
            tvPrice.setText(String.format("%,.0f đ", t.totalPrice));
            tvType.setText(t.screenType);

            boolean confirmed = Constants.STATUS_CONFIRMED.equals(t.status);
            tvStatus.setText(confirmed ? "✓ Đã xác nhận" : "✗ Đã hủy");
            tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(),
                    confirmed ? R.color.colorSuccess : R.color.colorError));

            Glide.with(itemView.getContext())
                    .load(t.moviePoster)
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .centerCrop()
                    .into(ivPoster);

            itemView.setOnClickListener(v -> listener.onTicketClick(t));
        }
    }
}