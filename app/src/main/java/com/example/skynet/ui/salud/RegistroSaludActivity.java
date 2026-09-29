package com.example.skynet.ui.salud;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.skynet.R;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.SaludRequestDto;
import com.example.skynet.data.remote.dto.SaludResponseDto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroSaludActivity extends AppCompatActivity {

    private TextInputEditText etPeso, etEstatura, etComentario;
    private AutoCompleteTextView actvActividad;
    private MaterialButton btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_salud);

        etPeso = findViewById(R.id.etRegistroPeso);
        etEstatura = findViewById(R.id.etRegistroEstatura);
        etComentario = findViewById(R.id.etRegistroComentario);
        actvActividad = findViewById(R.id.actvRegistroActividad);
        btnGuardar = findViewById(R.id.btnGuardarRegistroSalud);

        findViewById(R.id.btnBackRegistroSalud).setOnClickListener(v -> finish());

        setupDropdownActividad();

        btnGuardar.setOnClickListener(v -> guardarRegistro());
        
        cargarDatosActuales();
    }

    private void setupDropdownActividad() {
        String[] niveles = {"SEDENTARIO", "LIGERO", "MODERADO", "INTENSO", "ATLETA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, niveles);
        actvActividad.setAdapter(adapter);
        actvActividad.setText(niveles[2], false); // Moderado por defecto
    }

    private void cargarDatosActuales() {
        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId == -1) return;

        RetrofitClient.getApiService().getSaludActual(usuarioId).enqueue(new Callback<ApiResponseDto<SaludResponseDto>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<SaludResponseDto>> call, Response<ApiResponseDto<SaludResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    SaludResponseDto salud = response.body().getDatos();
                    if (salud.getPeso() != null) etPeso.setText(String.valueOf(salud.getPeso()));
                    if (salud.getEstatura() != null) etEstatura.setText(String.valueOf(salud.getEstatura()));
                    if (salud.getNivelActividad() != null) actvActividad.setText(salud.getNivelActividad(), false);
                }
            }
            @Override
            public void onFailure(Call<ApiResponseDto<SaludResponseDto>> call, Throwable t) {}
        });
    }

    private void guardarRegistro() {
        String pesoStr = etPeso.getText().toString();
        String estaturaStr = etEstatura.getText().toString();
        String actividad = actvActividad.getText().toString();
        String comentario = etComentario.getText().toString();

        if (pesoStr.isEmpty() || estaturaStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa peso y estatura", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso = Double.parseDouble(pesoStr);
        double estaturaCm = Double.parseDouble(estaturaStr);
        double estaturaMetros = estaturaCm / 100.0; // Conversión a metros para el Backend

        SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);

        SaludRequestDto request = new SaludRequestDto();
        request.setPeso(peso);
        request.setEstatura(estaturaMetros);
        request.setNivelActividad(actividad);
        request.setComentario(comentario);

        btnGuardar.setEnabled(false);
        btnGuardar.setText("GUARDANDO...");

        RetrofitClient.getApiService().registrarSalud(usuarioId, request).enqueue(new Callback<ApiResponseDto<SaludResponseDto>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<SaludResponseDto>> call, Response<ApiResponseDto<SaludResponseDto>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegistroSaludActivity.this, "¡Progreso guardado correctamente!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(RegistroSaludActivity.this, "Error al guardar registro", Toast.LENGTH_SHORT).show();
                    btnGuardar.setEnabled(true);
                    btnGuardar.setText("GUARDAR REGISTRO");
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<SaludResponseDto>> call, Throwable t) {
                Toast.makeText(RegistroSaludActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                btnGuardar.setEnabled(true);
                btnGuardar.setText("GUARDAR REGISTRO");
            }
        });
    }
}