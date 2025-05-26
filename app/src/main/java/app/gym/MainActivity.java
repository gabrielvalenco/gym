package app.gym;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Configurar ActionBarDrawerToggle (botão hambúrguer)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Configurar Navigation Component com tratamento de exceções
        try {
            navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment, R.id.usuariosFragment, R.id.exerciciosFragment, R.id.treinosFragment)
                    .setOpenableLayout(drawerLayout)
                    .build();

            // Conectar componentes de navegação
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
            
            // REIMPLEMENTAÇÃO COMPLETA DO MENU
            // Remover configuração automática para evitar conflitos
            navigationView.setNavigationItemSelectedListener(null);
            
            // Implementar nosso próprio listener mais direto
            navigationView.setNavigationItemSelectedListener(menuItem -> {
                try {
                    // Log para depuração
                    android.util.Log.d("MainActivity", "Menu item clicado: " + menuItem.getTitle());
                    
                    // Obter o ID do item clicado
                    int itemId = menuItem.getItemId();
                    android.util.Log.d("MainActivity", "ID do item: " + itemId);
                    
                    // Fazer a navegação diretamente usando IDs específicos em vez de R.id
                    Bundle emptyBundle = new Bundle();
                    boolean navegado = false;
                    
                    // Mapear diretamente os IDs e usar NavOptions para garantir comportamento adequado
                    androidx.navigation.NavOptions navOptions = new androidx.navigation.NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
                            .build();
                    
                    // Navegar com base no ID
                    if (itemId == R.id.homeFragment) {
                        navController.navigate(R.id.homeFragment, emptyBundle, navOptions);
                        navegado = true;
                    } else if (itemId == R.id.usuariosFragment) {
                        navController.navigate(R.id.usuariosFragment, emptyBundle, navOptions);
                        navegado = true;
                    } else if (itemId == R.id.exerciciosFragment) {
                        navController.navigate(R.id.exerciciosFragment, emptyBundle, navOptions);
                        navegado = true;
                    } else if (itemId == R.id.treinosFragment) {
                        navController.navigate(R.id.treinosFragment, emptyBundle, navOptions);
                        navegado = true;
                    }
                    
                    // Verificar se a navegação foi bem-sucedida
                    if (navegado) {
                        // Fechar o drawer imediatamente após navegar com sucesso
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    } else {
                        android.util.Log.e("MainActivity", "Menu item não tratado: " + menuItem.getTitle());
                        return false;
                    }
                } catch (Exception e) {
                    android.util.Log.e("MainActivity", "Erro ao processar clique no menu: " + e.getMessage(), e);
                    android.widget.Toast.makeText(MainActivity.this, "Erro ao navegar: " + e.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            
            // Adicionar listener no drawer para debug
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull android.view.View drawerView, float slideOffset) {}
                
                @Override
                public void onDrawerOpened(@NonNull android.view.View drawerView) {
                    android.util.Log.d("MainActivity", "Drawer aberto");
                }
                
                @Override
                public void onDrawerClosed(@NonNull android.view.View drawerView) {
                    android.util.Log.d("MainActivity", "Drawer fechado");
                }
                
                @Override
                public void onDrawerStateChanged(int newState) {}
            });
            
        } catch (Exception e) {
            // Log do erro
            android.util.Log.e("MainActivity", "Erro ao configurar a navegação: " + e.getMessage(), e);
            // Mostrar mensagem para o usuário
            android.widget.Toast.makeText(this, "Erro ao inicializar: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // Fechar o drawer se estiver aberto ao pressionar o botão voltar
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}