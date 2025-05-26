package app.gym.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import app.gym.data.AppDatabase;
import app.gym.data.dao.UsuarioTreinoDao;
import app.gym.data.entities.UsuarioTreino;

/**
 * Repositório para gerenciar operações relacionadas às relações entre usuários e treinos
 */
public class UsuarioTreinoRepository {
    
    private final UsuarioTreinoDao usuarioTreinoDao;
    
    public UsuarioTreinoRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        usuarioTreinoDao = database.usuarioTreinoDao();
    }
    
    // Métodos para recuperar dados
    public LiveData<UsuarioTreino> getUsuarioTreinoPorId(long id) {
        return usuarioTreinoDao.obterPorId(id);
    }
    
    public LiveData<List<UsuarioTreino>> getTreinosDoUsuario(long usuarioId) {
        return usuarioTreinoDao.obterTreinosDoUsuario(usuarioId);
    }
    
    public LiveData<List<UsuarioTreino>> getUsuariosDoTreino(long treinoId) {
        return usuarioTreinoDao.obterUsuariosDoTreino(treinoId);
    }
    
    public LiveData<UsuarioTreino> getUsuarioTreino(long usuarioId, long treinoId) {
        return usuarioTreinoDao.obterUsuarioTreino(usuarioId, treinoId);
    }
    
    public LiveData<Integer> getQuantidadeTreinosAtivosDoUsuario(long usuarioId) {
        return usuarioTreinoDao.contarTreinosAtivosDoUsuario(usuarioId);
    }
    
    // Métodos para operações CRUD
    public void inserir(UsuarioTreino usuarioTreino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.inserir(usuarioTreino);
        });
    }
    
    public void atualizar(UsuarioTreino usuarioTreino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.atualizar(usuarioTreino);
        });
    }
    
    public void deletar(UsuarioTreino usuarioTreino) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.deletar(usuarioTreino);
        });
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.atualizarStatusAtivo(id, ativo);
        });
    }
    
    public void atualizarStatusProgresso(long id, String statusProgresso) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.atualizarStatusProgresso(id, statusProgresso);
        });
    }
    
    public void finalizarTreino(long id) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioTreinoDao.finalizarTreino(id, new Date().getTime());
        });
    }
}
