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
import app.gym.data.entities.Exercicio;
import app.gym.data.entities.Treino;
import app.gym.data.entities.TreinoExercicio;
import app.gym.ui.adapters.TreinoExercicioAdapter;
import app.gym.ui.viewmodels.ExercicioViewModel;
import app.gym.ui.viewmodels.TreinoExercicioViewModel;
import app.gym.ui.viewmodels.TreinoViewModel;

/**
 * Fragmento para exibir os exercícios de um treino específico
 */
public class TreinoExerciciosFragment extends Fragment implements TreinoExercicioAdapter.TreinoExercicioListener {

    private TreinoViewModel treinoViewModel;
    private ExercicioViewModel exercicioViewModel;
    private TreinoExercicioViewModel treinoExercicioViewModel;
    private TreinoExercicioAdapter adapter;
    
    private TextView tvTreinoTitulo;
    private TextView tvEmptyList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddExercicio;

    private long treinoId = -1;
    private Treino currentTreino;
    
    private List<TreinoExercicio> treinoExercicios = new ArrayList<>();
    private List<Exercicio> allExercicios = new ArrayList<>();
    private List<TreinoExercicioAdapter.TreinoExercicioWithExercicio> treinoExercicioItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treino_exercicios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter argumentos (ID do treino)
        if (getArguments() != null) {
            treinoId = getArguments().getLong("treinoId", -1L);
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
        tvTreinoTitulo = view.findViewById(R.id.tv_treino_titulo);
        tvEmptyList = view.findViewById(R.id.tv_empty_list);
        recyclerView = view.findViewById(R.id.rv_treino_exercicios);
        fabAddExercicio = view.findViewById(R.id.fab_add_exercicio);
    }

    private void setupViewModels() {
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);
        exercicioViewModel = new ViewModelProvider(this).get(ExercicioViewModel.class);
        treinoExercicioViewModel = new ViewModelProvider(this).get(TreinoExercicioViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new TreinoExercicioAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadData() {
        // Carregar treino
        treinoViewModel.getTreinoPorId(treinoId).observe(getViewLifecycleOwner(), treino -> {
            if (treino != null) {
                currentTreino = treino;
                tvTreinoTitulo.setText("Exercícios do Treino: " + treino.getNome());
            }
        });
        
        // Carregar todos os exercícios disponíveis
        exercicioViewModel.getTodosExercicios().observe(getViewLifecycleOwner(), exercicios -> {
            if (exercicios != null) {
                allExercicios = exercicios;
                updateTreinoExercicioItems();
            }
        });
        
        // Carregar exercícios do treino
        treinoExercicioViewModel.getExerciciosDoTreino(treinoId).observe(getViewLifecycleOwner(), exerciciosDoTreino -> {
            if (exerciciosDoTreino != null) {
                treinoExercicios = exerciciosDoTreino;
                updateTreinoExercicioItems();
            }
        });
    }

    private void updateTreinoExercicioItems() {
        // Criar lista combinada de TreinoExercicio e Exercicio
        List<TreinoExercicioAdapter.TreinoExercicioWithExercicio> items = new ArrayList<>();
        
        for (TreinoExercicio treinoExercicio : treinoExercicios) {
            for (Exercicio exercicio : allExercicios) {
                if (exercicio.getId() == treinoExercicio.getExercicioId()) {
                    items.add(new TreinoExercicioAdapter.TreinoExercicioWithExercicio(treinoExercicio, exercicio));
                    break;
                }
            }
        }
        
        treinoExercicioItems = items;
        updateUI();
    }

    private void updateUI() {
        if (treinoExercicioItems.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.submitList(treinoExercicioItems);
        }
    }

    private void setupListeners() {
        fabAddExercicio.setOnClickListener(v -> {
            showAddExercicioDialog();
        });
    }

    private void showAddExercicioDialog() {
        // Verificar se há exercícios disponíveis
        if (allExercicios.isEmpty()) {
            Snackbar.make(requireView(), "Não há exercícios disponíveis para adicionar.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Criar lista de nomes de exercícios para o diálogo
        List<String> exercicioNames = new ArrayList<>();
        List<Exercicio> availableExercicios = new ArrayList<>();
        
        // Filtrar exercícios que o treino ainda não possui
        for (Exercicio exercicio : allExercicios) {
            boolean alreadyAssigned = false;
            for (TreinoExercicio te : treinoExercicios) {
                if (te.getExercicioId() == exercicio.getId()) {
                    alreadyAssigned = true;
                    break;
                }
            }
            
            if (!alreadyAssigned) {
                availableExercicios.add(exercicio);
                exercicioNames.add(exercicio.getNome());
            }
        }
        
        if (availableExercicios.isEmpty()) {
            Snackbar.make(requireView(), "O treino já possui todos os exercícios disponíveis.", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Mostrar diálogo para selecionar o exercício
        String[] exercicioNamesArray = exercicioNames.toArray(new String[0]);
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Selecione um Exercício")
                .setItems(exercicioNamesArray, (dialog, which) -> {
                    Exercicio selectedExercicio = availableExercicios.get(which);
                    showSeriesRepeticoesDialog(selectedExercicio);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showSeriesRepeticoesDialog(Exercicio exercicio) {
        // Criar View para o diálogo com os campos de séries e repetições
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_series_repeticoes, null);
        
        // Configurar diálogo para séries, repetições e intervalo
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Configurar " + exercicio.getNome())
                .setView(dialogView)
                .setPositiveButton("Adicionar", (dialog, which) -> {
                    // Obter valores dos campos
                    int series = getIntFromTextView(dialogView.findViewById(R.id.et_series), 3);
                    int repeticoes = getIntFromTextView(dialogView.findViewById(R.id.et_repeticoes), 12);
                    int intervalo = getIntFromTextView(dialogView.findViewById(R.id.et_intervalo), 60);
                    
                    // Obter próxima ordem de execução
                    int ordem = treinoExercicioViewModel.getProximaOrdemExecucao(treinoId);
                    
                    // Criar novo TreinoExercicio
                    TreinoExercicio treinoExercicio = new TreinoExercicio(treinoId, exercicio.getId(), ordem, series, repeticoes, intervalo);
                    
                    // Salvar no banco de dados
                    treinoExercicioViewModel.inserir(treinoExercicio);
                    
                    Snackbar.make(requireView(), "Exercício adicionado com sucesso", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private int getIntFromTextView(View view, int defaultValue) {
        if (view instanceof android.widget.EditText) {
            android.widget.EditText editText = (android.widget.EditText) view;
            try {
                String text = editText.getText().toString().trim();
                if (!text.isEmpty()) {
                    return Integer.parseInt(text);
                }
            } catch (NumberFormatException e) {
                // Ignorar e retornar valor padrão
            }
        }
        return defaultValue;
    }

    @Override
    public void onTreinoExercicioClick(TreinoExercicio treinoExercicio) {
        // Ao clicar em um item, exibir detalhes ou instruções (implementação futura)
        Snackbar.make(requireView(), "Exercício selecionado: ID " + treinoExercicio.getId(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onEditTreinoExercicio(long treinoExercicioId) {
        // Obter o objeto TreinoExercicio correspondente
        TreinoExercicio treinoExercicio = null;
        for (TreinoExercicio te : treinoExercicios) {
            if (te.getId() == treinoExercicioId) {
                treinoExercicio = te;
                break;
            }
        }
        
        if (treinoExercicio == null) return;
        
        // Obter o Exercicio correspondente
        Exercicio exercicio = null;
        for (Exercicio e : allExercicios) {
            if (e.getId() == treinoExercicio.getExercicioId()) {
                exercicio = e;
                break;
            }
        }
        
        if (exercicio == null) return;
        
        // Criar View para o diálogo com os campos de séries e repetições
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_series_repeticoes, null);
        
        // Preencher com valores atuais
        ((android.widget.EditText) dialogView.findViewById(R.id.et_series)).setText(String.valueOf(treinoExercicio.getSeries()));
        ((android.widget.EditText) dialogView.findViewById(R.id.et_repeticoes)).setText(String.valueOf(treinoExercicio.getRepeticoes()));
        ((android.widget.EditText) dialogView.findViewById(R.id.et_intervalo)).setText(String.valueOf(treinoExercicio.getIntervaloSegundos()));
        
        // Configurar diálogo para editar séries, repetições e intervalo
        TreinoExercicio finalTreinoExercicio = treinoExercicio;
        Exercicio finalExercicio = exercicio;
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Editar " + exercicio.getNome())
                .setView(dialogView)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    // Obter novos valores dos campos
                    int series = getIntFromTextView(dialogView.findViewById(R.id.et_series), finalTreinoExercicio.getSeries());
                    int repeticoes = getIntFromTextView(dialogView.findViewById(R.id.et_repeticoes), finalTreinoExercicio.getRepeticoes());
                    int intervalo = getIntFromTextView(dialogView.findViewById(R.id.et_intervalo), finalTreinoExercicio.getIntervaloSegundos());
                    
                    // Atualizar objeto
                    finalTreinoExercicio.setSeries(series);
                    finalTreinoExercicio.setRepeticoes(repeticoes);
                    finalTreinoExercicio.setIntervaloSegundos(intervalo);
                    
                    // Salvar no banco de dados
                    treinoExercicioViewModel.atualizar(finalTreinoExercicio);
                    
                    Snackbar.make(requireView(), "Exercício atualizado com sucesso", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDeleteTreinoExercicio(TreinoExercicio treinoExercicio) {
        // Confirmar exclusão
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Remover Exercício")
                .setMessage("Tem certeza que deseja remover este exercício do treino?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Excluir do banco de dados
                    treinoExercicioViewModel.deletar(treinoExercicio);
                    Snackbar.make(requireView(), "Exercício removido com sucesso", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Não", null)
                .show();
    }
}
