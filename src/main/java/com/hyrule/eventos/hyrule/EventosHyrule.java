/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Actividad;
import com.hyrule.eventos.hyrule.servicio.ActividadService;
import com.hyrule.eventos.hyrule.servicio.AsistenciaService;

import java.time.LocalTime;

/**
 *
 * @author Ludvi
 */
public class EventosHyrule {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        DBConnection connection = new DBConnection();
        connection.connect();

        // Funcionalidades para actividad
        ActividadService aService = new ActividadService();
        Actividad nueva = new Actividad(
                "ACT-00000003",
                "EVT-00000001",
                "charla",
                "Charla de arqueria",
                "link@hyrule.org", // instructor valido en EVT-00000001
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                2
        );
        System.out.println("Crear actividad validada: " + aService.crearConValidacion(nueva));

        // Funcionalidades para asistencia
        AsistenciaService asistService = new AsistenciaService();

        // Registramos asistencia de Malon (la inscripcion debe ser validada ya que hay cupo)
        System.out.println("Asistencia Malon: " + asistService.registrarAsistencia("malon@hyrule.org", "ACT-00000003"));

        // Registramos asistencia de Zelda (la inscripcion debe ser validada ya que hay cupo)
        System.out.println("Asistencia Zelda: " + asistService.registrarAsistencia("zelda@hyrule.edu", "ACT-00000003"));

        // Intentamos registrar asistencia duplicada de Malon (debe fallar ya que Malon fue inscrito anteriormente)
        System.out.println("Asistencia Malon (duplicada): " + asistService.registrarAsistencia("malon@hyrule.org", "ACT-00000003"));

        // Intentamos registrar a un tercero para saturar cupo (deberia fallar en cupo lleno si ya hay 2)
        System.out.println("Asistencia Link (como asistente): " + asistService.registrarAsistencia("link@hyrule.org", "ACT-00000003"));
    }
}
