package app.gym.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import app.gym.R;
import app.gym.ui.viewmodels.ExercicioViewModel;
import app.gym.ui.viewmodels.TreinoViewModel;
import app.gym.ui.viewmodels.UsuarioViewModel;

/**
 * Fragmento para a tela inicial do aplicativo
 */
public class HomeFragment extends Fragment {

    private CardView cardUsuarios;
    private CardView cardExercicios;
    private CardView cardTreinos;
    private CardView cardEstatisticas;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView tvUserCount, tvExerciseCount, tvTrainingCount;
    private ImageView ivUserStatus, ivExerciseStatus, ivTrainingStatus;

    // ViewModels
    private UsuarioViewModel usuarioViewModel;
    private ExercicioViewModel exercicioViewModel;
    private TreinoViewModel treinoViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializar ViewModels
        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
        exercicioViewModel = new ViewModelProvider(this).get(ExercicioViewModel.class);
        treinoViewModel = new ViewModelProvider(this).get(TreinoViewModel.class);

        // Inicializar cards
        cardUsuarios = view.findViewById(R.id.card_usuarios);
        cardExercicios = view.findViewById(R.id.card_exercicios);
        cardTreinos = view.findViewById(R.id.card_treinos);
        cardEstatisticas = view.findViewById(R.id.card_estatisticas);

        // Inicializar gráficos
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);

        // Inicializar componentes da tabela
        tvUserCount = view.findViewById(R.id.tv_user_count);
        tvExerciseCount = view.findViewById(R.id.tv_exercise_count);
        tvTrainingCount = view.findViewById(R.id.tv_training_count);
        ivUserStatus = view.findViewById(R.id.iv_user_status);
        ivExerciseStatus = view.findViewById(R.id.iv_exercise_status);
        ivTrainingStatus = view.findViewById(R.id.iv_training_status);

        setupClickListeners();
        observeData();
        setupCharts();

        return view;
    }

    private void setupClickListeners() {
        // Configurar click listeners para os cards
        cardUsuarios.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_usuariosFragment);
        });

        cardExercicios.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_exerciciosFragment);
        });

        cardTreinos.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_treinosFragment);
        });

        // Implementação de estatísticas - Abre o diálogo resumido
        cardEstatisticas.setOnClickListener(v -> {
            try {
                // Criar e configurar um diálogo para mostrar estatísticas
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Estatísticas do Aplicativo");

                // Criar view personalizada para o diálogo
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_estatisticas, null);
                TextView tvUsuarios = dialogView.findViewById(R.id.tv_total_usuarios);
                TextView tvExercicios = dialogView.findViewById(R.id.tv_total_exercicios);
                TextView tvTreinos = dialogView.findViewById(R.id.tv_total_treinos);

                // Atualizar UI com os valores que já estamos observando
                usuarioViewModel.getQuantidadeUsuarios().observe(getViewLifecycleOwner(), quantidade -> {
                    tvUsuarios.setText(String.valueOf(quantidade));
                });

                exercicioViewModel.getQuantidadeExercicios().observe(getViewLifecycleOwner(), quantidade -> {
                    tvExercicios.setText(String.valueOf(quantidade));
                });

                treinoViewModel.getQuantidadeTreinos().observe(getViewLifecycleOwner(), quantidade -> {
                    tvTreinos.setText(String.valueOf(quantidade));
                });

                builder.setView(dialogView);
                builder.setPositiveButton("Fechar", null);
                builder.show();
            } catch (Exception e) {
                Log.e("HomeFragment", "Erro ao exibir estatísticas: " + e.getMessage(), e);
                Toast.makeText(requireContext(), "Erro ao carregar estatísticas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Observa os dados dos ViewModels e atualiza a interface
     */
    private void observeData() {
        // Observar contagem de usuários
        usuarioViewModel.getQuantidadeUsuarios().observe(getViewLifecycleOwner(), count -> {
            tvUserCount.setText(String.valueOf(count));
            updateStatusIcon(ivUserStatus, count);
            // Atualizar gráficos quando os dados mudarem
            updateCharts();
        });

        // Observar contagem de exercícios
        exercicioViewModel.getQuantidadeExercicios().observe(getViewLifecycleOwner(), count -> {
            tvExerciseCount.setText(String.valueOf(count));
            updateStatusIcon(ivExerciseStatus, count);
            // Atualizar gráficos quando os dados mudarem
            updateCharts();
        });

        // Observar contagem de treinos
        treinoViewModel.getQuantidadeTreinos().observe(getViewLifecycleOwner(), count -> {
            tvTrainingCount.setText(String.valueOf(count));
            updateStatusIcon(ivTrainingStatus, count);
            // Atualizar gráficos quando os dados mudarem
            updateCharts();
        });
    }

    /**
     * Atualiza o ícone de status com base na contagem
     */
    private void updateStatusIcon(ImageView icon, int count) {
        if (count > 10) {
            icon.setImageResource(android.R.drawable.presence_online); // Verde/Bom
        } else if (count > 0) {
            icon.setImageResource(android.R.drawable.presence_away); // Amarelo/Médio
        } else {
            icon.setImageResource(android.R.drawable.presence_busy); // Vermelho/Baixo
        }
    }

    /**
     * Configura os gráficos inicialmente
     */
    private void setupCharts() {
        // Configurar gráfico de pizza
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Recursos");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(31f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        // Configurar gráfico de barras
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Inicialmente atualizar com zeros
        updatePieChart(0, 0, 0);
        updateBarChart(0, 0, 0, 0);
    }

    /**
     * Atualiza os gráficos com os dados mais recentes
     */
    private void updateCharts() {
        // Obter valores atuais
        int userCount = 0;
        int exerciseCount = 0;
        int trainingCount = 0;

        try {
            userCount = Integer.parseInt(tvUserCount.getText().toString());
            exerciseCount = Integer.parseInt(tvExerciseCount.getText().toString());
            trainingCount = Integer.parseInt(tvTrainingCount.getText().toString());
        } catch (NumberFormatException e) {
            Log.e("HomeFragment", "Erro ao converter valores: " + e.getMessage());
        }

        // Atualizar gráficos
        updatePieChart(userCount, exerciseCount, trainingCount);

        // Tentar obter valores de ativos vs. inativos dos ViewModels
        final int finalUserCount = userCount;
        usuarioViewModel.getQuantidadeUsuariosAtivos().observe(getViewLifecycleOwner(), usuariosAtivos -> {
            int usuariosInativos = finalUserCount - usuariosAtivos;
            updateBarChart(usuariosAtivos, usuariosInativos, 0, 0);
        });
    }

    /**
     * Atualiza o gráfico de pizza com os valores fornecidos
     */
    private void updatePieChart(int userCount, int exerciseCount, int trainingCount) {
        List<PieEntry> entries = new ArrayList<>();

        // Adicionar apenas valores maiores que zero para melhor visualização
        if (userCount > 0) entries.add(new PieEntry(userCount, "Usuários"));
        if (exerciseCount > 0) entries.add(new PieEntry(exerciseCount, "Exercícios"));
        if (trainingCount > 0) entries.add(new PieEntry(trainingCount, "Treinos"));

        // Se não houver dados, adicionar um placeholder
        if (entries.isEmpty()) {
            entries.add(new PieEntry(1, "Sem dados"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Categorias");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate(); // Redesenhar o gráfico
    }

    /**
     * Atualiza o gráfico de barras com os valores fornecidos
     */
    private void updateBarChart(int usuariosAtivos, int usuariosInativos, int exerciciosAtivos, int exerciciosInativos) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, new float[]{usuariosAtivos, usuariosInativos}));
        entries.add(new BarEntry(1f, new float[]{exerciciosAtivos, exerciciosInativos}));

        BarDataSet dataSet = new BarDataSet(entries, "Estatísticas");
        dataSet.setColors(new int[]{Color.rgb(67, 160, 71), Color.rgb(239, 83, 80)}); // Verde e vermelho
        dataSet.setStackLabels(new String[]{"Ativos", "Inativos"});

        BarData data = new BarData(dataSet);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(10f);
        data.setBarWidth(0.6f);

        // Configurar rótulos do eixo X
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Usuários", "Exercícios"}));

        barChart.setData(data);
        barChart.invalidate(); // Redesenhar o gráfico
    }
}
