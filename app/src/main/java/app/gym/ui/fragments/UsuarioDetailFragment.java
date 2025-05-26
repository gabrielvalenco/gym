package app.gym.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.gym.R;
import app.gym.data.entities.Usuario;
import app.gym.ui.viewmodels.UsuarioViewModel;

/**
 * Fragmento para criar ou editar um usuário
 */
public class UsuarioDetailFragment extends Fragment {

    private UsuarioViewModel viewModel;
    private Usuario currentUsuario;
    private long usuarioId = -1;
    private boolean isEditMode = false;

    // UI Components
    private TextInputLayout tilNome;
    private TextInputEditText etNome;
    private TextInputLayout tilEmail;
    private TextInputEditText etEmail;
    private TextInputLayout tilTelefone;
    private TextInputEditText etTelefone;
    private TextInputLayout tilDataNascimento;
    private TextInputEditText etDataNascimento;
    private TextInputLayout tilGenero;
    private AutoCompleteTextView dropdownGenero;
    private SwitchMaterial switchAtivo;
    private Button btnPerfilFisico;
    private Button btnTreinos;
    private Button btnSalvar;

    // Formato de data
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usuario_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obter argumentos (ID do usuário)
        if (getArguments() != null) {
            usuarioId = getArguments().getLong("usuarioId", -1L);
            isEditMode = usuarioId != -1;
        }

        // Inicializar componentes da UI
        initViews(view);

        // Configurar o dropdown de gênero
        setupGenderDropdown();

        // Configurar datepicker para a data de nascimento
        setupDatePicker();

        // Configurar ViewModel
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        // Carregar dados do usuário se estiver no modo de edição
        if (isEditMode) {
            loadUserData();
            // Habilitar botões para perfil físico e treinos
            btnPerfilFisico.setEnabled(true);
            btnTreinos.setEnabled(true);
        } else {
            // Inicializar um novo usuário
            currentUsuario = new Usuario("", "", "", null, "");
            // Desabilitar botões para perfil físico e treinos
            btnPerfilFisico.setEnabled(false);
            btnTreinos.setEnabled(false);
        }

        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        tilNome = view.findViewById(R.id.til_nome);
        etNome = view.findViewById(R.id.et_nome);
        tilEmail = view.findViewById(R.id.til_email);
        etEmail = view.findViewById(R.id.et_email);
        tilTelefone = view.findViewById(R.id.til_telefone);
        etTelefone = view.findViewById(R.id.et_telefone);
        tilDataNascimento = view.findViewById(R.id.til_data_nascimento);
        etDataNascimento = view.findViewById(R.id.et_data_nascimento);
        tilGenero = view.findViewById(R.id.til_genero);
        dropdownGenero = view.findViewById(R.id.dropdown_genero);
        switchAtivo = view.findViewById(R.id.switch_ativo);
        btnPerfilFisico = view.findViewById(R.id.btn_perfil_fisico);
        btnTreinos = view.findViewById(R.id.btn_treinos);
        btnSalvar = view.findViewById(R.id.btn_salvar);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Masculino", "Feminino", "Outro", "Prefiro não informar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                genders
        );
        dropdownGenero.setAdapter(adapter);
    }

    private void setupDatePicker() {
        etDataNascimento.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Se já houver uma data selecionada, usar essa data no DatePicker
            if (!TextUtils.isEmpty(etDataNascimento.getText())) {
                try {
                    Date date = dateFormat.parse(etDataNascimento.getText().toString());
                    if (date != null) {
                        calendar.setTime(date);
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DAY_OF_MONTH);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        etDataNascimento.setText(dateFormat.format(calendar.getTime()));
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void loadUserData() {
        viewModel.getUsuarioPorId(usuarioId).observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                currentUsuario = usuario;
                // Preencher os campos do formulário
                etNome.setText(usuario.getNome());
                etEmail.setText(usuario.getEmail());
                etTelefone.setText(usuario.getTelefone());
                if (usuario.getDataNascimento() != null) {
                    etDataNascimento.setText(dateFormat.format(usuario.getDataNascimento()));
                }
                dropdownGenero.setText(usuario.getGenero(), false);
                switchAtivo.setChecked(usuario.isAtivo());
            }
        });
    }

    private void setupListeners() {
        // Botão Salvar
        btnSalvar.setOnClickListener(v -> saveUsuario());

        // Botão Perfil Físico
        btnPerfilFisico.setOnClickListener(v -> {
            if (isEditMode) {
                Bundle args = new Bundle();
                args.putLong("usuarioId", usuarioId);
                Navigation.findNavController(v).navigate(
                    R.id.action_usuarioDetailFragment_to_perfilFisicoFragment, args);
            }
        });

        // Botão Treinos
        btnTreinos.setOnClickListener(v -> {
            if (isEditMode) {
                Bundle args = new Bundle();
                args.putLong("usuarioId", usuarioId);
                Navigation.findNavController(v).navigate(
                    R.id.action_usuarioDetailFragment_to_usuarioTreinosFragment, args);
            }
        });
    }

    private void saveUsuario() {
        // Validar campos
        if (validateForm()) {
            try {
                // Atualizar objeto Usuario com os dados do formulário
                currentUsuario.setNome(etNome.getText().toString().trim());
                currentUsuario.setEmail(etEmail.getText().toString().trim());
                currentUsuario.setTelefone(etTelefone.getText().toString().trim());
                
                // Processar data de nascimento
                String dataNascString = etDataNascimento.getText().toString().trim();
                if (!TextUtils.isEmpty(dataNascString)) {
                    try {
                        Date dataNasc = dateFormat.parse(dataNascString);
                        currentUsuario.setDataNascimento(dataNasc);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                
                currentUsuario.setGenero(dropdownGenero.getText().toString());
                currentUsuario.setAtivo(switchAtivo.isChecked());

                // Salvar usuário
                if (isEditMode) {
                    viewModel.atualizar(currentUsuario);
                    Snackbar.make(requireView(), "Usuário atualizado com sucesso", Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.inserir(currentUsuario);
                    Snackbar.make(requireView(), "Usuário criado com sucesso", Snackbar.LENGTH_SHORT).show();
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

        // Validar email
        if (TextUtils.isEmpty(etEmail.getText())) {
            tilEmail.setError("Email é obrigatório");
            isValid = false;
        } else if (!isValidEmail(etEmail.getText().toString())) {
            tilEmail.setError("Email inválido");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        return isValid;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
