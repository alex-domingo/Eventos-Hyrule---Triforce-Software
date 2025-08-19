/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Evento;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class EventoDAO {

    private final DBConnection db;

    public EventoDAO(DBConnection db) {
        this.db = db;
    }

    // CREATE
    public boolean crear(Evento e) {
        String sql = "INSERT INTO evento (codigo, fecha_evento, tipo, titulo, lugar, cupo, tarifa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, e.getCodigo());
            ps.setDate(2, Date.valueOf(e.getFechaEvento()));
            ps.setString(3, e.getTipo());
            ps.setString(4, e.getTitulo());
            ps.setString(5, e.getLugar());
            ps.setInt(6, e.getCupo());
            ps.setDouble(7, e.getTarifa());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error crear evento: " + ex.getMessage());
            return false;
        }
    }

    // READ (obtenener evento por codigo)
    public Evento obtenerPorCodigo(String codigo) {
        String sql = "SELECT codigo, fecha_evento, tipo, titulo, lugar, cupo, tarifa "
                + "FROM evento WHERE codigo = ?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener evento: " + ex.getMessage());
        }
        return null;
    }

    // READ (listar todos los eventos)
    public List<Evento> listar() {
        String sql = "SELECT codigo, fecha_evento, tipo, titulo, lugar, cupo, tarifa FROM evento ORDER BY codigo";
        List<Evento> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error listar eventos: " + ex.getMessage());
        }
        return lista;
    }

    // UPDATE
    public boolean actualizar(Evento e) {
        String sql = "UPDATE evento SET fecha_evento=?, tipo=?, titulo=?, lugar=?, cupo=?, tarifa=? WHERE codigo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(e.getFechaEvento()));
            ps.setString(2, e.getTipo());
            ps.setString(3, e.getTitulo());
            ps.setString(4, e.getLugar());
            ps.setInt(5, e.getCupo());
            ps.setDouble(6, e.getTarifa());
            ps.setString(7, e.getCodigo());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error actualizar evento: " + ex.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM evento WHERE codigo = ?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error eliminar evento: " + ex.getMessage());
            return false;
        }
    }

    // Realizamos las consultas a nuestra base de datos "eventos_hyrule"
    private Connection obtener() throws SQLException {
        // Utilizamos el metodo DBConnection verificando su conexion anteriormente 
        return DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD);
    }

    private Evento mapear(ResultSet rs) throws SQLException {
        Evento e = new Evento();
        e.setCodigo(rs.getString("codigo"));
        Date d = rs.getDate("fecha_evento");
        e.setFechaEvento(d != null ? d.toLocalDate() : LocalDate.now());
        e.setTipo(rs.getString("tipo"));
        e.setTitulo(rs.getString("titulo"));
        e.setLugar(rs.getString("lugar"));
        e.setCupo(rs.getInt("cupo"));
        e.setTarifa(rs.getDouble("tarifa"));
        return e;
    }
}
