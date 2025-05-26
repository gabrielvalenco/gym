package app.gym.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;

/**
 * Entidade que representa o perfil físico de um usuário (relação 1:1 com Usuario)
 */
@Entity(tableName = "perfis_fisicos",
        foreignKeys = @ForeignKey(
                entity = Usuario.class,
                parentColumns = "id",
                childColumns = "usuarioId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index(value = "usuarioId", unique = true)}
)
public class PerfilFisico {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private long usuarioId; // Chave estrangeira para Usuario (1:1)
    
    private float altura; // em metros
    
    private float peso; // em kg
    
    private float percentualGordura;
    
    private float imc; // Índice de Massa Corporal
    
    private Date dataAtualizacao;
    
    private String objetivoFitness; // Ex: "Perda de peso", "Ganho de massa", "Definição"
    
    private String restricoesSaude; // Restrições ou condições de saúde

    // Construtor
    public PerfilFisico(long usuarioId, float altura, float peso) {
        this.usuarioId = usuarioId;
        this.altura = altura;
        this.peso = peso;
        this.imc = calcularIMC(peso, altura);
        this.dataAtualizacao = new Date();
    }
    
    // Método para calcular o IMC
    private float calcularIMC(float peso, float altura) {
        if (altura <= 0) {
            return 0;
        }
        return peso / (altura * altura);
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

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
        this.imc = calcularIMC(this.peso, altura);
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
        this.imc = calcularIMC(peso, this.altura);
    }

    public float getPercentualGordura() {
        return percentualGordura;
    }

    public void setPercentualGordura(float percentualGordura) {
        this.percentualGordura = percentualGordura;
    }

    public float getImc() {
        return imc;
    }
    
    public void setImc(float imc) {
        this.imc = imc;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getObjetivoFitness() {
        return objetivoFitness;
    }

    public void setObjetivoFitness(String objetivoFitness) {
        this.objetivoFitness = objetivoFitness;
    }

    public String getRestricoesSaude() {
        return restricoesSaude;
    }

    public void setRestricoesSaude(String restricoesSaude) {
        this.restricoesSaude = restricoesSaude;
    }
}
