package com.example.skynet.ui.ejercicios;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import com.example.skynet.R;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.TutorialRequestDto;
import com.example.skynet.data.remote.dto.TutorialResponseDto;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarEjercicioActivity extends AppCompatActivity {

    private AgregarEjercicioViewModel viewModel;
    private EntrenamientoAdapter adapter;
    private ProgressBar progressBar;
    private com.google.android.material.button.MaterialButton btnAgregaEjercicios;
    private ArrayList<Ejercicio> seleccionados = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_ejercicio);

        viewModel = new ViewModelProvider(this).get(AgregarEjercicioViewModel.class);

        progressBar = findViewById(R.id.progressBar);
        RecyclerView rvEjercicios = findViewById(R.id.rvEjercicios);
        EditText etBuscar = findViewById(R.id.etBuscar);
        btnAgregaEjercicios = findViewById(R.id.btnAgregaEjercicios);

        rvEjercicios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EntrenamientoAdapter(new ArrayList<>(), dto -> {
            if ("Wger".equals(dto.getOrigen())) {
                importarEjercicioExterno(dto);
            } else {
                actualizarSeleccionLocal(dto.getId(), dto.getNombre(), dto.getDescripcion(), dto.getImagenUrl(), dto.getOrigen());
            }
        });
        rvEjercicios.setAdapter(adapter);

        rvEjercicios.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 5) {
                            viewModel.loadNextPage();
                        }
                    }
                }
            }
        });

        btnAgregaEjercicios.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putParcelableArrayListExtra("EJERCICIOS_SELECCIONADOS", seleccionados);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish());
        findViewById(R.id.btnCrear).setOnClickListener(v -> {
            // Navegar a creación manual si fuera necesario
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.onSearchQueryChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        viewModel.getUiState().observe(this, state -> {
            if (state instanceof AgregarEjercicioUiState.Loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else if (state instanceof AgregarEjercicioUiState.Success) {
                progressBar.setVisibility(View.GONE);
                AgregarEjercicioUiState.Success success = (AgregarEjercicioUiState.Success) state;
                adapter.updateList(success.getEjercicios(), success.getSelectedIds());
            } else if (state instanceof AgregarEjercicioUiState.Error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Error al cargar ejercicios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void importarEjercicioExterno(EntrenamientoDto dto) {
        progressBar.setVisibility(View.VISIBLE);
        
        TutorialRequestDto request = new TutorialRequestDto();
        request.setTitulo(dto.getNombre());
        request.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion() : "Ejercicio importado de Wger");
        request.setUrlVideo(dto.getImagenUrl());
        request.setDuracionMin(10);
        request.setCategoriaId(1L); 
        request.setEsGlobal(false);

        RetrofitClient.getApiService().crearTutorial(request)
            .enqueue(new Callback<ApiResponseDto<TutorialResponseDto>>() {
                @Override
                public void onResponse(Call<ApiResponseDto<TutorialResponseDto>> call, 
                                     Response<ApiResponseDto<TutorialResponseDto>> response) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {
                        Long idLocal = response.body().getDatos().getId();
                        actualizarSeleccionLocal(idLocal, dto.getNombre(), dto.getDescripcion(), dto.getImagenUrl(), "Local");
                        viewModel.toggleExerciseSelection(dto.getId()); 
                    } else {
                        Toast.makeText(AgregarEjercicioActivity.this, "Error al sincronizar ejercicio", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponseDto<TutorialResponseDto>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AgregarEjercicioActivity.this, "Error de red al importar", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void actualizarSeleccionLocal(Long id, String nombre, String desc, String img, String origen) {
        Ejercicio ejercicio = new Ejercicio(
                id,
                nombre,
                origen,
                desc,
                "10 min",
                img,
                R.drawable.ic_workout,
                "Media"
        );
        
        boolean exists = false;
        for (int i = 0; i < seleccionados.size(); i++) {
            if (Objects.equals(seleccionados.get(i).getId(), id)) {
                seleccionados.remove(i);
                exists = true;
                break;
            }
        }
        if (!exists) {
            seleccionados.add(ejercicio);
        }
        actualizarBotonSeleccion();
        
        // Si el origen NO es Wger, notificamos al ViewModel para que mantenga el estado visual del check
        if (!"Wger".equals(origen)) {
            viewModel.toggleExerciseSelection(id);
        }
    }

    private void actualizarBotonSeleccion() {
        if (seleccionados.isEmpty()) {
            btnAgregaEjercicios.setVisibility(View.GONE);
        } else {
            btnAgregaEjercicios.setVisibility(View.VISIBLE);
            btnAgregaEjercicios.setText("Agrega " + seleccionados.size() + " ejercicio" + (seleccionados.size() > 1 ? "s" : ""));
        }
    }
}
