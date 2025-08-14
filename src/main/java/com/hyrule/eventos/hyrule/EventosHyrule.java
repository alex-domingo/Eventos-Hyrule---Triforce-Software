/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dao.EventoDAO;
import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Evento;
import java.time.LocalDate;

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
        EventoDAO dao = new EventoDAO(connection);

        // 1) Crear un evento de prueba
        Evento nuevo = new Evento(
                "EVT-00000003",
                LocalDate.of(2025, 10, 5),
                "debate",
                "Debate sobre reliquias antiguas",
                "Aula Magna",
                80,
                0.00
        );
        System.out.println("Crear: " + dao.crear(nuevo));

        // 2) Leer por codigo
        Evento e = dao.obtenerPorCodigo("EVT-00000003");
        System.out.println("Obtener: " + (e != null ? e.getTitulo() : "no encontrado"));

        // 3) Actualizar
        if (e != null) {
            e.setTitulo("Debate sobre reliquias antiguas (Actualizado)");
            System.out.println("Actualizar: " + dao.actualizar(e));
        }

        // 4) Listar
        System.out.println("Listar total: " + dao.listar().size());

        // 5) Eliminar
        System.out.println("Eliminar: " + dao.eliminar("EVT-00000003"));
    }
}
