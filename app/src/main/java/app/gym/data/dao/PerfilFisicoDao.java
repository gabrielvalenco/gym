package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import app.gym.data.entities.PerfilFisico;

/**
 * DAO para operações com a entidade PerfilFisico
 */
@Dao
public interface PerfilFisicoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(PerfilFisico perfilFisico);
    
    @Update
    void atualizar(PerfilFisico perfilFisico);
    
    @Delete
    void deletar(PerfilFisico perfilFisico);
    
    @Query("SELECT * FROM perfis_fisicos WHERE id = :id")
    LiveData<PerfilFisico> obterPorId(long id);
    
    @Query("SELECT * FROM perfis_fisicos WHERE usuarioId = :usuarioId")
    LiveData<PerfilFisico> obterPorUsuarioId(long usuarioId);
    
    @Query("SELECT COUNT(*) FROM perfis_fisicos WHERE usuarioId = :usuarioId")
    int contarPorUsuarioId(long usuarioId);
    
    @Query("UPDATE perfis_fisicos SET peso = :peso, altura = :altura, percentualGordura = :percentualGordura, dataAtualizacao = :dataAtualizacao WHERE usuarioId = :usuarioId")
    void atualizarMedidas(long usuarioId, float peso, float altura, float percentualGordura, long dataAtualizacao);
}
