package app.gym;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe de aplicação para inicialização global
 */
public class GymApplication extends Application {
    
    private static final String TAG = "GymApplication";
    private static Context appContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        
        // Registrar o handler de exceções na inicialização do app
        registerExceptionHandler();
        
        // Log para debug
        logAppInfo();
    }
    
    /**
     * Registra o handler global de exceções não capturadas
     */
    private void registerExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            try {
                // Log detalhado do erro
                String logMessage = "Erro não tratado: " + throwable.getMessage();
                Log.e(TAG, logMessage, throwable);
                
                // Salvar stack trace completo em arquivo
                saveExceptionToFile(throwable);
                
                // Mostrar mensagem ao usuário
                Toast.makeText(appContext, 
                    "Ocorreu um erro inesperado: " + throwable.getMessage(), 
                    Toast.LENGTH_LONG).show();
                
                // Aguardar para que o Toast seja exibido
                Thread.sleep(2000);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao processar exceção não tratada", e);
            }
        });
    }
    
    /**
     * Salva informações da exceção em um arquivo de log
     */
    private void saveExceptionToFile(Throwable throwable) {
        try {
            // Preparar o diretório para os logs
            File logDir = new File(getExternalFilesDir(null), "crash_logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // Nome do arquivo com timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File logFile = new File(logDir, "crash_" + timestamp + ".txt");
            
            // Escrever informações detalhadas no arquivo
            FileWriter writer = new FileWriter(logFile);
            writer.append("Timestamp: ").append(timestamp).append("\n\n");
            writer.append("Device: ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n");
            writer.append("Android: ").append(Build.VERSION.RELEASE).append(" (SDK ").append(String.valueOf(Build.VERSION.SDK_INT)).append(")\n\n");
            
            // Stack trace completo
            writer.append("Stack trace:\n");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            writer.append(sw.toString());
            
            writer.flush();
            writer.close();
            
            Log.i(TAG, "Arquivo de log de crash salvo em: " + logFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar arquivo de log", e);
        }
    }
    
    /**
     * Registra informações sobre o app e o ambiente
     */
    private void logAppInfo() {
        Log.i(TAG, "======== Iniciando GYM Fitness ========");
        Log.i(TAG, "Device: " + Build.MANUFACTURER + " " + Build.MODEL);
        Log.i(TAG, "Android: " + Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")");
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            int versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            Log.i(TAG, "App Version: " + versionName + " (" + versionCode + ")");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao obter informações do pacote", e);
        }
        Log.i(TAG, "====================================");
    }
}
