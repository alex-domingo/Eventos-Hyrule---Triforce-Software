/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dao.ActividadDAO;
import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Actividad;

import java.sql.*;

/**
 *
 * @author Ludvi
 */
public class ActividadService {

    private final ActividadDAO actividadDAO = new ActividadDAO();

    public boolean crearConValidacion(Actividad a) {
        // Verificamos que instructor este inscrito al evento y no como 'asistente'
        final String sql = "SELECT tipo FROM inscripcion WHERE correo=? AND codigo_evento=? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getCorreoInstructor());
            ps.setString(2, a.getCodigoEvento());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.err.println("Instructor no esta inscrito al evento.");
                    return false;
                }
                String tipoIns = rs.getString("tipo");
                if ("asistente".equalsIgnoreCase(tipoIns)) {
                    System.err.println("Instructor no puede ser 'asistente'.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error validando instructor: " + ex.getMessage());
            return false;
        }

        // Creamos actividad
        return actividadDAO.crear(a);
    }
}
