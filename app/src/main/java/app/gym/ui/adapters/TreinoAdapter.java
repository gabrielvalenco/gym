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
import app.gym.data.entities.Treino;

/**
 * Adaptador para exibir a lista de treinos em um RecyclerView
 */
public class TreinoAdapter extends ListAdapter<Treino, TreinoAdapter.TreinoViewHolder> {

    private final OnTreinoClickListener listener;

    public TreinoAdapter(OnTreinoClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil para otimizar as atualizações da RecyclerView
    private static final DiffUtil.ItemCallback<Treino> DIFF_CALLBACK = new DiffUtil.ItemCallback<Treino>() {
        @Override
        public boolean areItemsTheSame(@NonNull Treino oldItem, @NonNull Treino newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Treino oldItem, @NonNull Treino newItem) {
            return oldItem.getNome().equals(newItem.getNome()) &&
                    oldItem.getObjetivo().equals(newItem.getObjetivo()) &&
                    oldItem.isAtivo() == newItem.isAtivo();
        }
    };

    @NonNull
    @Override
    public TreinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_treino, parent, false);
        return new TreinoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TreinoViewHolder holder, int position) {
        Treino treino = getItem(position);
        holder.bind(treino, listener);
    }

    /**
     * ViewHolder para os itens de treino
     */
    static class TreinoViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNome;
        private final TextView tvObjetivo;
        private final TextView tvNivel;
        private final TextView tvDuracao;
        private final TextView tvDescricao;
        private final ImageView ivStatus;

        public TreinoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_treino_nome);
            tvObjetivo = itemView.findViewById(R.id.tv_objetivo);
            tvNivel = itemView.findViewById(R.id.tv_nivel);
            tvDuracao = itemView.findViewById(R.id.tv_duracao);
            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            ivStatus = itemView.findViewById(R.id.iv_treino_status);
        }

        public void bind(Treino treino, OnTreinoClickListener listener) {
            tvNome.setText(treino.getNome());
            tvObjetivo.setText("Objetivo: " + treino.getObjetivo());
            tvNivel.setText(treino.getNivelDificuldade());
            tvDuracao.setText(treino.getDuracaoEstimadaMinutos() + " min");
            tvDescricao.setText(treino.getDescricao());
            
            // Definir o status visual (ativo/inativo)
            if (treino.isAtivo()) {
                ivStatus.setImageResource(android.R.drawable.presence_online);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorSuccess));
            } else {
                ivStatus.setImageResource(android.R.drawable.presence_offline);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorError));
            }
            
            // Configurar o clique no item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTreinoClick(treino);
                }
            });
        }
    }

    /**
     * Interface para tratar cliques nos itens de treino
     */
    public interface OnTreinoClickListener {
        void onTreinoClick(Treino treino);
    }
}
