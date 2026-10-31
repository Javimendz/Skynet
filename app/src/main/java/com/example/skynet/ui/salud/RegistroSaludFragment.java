package com.example.skynet.ui.salud;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class RegistroSaludFragment extends Fragment {

    private TextInputEditText etPeso, etEstatura, etComentario;
    private AutoCompleteTextView actvActividad;
    private MaterialButton btnGuardar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_salud, container, false);

        etPeso = view.findViewById(R.id.etRegistroPeso);
        etEstatura = view.findViewById(R.id.etRegistroEstatura);
        etComentario = view.findViewById(R.id.etRegistroComentario);
        actvActividad = view.findViewById(R.id.actvRegistroActividad);
        btnGuardar = view.findViewById(R.id.btnGuardarRegistroSalud);

        setupDropdownActividad();

        btnGuardar.setOnClickListener(v -> guardarRegistro());

        cargarDatosActuales();

        return view;
    }

    private void setupDropdownActividad() {
        String[] niveles = {"SEDENTARIO", "LIGERO", "MODERADO", "INTENSO", "ATLETA"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, niveles);
        actvActividad.setAdapter(adapter);
        actvActividad.setText(niveles[2], false); // Moderado por defecto
        actvActividad.setInputType(android.text.InputType.TYPE_NULL);
    }

    private void cargarDatosActuales() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        long usuarioId = prefs.getLong("user_id", -1);
        if (usuarioId == -1) return;

        RetrofitClient.getApiService().getSaludActual(usuarioId).enqueue(new Callback<ApiResponseDto<SaludResponseDto>>() {
            @Override
            public void onResponse(Call<ApiResponseDto<SaludResponseDto>> call, Response<ApiResponseDto<SaludResponseDto>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().getDatos() != null) {
                    SaludResponseDto salud = response.body().getDatos();
                    if (salud.getPeso() != null) etPeso.setText(String.valueOf(salud.getPeso()));
                    if (salud.getEstatura() != null) {
                        double estatura = salud.getEstatura();
                        etEstatura.setText(String.valueOf(estatura < 3 ? (int)(estatura * 100) : (int)estatura));
                    }
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
            Toast.makeText(getContext(), "Por favor, completa peso y estatura", Toast.LENGTH_SHORT).show();
            return;
        }

        double peso = Double.parseDouble(pesoStr);
        double estaturaCm = Double.parseDouble(estaturaStr);
        double estaturaMetros = estaturaCm / 100.0;

        SharedPreferences prefs = requireActivity().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
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
                if (isAdded()) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "¡Progreso guardado correctamente!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Error al guardar registro", Toast.LENGTH_SHORT).show();
                        btnGuardar.setEnabled(true);
                        btnGuardar.setText("GUARDAR REGISTRO");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseDto<SaludResponseDto>> call, Throwable t) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    btnGuardar.setEnabled(true);
                    btnGuardar.setText("GUARDAR REGISTRO");
                }
            }
        });
    }
}
