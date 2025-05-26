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

import java.util.concurrent.TimeUnit;

import app.gym.R;
import app.gym.data.entities.Exercicio;
import app.gym.data.entities.TreinoExercicio;

/**
 * Adaptador para exibir a lista de exercícios de um treino (relação N:N)
 */
public class TreinoExercicioAdapter extends ListAdapter<TreinoExercicioAdapter.TreinoExercicioWithExercicio, TreinoExercicioAdapter.TreinoExercicioViewHolder> {

    private final TreinoExercicioListener listener;

    public TreinoExercicioAdapter(TreinoExercicioListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil para otimizar as atualizações da RecyclerView
    private static final DiffUtil.ItemCallback<TreinoExercicioWithExercicio> DIFF_CALLBACK = new DiffUtil.ItemCallback<TreinoExercicioWithExercicio>() {
        @Override
        public boolean areItemsTheSame(@NonNull TreinoExercicioWithExercicio oldItem, @NonNull TreinoExercicioWithExercicio newItem) {
            return oldItem.treinoExercicio.getId() == newItem.treinoExercicio.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TreinoExercicioWithExercicio oldItem, @NonNull TreinoExercicioWithExercicio newItem) {
            return oldItem.treinoExercicio.getOrdemExecucao() == newItem.treinoExercicio.getOrdemExecucao() &&
                    oldItem.treinoExercicio.getSeries() == newItem.treinoExercicio.getSeries() &&
                    oldItem.treinoExercicio.getRepeticoes() == newItem.treinoExercicio.getRepeticoes() &&
                    oldItem.treinoExercicio.getIntervaloSegundos() == newItem.treinoExercicio.getIntervaloSegundos();
        }
    };

    @NonNull
    @Override
    public TreinoExercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_treino_exercicio, parent, false);
        return new TreinoExercicioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TreinoExercicioViewHolder holder, int position) {
        TreinoExercicioWithExercicio item = getItem(position);
        holder.bind(item, listener);
    }

    /**
     * ViewHolder para os itens de exercício do treino
     */
    static class TreinoExercicioViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrdem;
        private final TextView tvExercicioNome;
        private final TextView tvDescricao;
        private final TextView tvSeriesRepeticoes;
        private final TextView tvIntervalo;
        private final TextView tvObservacoes;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public TreinoExercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrdem = itemView.findViewById(R.id.tv_ordem);
            tvExercicioNome = itemView.findViewById(R.id.tv_exercicio_nome);
            tvDescricao = itemView.findViewById(R.id.tv_descricao);
            tvSeriesRepeticoes = itemView.findViewById(R.id.tv_series_repeticoes);
            tvIntervalo = itemView.findViewById(R.id.tv_intervalo);
            tvObservacoes = itemView.findViewById(R.id.tv_observacoes);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(TreinoExercicioWithExercicio item, TreinoExercicioListener listener) {
            TreinoExercicio treinoExercicio = item.treinoExercicio;
            Exercicio exercicio = item.exercicio;

            // Exibir dados do exercício no treino
            tvOrdem.setText(String.valueOf(treinoExercicio.getOrdemExecucao()));
            tvExercicioNome.setText(exercicio.getNome());
            tvDescricao.setText(exercicio.getDescricao());
            
            // Formatar séries e repetições
            tvSeriesRepeticoes.setText(treinoExercicio.getSeries() + " séries x " + 
                    treinoExercicio.getRepeticoes() + " repetições");
            
            // Formatar intervalo
            int intervalo = treinoExercicio.getIntervaloSegundos();
            String intervaloFormatado;
            if (intervalo >= 60) {
                long minutos = TimeUnit.SECONDS.toMinutes(intervalo);
                long segundosRestantes = intervalo - TimeUnit.MINUTES.toSeconds(minutos);
                if (segundosRestantes > 0) {
                    intervaloFormatado = minutos + "m " + segundosRestantes + "s";
                } else {
                    intervaloFormatado = minutos + "m";
                }
            } else {
                intervaloFormatado = intervalo + "s";
            }
            tvIntervalo.setText("Intervalo: " + intervaloFormatado);
            
            // Exibir observações se houver
            if (treinoExercicio.getObservacoes() != null && !treinoExercicio.getObservacoes().isEmpty()) {
                tvObservacoes.setText(treinoExercicio.getObservacoes());
                tvObservacoes.setVisibility(View.VISIBLE);
            } else {
                tvObservacoes.setVisibility(View.GONE);
            }
            
            // Configurar botões
            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditTreinoExercicio(treinoExercicio.getId());
                }
            });
            
            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteTreinoExercicio(treinoExercicio);
                }
            });
            
            // Configurar clique no item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTreinoExercicioClick(treinoExercicio);
                }
            });
        }
    }

    /**
     * Classe que contém um TreinoExercicio e o Exercicio correspondente
     */
    public static class TreinoExercicioWithExercicio {
        public final TreinoExercicio treinoExercicio;
        public final Exercicio exercicio;

        public TreinoExercicioWithExercicio(TreinoExercicio treinoExercicio, Exercicio exercicio) {
            this.treinoExercicio = treinoExercicio;
            this.exercicio = exercicio;
        }
    }

    /**
     * Interface para tratar interações nos itens de exercício do treino
     */
    public interface TreinoExercicioListener {
        void onTreinoExercicioClick(TreinoExercicio treinoExercicio);
        void onEditTreinoExercicio(long treinoExercicioId);
        void onDeleteTreinoExercicio(TreinoExercicio treinoExercicio);
    }
}
