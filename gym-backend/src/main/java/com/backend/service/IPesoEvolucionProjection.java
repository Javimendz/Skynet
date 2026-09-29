package com.backend.service;


import java.time.LocalDate;

public interface IPesoEvolucionProjection {
    LocalDate getFecha();
    Double getPeso();
}