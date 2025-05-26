package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.entities.UsuarioTreino;
import app.gym.data.repositories.UsuarioTreinoRepository;

/**
 * ViewModel para operações relacionadas às relações entre usuários e treinos
 */
public class UsuarioTreinoViewModel extends AndroidViewModel {
    
    private final UsuarioTreinoRepository repository;
    
    public UsuarioTreinoViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioTreinoRepository(application);
    }
    
    // Métodos para acesso aos dados
    public LiveData<UsuarioTreino> getUsuarioTreinoPorId(long id) {
        return repository.getUsuarioTreinoPorId(id);
    }
    
    public LiveData<List<UsuarioTreino>> getTreinosDoUsuario(long usuarioId) {
        return repository.getTreinosDoUsuario(usuarioId);
    }
    
    public LiveData<List<UsuarioTreino>> getUsuariosDoTreino(long treinoId) {
        return repository.getUsuariosDoTreino(treinoId);
    }
    
    public LiveData<UsuarioTreino> getUsuarioTreino(long usuarioId, long treinoId) {
        return repository.getUsuarioTreino(usuarioId, treinoId);
    }
    
    public LiveData<Integer> getQuantidadeTreinosAtivosDoUsuario(long usuarioId) {
        return repository.getQuantidadeTreinosAtivosDoUsuario(usuarioId);
    }
    
    // Métodos para operações CRUD
    public void inserir(UsuarioTreino usuarioTreino) {
        repository.inserir(usuarioTreino);
    }
    
    public void atualizar(UsuarioTreino usuarioTreino) {
        repository.atualizar(usuarioTreino);
    }
    
    public void deletar(UsuarioTreino usuarioTreino) {
        repository.deletar(usuarioTreino);
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        repository.atualizarStatusAtivo(id, ativo);
    }
    
    public void atualizarStatusProgresso(long id, String statusProgresso) {
        repository.atualizarStatusProgresso(id, statusProgresso);
    }
    
    public void finalizarTreino(long id) {
        repository.finalizarTreino(id);
    }
}
