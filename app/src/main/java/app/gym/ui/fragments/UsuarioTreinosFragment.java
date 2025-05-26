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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import app.gym.R;
import app.gym.data.entities.Treino;
import app.gym.data.entities.Usuario;
import app.gym.data.entities.UsuarioTreino;
import app.gym.ui.adapters.UsuarioTreinoAdapter;
import app.gym.ui.viewmodels.TreinoViewModel;
import app.gym.ui.viewmodels.UsuarioTreinoViewModel;
import app.gym.ui.viewmodels.UsuarioViewModel;

/**
 * Fragmento para exibir os treinos de um usuário específico
 */
public class UsuarioTreinosFragment extends Fragment implements UsuarioTreinoAdapter.UsuarioTreinoListener {

    private UsuarioViewModel usuarioViewModel;
    private TreinoViewModel treinoViewModel;
    private UsuarioTreinoViewModel usuarioTreinoViewModel;
    private UsuarioTreinoAdapter adapter;
    
    private TextView tvUsuarioNome;
    private TextView tvEmptyList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddUsuarioTreino;

    private long usuarioId = -1;
    private Usuario currentUsuario;
    
    private List<UsuarioTreino> usuarioTreinos = new ArrayList<>();
    private List<Treino> allTreinos = new ArrayList<>();
    private List<UsuarioTreinoAdapter.UsuarioTreinoWithTreino> usuarioTreinoItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario_treinos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter argumentos (ID do usuário)
        if (getArguments() != null) {
            usuarioId = getArguments().getLong("usuarioId", -1L);
        }

        // Inicializar componentes da UI
        initViews(view);
        
        // Configurar ViewModels
        setupViewModels();
        
        // Configurar RecyclerView
        setupRecyclerView();
        
        // Carregar dados
        loadData();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        tvUsuarioNome = view.findViewById(R.id.tv_usuario_nome);
        tvEmptyList = view.findViewById(R.id.tv_empty_list);
        recyclerView = view.findViewById(R.id.rv_usuario_treinos);
        fabAddUsuarioTreino = view.findViewById(R.id.fab_add_usuario_treino);
    }

    private void setupViewModels() {
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        usuarioTreinoViewModel = new ViewModelProvider(this).get(UsuarioTreinoViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new UsuarioTreinoAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        // Carregar usuário
        usuarioViewModel.getUsuarioPorId(usuarioId).observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                currentUsuario = usuario;
                tvUsuarioNome.setText("Treinos de " + usuario.getNome());
            }
        });
        
        // Carregar todos os treinos disponíveis
        treinoViewModel.getTreinosAtivos().observe(getViewLifecycleOwner(), treinos -> {
            if (treinos != null) {
                allTreinos = treinos;
                updateUsuarioTreinoItems();
            }
        });
        
        // Carregar treinos do usuário
        usuarioTreinoViewModel.getTreinosDoUsuario(usuarioId).observe(getViewLifecycleOwner(), treinosDoUsuario -> {
            if (treinosDoUsuario != null) {
                usuarioTreinos = treinosDoUsuario;
                updateUsuarioTreinoItems();
            }
        });
    }

    private void updateUsuarioTreinoItems() {
        // Criar lista combinada de UsuarioTreino e Treino
        List<UsuarioTreinoAdapter.UsuarioTreinoWithTreino> items = new ArrayList<>();
        
        for (UsuarioTreino usuarioTreino : usuarioTreinos) {
            for (Treino treino : allTreinos) {
                if (treino.getId() == usuarioTreino.getTreinoId()) {
                    items.add(new UsuarioTreinoAdapter.UsuarioTreinoWithTreino(usuarioTreino, treino));
                    break;
                }
            }
        }
        
        usuarioTreinoItems = items;
        updateUI();
    }

    private void updateUI() {
        if (usuarioTreinoItems.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(usuarioTreinoItems);
        }
    }

    private void setupListeners() {
        fabAddUsuarioTreino.setOnClickListener(v -> {
            showAddTreinoDialog();
        });
    }

    private void showAddTreinoDialog() {
        // Verificar se há treinos disponíveis
        if (allTreinos.isEmpty()) {
            Snackbar.make(requireView(), "Não há treinos disponíveis para adicionar.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Criar lista de nomes de treinos para o diálogo
        List<String> treinoNames = new ArrayList<>();
        List<Treino> availableTreinos = new ArrayList<>();
        
        // Filtrar treinos que o usuário ainda não possui
        for (Treino treino : allTreinos) {
            boolean alreadyAssigned = false;
            for (UsuarioTreino ut : usuarioTreinos) {
                if (ut.getTreinoId() == treino.getId()) {
                    alreadyAssigned = true;
                    break;
                }
            }
            
            if (!alreadyAssigned) {
                availableTreinos.add(treino);
                treinoNames.add(treino.getNome());
            }
        }
        
        if (availableTreinos.isEmpty()) {
            Snackbar.make(requireView(), "O usuário já possui todos os treinos disponíveis.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Mostrar diálogo para selecionar o treino
        String[] treinoNamesArray = treinoNames.toArray(new String[0]);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Selecione um Treino")
                .setItems(treinoNamesArray, (dialog, which) -> {
                    Treino selectedTreino = availableTreinos.get(which);
                    showFrequencyDialog(selectedTreino);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showFrequencyDialog(Treino treino) {
        // Opções de frequência semanal
        String[] frequencies = {"1x por semana", "2x por semana", "3x por semana", "4x por semana", "5x por semana"};
        
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Frequência Semanal")
                .setItems(frequencies, (dialog, which) -> {
                    // Frequência semanal (index + 1)
                    int frequency = which + 1;
                    
                    // Criar nova relação UsuarioTreino
                    UsuarioTreino usuarioTreino = new UsuarioTreino(usuarioId, treino.getId(), frequency);
                    
                    // Salvar no banco de dados
                    usuarioTreinoViewModel.inserir(usuarioTreino);
                    
                    Snackbar.make(requireView(), "Treino adicionado com sucesso", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onUsuarioTreinoClick(UsuarioTreino usuarioTreino) {
        // Ao clicar em um item, exibir detalhes (implementação futura)
        Snackbar.make(requireView(), "Treino selecionado: ID " + usuarioTreino.getId(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEditUsuarioTreino(long usuarioTreinoId) {
        // Editar relação (implementação futura)
        Snackbar.make(requireView(), "Editar treino: ID " + usuarioTreinoId, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteUsuarioTreino(UsuarioTreino usuarioTreino) {
        // Confirmar exclusão
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Remover Treino")
                .setMessage("Tem certeza que deseja remover este treino do usuário?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Excluir do banco de dados
                    usuarioTreinoViewModel.deletar(usuarioTreino);
                    Snackbar.make(requireView(), "Treino removido com sucesso", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
