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
import app.gym.data.entities.Exercicio;

/**
 * Adaptador para exibir a lista de exercícios em um RecyclerView
 */
public class ExercicioAdapter extends ListAdapter<Exercicio, ExercicioAdapter.ExercicioViewHolder> {

    private final OnExercicioClickListener listener;

    public ExercicioAdapter(OnExercicioClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil para otimizar as atualizações da RecyclerView
    private static final DiffUtil.ItemCallback<Exercicio> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exercicio>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exercicio oldItem, @NonNull Exercicio newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exercicio oldItem, @NonNull Exercicio newItem) {
            return oldItem.getNome().equals(newItem.getNome()) &&
                    oldItem.getGrupoMuscular().equals(newItem.getGrupoMuscular()) &&
                    oldItem.isAtivo() == newItem.isAtivo();
        }
    };

    @NonNull
    @Override
    public ExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercicio, parent, false);
        return new ExercicioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercicioViewHolder holder, int position) {
        Exercicio exercicio = getItem(position);
        holder.bind(exercicio, listener);
    }

    /**
     * ViewHolder para os itens de exercício
     */
    static class ExercicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNome;
        private final TextView tvGrupoMuscular;
        private final TextView tvNivel;
        private final TextView tvEquipamento;
        private final ImageView ivStatus;

        public ExercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tv_exercicio_nome);
            tvGrupoMuscular = itemView.findViewById(R.id.tv_grupo_muscular);
            tvNivel = itemView.findViewById(R.id.tv_nivel);
            tvEquipamento = itemView.findViewById(R.id.tv_equipamento);
            ivStatus = itemView.findViewById(R.id.iv_exercicio_status);
        }

        public void bind(Exercicio exercicio, OnExercicioClickListener listener) {
            tvNome.setText(exercicio.getNome());
            tvGrupoMuscular.setText(exercicio.getGrupoMuscular());
            tvNivel.setText(exercicio.getNivelDificuldade());
            tvEquipamento.setText("Equipamento: " + exercicio.getEquipamento());
            
            // Definir o status visual (ativo/inativo)
            if (exercicio.isAtivo()) {
                ivStatus.setImageResource(android.R.drawable.presence_online);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorSuccess));
            } else {
                ivStatus.setImageResource(android.R.drawable.presence_offline);
                ivStatus.setColorFilter(itemView.getContext().getResources().getColor(R.color.colorError));
            }
            
            // Configurar o clique no item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onExercicioClick(exercicio);
                }
            });
        }
    }

    /**
     * Interface para tratar cliques nos itens de exercício
     */
    public interface OnExercicioClickListener {
        void onExercicioClick(Exercicio exercicio);
    }
}
