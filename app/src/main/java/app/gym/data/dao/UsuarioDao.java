package app.gym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.gym.data.entities.Usuario;

/**
 * DAO para operações com a entidade Usuario
 */
@Dao
public interface UsuarioDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long inserir(Usuario usuario);
    
    @Update
    void atualizar(Usuario usuario);
    
    @Delete
    void deletar(Usuario usuario);
    
    @Query("SELECT * FROM usuarios WHERE id = :id")
    LiveData<Usuario> obterPorId(long id);
    
    @Query("SELECT * FROM usuarios WHERE ativo = 1 ORDER BY nome")
    LiveData<List<Usuario>> obterTodosAtivos();
    
    @Query("SELECT * FROM usuarios ORDER BY nome")
    LiveData<List<Usuario>> obterTodos();
    
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    LiveData<Usuario> obterPorEmail(String email);
    
    @Query("UPDATE usuarios SET ativo = :ativo WHERE id = :id")
    void atualizarStatusAtivo(long id, boolean ativo);
    
    @Query("SELECT COUNT(*) FROM usuarios WHERE ativo = 1")
    LiveData<Integer> contarUsuariosAtivos();
}
