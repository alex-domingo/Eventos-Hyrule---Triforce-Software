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
public class Pago {

    private Long id; // AUTO_INCREMENT
    private String correo; // FK participante
    private String codigoEvento; // FK evento
    private String metodo; // Metodo de pago: efectivo, transferencia o tarjeta
    private double monto; // >= 0
    private LocalDateTime creadoEn;

    public Pago() {
    }

    public Pago(String correo, String codigoEvento, String metodo, double monto) {
        this.correo = correo;
        this.codigoEvento = codigoEvento;
        this.metodo = metodo;
        this.monto = monto;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigoEvento() {
        return codigoEvento;
    }

    public void setCodigoEvento(String codigoEvento) {
        this.codigoEvento = codigoEvento;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
