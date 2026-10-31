package com.example.skynet.ui.logros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skynet.R;
import com.example.skynet.data.remote.dto.LogroResponseDto;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import java.util.ArrayList;
import java.util.List;

public class LogroAdapter extends RecyclerView.Adapter<LogroAdapter.LogroViewHolder> {
    private List<LogroResponseDto> listaCompleta = new ArrayList<>();
    private final List<LogroResponseDto> listaFiltrada = new ArrayList<>();
    private String filtroActual = "TODOS"; // TODOS, DESBLOQUEADOS, EN_PROGRESO

    public void setLogros(List<LogroResponseDto> logros) {
        this.listaCompleta = logros != null ? logros : new ArrayList<>();
        aplicarFiltro(this.filtroActual);
    }

    public void aplicarFiltro(String filtro) {
        this.filtroActual = filtro;
        listaFiltrada.clear();
        for (LogroResponseDto l : listaCompleta) {
            if ("TODOS".equals(filtro)) {
                listaFiltrada.add(l);
            } else if ("DESBLOQUEADOS".equals(filtro) && l.isDesbloqueado()) {
                listaFiltrada.add(l);
            } else if ("EN_PROGRESO".equals(filtro) && !l.isDesbloqueado()) {
                listaFiltrada.add(l);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_logro, parent, false);
        return new LogroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogroViewHolder holder, int position) {
        LogroResponseDto logro = listaFiltrada.get(position);
        holder.bind(logro);
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    static class LogroViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvStatus, tvFecha;
        LinearProgressIndicator progressIndicator;
        MaterialCardView cardIcono;
        ImageView imgIcono;

        public LogroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreLogro);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionLogro);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvFecha = itemView.findViewById(R.id.tvFechaDesbloqueo);
            progressIndicator = itemView.findViewById(R.id.progressLogro);
            cardIcono = itemView.findViewById(R.id.cardIcono);
            imgIcono = itemView.findViewById(R.id.imgIcono);
        }

        public void bind(LogroResponseDto logro) {
            tvNombre.setText(logro.getNombre());
            tvDescripcion.setText(logro.getDescripcion());

            double pct = logro.getPorcentajeProgreso() != null ? logro.getPorcentajeProgreso() : 0.0;
            progressIndicator.setProgress((int) pct);

            if (logro.isDesbloqueado()) {
                tvStatus.setText("Completado");
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.accent_green));
                cardIcono.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.accent_green));
                imgIcono.setColorFilter(itemView.getContext().getResources().getColor(R.color.white));
                progressIndicator.setIndicatorColor(itemView.getContext().getResources().getColor(R.color.accent_green));
                if (logro.getFechaDesbloqueo() != null && !logro.getFechaDesbloqueo().isEmpty()) {
                    tvFecha.setText("Desbloqueado: " + logro.getFechaDesbloqueo());
                    tvFecha.setVisibility(View.VISIBLE);
                } else {
                    tvFecha.setVisibility(View.GONE);
                }
            } else {
                if (logro.getProgresoActual() != null && logro.getMeta() != null) {
                    tvStatus.setText(logro.getProgresoActual() + " / " + logro.getMeta());
                } else {
                    tvStatus.setText((int) pct + "%");
                }
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.accent_blue));
                cardIcono.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.accent_blue_light));
                imgIcono.setColorFilter(itemView.getContext().getResources().getColor(R.color.accent_blue));
                progressIndicator.setIndicatorColor(itemView.getContext().getResources().getColor(R.color.accent_blue));
                tvFecha.setVisibility(View.GONE);
            }
        }
    }
}
