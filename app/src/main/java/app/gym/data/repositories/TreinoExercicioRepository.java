package app.gym.data.repositories;

import android.app.Application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.AppDatabase;
import app.gym.data.dao.TreinoExercicioDao;
import app.gym.data.entities.TreinoExercicio;

/**
 * Repositório para operações com a entidade TreinoExercicio
 */
public class TreinoExercicioRepository {
    
    private final TreinoExercicioDao treinoExercicioDao;
    private static TreinoExercicioRepository instance;
    private final ExecutorService executorService;
    
    private TreinoExercicioRepository(AppDatabase database) {
        treinoExercicioDao = database.treinoExercicioDao();
        executorService = Executors.newFixedThreadPool(4);
    }
    
    public static TreinoExercicioRepository getInstance(Application application) {
        if (instance == null) {
            instance = new TreinoExercicioRepository(AppDatabase.getDatabase(application));
        }
        return instance;
    }
    
    // Métodos para acesso aos dados
    public LiveData<TreinoExercicio> getTreinoExercicioPorId(long id) {
        return treinoExercicioDao.obterPorId(id);
    }
    
    public LiveData<List<TreinoExercicio>> getExerciciosDoTreino(long treinoId) {
        return treinoExercicioDao.obterExerciciosDoTreino(treinoId);
    }
    
    public LiveData<List<TreinoExercicio>> getTreinosComExercicio(long exercicioId) {
        return treinoExercicioDao.obterTreinosComExercicio(exercicioId);
    }
    
    public LiveData<Integer> getQuantidadeExerciciosDoTreino(long treinoId) {
        return treinoExercicioDao.contarExerciciosDoTreino(treinoId);
    }
    
    public LiveData<TreinoExercicio> getTreinoExercicio(long treinoId, long exercicioId) {
        return treinoExercicioDao.obterTreinoExercicio(treinoId, exercicioId);
    }
    
    // Método auxiliar para obter a próxima ordem de execução
    public int getProximaOrdemExecucao(long treinoId) {
        // Executar de forma síncrona para obter o resultado imediatamente
        try {
            // Usando CompletableFuture para obter o resultado de forma síncrona
            java.util.concurrent.CompletableFuture<Integer> future = new java.util.concurrent.CompletableFuture<>();
            
            executorService.execute(() -> {
                try {
                    // Obter a última ordem e adicionar 1
                    int ultimaOrdem = treinoExercicioDao.obterUltimaOrdemExecucao(treinoId);
                    future.complete(ultimaOrdem + 1);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            return future.get(); // Aguarda a conclusão e retorna o resultado
        } catch (Exception e) {
            e.printStackTrace();
            return 1; // Valor padrão se houver erro
        }
    }
    
    // Métodos para operações CRUD
    public void inserir(TreinoExercicio treinoExercicio) {
        executorService.execute(() -> treinoExercicioDao.inserir(treinoExercicio));
    }
    
    public void atualizar(TreinoExercicio treinoExercicio) {
        executorService.execute(() -> treinoExercicioDao.atualizar(treinoExercicio));
    }
    
    public void deletar(TreinoExercicio treinoExercicio) {
        executorService.execute(() -> treinoExercicioDao.deletar(treinoExercicio));
    }
    
    public void atualizarSeries(long id, int series) {
        executorService.execute(() -> treinoExercicioDao.atualizarSeries(id, series));
    }
    
    public void atualizarRepeticoes(long id, int repeticoes) {
        executorService.execute(() -> treinoExercicioDao.atualizarRepeticoes(id, repeticoes));
    }
    
    public void atualizarIntervalo(long id, int intervaloSegundos) {
        executorService.execute(() -> treinoExercicioDao.atualizarIntervalo(id, intervaloSegundos));
    }
    
    public void removerTodosExerciciosDoTreino(long treinoId) {
        executorService.execute(() -> treinoExercicioDao.removerTodosExerciciosDoTreino(treinoId));
    }
    
    /**
     * Método para fechar o ExecutorService quando não for mais necessário
     * Deve ser chamado no onDestroy() da Activity ou no onCleared() do ViewModel
     */
    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
