package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.entities.Exercicio;
import app.gym.data.repositories.ExercicioRepository;

/**
 * ViewModel para operações relacionadas aos exercícios
 */
public class ExercicioViewModel extends AndroidViewModel {
    
    private final ExercicioRepository repository;
    private final LiveData<List<Exercicio>> todosExercicios;
    private final LiveData<List<Exercicio>> exerciciosAtivos;
    private final LiveData<Integer> quantidadeExerciciosAtivos;
    
    public ExercicioViewModel(@NonNull Application application) {
        super(application);
        repository = new ExercicioRepository(application);
        todosExercicios = repository.getTodosExercicios();
        exerciciosAtivos = repository.getExerciciosAtivos();
        quantidadeExerciciosAtivos = repository.getQuantidadeExerciciosAtivos();
    }
    
    // Métodos para acesso aos dados
    public LiveData<List<Exercicio>> getTodosExercicios() {
        return todosExercicios;
    }
    
    public LiveData<List<Exercicio>> getExerciciosAtivos() {
        return exerciciosAtivos;
    }
    
    public LiveData<Exercicio> getExercicioPorId(long id) {
        return repository.getExercicioPorId(id);
    }
    
    public LiveData<List<Exercicio>> getExerciciosPorGrupoMuscular(String grupoMuscular) {
        return repository.getExerciciosPorGrupoMuscular(grupoMuscular);
    }
    
    public LiveData<List<Exercicio>> getExerciciosPorNivelDificuldade(String nivelDificuldade) {
        return repository.getExerciciosPorNivelDificuldade(nivelDificuldade);
    }
    
    public LiveData<Integer> getQuantidadeExerciciosAtivos() {
        return quantidadeExerciciosAtivos;
    }
    
    // Métodos para operações CRUD
    public void inserir(Exercicio exercicio) {
        repository.inserir(exercicio);
    }
    
    public void atualizar(Exercicio exercicio) {
        repository.atualizar(exercicio);
    }
    
    public void deletar(Exercicio exercicio) {
        repository.deletar(exercicio);
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        repository.atualizarStatusAtivo(id, ativo);
    }
}
