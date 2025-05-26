package app.gym.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entidade que representa um treino disponível na academia
 */
@Entity(tableName = "treinos")
public class Treino {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    @NonNull
    private String nome;
    
    private String descricao;
    
    private String objetivo; // Ex: "Hipertrofia", "Perda de peso", "Resistência"
    
    private String nivelDificuldade; // Ex: "Iniciante", "Intermediário", "Avançado"
    
    private int duracaoEstimadaMinutos;
    
    private Date dataCriacao;
    
    private boolean ativo;
    
    // Construtor
    public Treino(@NonNull String nome, String descricao, String objetivo, 
                 String nivelDificuldade, int duracaoEstimadaMinutos) {
        this.nome = nome;
        this.descricao = descricao;
        this.objetivo = objetivo;
        this.nivelDificuldade = nivelDificuldade;
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
        this.dataCriacao = new Date();
        this.ativo = true;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(String nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public int getDuracaoEstimadaMinutos() {
        return duracaoEstimadaMinutos;
    }

    public void setDuracaoEstimadaMinutos(int duracaoEstimadaMinutos) {
        this.duracaoEstimadaMinutos = duracaoEstimadaMinutos;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
