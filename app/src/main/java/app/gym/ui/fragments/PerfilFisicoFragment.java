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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.gym.R;
import app.gym.data.entities.PerfilFisico;
import app.gym.data.entities.Usuario;
import app.gym.ui.viewmodels.PerfilFisicoViewModel;
import app.gym.ui.viewmodels.UsuarioViewModel;

/**
 * Fragmento para exibir e editar o perfil físico do usuário
 */
public class PerfilFisicoFragment extends Fragment {

    private PerfilFisicoViewModel perfilViewModel;
    private UsuarioViewModel usuarioViewModel;
    private PerfilFisico currentPerfil;
    private Usuario currentUsuario;
    private long usuarioId = -1;
    private boolean isNewPerfil = false;

    // UI Components
    private TextView tvPerfilTitulo;
    private TextView tvNomeUsuario;
    private TextInputLayout tilAltura;
    private TextInputEditText etAltura;
    private TextInputLayout tilPeso;
    private TextInputEditText etPeso;
    private TextInputLayout tilGordura;
    private TextInputEditText etGordura;
    private TextInputLayout tilObjetivo;
    private AutoCompleteTextView dropdownObjetivo;
    private TextInputLayout tilRestricoes;
    private TextInputEditText etRestricoes;
    private TextView tvImc;
    private TextView tvImcClassificacao;
    private TextView tvDataAtualizacao;
    private Button btnSalvarPerfil;

    // Formato de data
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil_fisico, container, false);
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

        // Configurar o dropdown de objetivos
        setupObjetivosDropdown();

        // Configurar ViewModels
        perfilViewModel = new ViewModelProvider(this).get(PerfilFisicoViewModel.class);
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        // Carregar dados do usuário
        loadUsuarioData();

        // Verificar se o usuário já tem um perfil físico
        checkExistingPerfil();

        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        tvPerfilTitulo = view.findViewById(R.id.tv_perfil_titulo);
        tvNomeUsuario = view.findViewById(R.id.tv_nome_usuario);
        tilAltura = view.findViewById(R.id.til_altura);
        etAltura = view.findViewById(R.id.et_altura);
        tilPeso = view.findViewById(R.id.til_peso);
        etPeso = view.findViewById(R.id.et_peso);
        tilGordura = view.findViewById(R.id.til_gordura);
        etGordura = view.findViewById(R.id.et_gordura);
        tilObjetivo = view.findViewById(R.id.til_objetivo);
        dropdownObjetivo = view.findViewById(R.id.dropdown_objetivo);
        tilRestricoes = view.findViewById(R.id.til_restricoes);
        etRestricoes = view.findViewById(R.id.et_restricoes);
        tvImc = view.findViewById(R.id.tv_imc);
        tvImcClassificacao = view.findViewById(R.id.tv_imc_classificacao);
        tvDataAtualizacao = view.findViewById(R.id.tv_data_atualizacao);
        btnSalvarPerfil = view.findViewById(R.id.btn_salvar_perfil);
    }

    private void setupObjetivosDropdown() {
        String[] objetivos = {"Perda de peso", "Ganho de massa", "Definição muscular", "Condicionamento", "Saúde geral"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_item,
                objetivos
        );
        dropdownObjetivo.setAdapter(adapter);
    }

    private void loadUsuarioData() {
        usuarioViewModel.getUsuarioPorId(usuarioId).observe(getViewLifecycleOwner(), usuario -> {
            if (usuario != null) {
                currentUsuario = usuario;
                tvNomeUsuario.setText(usuario.getNome());
            }
        });
    }

    private void checkExistingPerfil() {
        // Verificar se o usuário já tem um perfil físico
        isNewPerfil = !perfilViewModel.existePerfilParaUsuario(usuarioId);

        if (!isNewPerfil) {
            // Carregar dados do perfil existente
            perfilViewModel.getPerfilFisicoPorUsuarioId(usuarioId).observe(getViewLifecycleOwner(), perfil -> {
                if (perfil != null) {
                    currentPerfil = perfil;
                    populateFormWithProfileData(perfil);
                }
            });
        } else {
            // Inicializar um novo perfil
            currentPerfil = new PerfilFisico(usuarioId, 0, 0);
            // Atualizar UI para perfil novo
            tvPerfilTitulo.setText("Novo Perfil Físico");
            tvImc.setText("0.0");
            tvImcClassificacao.setText("(Calcular)");
            tvDataAtualizacao.setText("Novo perfil");
        }
    }

    private void populateFormWithProfileData(PerfilFisico perfil) {
        etAltura.setText(String.valueOf(perfil.getAltura()));
        etPeso.setText(String.valueOf(perfil.getPeso()));
        etGordura.setText(String.valueOf(perfil.getPercentualGordura()));
        dropdownObjetivo.setText(perfil.getObjetivoFitness(), false);
        etRestricoes.setText(perfil.getRestricoesSaude());
        
        // Exibir IMC e classificação
        tvImc.setText(String.format(Locale.getDefault(), "%.1f", perfil.getImc()));
        tvImcClassificacao.setText("(" + getImcClassificacao(perfil.getImc()) + ")");
        
        // Exibir data da última atualização
        if (perfil.getDataAtualizacao() != null) {
            tvDataAtualizacao.setText("Última atualização: " + dateFormat.format(perfil.getDataAtualizacao()));
        }
    }

    private String getImcClassificacao(float imc) {
        if (imc < 18.5) {
            return "Abaixo do peso";
        } else if (imc < 25) {
            return "Normal";
        } else if (imc < 30) {
            return "Sobrepeso";
        } else if (imc < 35) {
            return "Obesidade Grau I";
        } else if (imc < 40) {
            return "Obesidade Grau II";
        } else {
            return "Obesidade Grau III";
        }
    }

    private void setupListeners() {
        btnSalvarPerfil.setOnClickListener(v -> savePerfilFisico());
    }

    private void savePerfilFisico() {
        // Validar campos
        if (validateForm()) {
            try {
                // Obter valores dos campos
                float altura = Float.parseFloat(etAltura.getText().toString());
                float peso = Float.parseFloat(etPeso.getText().toString());
                float gordura = 0;
                if (!TextUtils.isEmpty(etGordura.getText())) {
                    gordura = Float.parseFloat(etGordura.getText().toString());
                }
                
                // Atualizar objeto PerfilFisico
                currentPerfil.setAltura(altura);
                currentPerfil.setPeso(peso);
                currentPerfil.setPercentualGordura(gordura);
                currentPerfil.setObjetivoFitness(dropdownObjetivo.getText().toString());
                currentPerfil.setRestricoesSaude(etRestricoes.getText().toString());
                currentPerfil.setDataAtualizacao(new Date());
                
                // Salvar perfil
                if (isNewPerfil) {
                    perfilViewModel.inserir(currentPerfil);
                    Snackbar.make(requireView(), "Perfil criado com sucesso", Snackbar.LENGTH_SHORT).show();
                    isNewPerfil = false;
                } else {
                    perfilViewModel.atualizar(currentPerfil);
                    Snackbar.make(requireView(), "Perfil atualizado com sucesso", Snackbar.LENGTH_SHORT).show();
                }
                
                // Atualizar UI com IMC calculado
                tvImc.setText(String.format(Locale.getDefault(), "%.1f", currentPerfil.getImc()));
                tvImcClassificacao.setText("(" + getImcClassificacao(currentPerfil.getImc()) + ")");
                tvDataAtualizacao.setText("Última atualização: " + dateFormat.format(new Date()));
                
            } catch (Exception e) {
                Snackbar.make(requireView(), "Erro ao salvar: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validar altura
        if (TextUtils.isEmpty(etAltura.getText())) {
            tilAltura.setError("Altura é obrigatória");
            isValid = false;
        } else {
            try {
                float altura = Float.parseFloat(etAltura.getText().toString());
                if (altura <= 0 || altura > 3) {
                    tilAltura.setError("Altura inválida (entre 0 e 3 metros)");
                    isValid = false;
                } else {
                    tilAltura.setError(null);
                }
            } catch (NumberFormatException e) {
                tilAltura.setError("Formato inválido");
                isValid = false;
            }
        }

        // Validar peso
        if (TextUtils.isEmpty(etPeso.getText())) {
            tilPeso.setError("Peso é obrigatório");
            isValid = false;
        } else {
            try {
                float peso = Float.parseFloat(etPeso.getText().toString());
                if (peso <= 0 || peso > 500) {
                    tilPeso.setError("Peso inválido (entre 0 e 500 kg)");
                    isValid = false;
                } else {
                    tilPeso.setError(null);
                }
            } catch (NumberFormatException e) {
                tilPeso.setError("Formato inválido");
                isValid = false;
            }
        }

        // Validar percentual de gordura (opcional)
        if (!TextUtils.isEmpty(etGordura.getText())) {
            try {
                float gordura = Float.parseFloat(etGordura.getText().toString());
                if (gordura < 0 || gordura > 100) {
                    tilGordura.setError("Percentual inválido (entre 0 e 100%)");
                    isValid = false;
                } else {
                    tilGordura.setError(null);
                }
            } catch (NumberFormatException e) {
                tilGordura.setError("Formato inválido");
                isValid = false;
            }
        } else {
            tilGordura.setError(null);
        }

        return isValid;
    }
}
