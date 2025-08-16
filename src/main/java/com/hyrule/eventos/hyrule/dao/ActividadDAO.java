/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Actividad;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class ActividadDAO {

    public boolean crear(Actividad a) {
        final String sql = "INSERT INTO actividad (codigo, codigo_evento, tipo, titulo, correo_instructor, hora_inicio, hora_fin, cupo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, a.getCodigo());
            ps.setString(2, a.getCodigoEvento());
            ps.setString(3, a.getTipo());
            ps.setString(4, a.getTitulo());
            ps.setString(5, a.getCorreoInstructor());
            ps.setTime(6, Time.valueOf(a.getHoraInicio()));
            ps.setTime(7, Time.valueOf(a.getHoraFin()));
            ps.setInt(8, a.getCupo());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error crear actividad: " + ex.getMessage());
            return false;
        }
    }

    public Actividad obtenerPorCodigo(String codigo) {
        final String sql = "SELECT codigo, codigo_evento, tipo, titulo, correo_instructor, hora_inicio, hora_fin, cupo "
                + "FROM actividad WHERE codigo = ?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener actividad: " + ex.getMessage());
        }
        return null;
    }

    public List<Actividad> listarPorEvento(String codigoEvento) {
        final String sql = "SELECT codigo, codigo_evento, tipo, titulo, correo_instructor, hora_inicio, hora_fin, cupo "
                + "FROM actividad WHERE codigo_evento=? ORDER BY codigo";
        List<Actividad> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listar actividades: " + ex.getMessage());
        }
        return lista;
    }

    public int contarAsistencias(String codigoActividad) {
        final String sql = "SELECT COUNT(*) FROM asistencia WHERE codigo_actividad=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoActividad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error contar asistencias: " + ex.getMessage());
            return 0;
        }
    }

    public int obtenerCupo(String codigoActividad) {
        final String sql = "SELECT cupo FROM actividad WHERE codigo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoActividad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cupo") : 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener cupo actividad: " + ex.getMessage());
            return 0;
        }
    }

    public String obtenerEventoDeActividad(String codigoActividad) {
        final String sql = "SELECT codigo_evento FROM actividad WHERE codigo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoActividad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener evento de actividad: " + ex.getMessage());
            return null;
        }
    }

    private Actividad mapear(ResultSet rs) throws SQLException {
        Actividad a = new Actividad();
        a.setCodigo(rs.getString("codigo"));
        a.setCodigoEvento(rs.getString("codigo_evento"));
        a.setTipo(rs.getString("tipo"));
        a.setTitulo(rs.getString("titulo"));
        a.setCorreoInstructor(rs.getString("correo_instructor"));
        Time hi = rs.getTime("hora_inicio");
        Time hf = rs.getTime("hora_fin");
        a.setHoraInicio(hi != null ? hi.toLocalTime() : LocalTime.of(0, 0));
        a.setHoraFin(hf != null ? hf.toLocalTime() : LocalTime.of(0, 0));
        a.setCupo(rs.getInt("cupo"));
        return a;
    }

    private Connection obtener() throws SQLException {
        return DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD);
    }
}
