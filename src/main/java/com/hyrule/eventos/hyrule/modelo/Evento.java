/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.modelo;

import java.time.LocalDate;

/**
 *
 * @author Ludvi
 */
public class Evento {

    private String codigo; // PK: EVT-00000001
    private LocalDate fechaEvento;
    private String tipo; // Tipo de evento: charla, congreso, taller o debate
    private String titulo;
    private String lugar;
    private int cupo;
    private double tarifa;

    public Evento() {
    }

    public Evento(String codigo, LocalDate fechaEvento, String tipo,
            String titulo, String lugar, int cupo, double tarifa) {
        this.codigo = codigo;
        this.fechaEvento = fechaEvento;
        this.tipo = tipo;
        this.titulo = titulo;
        this.lugar = lugar;
        this.cupo = cupo;
        this.tarifa = tarifa;
    }

    // Getters y setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }
}
