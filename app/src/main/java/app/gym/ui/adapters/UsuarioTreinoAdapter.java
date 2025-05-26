package app.gym.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import app.gym.R;
import app.gym.data.entities.Treino;
import app.gym.data.entities.UsuarioTreino;

/**
 * Adaptador para exibir a lista de treinos de um usuário (relação N:N)
 */
public class UsuarioTreinoAdapter extends ListAdapter<UsuarioTreinoAdapter.UsuarioTreinoWithTreino, UsuarioTreinoAdapter.UsuarioTreinoViewHolder> {

    private final UsuarioTreinoListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public UsuarioTreinoAdapter(UsuarioTreinoListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil para otimizar as atualizações da RecyclerView
    private static final DiffUtil.ItemCallback<UsuarioTreinoWithTreino> DIFF_CALLBACK = new DiffUtil.ItemCallback<UsuarioTreinoWithTreino>() {
        @Override
        public boolean areItemsTheSame(@NonNull UsuarioTreinoWithTreino oldItem, @NonNull UsuarioTreinoWithTreino newItem) {
            return oldItem.usuarioTreino.getId() == newItem.usuarioTreino.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull UsuarioTreinoWithTreino oldItem, @NonNull UsuarioTreinoWithTreino newItem) {
            return oldItem.usuarioTreino.getStatusProgresso().equals(newItem.usuarioTreino.getStatusProgresso()) &&
                    oldItem.usuarioTreino.isAtivo() == newItem.usuarioTreino.isAtivo() &&
                    oldItem.usuarioTreino.getFrequenciaSemanal() == newItem.usuarioTreino.getFrequenciaSemanal();
        }
    };

    @NonNull
    @Override
    public UsuarioTreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario_treino, parent, false);
        return new UsuarioTreinoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioTreinoViewHolder holder, int position) {
        UsuarioTreinoWithTreino item = getItem(position);
        holder.bind(item, listener);
    }

    /**
     * ViewHolder para os itens de treino do usuário
     */
    static class UsuarioTreinoViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTreinoNome;
        private final TextView tvObjetivo;
        private final TextView tvFrequencia;
        private final TextView tvDataInicio;
        private final TextView tvObservacoes;
        private final TextView tvStatus;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        public UsuarioTreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTreinoNome = itemView.findViewById(R.id.tv_treino_nome);
            tvObjetivo = itemView.findViewById(R.id.tv_objetivo);
            tvFrequencia = itemView.findViewById(R.id.tv_frequencia);
            tvDataInicio = itemView.findViewById(R.id.tv_data_inicio);
            tvObservacoes = itemView.findViewById(R.id.tv_observacoes);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(UsuarioTreinoWithTreino item, UsuarioTreinoListener listener) {
            UsuarioTreino usuarioTreino = item.usuarioTreino;
            Treino treino = item.treino;

            // Exibir dados do treino
            tvTreinoNome.setText(treino.getNome());
            tvObjetivo.setText("Objetivo: " + treino.getObjetivo());
            tvFrequencia.setText(usuarioTreino.getFrequenciaSemanal() + "x/semana");
            
            // Exibir data de início
            if (usuarioTreino.getDataInicio() != null) {
                tvDataInicio.setText("Início: " + dateFormat.format(usuarioTreino.getDataInicio()));
            } else {
                tvDataInicio.setText("Início: N/A");
            }
            
            // Exibir observações se houver
            if (usuarioTreino.getObservacoes() != null && !usuarioTreino.getObservacoes().isEmpty()) {
                tvObservacoes.setText(usuarioTreino.getObservacoes());
                tvObservacoes.setVisibility(View.VISIBLE);
            } else {
                tvObservacoes.setVisibility(View.GONE);
            }
            
            // Exibir status
            tvStatus.setText(usuarioTreino.getStatusProgresso());
            
            // Definir cor do status
            int statusColor;
            switch (usuarioTreino.getStatusProgresso()) {
                case "Em andamento":
                    statusColor = itemView.getContext().getResources().getColor(R.color.colorPrimary);
                    break;
                case "Concluído":
                    statusColor = itemView.getContext().getResources().getColor(R.color.colorSuccess);
                    break;
                default: // "Não iniciado" ou outro
                    statusColor = itemView.getContext().getResources().getColor(R.color.colorWarning);
                    break;
            }
            tvStatus.setBackgroundColor(statusColor);
            
            // Configurar botões
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditUsuarioTreino(usuarioTreino.getId());
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteUsuarioTreino(usuarioTreino);
                }
            });
            
            // Configurar clique no item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUsuarioTreinoClick(usuarioTreino);
                }
            });
        }
    }

    /**
     * Classe que contém um UsuarioTreino e o Treino correspondente
     */
    public static class UsuarioTreinoWithTreino {
        public final UsuarioTreino usuarioTreino;
        public final Treino treino;

        public UsuarioTreinoWithTreino(UsuarioTreino usuarioTreino, Treino treino) {
            this.usuarioTreino = usuarioTreino;
            this.treino = treino;
        }
    }

    /**
     * Interface para tratar interações nos itens de treino do usuário
     */
    public interface UsuarioTreinoListener {
        void onUsuarioTreinoClick(UsuarioTreino usuarioTreino);
        void onEditUsuarioTreino(long usuarioTreinoId);
        void onDeleteUsuarioTreino(UsuarioTreino usuarioTreino);
    }
}
