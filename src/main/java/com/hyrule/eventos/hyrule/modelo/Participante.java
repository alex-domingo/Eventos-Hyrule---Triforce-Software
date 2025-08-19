/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.modelo;

/**
 *
 * @author Ludvi
 */
public class Participante {

    private String correo; // PK
    private String nombreCompleto; // <= 45
    private String tipo; // Tipo: estudiante, profesional o invitado
    private String institucion; // <= 150

    public Participante() {
    }

    public Participante(String correo, String nombreCompleto, String tipo, String institucion) {
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
        this.institucion = institucion;
    }

    // Getters y setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getInstitucion() {
        return institucion;
    }

    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
}
