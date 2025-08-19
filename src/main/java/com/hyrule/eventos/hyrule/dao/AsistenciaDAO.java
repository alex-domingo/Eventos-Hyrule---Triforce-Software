/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Asistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class AsistenciaDAO {

    // CREATE
    public boolean crear(Asistencia a) {
        final String sql = "INSERT INTO asistencia (correo, codigo_actividad) VALUES (?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getCorreo());
            ps.setString(2, a.getCodigoActividad());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error crear asistencia: " + ex.getMessage());
            return false;
        }
    }

    // Verificamos si existe una asistencia registrada para un usuario en una actividad especifica
    public boolean existe(String correo, String codigoActividad) {
        final String sql = "SELECT 1 FROM asistencia WHERE correo=? AND codigo_actividad=? LIMIT 1";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoActividad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.err.println("Error existe asistencia: " + ex.getMessage());
            return false;
        }
    }

    // Listamos todas las asistencias registradas para una actividad especifica
    public List<Asistencia> listarPorActividad(String codigoActividad) {
        final String sql = "SELECT correo, codigo_actividad, asistio_en FROM asistencia WHERE codigo_actividad=? ORDER BY asistio_en";
        List<Asistencia> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoActividad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Asistencia a = new Asistencia();
                    a.setCorreo(rs.getString("correo"));
                    a.setCodigoActividad(rs.getString("codigo_actividad"));
                    Timestamp ts = rs.getTimestamp("asistio_en");
                    if (ts != null) {
                        a.setAsistioEn(ts.toLocalDateTime());
                    }
                    lista.add(a);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listar asistencias: " + ex.getMessage());
        }
        return lista;
    }

    // Utilizamos el metodo DBConnection verificando su conexion anteriormente
    private Connection obtener() throws SQLException {
        // Utilizamos el metodo DBConnection verificando su conexion anteriormente
        return DriverManager.getConnection(
                DBConnection.URL,
                DBConnection.USER_NAME,
                DBConnection.PASSWORD);
    }
}
