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

import app.gym.R;
import app.gym.data.entities.Exercicio;
import app.gym.ui.viewmodels.ExercicioViewModel;

/**
 * Fragmento para criar ou editar um exercício
 */
public class ExercicioDetailFragment extends Fragment {

    private ExercicioViewModel viewModel;
    private Exercicio currentExercicio;
    private long exercicioId = -1;
    private boolean isEditMode = false;

    // UI Components
    private TextView tvExercicioTitulo;
    private TextInputLayout tilNome;
    private TextInputEditText etNome;
    private TextInputLayout tilDescricao;
    private TextInputEditText etDescricao;
    private TextInputLayout tilGrupoMuscular;
    private AutoCompleteTextView dropdownGrupoMuscular;
    private TextInputLayout tilEquipamento;
    private AutoCompleteTextView dropdownEquipamento;
    private TextInputLayout tilNivelDificuldade;
    private AutoCompleteTextView dropdownNivelDificuldade;
    private TextInputLayout tilInstrucoes;
    private TextInputEditText etInstrucoes;
    private SwitchMaterial switchAtivo;
    private Button btnSalvar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exercicio_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter argumentos (ID do exercício)
        if (getArguments() != null) {
            exercicioId = getArguments().getLong("exercicioId", -1L);
            isEditMode = exercicioId != -1;
        }

        // Inicializar componentes da UI
        initViews(view);

        // Configurar os dropdowns
        setupDropdowns();

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(ExercicioViewModel.class);

        // Carregar dados do exercício se estiver no modo de edição
        if (isEditMode) {
            loadExercicioData();
            tvExercicioTitulo.setText("Editar Exercício");
        } else {
            // Inicializar um novo exercício
            currentExercicio = new Exercicio("", "", "", "", "");
            tvExercicioTitulo.setText("Novo Exercício");
        }

        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        tvExercicioTitulo = view.findViewById(R.id.tv_exercicio_titulo);
        tilNome = view.findViewById(R.id.til_nome);
        etNome = view.findViewById(R.id.et_nome);
        tilDescricao = view.findViewById(R.id.til_descricao);
        etDescricao = view.findViewById(R.id.et_descricao);
        tilGrupoMuscular = view.findViewById(R.id.til_grupo_muscular);
        dropdownGrupoMuscular = view.findViewById(R.id.dropdown_grupo_muscular);
        tilEquipamento = view.findViewById(R.id.til_equipamento);
        dropdownEquipamento = view.findViewById(R.id.dropdown_equipamento);
        tilNivelDificuldade = view.findViewById(R.id.til_nivel_dificuldade);
        dropdownNivelDificuldade = view.findViewById(R.id.dropdown_nivel_dificuldade);
        tilInstrucoes = view.findViewById(R.id.til_instrucoes);
        etInstrucoes = view.findViewById(R.id.et_instrucoes);
        switchAtivo = view.findViewById(R.id.switch_ativo);
        btnSalvar = view.findViewById(R.id.btn_salvar);
    }

    private void setupDropdowns() {
        // Grupo Muscular
        String[] gruposMusculares = {"Peito", "Costas", "Pernas", "Braços", "Abdômen", "Ombros", "Glúteos", "Panturrilha"};
        ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                gruposMusculares
        );
        dropdownGrupoMuscular.setAdapter(adapterGrupo);

        // Equipamento
        String[] equipamentos = {"Peso Corporal", "Halteres", "Barra", "Máquina", "Kettlebell", "Elástico", "Bola Suíça", "TRX"};
        ArrayAdapter<String> adapterEquipamento = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                equipamentos
        );
        dropdownEquipamento.setAdapter(adapterEquipamento);

        // Nível de Dificuldade
        String[] niveis = {"Iniciante", "Intermediário", "Avançado"};
        ArrayAdapter<String> adapterNivel = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                niveis
        );
        dropdownNivelDificuldade.setAdapter(adapterNivel);
    }

    private void loadExercicioData() {
        viewModel.getExercicioPorId(exercicioId).observe(getViewLifecycleOwner(), exercicio -> {
            if (exercicio != null) {
                currentExercicio = exercicio;
                // Preencher os campos do formulário
                etNome.setText(exercicio.getNome());
                etDescricao.setText(exercicio.getDescricao());
                dropdownGrupoMuscular.setText(exercicio.getGrupoMuscular(), false);
                dropdownEquipamento.setText(exercicio.getEquipamento(), false);
                dropdownNivelDificuldade.setText(exercicio.getNivelDificuldade(), false);
                etInstrucoes.setText(exercicio.getInstrucoes());
                switchAtivo.setChecked(exercicio.isAtivo());
            }
        });
    }

    private void setupListeners() {
        // Botão Salvar
        btnSalvar.setOnClickListener(v -> saveExercicio());
    }

    private void saveExercicio() {
        // Validar campos
        if (validateForm()) {
            try {
                // Atualizar objeto Exercicio com os dados do formulário
                currentExercicio.setNome(etNome.getText().toString().trim());
                currentExercicio.setDescricao(etDescricao.getText().toString().trim());
                currentExercicio.setGrupoMuscular(dropdownGrupoMuscular.getText().toString());
                currentExercicio.setEquipamento(dropdownEquipamento.getText().toString());
                currentExercicio.setNivelDificuldade(dropdownNivelDificuldade.getText().toString());
                currentExercicio.setInstrucoes(etInstrucoes.getText().toString().trim());
                currentExercicio.setAtivo(switchAtivo.isChecked());

                // Salvar exercício
                if (isEditMode) {
                    viewModel.atualizar(currentExercicio);
                    Snackbar.make(requireView(), "Exercício atualizado com sucesso", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.inserir(currentExercicio);
                    Snackbar.make(requireView(), "Exercício criado com sucesso", Snackbar.LENGTH_SHORT).show();
                    // Voltar para a tela anterior
                    Navigation.findNavController(requireView()).navigateUp();
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

        // Validar grupo muscular
        if (TextUtils.isEmpty(dropdownGrupoMuscular.getText())) {
            tilGrupoMuscular.setError("Grupo muscular é obrigatório");
            isValid = false;
        } else {
            tilGrupoMuscular.setError(null);
        }

        // Validar equipamento
        if (TextUtils.isEmpty(dropdownEquipamento.getText())) {
            tilEquipamento.setError("Equipamento é obrigatório");
            isValid = false;
        } else {
            tilEquipamento.setError(null);
        }

        // Validar nível de dificuldade
        if (TextUtils.isEmpty(dropdownNivelDificuldade.getText())) {
            tilNivelDificuldade.setError("Nível de dificuldade é obrigatório");
            isValid = false;
        } else {
            tilNivelDificuldade.setError(null);
        }

        return isValid;
    }
}
