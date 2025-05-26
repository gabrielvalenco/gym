package app.gym.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.entities.Usuario;
import app.gym.data.repositories.UsuarioRepository;

/**
 * ViewModel para operações relacionadas a usuários
 */
public class UsuarioViewModel extends AndroidViewModel {
    
    private final UsuarioRepository repository;
    private final LiveData<List<Usuario>> todosUsuarios;
    private final LiveData<List<Usuario>> usuariosAtivos;
    private final LiveData<Integer> quantidadeUsuariosAtivos;
    private LiveData<Integer> quantidadeUsuarios;
    
    public UsuarioViewModel(@NonNull Application application) {
        super(application);
        repository = new UsuarioRepository(application);
        todosUsuarios = repository.getTodosUsuarios();
        usuariosAtivos = repository.getUsuariosAtivos();
        quantidadeUsuariosAtivos = repository.getQuantidadeUsuariosAtivos();
        quantidadeUsuarios = repository.getQuantidadeUsuarios();
    }
    
    // Métodos para acesso aos dados
    public LiveData<List<Usuario>> getTodosUsuarios() {
        return todosUsuarios;
    }
    
    public LiveData<List<Usuario>> getUsuariosAtivos() {
        return usuariosAtivos;
    }
    
    public LiveData<Usuario> getUsuarioPorId(long id) {
        return repository.getUsuarioPorId(id);
    }
    
    public LiveData<Usuario> getUsuarioPorEmail(String email) {
        return repository.getUsuarioPorEmail(email);
    }
    
    public LiveData<Integer> getQuantidadeUsuariosAtivos() {
        return quantidadeUsuariosAtivos;
    }
    
    public LiveData<Integer> getQuantidadeUsuarios() {
        return quantidadeUsuarios;
    }
    
    // Métodos para operações CRUD
    public void inserir(Usuario usuario) {
        repository.inserir(usuario);
    }
    
    public void atualizar(Usuario usuario) {
        repository.atualizar(usuario);
    }
    
    public void deletar(Usuario usuario) {
        repository.deletar(usuario);
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        repository.atualizarStatusAtivo(id, ativo);
    }
}
