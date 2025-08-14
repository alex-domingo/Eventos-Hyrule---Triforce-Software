/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.dao.InscripcionDAO;
import com.hyrule.eventos.hyrule.modelo.Inscripcion;
import com.hyrule.eventos.hyrule.servicio.InscripcionService;

/**
 *
 * @author Ludvi
 */
public class EventosHyrule {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DBConnection connection = new DBConnection();
        connection.connect();

        // Pruebas de validacion inscripcion
        InscripcionDAO idao = new InscripcionDAO();

        // Crea una inscripcion pendiente (para un participante y evento existentes)
        Inscripcion ins = new Inscripcion("zelda@hyrule.edu", "EVT-00000001", "asistente", "pendiente");
        System.out.println("Crear inscripcion: " + idao.crear(ins));

        // Listar por evento
        System.out.println("Inscripciones EVT-00000001: " + idao.listarPorEvento("EVT-00000001").size());

        // Validar (aplica reglas: pagos suficientes + cupo)
        InscripcionService service = new InscripcionService();
        System.out.println("Validar inscripcion: " + service.validarInscripcion("zelda@hyrule.edu", "EVT-00000001"));

        // Verificar estado
        Inscripcion ver = idao.obtener("zelda@hyrule.edu", "EVT-00000001");
        System.out.println("Estado actual: " + (ver != null ? ver.getEstado() : "no existe"));
    }
}
