package app.gym.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import app.gym.R;

/**
 * Fragmento para a tela inicial do aplicativo
 */
public class HomeFragment extends Fragment {

    private CardView cardUsuarios;
    private CardView cardExercicios;
    private CardView cardTreinos;
    private CardView cardEstatisticas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar componentes da UI
        initViews(view);
        
        // Configurar listeners
        setupListeners();
    }

    private void initViews(View view) {
        cardUsuarios = view.findViewById(R.id.card_usuarios);
        cardExercicios = view.findViewById(R.id.card_exercicios);
        cardTreinos = view.findViewById(R.id.card_treinos);
        cardEstatisticas = view.findViewById(R.id.card_estatisticas);
    }

    private void setupListeners() {
        // Navegar para a tela de Usuários
        cardUsuarios.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_usuariosFragment);
        });

        // Navegar para a tela de Exercícios
        cardExercicios.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_exerciciosFragment);
        });

        // Navegar para a tela de Treinos
        cardTreinos.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_treinosFragment);
        });

        // Para estatísticas, apenas um exemplo de placeholder por enquanto
        cardEstatisticas.setOnClickListener(v -> {
            // Futura implementação
        });
    }
}
