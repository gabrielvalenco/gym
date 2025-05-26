package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.gym.data.entities.Treino;

/**
 * DAO para operações com a entidade Treino
 */
@Dao
public interface TreinoDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(Treino treino);
    
    @Update
    void atualizar(Treino treino);
    
    @Delete
    void deletar(Treino treino);
    
    @Query("SELECT * FROM treinos WHERE id = :id")
    LiveData<Treino> obterPorId(long id);
    
    @Query("SELECT * FROM treinos WHERE ativo = 1 ORDER BY nome")
    LiveData<List<Treino>> obterTodosAtivos();
    
    @Query("SELECT * FROM treinos ORDER BY nome")
    LiveData<List<Treino>> obterTodos();
    
    @Query("SELECT * FROM treinos WHERE objetivo = :objetivo AND ativo = 1 ORDER BY nome")
    LiveData<List<Treino>> obterPorObjetivo(String objetivo);
    
    @Query("SELECT * FROM treinos WHERE nivelDificuldade = :nivelDificuldade AND ativo = 1 ORDER BY nome")
    LiveData<List<Treino>> obterPorNivelDificuldade(String nivelDificuldade);
    
    @Query("UPDATE treinos SET ativo = :ativo WHERE id = :id")
    void atualizarStatusAtivo(long id, boolean ativo);
    
    @Query("SELECT COUNT(*) FROM treinos WHERE ativo = 1")
    LiveData<Integer> contarTreinosAtivos();
    
    @Query("SELECT COUNT(*) FROM treinos")
    LiveData<Integer> contarTodosTreinos();
}
