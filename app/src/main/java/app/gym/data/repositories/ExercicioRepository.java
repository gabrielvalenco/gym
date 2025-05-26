package app.gym.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.AppDatabase;
import app.gym.data.dao.ExercicioDao;
import app.gym.data.entities.Exercicio;

/**
 * Repositório para gerenciar operações relacionadas aos exercícios
 */
public class ExercicioRepository {
    
    private final ExercicioDao exercicioDao;
    private final LiveData<List<Exercicio>> todosExercicios;
    private final LiveData<List<Exercicio>> exerciciosAtivos;
    private final LiveData<Integer> quantidadeExerciciosAtivos;
    
    public ExercicioRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        exercicioDao = database.exercicioDao();
        todosExercicios = exercicioDao.obterTodos();
        exerciciosAtivos = exercicioDao.obterTodosAtivos();
        quantidadeExerciciosAtivos = exercicioDao.contarExerciciosAtivos();
    }
    
    // Métodos para recuperar dados
    public LiveData<List<Exercicio>> getTodosExercicios() {
        return todosExercicios;
    }
    
    public LiveData<List<Exercicio>> getExerciciosAtivos() {
        return exerciciosAtivos;
    }
    
    public LiveData<Exercicio> getExercicioPorId(long id) {
        return exercicioDao.obterPorId(id);
    }
    
    public LiveData<List<Exercicio>> getExerciciosPorGrupoMuscular(String grupoMuscular) {
        return exercicioDao.obterPorGrupoMuscular(grupoMuscular);
    }
    
    public LiveData<List<Exercicio>> getExerciciosPorNivelDificuldade(String nivelDificuldade) {
        return exercicioDao.obterPorNivelDificuldade(nivelDificuldade);
    }
    
    public LiveData<Integer> getQuantidadeExerciciosAtivos() {
        return quantidadeExerciciosAtivos;
    }
    
    // Métodos para operações CRUD
    public void inserir(Exercicio exercicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exercicioDao.inserir(exercicio);
        });
    }
    
    public void atualizar(Exercicio exercicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exercicioDao.atualizar(exercicio);
        });
    }
    
    public void deletar(Exercicio exercicio) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exercicioDao.deletar(exercicio);
        });
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            exercicioDao.atualizarStatusAtivo(id, ativo);
        });
    }
}
