/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.modelo;

import java.time.LocalDateTime;

/**
 *
 * @author Ludvi
 */
public class Asistencia {

    private String correo; // PK (FK participante)
    private String codigoActividad; // PK (FK actividad)
    private LocalDateTime asistioEn;

    public Asistencia() {
    }

    public Asistencia(String correo, String codigoActividad) {
        this.correo = correo;
        this.codigoActividad = codigoActividad;
    }

    // Getters y setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public LocalDateTime getAsistioEn() {
        return asistioEn;
    }

    public void setAsistioEn(LocalDateTime asistioEn) {
        this.asistioEn = asistioEn;
    }
}
