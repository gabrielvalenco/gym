package app.gym.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import app.gym.R;
import app.gym.data.entities.Usuario;
import app.gym.ui.adapters.UsuarioAdapter;
import app.gym.ui.viewmodels.UsuarioViewModel;

/**
 * Fragmento para exibir a lista de usuários
 */
public class UsuariosFragment extends Fragment implements UsuarioAdapter.OnUsuarioClickListener {

    private UsuarioViewModel viewModel;
    private UsuarioAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptyList;
    private FloatingActionButton fabAddUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuarios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar componentes da UI
        initViews(view);
        
        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        
        // Configurar Adapter
        setupRecyclerView();
        
        // Observar mudanças nos dados
        observeData();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_usuarios);
        tvEmptyList = view.findViewById(R.id.tv_empty_list);
        fabAddUsuario = view.findViewById(R.id.fab_add_usuario);
    }

    private void setupRecyclerView() {
        adapter = new UsuarioAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel.getUsuariosAtivos().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<Usuario> usuarios) {
        if (usuarios != null && !usuarios.isEmpty()) {
            adapter.submitList(usuarios);
            tvEmptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        fabAddUsuario.setOnClickListener(v -> {
            // Navegar para o fragmento de detalhes do usuário com ID -1 (novo usuário)
            Bundle args = new Bundle();
            args.putLong("usuarioId", -1L);
            Navigation.findNavController(v).navigate(
                R.id.action_usuariosFragment_to_usuarioDetailFragment, args);
        });
    }

    @Override
    public void onUsuarioClick(Usuario usuario) {
        // Navegar para o fragmento de detalhes do usuário com o ID do usuário selecionado
        Bundle args = new Bundle();
        args.putLong("usuarioId", usuario.getId());
        Navigation.findNavController(getView()).navigate(
            R.id.action_usuariosFragment_to_usuarioDetailFragment, args);
    }
}
