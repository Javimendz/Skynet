package com.example.skynet.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.skynet.R;
import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<ActiveFriendDto> friends = new ArrayList<>();

    public static class ActiveFriendDto {
        private Long id;
        private String nombre;
        private String foto;
        private String actividadActual;

        public ActiveFriendDto(Long id, String nombre, String foto, String actividadActual) {
            this.id = id;
            this.nombre = nombre;
            this.foto = foto;
            this.actividadActual = actividadActual;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getFoto() { return foto; }
        public String getActividadActual() { return actividadActual; }
    }

    public void setFriends(List<ActiveFriendDto> newFriends) {
        this.friends = newFriends;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_active, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActiveFriendDto friend = friends.get(position);
        holder.tvName.setText(friend.getNombre());
        holder.tvActivity.setText(friend.getActividadActual());
        
        if (friend.getFoto() != null && !friend.getFoto().isEmpty()) {
             // Handle Base64 if needed, but assuming URL or standard Glide handle
             if (friend.getFoto().length() > 200) { // Likely base64
                 byte[] decodedString = android.util.Base64.decode(friend.getFoto(), android.util.Base64.DEFAULT);
                 Glide.with(holder.itemView.getContext()).load(decodedString).circleCrop().into(holder.ivAvatar);
             } else {
                 Glide.with(holder.itemView.getContext()).load(friend.getFoto()).circleCrop().into(holder.ivAvatar);
             }
        } else {
            holder.ivAvatar.setImageResource(R.drawable.logo); // Fallback
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvActivity;

        ViewHolder(View view) {
            super(view);
            ivAvatar = view.findViewById(R.id.ivFriendAvatar);
            tvName = view.findViewById(R.id.tvFriendName);
            tvActivity = view.findViewById(R.id.tvFriendActivity);
        }
    }
}
