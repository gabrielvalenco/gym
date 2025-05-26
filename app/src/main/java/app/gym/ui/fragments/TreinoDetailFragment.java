package app.gym.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import app.gym.R;
import app.gym.data.entities.Treino;
import app.gym.ui.viewmodels.TreinoViewModel;

/**
 * Fragmento para criar ou editar um treino
 */
public class TreinoDetailFragment extends Fragment {

    private TreinoViewModel viewModel;
    private Treino currentTreino;
    private long treinoId = -1;
    private boolean isEditMode = false;

    // UI Components
    private TextView tvTreinoTitulo;
    private TextInputLayout tilNome;
    private TextInputEditText etNome;
    private TextInputLayout tilDescricao;
    private TextInputEditText etDescricao;
    private TextInputLayout tilObjetivo;
    private AutoCompleteTextView dropdownObjetivo;
    private TextInputLayout tilNivelDificuldade;
    private AutoCompleteTextView dropdownNivelDificuldade;
    private TextInputLayout tilDuracao;
    private TextInputEditText etDuracao;
    private SwitchMaterial switchAtivo;
    private TextView tvExerciciosTitulo;
    private TextView tvExerciciosSubtitulo;
    private Button btnSalvar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treino_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter argumentos (ID do treino)
        if (getArguments() != null) {
            treinoId = getArguments().getLong("treinoId", -1L);
            isEditMode = treinoId != -1;
        }

        // Inicializar componentes da UI
        initViews(view);

        // Configurar os dropdowns
        setupDropdowns();

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(TreinoViewModel.class);

        // Carregar dados do treino se estiver no modo de edição
        if (isEditMode) {
            loadTreinoData();
            tvTreinoTitulo.setText("Editar Treino");
            tvExerciciosSubtitulo.setText("Exercícios incluídos neste treino");
        } else {
            // Inicializar um novo treino
            currentTreino = new Treino("", "", "", "", 0);
            tvTreinoTitulo.setText("Novo Treino");
            tvExerciciosSubtitulo.setText("Salve o treino para adicionar exercícios");
        }

        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        tvTreinoTitulo = view.findViewById(R.id.tv_treino_titulo);
        tilNome = view.findViewById(R.id.til_nome);
        etNome = view.findViewById(R.id.et_nome);
        tilDescricao = view.findViewById(R.id.til_descricao);
        etDescricao = view.findViewById(R.id.et_descricao);
        tilObjetivo = view.findViewById(R.id.til_objetivo);
        dropdownObjetivo = view.findViewById(R.id.dropdown_objetivo);
        tilNivelDificuldade = view.findViewById(R.id.til_nivel_dificuldade);
        dropdownNivelDificuldade = view.findViewById(R.id.dropdown_nivel_dificuldade);
        tilDuracao = view.findViewById(R.id.til_duracao);
        etDuracao = view.findViewById(R.id.et_duracao);
        switchAtivo = view.findViewById(R.id.switch_ativo);
        tvExerciciosTitulo = view.findViewById(R.id.tv_exercicios_titulo);
        tvExerciciosSubtitulo = view.findViewById(R.id.tv_exercicios_subtitulo);
        btnSalvar = view.findViewById(R.id.btn_salvar);
    }

    private void setupDropdowns() {
        // Objetivo
        String[] objetivos = {"Hipertrofia", "Perda de peso", "Resistência", "Definição", "Reabilitação", "Condicionamento"};
        ArrayAdapter<String> adapterObjetivo = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                objetivos
        );
        dropdownObjetivo.setAdapter(adapterObjetivo);

        // Nível de Dificuldade
        String[] niveis = {"Iniciante", "Intermediário", "Avançado"};
        ArrayAdapter<String> adapterNivel = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                niveis
        );
        dropdownNivelDificuldade.setAdapter(adapterNivel);
    }

    private void loadTreinoData() {
        viewModel.getTreinoPorId(treinoId).observe(getViewLifecycleOwner(), treino -> {
            if (treino != null) {
                currentTreino = treino;
                // Preencher os campos do formulário
                etNome.setText(treino.getNome());
                etDescricao.setText(treino.getDescricao());
                dropdownObjetivo.setText(treino.getObjetivo(), false);
                dropdownNivelDificuldade.setText(treino.getNivelDificuldade(), false);
                etDuracao.setText(String.valueOf(treino.getDuracaoEstimadaMinutos()));
                switchAtivo.setChecked(treino.isAtivo());
            }
        });
    }

    private void setupListeners() {
        // Botão Salvar
        btnSalvar.setOnClickListener(v -> saveTreino());
    }

    private void saveTreino() {
        // Validar campos
        if (validateForm()) {
            try {
                // Atualizar objeto Treino com os dados do formulário
                currentTreino.setNome(etNome.getText().toString().trim());
                currentTreino.setDescricao(etDescricao.getText().toString().trim());
                currentTreino.setObjetivo(dropdownObjetivo.getText().toString());
                currentTreino.setNivelDificuldade(dropdownNivelDificuldade.getText().toString());
                
                // Processar duração
                int duracao = 0;
                if (!TextUtils.isEmpty(etDuracao.getText())) {
                    try {
                        duracao = Integer.parseInt(etDuracao.getText().toString());
                    } catch (NumberFormatException e) {
                        // Usar valor padrão 0
                    }
                }
                currentTreino.setDuracaoEstimadaMinutos(duracao);
                
                // Se for novo treino, definir a data de criação
                if (!isEditMode) {
                    currentTreino.setDataCriacao(new Date());
                }
                
                currentTreino.setAtivo(switchAtivo.isChecked());

                // Salvar treino
                if (isEditMode) {
                    viewModel.atualizar(currentTreino);
                    Snackbar.make(requireView(), "Treino atualizado com sucesso", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.inserir(currentTreino);
                    Bundle args = new Bundle();
                    args.putLong("treinoId", treinoId);
                    Navigation.findNavController(requireView()).navigate(
                        R.id.action_treinoDetailFragment_to_treinoExerciciosFragment, args);
                    Snackbar.make(requireView(), "Treino criado com sucesso", Snackbar.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Snackbar.make(requireView(), "Erro ao salvar: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validar nome
        if (TextUtils.isEmpty(etNome.getText())) {
            tilNome.setError("Nome é obrigatório");
            isValid = false;
        } else {
            tilNome.setError(null);
        }

        // Validar objetivo
        if (TextUtils.isEmpty(dropdownObjetivo.getText())) {
            tilObjetivo.setError("Objetivo é obrigatório");
            isValid = false;
        } else {
            tilObjetivo.setError(null);
        }

        // Validar nível de dificuldade
        if (TextUtils.isEmpty(dropdownNivelDificuldade.getText())) {
            tilNivelDificuldade.setError("Nível de dificuldade é obrigatório");
            isValid = false;
        } else {
            tilNivelDificuldade.setError(null);
        }

        // Validar duração
        if (TextUtils.isEmpty(etDuracao.getText())) {
            tilDuracao.setError("Duração é obrigatória");
            isValid = false;
        } else {
            try {
                int duracao = Integer.parseInt(etDuracao.getText().toString());
                if (duracao <= 0 || duracao > 240) {
                    tilDuracao.setError("Duração inválida (entre 1 e 240 minutos)");
                    isValid = false;
                } else {
                    tilDuracao.setError(null);
                }
            } catch (NumberFormatException e) {
                tilDuracao.setError("Formato inválido");
                isValid = false;
            }
        }

        return isValid;
    }
}
