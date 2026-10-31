package com.example.skynet.data.repository;

import androidx.annotation.NonNull;
import com.example.skynet.data.remote.ApiService;
import com.example.skynet.data.remote.RetrofitClient;
import com.example.skynet.data.remote.dto.ApiResponseDto;
import com.example.skynet.data.remote.dto.LogroResponseDto;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogroRepository {
    private final ApiService apiService;

    public LogroRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    public interface LogroCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public void obtenerLogros(LogroCallback<List<LogroResponseDto>> callback) {
        apiService.obtenerLogros().enqueue(new Callback<ApiResponseDto<List<LogroResponseDto>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseDto<List<LogroResponseDto>>> call,
                                   @NonNull Response<ApiResponseDto<List<LogroResponseDto>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        callback.onSuccess(response.body().getDatos());
                    } else {
                        callback.onError(response.body().getMensaje());
                    }
                } else {
                    callback.onError("Error al obtener logros: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseDto<List<LogroResponseDto>>> call, @NonNull Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    public void obtenerProximoLogro(LogroCallback<LogroResponseDto> callback) {
        apiService.obtenerProximoLogro().enqueue(new Callback<ApiResponseDto<LogroResponseDto>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponseDto<LogroResponseDto>> call,
                                   @NonNull Response<ApiResponseDto<LogroResponseDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        callback.onSuccess(response.body().getDatos());
                    } else {
                        callback.onError(response.body().getMensaje());
                    }
                } else {
                    callback.onError("Error al obtener el próximo logro: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponseDto<LogroResponseDto>> call, @NonNull Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }
}
