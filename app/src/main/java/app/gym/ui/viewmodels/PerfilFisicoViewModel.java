package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import app.gym.data.entities.PerfilFisico;
import app.gym.data.repositories.PerfilFisicoRepository;

/**
 * ViewModel para operações relacionadas aos perfis físicos dos usuários
 */
public class PerfilFisicoViewModel extends AndroidViewModel {
    
    private final PerfilFisicoRepository repository;
    
    public PerfilFisicoViewModel(@NonNull Application application) {
        super(application);
        repository = new PerfilFisicoRepository(application);
    }
    
    // Métodos para acesso aos dados
    public LiveData<PerfilFisico> getPerfilFisicoPorId(long id) {
        return repository.getPerfilFisicoPorId(id);
    }
    
    public LiveData<PerfilFisico> getPerfilFisicoPorUsuarioId(long usuarioId) {
        return repository.getPerfilFisicoPorUsuarioId(usuarioId);
    }
    
    public boolean existePerfilParaUsuario(long usuarioId) {
        return repository.existePerfilParaUsuario(usuarioId);
    }
    
    // Métodos para operações CRUD
    public void inserir(PerfilFisico perfilFisico) {
        repository.inserir(perfilFisico);
    }
    
    public void atualizar(PerfilFisico perfilFisico) {
        repository.atualizar(perfilFisico);
    }
    
    public void deletar(PerfilFisico perfilFisico) {
        repository.deletar(perfilFisico);
    }
    
    public void atualizarMedidas(long usuarioId, float peso, float altura, float percentualGordura) {
        repository.atualizarMedidas(usuarioId, peso, altura, percentualGordura);
    }
}
