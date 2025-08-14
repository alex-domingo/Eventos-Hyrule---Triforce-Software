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
public class Inscripcion {

    private String correo;          // PK (FK a participante)
    private String codigoEvento;    // PK (FK a evento)
    private String tipo;            // 'asistente','conferencista','tallerista','otro'
    private String estado;          // 'pendiente','validada','anulada'
    private LocalDateTime creadoEn; // timestamp

    public Inscripcion() {
    }

    public Inscripcion(String correo, String codigoEvento, String tipo, String estado) {
        this.correo = correo;
        this.codigoEvento = codigoEvento;
        this.tipo = tipo;
        this.estado = estado;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}
