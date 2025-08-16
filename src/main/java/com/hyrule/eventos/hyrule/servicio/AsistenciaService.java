/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dao.ActividadDAO;
import com.hyrule.eventos.hyrule.dao.AsistenciaDAO;
import com.hyrule.eventos.hyrule.dbconnection.DBConnection;

import java.sql.*;

/**
 *
 * @author Ludvi
 */
public class AsistenciaService {

    private final ActividadDAO actividadDAO = new ActividadDAO();
    private final AsistenciaDAO asistenciaDAO = new AsistenciaDAO();

    public boolean registrarAsistencia(String correo, String codigoActividad) {
        // Tratamos de evitar asistencia duplicada
        if (asistenciaDAO.existe(correo, codigoActividad)) {
            System.err.println("Asistencia duplicada.");
            return false;
        }

        // Obtenemos el evento de la actividad
        String evento = actividadDAO.obtenerEventoDeActividad(codigoActividad);
        if (evento == null) {
            System.err.println("Actividad no existe.");
            return false;
        }

        // Verificamos si la inscripcion es VALIDADA al evento
        final String sqlVal = "SELECT estado FROM inscripcion WHERE correo=? AND codigo_evento=? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sqlVal)) {
            ps.setString(1, correo);
            ps.setString(2, evento);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("Participante no inscrito al evento de la actividad.");
                    return false;
                }
                if (!"validada".equalsIgnoreCase(rs.getString("estado"))) {
                    System.err.println("Inscripcion no esta validada.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error validando inscripcion: " + ex.getMessage());
            return false;
        }

        // Verificamo el cupo de la actividad
        int cupo = actividadDAO.obtenerCupo(codigoActividad);
        int ocupados = actividadDAO.contarAsistencias(codigoActividad);
        if (ocupados >= cupo) {
            System.err.println("Cupo de la actividad lleno.");
            return false;
        }

        // Insertamos la asistencia
        return asistenciaDAO.crear(new com.hyrule.eventos.hyrule.modelo.Asistencia(correo, codigoActividad));
    }
}
