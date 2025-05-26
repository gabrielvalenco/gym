package app.gym.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import app.gym.data.AppDatabase;
import app.gym.data.dao.UsuarioDao;
import app.gym.data.entities.Usuario;

/**
 * Repositório para gerenciar operações relacionadas a usuários
 */
public class UsuarioRepository {
    
    private final UsuarioDao usuarioDao;
    private final LiveData<List<Usuario>> todosUsuarios;
    private final LiveData<List<Usuario>> usuariosAtivos;
    private final LiveData<Integer> quantidadeUsuariosAtivos;
    private final LiveData<Integer> quantidadeUsuarios;
    
    public UsuarioRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabase(application);
        usuarioDao = database.usuarioDao();
        todosUsuarios = usuarioDao.obterTodos();
        usuariosAtivos = usuarioDao.obterTodosAtivos();
        quantidadeUsuariosAtivos = usuarioDao.contarUsuariosAtivos();
        quantidadeUsuarios = usuarioDao.contarTodosUsuarios();
    }
    
    // Métodos para recuperar dados
    public LiveData<List<Usuario>> getTodosUsuarios() {
        return todosUsuarios;
    }
    
    public LiveData<List<Usuario>> getUsuariosAtivos() {
        return usuariosAtivos;
    }
    
    public LiveData<Usuario> getUsuarioPorId(long id) {
        return usuarioDao.obterPorId(id);
    }
    
    public LiveData<Usuario> getUsuarioPorEmail(String email) {
        return usuarioDao.obterPorEmail(email);
    }
    
    public LiveData<Integer> getQuantidadeUsuariosAtivos() {
        return quantidadeUsuariosAtivos;
    }
    
    public LiveData<Integer> getQuantidadeUsuarios() {
        return quantidadeUsuarios;
    }
    
    // Métodos para operações CRUD
    public void inserir(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.inserir(usuario);
        });
    }
    
    public void atualizar(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.atualizar(usuario);
        });
    }
    
    public void deletar(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.deletar(usuario);
        });
    }
    
    public void atualizarStatusAtivo(long id, boolean ativo) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            usuarioDao.atualizarStatusAtivo(id, ativo);
        });
    }
}
