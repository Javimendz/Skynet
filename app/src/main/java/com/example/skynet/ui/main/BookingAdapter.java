package com.example.skynet.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skynet.R;
import com.example.skynet.data.remote.dto.HorarioResponseDto;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<HorarioResponseDto> items = new ArrayList<>();
    private OnBookingClickListener listener;

    public interface OnBookingClickListener {
        void onBookingClick(HorarioResponseDto horario);
    }

    public BookingAdapter(OnBookingClickListener listener) {
        this.listener = listener;
    }

    public void updateItems(List<HorarioResponseDto> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HorarioResponseDto item = items.get(position);
        holder.tvDayName.setText(item.getDiaSemana());
        holder.tvClassTime.setText(item.getHoraInicio() + " - " + item.getHoraFin());
        
        int cupo = item.getCupoMaximo() != null ? item.getCupoMaximo() : 20;
        // Simulamos plazas disponibles si no viene del DTO
        holder.tvAvailability.setText("Cupo: " + cupo);

        holder.btnQuickReserve.setOnClickListener(v -> {
            if (listener != null) listener.onBookingClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName, tvClassTime, tvAvailability;
        MaterialButton btnQuickReserve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.tvDayName);
            tvClassTime = itemView.findViewById(R.id.tvClassTime);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            btnQuickReserve = itemView.findViewById(R.id.btnQuickReserve);
        }
    }
}
