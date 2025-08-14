/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Inscripcion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class InscripcionDAO {

    // CREATE
    public boolean crear(Inscripcion ins) {
        final String sql = "INSERT INTO inscripcion (correo, codigo_evento, tipo, estado) VALUES (?, ?, ?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, ins.getCorreo());
            ps.setString(2, ins.getCodigoEvento());
            ps.setString(3, ins.getTipo());
            ps.setString(4, ins.getEstado());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error crear inscripcion: " + ex.getMessage());
            return false;
        }
    }

    // READ (por PK)
    public Inscripcion obtener(String correo, String codigoEvento) {
        final String sql = "SELECT correo, codigo_evento, tipo, estado, creado_en "
                + "FROM inscripcion WHERE correo=? AND codigo_evento=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener inscripcion: " + ex.getMessage());
        }
        return null;
    }

    // READ (listar por evento)
    public List<Inscripcion> listarPorEvento(String codigoEvento) {
        final String sql = "SELECT correo, codigo_evento, tipo, estado, creado_en "
                + "FROM inscripcion WHERE codigo_evento=? ORDER BY correo";
        List<Inscripcion> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listar inscripciones por evento: " + ex.getMessage());
        }
        return lista;
    }

    // READ (listar por correo)
    public List<Inscripcion> listarPorCorreo(String correo) {
        final String sql = "SELECT correo, codigo_evento, tipo, estado, creado_en "
                + "FROM inscripcion WHERE correo=? ORDER BY codigo_evento";
        List<Inscripcion> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listar inscripciones por correo: " + ex.getMessage());
        }
        return lista;
    }

    // UPDATE (estado)
    public boolean actualizarEstado(String correo, String codigoEvento, String nuevoEstado) {
        final String sql = "UPDATE inscripcion SET estado=? WHERE correo=? AND codigo_evento=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setString(2, correo);
            ps.setString(3, codigoEvento);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error actualizar estado inscripcion: " + ex.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(String correo, String codigoEvento) {
        final String sql = "DELETE FROM inscripcion WHERE correo=? AND codigo_evento=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            // Si existen pagos o asistencias relacionadas, MySQL impedirá borrar por FKs
            System.err.println("Error eliminar inscripcion: " + ex.getMessage());
            return false;
        }
    }

    // Lecturas de apoyo para validaciones (simples)
    public int contarValidadasPorEvento(String codigoEvento) {
        final String sql = "SELECT COUNT(*) FROM inscripcion WHERE codigo_evento=? AND estado='validada'";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error contar validadas: " + ex.getMessage());
            return 0;
        }
    }

    public int obtenerCupoEvento(String codigoEvento) {
        final String sql = "SELECT cupo FROM evento WHERE codigo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("cupo") : 0;
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener cupo evento: " + ex.getMessage());
            return 0;
        }
    }

    public double obtenerTarifaEvento(String codigoEvento) {
        final String sql = "SELECT tarifa FROM evento WHERE codigo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble("tarifa") : 0.0;
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener tarifa evento: " + ex.getMessage());
            return 0.0;
        }
    }

    public double sumarPagosInscripcion(String correo, String codigoEvento) {
        final String sql = "SELECT COALESCE(SUM(monto),0) FROM pago WHERE correo=? AND codigo_evento=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (SQLException ex) {
            System.err.println("Error sumar pagos: " + ex.getMessage());
            return 0.0;
        }
    }

    // Realizamos las consultas a nuestra base de datos "eventos_hyrule"
    private Connection obtener() throws SQLException {
        // Utilizamos el metodo DBConnection verificando su conexion anteriormente
        return DriverManager.getConnection(
                DBConnection.URL,
                DBConnection.USER_NAME,
                DBConnection.PASSWORD
        );
    }

    private Inscripcion mapear(ResultSet rs) throws SQLException {
        Inscripcion i = new Inscripcion();
        i.setCorreo(rs.getString("correo"));
        i.setCodigoEvento(rs.getString("codigo_evento"));
        i.setTipo(rs.getString("tipo"));
        i.setEstado(rs.getString("estado"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) {
            i.setCreadoEn(ts.toLocalDateTime());
        }
        return i;
    }
}
