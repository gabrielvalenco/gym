package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.gym.data.entities.TreinoExercicio;

/**
 * DAO para operações com a entidade TreinoExercicio (relação N:N entre Treino e Exercicio)
 */
@Dao
public interface TreinoExercicioDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(TreinoExercicio treinoExercicio);
    
    @Update
    void atualizar(TreinoExercicio treinoExercicio);
    
    @Delete
    void deletar(TreinoExercicio treinoExercicio);
    
    @Query("SELECT * FROM treino_exercicio WHERE id = :id")
    LiveData<TreinoExercicio> obterPorId(long id);
    
    @Query("SELECT * FROM treino_exercicio WHERE treinoId = :treinoId ORDER BY ordemExecucao ASC")
    LiveData<List<TreinoExercicio>> obterExerciciosDoTreino(long treinoId);
    
    @Query("SELECT * FROM treino_exercicio WHERE exercicioId = :exercicioId")
    LiveData<List<TreinoExercicio>> obterTreinosComExercicio(long exercicioId);
    
    @Query("SELECT COUNT(*) FROM treino_exercicio WHERE treinoId = :treinoId")
    LiveData<Integer> contarExerciciosDoTreino(long treinoId);
    
    @Query("SELECT * FROM treino_exercicio WHERE treinoId = :treinoId AND exercicioId = :exercicioId")
    LiveData<TreinoExercicio> obterTreinoExercicio(long treinoId, long exercicioId);
    
    @Query("SELECT MAX(ordemExecucao) FROM treino_exercicio WHERE treinoId = :treinoId")
    int obterUltimaOrdemExecucao(long treinoId);
    
    @Query("UPDATE treino_exercicio SET series = :series WHERE id = :id")
    void atualizarSeries(long id, int series);
    
    @Query("UPDATE treino_exercicio SET repeticoes = :repeticoes WHERE id = :id")
    void atualizarRepeticoes(long id, int repeticoes);
    
    @Query("UPDATE treino_exercicio SET intervaloSegundos = :intervaloSegundos WHERE id = :id")
    void atualizarIntervalo(long id, int intervaloSegundos);
    
    @Query("DELETE FROM treino_exercicio WHERE treinoId = :treinoId")
    void removerTodosExerciciosDoTreino(long treinoId);
}
