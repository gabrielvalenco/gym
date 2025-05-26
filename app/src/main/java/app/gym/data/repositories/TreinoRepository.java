package app.gym.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.AppDatabase;
import app.gym.data.dao.TreinoDao;
import app.gym.data.entities.Treino;

/**
 * Repositório para gerenciar operações relacionadas aos treinos
 */
public class TreinoRepository {
    
    private final TreinoDao treinoDao;
    private final LiveData<List<Treino>> todosTreinos;
    private final LiveData<List<Treino>> treinosAtivos;
    private final LiveData<Integer> quantidadeTreinosAtivos;
    
    public TreinoRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        treinoDao = database.treinoDao();
        todosTreinos = treinoDao.obterTodos();
        treinosAtivos = treinoDao.obterTodosAtivos();
        quantidadeTreinosAtivos = treinoDao.contarTreinosAtivos();
    }
    
    // Métodos para recuperar dados
    public LiveData<List<Treino>> getTodosTreinos() {
        return todosTreinos;
    }
    
    public LiveData<List<Treino>> getTreinosAtivos() {
        return treinosAtivos;
    }
    
    public LiveData<Treino> getTreinoPorId(long id) {
        return treinoDao.obterPorId(id);
    }
    
    public LiveData<List<Treino>> getTreinosPorObjetivo(String objetivo) {
        return treinoDao.obterPorObjetivo(objetivo);
    }
    
    public LiveData<List<Treino>> getTreinosPorNivelDificuldade(String nivelDificuldade) {
        return treinoDao.obterPorNivelDificuldade(nivelDificuldade);
    }
    
    public LiveData<Integer> getQuantidadeTreinosAtivos() {
        return quantidadeTreinosAtivos;
    }
    
    // Métodos para operações CRUD
    public void inserir(Treino treino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            treinoDao.inserir(treino);
        });
    }
    
    public void atualizar(Treino treino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            treinoDao.atualizar(treino);
        });
    }
    
    public void deletar(Treino treino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            treinoDao.deletar(treino);
        });
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            treinoDao.atualizarStatusAtivo(id, ativo);
        });
    }
}
