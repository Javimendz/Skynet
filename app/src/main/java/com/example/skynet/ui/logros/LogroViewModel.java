package com.example.skynet.ui.logros;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.skynet.data.repository.LogroRepository;
import com.example.skynet.data.remote.dto.LogroResponseDto;
import java.util.List;

public class LogroViewModel extends ViewModel {
    private final LogroRepository repository;

    private final MutableLiveData<List<LogroResponseDto>> logros = new MutableLiveData<>();
    private final MutableLiveData<LogroResponseDto> proximoLogro = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LogroViewModel() {
        this.repository = new LogroRepository();
    }

    public LiveData<List<LogroResponseDto>> getLogros() { return logros; }
    public LiveData<LogroResponseDto> getProximoLogro() { return proximoLogro; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void cargarLogros() {
        loading.setValue(true);
        repository.obtenerLogros(new LogroRepository.LogroCallback<List<LogroResponseDto>>() {
            @Override
            public void onSuccess(List<LogroResponseDto> result) {
                logros.setValue(result);
                loading.setValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                error.setValue(errorMessage);
                loading.setValue(false);
            }
        });
    }

    public void cargarProximoLogro() {
        repository.obtenerProximoLogro(new LogroRepository.LogroCallback<LogroResponseDto>() {
            @Override
            public void onSuccess(LogroResponseDto result) {
                proximoLogro.setValue(result);
            }

            @Override
            public void onError(String errorMessage) {
                // Silently handle or log if needed, or put to error
            }
        });
    }
}
