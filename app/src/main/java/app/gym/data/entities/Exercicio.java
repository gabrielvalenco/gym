package app.gym.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa um exercício disponível na academia
 */
@Entity(tableName = "exercicios")
public class Exercicio {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    @NonNull
    private String nome;
    
    private String descricao;
    
    private String grupoMuscular; // Ex: "Peitoral", "Pernas", "Costas", etc.
    
    private String equipamento; // Ex: "Halteres", "Máquina", "Peso corporal", etc.
    
    private String nivelDificuldade; // Ex: "Iniciante", "Intermediário", "Avançado"
    
    private String instrucoes; // Passos detalhados para realizar o exercício
    
    private String urlVideo; // Link para um vídeo demonstrativo, se disponível
    
    private boolean ativo;

    // Construtor
    public Exercicio(@NonNull String nome, String descricao, String grupoMuscular, 
                    String equipamento, String nivelDificuldade) {
        this.nome = nome;
        this.descricao = descricao;
        this.grupoMuscular = grupoMuscular;
        this.equipamento = equipamento;
        this.nivelDificuldade = nivelDificuldade;
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

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(String equipamento) {
        this.equipamento = equipamento;
    }

    public String getNivelDificuldade() {
        return nivelDificuldade;
    }

    public void setNivelDificuldade(String nivelDificuldade) {
        this.nivelDificuldade = nivelDificuldade;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
