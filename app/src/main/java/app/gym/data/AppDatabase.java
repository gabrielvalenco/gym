package app.gym.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.gym.data.dao.ExercicioDao;
import app.gym.data.dao.PerfilFisicoDao;
import app.gym.data.dao.TreinoDao;
import app.gym.data.dao.TreinoExercicioDao;
import app.gym.data.dao.UsuarioDao;
import app.gym.data.dao.UsuarioTreinoDao;
import app.gym.data.entities.Exercicio;
import app.gym.data.entities.PerfilFisico;
import app.gym.data.entities.Treino;
import app.gym.data.entities.TreinoExercicio;
import app.gym.data.entities.Usuario;
import app.gym.data.entities.UsuarioTreino;
import app.gym.util.DateConverter;

/**
 * Classe de banco de dados que contém as entidades e DAOs do aplicativo
 */
@Database(entities = {
        Usuario.class,
        PerfilFisico.class,
        Exercicio.class,
        Treino.class,
        UsuarioTreino.class,
        TreinoExercicio.class
}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAOs
    public abstract UsuarioDao usuarioDao();
    public abstract PerfilFisicoDao perfilFisicoDao();
    public abstract ExercicioDao exercicioDao();
    public abstract TreinoDao treinoDao();
    public abstract UsuarioTreinoDao usuarioTreinoDao();
    public abstract TreinoExercicioDao treinoExercicioDao();

    // Singleton para acesso ao banco de dados
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "gym_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback para inicializar o banco de dados com dados predefinidos, se necessário
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Preencher banco de dados com dados iniciais quando for criado pela primeira vez
                // Você pode adicionar dados iniciais aqui se desejar
            });
        }
    };
}
