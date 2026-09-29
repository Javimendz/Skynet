package com.example.skynet.ui.rutinas;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.skynet.R;
import com.example.skynet.ui.ejercicios.Ejercicio;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.EjercicioHistorialDto;
import com.example.skynet.data.remote.dto.RutinaResponseDto;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EjecucionRutinaActivity extends AppCompatActivity implements EjerciciosAgregadosAdapter.OnWorkoutUpdateListener {

    private TextView tvVolumenTotal, tvSeriesTotales, tvDuracionRun;
    private RecyclerView rvEjercicios;
    private EjerciciosAgregadosAdapter adapter;
    private List<Ejercicio> listaEjercicios;
    
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = SystemClock.elapsedRealtime() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            minutes = minutes % 60;
            seconds = seconds % 60;

            String time;
            if (hours > 0) {
                time = String.format(Locale.getDefault(), "%dh %02dm %02ds", hours, minutes, seconds);
            } else if (minutes > 0) {
                time = String.format(Locale.getDefault(), "%dm %02ds", minutes, seconds);
            } else {
                time = String.format(Locale.getDefault(), "%ds", seconds);
            }
            tvDuracionRun.setText(time);
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrenamiento_ejecucion);

        // Inicializar vistas
        tvVolumenTotal = findViewById(R.id.tvVolumenRun);
        tvSeriesTotales = findViewById(R.id.tvSeriesRun);
        tvDuracionRun = findViewById(R.id.tvDuracionRun);
        rvEjercicios = findViewById(R.id.rvEjerciciosRun);

        // Obtener datos del intent
        listaEjercicios = getIntent().getParcelableArrayListExtra("LISTA_EJERCICIOS");
        if (listaEjercicios == null) {
            listaEjercicios = new ArrayList<>();
        }

        // Configurar RecyclerView
        rvEjercicios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EjerciciosAgregadosAdapter(listaEjercicios);
        adapter.setOnWorkoutUpdateListener(this);
        rvEjercicios.setAdapter(adapter);

        // Configurar botones
        findViewById(R.id.btnTerminar).setOnClickListener(v -> terminarEntrenamiento());
        findViewById(R.id.btnBackRun).setOnClickListener(v -> finish());
        findViewById(R.id.btnAgregarEjercicioRun).setOnClickListener(v -> {
            // Reutilizar AgregarEjercicioActivity
            android.content.Intent intent = new android.content.Intent(this, com.example.skynet.ui.ejercicios.AgregarEjercicioActivity.class);
            startActivityForResult(intent, 1001);
        });

        // Iniciar cronómetro - Priorizando el estado del WorkoutManager para evitar reinicios
        WorkoutManager wm = WorkoutManager.getInstance();
        
        // Si el gestor ya está activo en memoria, evitamos restaurar para no pisar datos vivos
        if (!wm.isActive()) {
            wm.restoreState(this);
        }
        
        if (wm.isActive()) {
            // Recuperamos el tiempo de inicio persistido en el gestor
            startTime = wm.getStartTime();
            
            // Si hay un entrenamiento activo, recuperamos los ejercicios guardados para mantener el progreso
            List<Ejercicio> exercisesInProgress = wm.getCurrentExercises();
            if (exercisesInProgress != null && !exercisesInProgress.isEmpty()) {
                listaEjercicios.clear();
                listaEjercicios.addAll(exercisesInProgress);
                // Sincronizamos la referencia para que los cambios en el adaptador se reflejen en el gestor
                wm.setExercises(this, listaEjercicios);
                adapter.notifyDataSetChanged();
            }
        } else {
            // Si no hay nada activo, iniciamos el cronómetro y el estado global
            startTime = SystemClock.elapsedRealtime();
            wm.startWorkout(this, listaEjercicios);
        }
        timerHandler.postDelayed(timerRunnable, 0);

        updateStats();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            ArrayList<Ejercicio> seleccionados = data.getParcelableArrayListExtra("EJERCICIOS_SELECCIONADOS");
            if (seleccionados != null && !seleccionados.isEmpty()) {
                listaEjercicios.addAll(seleccionados);
                adapter.notifyDataSetChanged();
                WorkoutManager.getInstance().setExercises(this, listaEjercicios);
            }
        }
    }

    private void updateStats() {
        double totalVolumen = 0;
        int totalSeries = 0;

        for (Ejercicio ejercicio : listaEjercicios) {
            if (ejercicio.getSeriesList() != null) {
                for (Ejercicio.Serie serie : ejercicio.getSeriesList()) {
                    // Contamos volumen si tiene datos, aunque no esté el checkbox marcado
                    // para dar feedback visual al usuario mientras escribe.
                    if (serie.getKg() > 0 && serie.getReps() > 0) {
                        totalVolumen += (serie.getKg() * serie.getReps());
                    }
                    if (serie.isCompletada()) {
                        totalSeries++;
                    }
                }
            }
        }

        tvVolumenTotal.setText(String.format(Locale.getDefault(), "%.1f kg", totalVolumen));
        tvSeriesTotales.setText(String.valueOf(totalSeries));
    }

    @Override
    public void onWorkoutUpdate() {
        updateStats();
        // Persistir el estado actual para evitar pérdida de datos si la app se cierra
        WorkoutManager.getInstance().saveState(this);
    }

    private void terminarEntrenamiento() {
        findViewById(R.id.btnTerminar).setEnabled(false);
        timerHandler.removeCallbacks(timerRunnable);
        guardarEstadisticasYFinalizar();
    }

    private void guardarEstadisticasYFinalizar() {
        android.content.SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);

        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final int[] totalSeriesAGuardar = {0};
        final int[] guardadosCount = {0};

        // Primero contamos cuántas series tienen datos válidos para guardar
        for (Ejercicio ejercicio : listaEjercicios) {
            if (ejercicio.getSeriesList() != null) {
                if (ejercicio.getId() == null) {
                    android.util.Log.e("EjecucionRutina", "El ejercicio '" + ejercicio.getNombre() + "' no tiene ID. No se podrá guardar.");
                    continue;
                }
                for (Ejercicio.Serie s : ejercicio.getSeriesList()) {
                    // Consideramos válida si tiene peso y reps, o si está marcada como completada
                    if (s.isCompletada() || (s.getKg() > 0 && s.getReps() > 0)) {
                        totalSeriesAGuardar[0]++;
                    }
                }
            }
        }

        if (totalSeriesAGuardar[0] == 0) {
            marcarRutinaComoCompletadaSiEsNecesario();
            Toast.makeText(this, "Introduce peso y reps en las series para guardar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar diálogo de progreso
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Guardando estadísticas (0/" + totalSeriesAGuardar[0] + ")...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        final int total = totalSeriesAGuardar[0];

        // Enviamos cada serie individualmente
        for (Ejercicio ejercicio : listaEjercicios) {
            if (ejercicio.getSeriesList() == null || ejercicio.getId() == null) continue;

            for (Ejercicio.Serie s : ejercicio.getSeriesList()) {
                if (!s.isCompletada() && (s.getKg() <= 0 || s.getReps() <= 0)) continue;

                EjercicioHistorialDto dto = new EjercicioHistorialDto(
                        fechaActual,
                        s.getKg(),
                        s.getReps(),
                        tvDuracionRun.getText().toString()
                );

                android.util.Log.d("EjecucionRutina", "Enviando serie: Ejercicio=" + ejercicio.getId() + ", Usuario=" + usuarioId + ", Peso=" + s.getKg());

                RetrofitClient.getApiService().guardarHistorialEjercicio(ejercicio.getId(), usuarioId, dto)
                        .enqueue(new Callback<EjercicioHistorialDto>() {
                            @Override
                            public void onResponse(Call<EjercicioHistorialDto> call, Response<EjercicioHistorialDto> response) {
                                synchronized (guardadosCount) {
                                    guardadosCount[0]++;
                                    if (response.isSuccessful()) {
                                        android.util.Log.d("EjecucionRutina", "Serie guardada con éxito");
                                    } else {
                                        android.util.Log.e("EjecucionRutina", "Error del servidor: " + response.code() + " " + response.message());
                                    }

                                    progressDialog.setMessage("Guardando estadísticas (" + guardadosCount[0] + "/" + total + ")...");
                                    if (guardadosCount[0] >= total) {
                                        progressDialog.dismiss();
                                        marcarRutinaComoCompletadaSiEsNecesario();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<EjercicioHistorialDto> call, Throwable t) {
                                android.util.Log.e("EjecucionRutina", "Error al guardar serie: " + t.getMessage());
                                synchronized (guardadosCount) {
                                    guardadosCount[0]++;
                                    progressDialog.setMessage("Guardando estadísticas (" + guardadosCount[0] + "/" + total + ")...");
                                    if (guardadosCount[0] >= total) {
                                        progressDialog.dismiss();
                                        marcarRutinaComoCompletadaSiEsNecesario();
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void marcarRutinaComoCompletadaSiEsNecesario() {
        long rutinaId = getIntent().getLongExtra("RUTINA_ID", -1L);
        boolean isFromAgenda = getIntent().getBooleanExtra("IS_FROM_AGENDA", false);

        if (rutinaId != -1 && isFromAgenda) {
            RetrofitClient.getApiService().completarRutina(rutinaId).enqueue(new Callback<ApiResponseDto<RutinaResponseDto>>() {
                @Override
                public void onResponse(Call<ApiResponseDto<RutinaResponseDto>> call, Response<ApiResponseDto<RutinaResponseDto>> response) {
                    finalizarConExito();
                }

                @Override
                public void onFailure(Call<ApiResponseDto<RutinaResponseDto>> call, Throwable t) {
                    finalizarConExito();
                }
            });
        } else {
            finalizarConExito();
        }
    }

    private void finalizarConExito() {
        WorkoutManager.getInstance().stopWorkout(this);
        Toast.makeText(this, "Entrenamiento guardado correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}