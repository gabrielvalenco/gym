package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.entities.TreinoExercicio;
import app.gym.data.repositories.TreinoExercicioRepository;

/**
 * ViewModel para operações relacionadas à relação entre treinos e exercícios
 */
public class TreinoExercicioViewModel extends AndroidViewModel {
    
    private final TreinoExercicioRepository repository;
    
    public TreinoExercicioViewModel(@NonNull Application application) {
        super(application);
        repository = TreinoExercicioRepository.getInstance(application);
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        // Liberar recursos quando o ViewModel for destruído
        repository.shutdown();
    }
    
    // Métodos para acesso aos dados
    public LiveData<TreinoExercicio> getTreinoExercicioPorId(long id) {
        return repository.getTreinoExercicioPorId(id);
    }
    
    public LiveData<List<TreinoExercicio>> getExerciciosDoTreino(long treinoId) {
        return repository.getExerciciosDoTreino(treinoId);
    }
    
    public LiveData<List<TreinoExercicio>> getTreinosComExercicio(long exercicioId) {
        return repository.getTreinosComExercicio(exercicioId);
    }
    
    public LiveData<Integer> getQuantidadeExerciciosDoTreino(long treinoId) {
        return repository.getQuantidadeExerciciosDoTreino(treinoId);
    }
    
    public LiveData<TreinoExercicio> getTreinoExercicio(long treinoId, long exercicioId) {
        return repository.getTreinoExercicio(treinoId, exercicioId);
    }
    
    public int getProximaOrdemExecucao(long treinoId) {
        return repository.getProximaOrdemExecucao(treinoId);
    }
    
    // Métodos para operações CRUD
    public void inserir(TreinoExercicio treinoExercicio) {
        repository.inserir(treinoExercicio);
    }
    
    public void atualizar(TreinoExercicio treinoExercicio) {
        repository.atualizar(treinoExercicio);
    }
    
    public void deletar(TreinoExercicio treinoExercicio) {
        repository.deletar(treinoExercicio);
    }
    
    public void atualizarSeries(long id, int series) {
        repository.atualizarSeries(id, series);
    }
    
    public void atualizarRepeticoes(long id, int repeticoes) {
        repository.atualizarRepeticoes(id, repeticoes);
    }
    
    public void atualizarIntervalo(long id, int intervaloSegundos) {
        repository.atualizarIntervalo(id, intervaloSegundos);
    }
    
    public void removerTodosExerciciosDoTreino(long treinoId) {
        repository.removerTodosExerciciosDoTreino(treinoId);
    }
}
