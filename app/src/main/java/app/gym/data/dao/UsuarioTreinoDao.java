package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import app.gym.data.entities.UsuarioTreino;

/**
 * DAO para operações com a entidade UsuarioTreino (relação N:N entre Usuario e Treino)
 */
@Dao
public interface UsuarioTreinoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(UsuarioTreino usuarioTreino);
    
    @Update
    void atualizar(UsuarioTreino usuarioTreino);
    
    @Delete
    void deletar(UsuarioTreino usuarioTreino);
    
    @Query("SELECT * FROM usuario_treino WHERE id = :id")
    LiveData<UsuarioTreino> obterPorId(long id);
    
    @Query("SELECT * FROM usuario_treino WHERE usuarioId = :usuarioId AND ativo = 1")
    LiveData<List<UsuarioTreino>> obterTreinosDoUsuario(long usuarioId);
    
    @Query("SELECT * FROM usuario_treino WHERE treinoId = :treinoId AND ativo = 1")
    LiveData<List<UsuarioTreino>> obterUsuariosDoTreino(long treinoId);
    
    @Query("SELECT * FROM usuario_treino WHERE usuarioId = :usuarioId AND treinoId = :treinoId LIMIT 1")
    LiveData<UsuarioTreino> obterUsuarioTreino(long usuarioId, long treinoId);
    
    @Query("UPDATE usuario_treino SET ativo = :ativo WHERE id = :id")
    void atualizarStatusAtivo(long id, boolean ativo);
    
    @Query("UPDATE usuario_treino SET statusProgresso = :statusProgresso WHERE id = :id")
    void atualizarStatusProgresso(long id, String statusProgresso);
    
    @Query("SELECT COUNT(*) FROM usuario_treino WHERE usuarioId = :usuarioId AND ativo = 1")
    LiveData<Integer> contarTreinosAtivosDoUsuario(long usuarioId);
    
    @Transaction
    @Query("UPDATE usuario_treino SET dataFim = :dataFim, ativo = 0 WHERE id = :id")
    void finalizarTreino(long id, long dataFim);
}
