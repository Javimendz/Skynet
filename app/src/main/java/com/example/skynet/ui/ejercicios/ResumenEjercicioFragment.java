package com.example.skynet.ui.ejercicios;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.skynet.R;
import com.example.skynet.data.remote.ApiService;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.EjercicioHistorialDto;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumenEjercicioFragment extends Fragment {

    private LineChart chart;
    private TextView tv1RM, tvMejorVolumen;
    private Ejercicio ejercicio;

    public static ResumenEjercicioFragment newInstance(Ejercicio ejercicio) {
        ResumenEjercicioFragment fragment = new ResumenEjercicioFragment();
        Bundle args = new Bundle();
        args.putParcelable("ejercicio", ejercicio);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen_ejercicio, container, false);

        if (getArguments() != null) {
            ejercicio = getArguments().getParcelable("ejercicio");
        }

        chart = view.findViewById(R.id.chartVolumen);
        tv1RM = view.findViewById(R.id.tv1RM);
        tvMejorVolumen = view.findViewById(R.id.tvMejorVolumen);

        setupChart(chart);
        
        // Mostrar estado de carga inicial
        tv1RM.setText("Cargando...");
        tvMejorVolumen.setText("Cargando...");
        
        cargarDatosHistorial();

        return view;
    }

    private void cargarDatosHistorial() {
        if (ejercicio == null) {
            android.util.Log.e("ResumenEjercicio", "Ejercicio es null");
            return;
        }
        
        if (ejercicio.getId() == null) {
            android.util.Log.e("ResumenEjercicio", "El ejercicio '" + ejercicio.getNombre() + "' no tiene ID.");
            tv1RM.setText("N/A");
            tvMejorVolumen.setText("N/A");
            chart.setNoDataText("Sin datos de ejercicio (Falta ID)");
            return;
        }

        android.content.SharedPreferences prefs = requireContext().getSharedPreferences("DatosUsuario", android.content.Context.MODE_PRIVATE);
        Long usuarioId = prefs.getLong("user_id", -1L);

        if (usuarioId == -1L) {
            tv1RM.setText("Error");
            tvMejorVolumen.setText("Error");
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getHistorialEjercicio(ejercicio.getId(), usuarioId).enqueue(new Callback<List<EjercicioHistorialDto>>() {
            @Override
            public void onResponse(Call<List<EjercicioHistorialDto>> call, Response<List<EjercicioHistorialDto>> response) {
                if (!isAdded()) return;
                
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    actualizarUI(response.body());
                } else {
                    tv1RM.setText("0.0 kg");
                    tvMejorVolumen.setText("0.0 kg");
                    chart.setNoDataText("Aún no tienes registros para este ejercicio.");
                    chart.setData(null); // Asegura que se muestre el NoDataText
                    chart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<List<EjercicioHistorialDto>> call, Throwable t) {
                if (!isAdded()) return;
                tv1RM.setText("N/A");
                tvMejorVolumen.setText("N/A");
                chart.setNoDataText("Error de conexión con el servidor.");
                chart.invalidate();
            }
        });
    }

    private void actualizarUI(List<EjercicioHistorialDto> historial) {
        if (historial == null || historial.isEmpty()) return;

        // Agrupar por fecha para calcular volumen diario y el mejor 1RM histórico
        java.util.Map<String, Double> volumenPorDia = new java.util.LinkedHashMap<>();
        double max1RMGlobal = 0;
        double maxVolumenDiarioGlobal = 0;

        for (EjercicioHistorialDto dto : historial) {
            String fecha = dto.getFecha();
            if (fecha != null && fecha.contains("T")) {
                fecha = fecha.split("T")[0];
            }
            
            double volSerie = dto.getVolumenTotal();
            double r1mSerie = dto.getMejor1RM();

            volumenPorDia.put(fecha, volumenPorDia.getOrDefault(fecha, 0.0) + volSerie);
            if (r1mSerie > max1RMGlobal) max1RMGlobal = r1mSerie;
        }

        List<Entry> entries = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        int index = 0;

        for (java.util.Map.Entry<String, Double> entry : volumenPorDia.entrySet()) {
            entries.add(new Entry(index, entry.getValue().floatValue()));
            dates.add(entry.getKey());
            if (entry.getValue() > maxVolumenDiarioGlobal) maxVolumenDiarioGlobal = entry.getValue();
            index++;
        }

        tv1RM.setText(String.format(Locale.getDefault(), "%.1f kg", max1RMGlobal));
        tvMejorVolumen.setText(String.format(Locale.getDefault(), "%.1f kg", maxVolumenDiarioGlobal));

        LineDataSet dataSet = new LineDataSet(entries, "Volumen Total Diario");
        dataSet.setColor(Color.parseColor("#CD0277")); // Rosa Neon para coherencia
        dataSet.setCircleColor(Color.parseColor("#4FC3F7")); // Azul para puntos
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleColor(Color.parseColor("#1A2238"));
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#CD0277"));
        dataSet.setFillAlpha(40);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setGranularity(1f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-45);

        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getLegend().setTextColor(Color.WHITE);
        chart.animateY(1000);
        chart.invalidate();
    }

    private void setupChart(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setNoDataText("Buscando historial...");
        chart.setNoDataTextColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
    }
}