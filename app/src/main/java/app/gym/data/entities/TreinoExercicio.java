package app.gym.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa a relação N:N entre Treino e Exercicio
 */
@Entity(tableName = "treino_exercicio",
        foreignKeys = {
                @ForeignKey(
                        entity = Treino.class,
                        parentColumns = "id",
                        childColumns = "treinoId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Exercicio.class,
                        parentColumns = "id",
                        childColumns = "exercicioId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"treinoId", "exercicioId"}, unique = true),
                @Index(value = "treinoId"),
                @Index(value = "exercicioId")
        }
)
public class TreinoExercicio {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long treinoId;
    
    private long exercicioId;
    
    // Detalhes específicos do exercício no treino
    private int ordemExecucao;      // Ordem em que o exercício deve ser realizado no treino
    private int series;             // Número de séries
    private int repeticoes;         // Número de repetições por série
    private int intervaloSegundos;  // Intervalo de descanso entre séries em segundos
    private String observacoes;     // Observações específicas para este exercício no treino
    
    // Construtor
    public TreinoExercicio(long treinoId, long exercicioId, int ordemExecucao, int series, int repeticoes, int intervaloSegundos) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.ordemExecucao = ordemExecucao;
        this.series = series;
        this.repeticoes = repeticoes;
        this.intervaloSegundos = intervaloSegundos;
    }
    
    // Getters e Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getTreinoId() {
        return treinoId;
    }
    
    public void setTreinoId(long treinoId) {
        this.treinoId = treinoId;
    }
    
    public long getExercicioId() {
        return exercicioId;
    }
    
    public void setExercicioId(long exercicioId) {
        this.exercicioId = exercicioId;
    }
    
    public int getOrdemExecucao() {
        return ordemExecucao;
    }
    
    public void setOrdemExecucao(int ordemExecucao) {
        this.ordemExecucao = ordemExecucao;
    }
    
    public int getSeries() {
        return series;
    }
    
    public void setSeries(int series) {
        this.series = series;
    }
    
    public int getRepeticoes() {
        return repeticoes;
    }
    
    public void setRepeticoes(int repeticoes) {
        this.repeticoes = repeticoes;
    }
    
    public int getIntervaloSegundos() {
        return intervaloSegundos;
    }
    
    public void setIntervaloSegundos(int intervaloSegundos) {
        this.intervaloSegundos = intervaloSegundos;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
