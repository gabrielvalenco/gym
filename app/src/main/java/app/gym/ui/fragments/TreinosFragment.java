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

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import app.gym.R;
import app.gym.data.entities.Treino;
import app.gym.ui.adapters.TreinoAdapter;
import app.gym.ui.viewmodels.TreinoViewModel;

/**
 * Fragmento para exibir a lista de treinos
 */
public class TreinosFragment extends Fragment implements TreinoAdapter.OnTreinoClickListener {

    private TreinoViewModel viewModel;
    private TreinoAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptyList;
    private FloatingActionButton fabAddTreino;
    private ChipGroup chipGroupFiltros;

    private List<Treino> allTreinos = new ArrayList<>();
    private String currentFilter = "Todos";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treinos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar componentes da UI
        initViews(view);
        
        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        
        // Configurar Adapter
        setupRecyclerView();
        
        // Configurar filtros de objetivo
        setupChipGroupFilters();
        
        // Observar mudanças nos dados
        observeData();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_treinos);
        tvEmptyList = view.findViewById(R.id.tv_empty_list);
        fabAddTreino = view.findViewById(R.id.fab_add_treino);
        chipGroupFiltros = view.findViewById(R.id.chip_group_filtros);
    }

    private void setupRecyclerView() {
        adapter = new TreinoAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupChipGroupFilters() {
        chipGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip_todos) {
                currentFilter = "Todos";
                filterTreinos();
            } else if (checkedId == R.id.chip_hipertrofia) {
                currentFilter = "Hipertrofia";
                filterTreinos();
            } else if (checkedId == R.id.chip_perda_peso) {
                currentFilter = "Perda de peso";
                filterTreinos();
            } else if (checkedId == R.id.chip_resistencia) {
                currentFilter = "Resistência";
                filterTreinos();
            } else if (checkedId == R.id.chip_definicao) {
                currentFilter = "Definição";
                filterTreinos();
            }
        });
    }

    private void observeData() {
        viewModel.getTreinosAtivos().observe(getViewLifecycleOwner(), treinos -> {
            allTreinos = treinos;
            filterTreinos();
        });
    }

    private void filterTreinos() {
        if (allTreinos == null || allTreinos.isEmpty()) {
            updateUI(new ArrayList<>());
            return;
        }

        if (currentFilter.equals("Todos")) {
            updateUI(allTreinos);
            return;
        }

        List<Treino> filteredList = new ArrayList<>();
        for (Treino treino : allTreinos) {
            if (treino.getObjetivo().equals(currentFilter)) {
                filteredList.add(treino);
            }
        }
        updateUI(filteredList);
    }

    private void updateUI(List<Treino> treinos) {
        if (treinos != null && !treinos.isEmpty()) {
            adapter.submitList(treinos);
            tvEmptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        fabAddTreino.setOnClickListener(v -> {
            // Navegar para o fragmento de detalhes do treino com ID -1 (novo treino)
            Bundle args = new Bundle();
            args.putLong("treinoId", -1L);
            Navigation.findNavController(v).navigate(
                R.id.action_treinosFragment_to_treinoDetailFragment, args);
        });
    }

    @Override
    public void onTreinoClick(Treino treino) {
        // Navegar para o fragmento de detalhes do treino com o ID do treino selecionado
        Bundle args = new Bundle();
        args.putLong("treinoId", treino.getId());
        Navigation.findNavController(getView()).navigate(
            R.id.action_treinosFragment_to_treinoDetailFragment, args);
    }
}
