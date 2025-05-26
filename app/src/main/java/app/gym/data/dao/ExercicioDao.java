package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.gym.data.entities.Exercicio;

/**
 * DAO para operações com a entidade Exercicio
 */
@Dao
public interface ExercicioDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(Exercicio exercicio);
    
    @Update
    void atualizar(Exercicio exercicio);
    
    @Delete
    void deletar(Exercicio exercicio);
    
    @Query("SELECT * FROM exercicios WHERE id = :id")
    LiveData<Exercicio> obterPorId(long id);
    
    @Query("SELECT * FROM exercicios WHERE ativo = 1 ORDER BY nome")
    LiveData<List<Exercicio>> obterTodosAtivos();
    
    @Query("SELECT * FROM exercicios ORDER BY nome")
    LiveData<List<Exercicio>> obterTodos();
    
    @Query("SELECT * FROM exercicios WHERE grupoMuscular = :grupoMuscular AND ativo = 1 ORDER BY nome")
    LiveData<List<Exercicio>> obterPorGrupoMuscular(String grupoMuscular);
    
    @Query("SELECT * FROM exercicios WHERE nivelDificuldade = :nivelDificuldade AND ativo = 1 ORDER BY nome")
    LiveData<List<Exercicio>> obterPorNivelDificuldade(String nivelDificuldade);
    
    @Query("UPDATE exercicios SET ativo = :ativo WHERE id = :id")
    void atualizarStatusAtivo(long id, boolean ativo);
    
    @Query("SELECT COUNT(*) FROM exercicios WHERE ativo = 1")
    LiveData<Integer> contarExerciciosAtivos();
}
