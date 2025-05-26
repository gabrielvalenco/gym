package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.entities.Treino;
import app.gym.data.repositories.TreinoRepository;

/**
 * ViewModel para operações relacionadas aos treinos
 */
public class TreinoViewModel extends AndroidViewModel {
    
    private final TreinoRepository repository;
    private final LiveData<List<Treino>> todosTreinos;
    private final LiveData<List<Treino>> treinosAtivos;
    private final LiveData<Integer> quantidadeTreinosAtivos;
    
    public TreinoViewModel(@NonNull Application application) {
        super(application);
        repository = new TreinoRepository(application);
        todosTreinos = repository.getTodosTreinos();
        treinosAtivos = repository.getTreinosAtivos();
        quantidadeTreinosAtivos = repository.getQuantidadeTreinosAtivos();
    }
    
    // Métodos para acesso aos dados
    public LiveData<List<Treino>> getTodosTreinos() {
        return todosTreinos;
    }
    
    public LiveData<List<Treino>> getTreinosAtivos() {
        return treinosAtivos;
    }
    
    public LiveData<Treino> getTreinoPorId(long id) {
        return repository.getTreinoPorId(id);
    }
    
    public LiveData<List<Treino>> getTreinosPorObjetivo(String objetivo) {
        return repository.getTreinosPorObjetivo(objetivo);
    }
    
    public LiveData<List<Treino>> getTreinosPorNivelDificuldade(String nivelDificuldade) {
        return repository.getTreinosPorNivelDificuldade(nivelDificuldade);
    }
    
    public LiveData<Integer> getQuantidadeTreinosAtivos() {
        return quantidadeTreinosAtivos;
    }
    
    // Métodos para operações CRUD
    public void inserir(Treino treino) {
        repository.inserir(treino);
    }
    
    public void atualizar(Treino treino) {
        repository.atualizar(treino);
    }
    
    public void deletar(Treino treino) {
        repository.deletar(treino);
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        repository.atualizarStatusAtivo(id, ativo);
    }
}
