/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.dao.ParticipanteDAO;
import com.hyrule.eventos.hyrule.modelo.Participante;

/**
 *
 * @author Ludvi
 */
public class EventosHyrule {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        DBConnection connection = new DBConnection();
        connection.connect();

        // DAO
        ParticipanteDAO pdao = new ParticipanteDAO();

        // Pruebas directas a participante
        Participante p = new Participante(
                "demo@hyrule.org",
                "Demo Usuario",
                "estudiante",
                "Academia Hyliana"
        );

        System.out.println("Crear participante: " + pdao.crear(p));

        Participante p1 = pdao.obtenerPorCorreo("demo@hyrule.org");
        System.out.println("Obtener participante: " + (p1 != null ? p1.getNombreCompleto() : "no existe"));

        p1.setNombreCompleto("Demo Usuario Actualizado");
        p1.setTipo("profesional");
        System.out.println("Actualizar participante: " + pdao.actualizar(p1));

        System.out.println("Buscar por nombre LIKE 'Demo': " + pdao.buscarPorNombreLike("Demo").size());
        System.out.println("Listar participantes: " + pdao.listar().size());

        System.out.println("Eliminar participante: " + pdao.eliminar("demo@hyrule.org"));
    }
}
