package app.gym.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import app.gym.R;
import app.gym.data.entities.Usuario;

/**
 * Adaptador para exibir a lista de usuários em um RecyclerView
 */
public class UsuarioAdapter extends ListAdapter<Usuario, UsuarioAdapter.UsuarioViewHolder> {

    private final OnUsuarioClickListener listener;

    public UsuarioAdapter(OnUsuarioClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil para otimizar as atualizações da RecyclerView
    private static final DiffUtil.ItemCallback<Usuario> DIFF_CALLBACK = new DiffUtil.ItemCallback<Usuario>() {
        @Override
        public boolean areItemsTheSame(@NonNull Usuario oldItem, @NonNull Usuario newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Usuario oldItem, @NonNull Usuario newItem) {
            return oldItem.getNome().equals(newItem.getNome()) &&
                    oldItem.getEmail().equals(newItem.getEmail()) &&
                    oldItem.isAtivo() == newItem.isAtivo();
        }
    };

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usuario = getItem(position);
        holder.bind(usuario, listener);
    }

    /**
     * ViewHolder para os itens de usuário
     */
    static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNome;
        private final TextView tvEmail;
        private final ImageView ivStatus;
        private final ImageView ivAvatar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_usuario_nome);
            tvEmail = itemView.findViewById(R.id.tv_usuario_email);
            ivStatus = itemView.findViewById(R.id.iv_usuario_status);
            ivAvatar = itemView.findViewById(R.id.iv_usuario_avatar);
        }

        public void bind(Usuario usuario, OnUsuarioClickListener listener) {
            tvNome.setText(usuario.getNome());
            tvEmail.setText(usuario.getEmail());
            
            // Definir o status visual (ativo/inativo)
            if (usuario.isAtivo()) {
                ivStatus.setImageResource(android.R.drawable.presence_online);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorSuccess));
            } else {
                ivStatus.setImageResource(android.R.drawable.presence_offline);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorError));
            }
            
            // Configurar o clique no item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUsuarioClick(usuario);
                }
            });
        }
    }

    /**
     * Interface para tratar cliques nos itens de usuário
     */
    public interface OnUsuarioClickListener {
        void onUsuarioClick(Usuario usuario);
    }
}
