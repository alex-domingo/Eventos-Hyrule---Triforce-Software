/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.modelo;

import java.time.LocalTime;

/**
 *
 * @author Ludvi
 */
public class Actividad {

    private String codigo;          // PK: ACT-00000001
    private String codigoEvento;    // FK evento
    private String tipo;            // 'charla','taller','debate','otra'
    private String titulo;
    private String correoInstructor; // FK participante
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cupo;

    public Actividad() {
    }

    public Actividad(String codigo, String codigoEvento, String tipo, String titulo,
            String correoInstructor, LocalTime horaInicio, LocalTime horaFin, int cupo) {
        this.codigo = codigo;
        this.codigoEvento = codigoEvento;
        this.tipo = tipo;
        this.titulo = titulo;
        this.correoInstructor = correoInstructor;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cupo = cupo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorreoInstructor() {
        return correoInstructor;
    }

    public void setCorreoInstructor(String correoInstructor) {
        this.correoInstructor = correoInstructor;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }
}
