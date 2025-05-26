package app.gym.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;

import app.gym.data.AppDatabase;
import app.gym.data.dao.PerfilFisicoDao;
import app.gym.data.entities.PerfilFisico;

/**
 * Repositório para gerenciar operações relacionadas aos perfis físicos dos usuários
 */
public class PerfilFisicoRepository {
    
    private final PerfilFisicoDao perfilFisicoDao;
    
    public PerfilFisicoRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        perfilFisicoDao = database.perfilFisicoDao();
    }
    
    // Métodos para recuperar dados
    public LiveData<PerfilFisico> getPerfilFisicoPorId(long id) {
        return perfilFisicoDao.obterPorId(id);
    }
    
    public LiveData<PerfilFisico> getPerfilFisicoPorUsuarioId(long usuarioId) {
        return perfilFisicoDao.obterPorUsuarioId(usuarioId);
    }
    
    public boolean existePerfilParaUsuario(long usuarioId) {
        // Não pode ser executado na thread principal
        final int[] count = {0};
        Thread thread = new Thread(() -> {
            count[0] = perfilFisicoDao.contarPorUsuarioId(usuarioId);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return count[0] > 0;
    }
    
    // Métodos para operações CRUD
    public void inserir(PerfilFisico perfilFisico) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            perfilFisicoDao.inserir(perfilFisico);
        });
    }
    
    public void atualizar(PerfilFisico perfilFisico) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            perfilFisicoDao.atualizar(perfilFisico);
        });
    }
    
    public void deletar(PerfilFisico perfilFisico) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            perfilFisicoDao.deletar(perfilFisico);
        });
    }
    
    public void atualizarMedidas(long usuarioId, float peso, float altura, float percentualGordura) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            perfilFisicoDao.atualizarMedidas(usuarioId, peso, altura, percentualGordura, new Date().getTime());
        });
    }
}
