package com.example.skynet.ui.logros;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.skynet.R;
import com.google.android.material.chip.ChipGroup;

public class LogrosActivity extends AppCompatActivity {

    private LogroViewModel viewModel;
    private LogroAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        // Inicializar vistas
        findViewById(R.id.btnBack).setOnClickListener(v -> onBackPressed());
        swipeRefresh = findViewById(R.id.swipeRefreshLogros);
        progressCargando = findViewById(R.id.progressCargando);
        RecyclerView rvLogros = findViewById(R.id.rvLogros);
        ChipGroup chipGroupFiltros = findViewById(R.id.chipGroupFiltros);

        // Configurar RecyclerView
        rvLogros.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogroAdapter();
        rvLogros.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(LogroViewModel.class);

        // Configurar SwipeRefresh
        swipeRefresh.setOnRefreshListener(() -> viewModel.cargarLogros());

        // Configurar Filtros de Chip
        chipGroupFiltros.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chipTodos) {
                adapter.aplicarFiltro("TODOS");
            } else if (checkedId == R.id.chipDesbloqueados) {
                adapter.aplicarFiltro("DESBLOQUEADOS");
            } else if (checkedId == R.id.chipEnProgreso) {
                adapter.aplicarFiltro("EN_PROGRESO");
            }
        });

        // Observar datos del ViewModel
        viewModel.getLogros().observe(this, logros -> {
            adapter.setLogros(logros);
        });

        viewModel.getLoading().observe(this, isLoading -> {
            swipeRefresh.setRefreshing(isLoading);
            if (isLoading && adapter.getItemCount() == 0) {
                progressCargando.setVisibility(View.VISIBLE);
            } else {
                progressCargando.setVisibility(View.GONE);
            }
        });

        viewModel.getError().observe(this, errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar datos iniciales
        viewModel.cargarLogros();
    }
}
