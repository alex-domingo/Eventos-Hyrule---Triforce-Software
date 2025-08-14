/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.dao.ParticipanteDAO;
import com.hyrule.eventos.hyrule.modelo.Participante;
import com.hyrule.eventos.hyrule.servicio.PagoService;
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

        // Creamos participante nuevo
        ParticipanteDAO pdao = new ParticipanteDAO();
        Participante nuevo = new Participante("malon@hyrule.org", "Malon Ranch", "estudiante", "Lon Lon Ranch");
        System.out.println("Crear participante (malon): " + pdao.crear(nuevo));

        // Creamos inscripcion pendiente al evento pago EVT-00000001
        InscripcionDAO idao = new InscripcionDAO();
        Inscripcion ins = new Inscripcion("malon@hyrule.org", "EVT-00000001", "asistente", "pendiente");
        System.out.println("Crear inscripcion (malon): " + idao.crear(ins));

        // Intentamos validar inscripcion sin pagar (debe fallar por pago insuficiente)
        InscripcionService iservice = new InscripcionService();
        System.out.println("Validar sin pagar (malon): " + iservice.validarInscripcion("malon@hyrule.org", "EVT-00000001"));

        // Registramos el pago completo y revalidamos automaticamente la inscripcion
        PagoService pagoService = new PagoService();
        System.out.println("Registrar pago + revalidar (malon): "
                + pagoService.registrarPagoYRevalidar("malon@hyrule.org", "EVT-00000001", "efectivo", 75.00));

        // Verificamos el estado final de la inscripcion
        Inscripcion ver = idao.obtener("malon@hyrule.org", "EVT-00000001");
        System.out.println("Estado final (malon): " + (ver != null ? ver.getEstado() : "no existe"));
    }
}
