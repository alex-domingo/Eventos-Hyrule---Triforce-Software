/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

/**
 *
 * @author Ludvi
 */
public class ResultadoPago {

    public final boolean exito;
    public final String mensaje;

    public ResultadoPago(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return (exito ? "OK: " : "ERROR: ") + mensaje;
    }
}
