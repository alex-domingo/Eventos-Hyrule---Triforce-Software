/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Pago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class PagoDAO {

    // CREATE
    public boolean crear(Pago p) {
        final String sql = "INSERT INTO pago (correo, codigo_evento, metodo, monto) VALUES (?, ?, ?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCorreo());
            ps.setString(2, p.getCodigoEvento());
            ps.setString(3, p.getMetodo());
            ps.setDouble(4, p.getMonto());
            int ok = ps.executeUpdate();
            if (ok == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException ex) {
            System.err.println("Error crear pago: " + ex.getMessage());
        }
        return false;
    }

    // Listamos todos los pagos por correo (usuario) y codigo (evento)
    public List<Pago> listarPorInscripcion(String correo, String codigoEvento) {
        final String sql = "SELECT id, correo, codigo_evento, metodo, monto, creado_en "
                + "FROM pago WHERE correo=? AND codigo_evento=? ORDER BY creado_en";
        List<Pago> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error listar pagos: " + ex.getMessage());
        }
        return lista;
    }

    // Calculamos el total pagado por un usuario en un evento especifico
    public double totalPagado(String correo, String codigoEvento) {
        final String sql = "SELECT COALESCE(SUM(monto),0) FROM pago WHERE correo=? AND codigo_evento=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }
        } catch (SQLException ex) {
            System.err.println("Error totalPagado: " + ex.getMessage());
            return 0.0;
        }
    }

    private Pago mapear(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setId(rs.getLong("id"));
        p.setCorreo(rs.getString("correo"));
        p.setCodigoEvento(rs.getString("codigo_evento"));
        p.setMetodo(rs.getString("metodo"));
        p.setMonto(rs.getDouble("monto"));
        Timestamp ts = rs.getTimestamp("creado_en");
        if (ts != null) {
            p.setCreadoEn(ts.toLocalDateTime());
        }
        return p;
    }

    // Realizamos las consultas a nuestra base de datos "eventos_hyrule"
    private Connection obtener() throws SQLException {

        // Utilizamos el metodo DBConnection verificando su conexion anteriormente
        return DriverManager.getConnection(
                DBConnection.URL,
                DBConnection.USER_NAME,
                DBConnection.PASSWORD);
    }
}
