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
import app.gym.data.entities.Exercicio;
import app.gym.ui.adapters.ExercicioAdapter;
import app.gym.ui.viewmodels.ExercicioViewModel;

/**
 * Fragmento para exibir a lista de exercícios
 */
public class ExerciciosFragment extends Fragment implements ExercicioAdapter.OnExercicioClickListener {

    private ExercicioViewModel viewModel;
    private ExercicioAdapter adapter;
    private RecyclerView recyclerView;
    private TextView tvEmptyList;
    private FloatingActionButton fabAddExercicio;
    private ChipGroup chipGroupFiltros;

    private List<Exercicio> allExercicios = new ArrayList<>();
    private String currentFilter = "Todos";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercicios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar componentes da UI
        initViews(view);
        
        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(ExercicioViewModel.class);
        
        // Configurar Adapter
        setupRecyclerView();
        
        // Configurar filtros de grupo muscular
        setupChipGroupFilters();
        
        // Observar mudanças nos dados
        observeData();
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_exercicios);
        tvEmptyList = view.findViewById(R.id.tv_empty_list);
        fabAddExercicio = view.findViewById(R.id.fab_add_exercicio);
        chipGroupFiltros = view.findViewById(R.id.chip_group_filtros);
    }

    private void setupRecyclerView() {
        adapter = new ExercicioAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void setupChipGroupFilters() {
        chipGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip_todos) {
                currentFilter = "Todos";
                filterExercicios();
            } else if (checkedId == R.id.chip_peito) {
                currentFilter = "Peito";
                filterExercicios();
            } else if (checkedId == R.id.chip_costas) {
                currentFilter = "Costas";
                filterExercicios();
            } else if (checkedId == R.id.chip_pernas) {
                currentFilter = "Pernas";
                filterExercicios();
            } else if (checkedId == R.id.chip_bracos) {
                currentFilter = "Braços";
                filterExercicios();
            } else if (checkedId == R.id.chip_abdomen) {
                currentFilter = "Abdômen";
                filterExercicios();
            }
        });
    }

    private void observeData() {
        viewModel.getExerciciosAtivos().observe(getViewLifecycleOwner(), exercicios -> {
            allExercicios = exercicios;
            filterExercicios();
        });
    }

    private void filterExercicios() {
        if (allExercicios == null || allExercicios.isEmpty()) {
            updateUI(new ArrayList<>());
            return;
        }

        if (currentFilter.equals("Todos")) {
            updateUI(allExercicios);
            return;
        }

        List<Exercicio> filteredList = new ArrayList<>();
        for (Exercicio exercicio : allExercicios) {
            if (exercicio.getGrupoMuscular().equals(currentFilter)) {
                filteredList.add(exercicio);
            }
        }
        updateUI(filteredList);
    }

    private void updateUI(List<Exercicio> exercicios) {
        if (exercicios != null && !exercicios.isEmpty()) {
            adapter.submitList(exercicios);
            tvEmptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        fabAddExercicio.setOnClickListener(v -> {
            // Navegar para o fragmento de detalhes do exercício com ID -1 (novo exercício)
            Bundle args = new Bundle();
            args.putLong("exercicioId", -1L);
            Navigation.findNavController(v).navigate(
                R.id.action_exerciciosFragment_to_exercicioDetailFragment, args);
        });
    }

    @Override
    public void onExercicioClick(Exercicio exercicio) {
        // Navegar para o fragmento de detalhes do exercício com o ID do exercício selecionado
        Bundle args = new Bundle();
        args.putLong("exercicioId", exercicio.getId());
        Navigation.findNavController(getView()).navigate(
            R.id.action_exerciciosFragment_to_exercicioDetailFragment, args);
    }
}
