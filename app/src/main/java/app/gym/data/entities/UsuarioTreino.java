package app.gym.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entidade que representa a relação N:N entre Usuario e Treino
 */
@Entity(tableName = "usuario_treino",
        foreignKeys = {
                @ForeignKey(
                        entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "usuarioId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Treino.class,
                        parentColumns = "id",
                        childColumns = "treinoId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = {"usuarioId", "treinoId"}, unique = true),
                @Index(value = "usuarioId"),
                @Index(value = "treinoId")
        }
)
public class UsuarioTreino {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long usuarioId;
    
    private long treinoId;
    
    private Date dataInicio;
    
    private Date dataFim;
    
    private boolean ativo;
    
    private int frequenciaSemanal; // Número de vezes por semana
    
    private String observacoes;
    
    private String statusProgresso; // Ex: "Não iniciado", "Em andamento", "Concluído"

    // Construtor
    public UsuarioTreino(long usuarioId, long treinoId, int frequenciaSemanal) {
        this.usuarioId = usuarioId;
        this.treinoId = treinoId;
        this.frequenciaSemanal = frequenciaSemanal;
        this.dataInicio = new Date();
        this.ativo = true;
        this.statusProgresso = "Não iniciado";
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public long getTreinoId() {
        return treinoId;
    }

    public void setTreinoId(long treinoId) {
        this.treinoId = treinoId;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getFrequenciaSemanal() {
        return frequenciaSemanal;
    }

    public void setFrequenciaSemanal(int frequenciaSemanal) {
        this.frequenciaSemanal = frequenciaSemanal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getStatusProgresso() {
        return statusProgresso;
    }

    public void setStatusProgresso(String statusProgresso) {
        this.statusProgresso = statusProgresso;
    }
}
