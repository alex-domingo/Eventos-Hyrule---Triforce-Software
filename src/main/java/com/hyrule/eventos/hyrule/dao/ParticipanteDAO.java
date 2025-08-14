/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dao;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Participante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class ParticipanteDAO {

    // CREATE
    public boolean crear(Participante p) {
        final String sql = "INSERT INTO participante (correo, nombre_completo, tipo, institucion) VALUES (?, ?, ?, ?)";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getCorreo());
            ps.setString(2, p.getNombreCompleto());
            ps.setString(3, p.getTipo());
            ps.setString(4, p.getInstitucion());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error crear participante: " + ex.getMessage());
            return false;
        }
    }

    // READ (por correo (PK))
    public Participante obtenerPorCorreo(String correo) {
        final String sql = "SELECT correo, nombre_completo, tipo, institucion FROM participante WHERE correo = ?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error obtener participante: " + ex.getMessage());
        }
        return null;
    }

    // READ (lo hace por nombre)
    public List<Participante> buscarPorNombreLike(String termino) {
        final String sql = "SELECT correo, nombre_completo, tipo, institucion "
                + "FROM participante WHERE nombre_completo LIKE ? ORDER BY nombre_completo";
        List<Participante> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, "%" + termino + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error buscar participantes: " + ex.getMessage());
        }
        return lista;
    }

    // READ (listar todos)
    public List<Participante> listar() {
        final String sql = "SELECT correo, nombre_completo, tipo, institucion FROM participante ORDER BY nombre_completo";
        List<Participante> lista = new ArrayList<>();
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException ex) {
            System.err.println("Error listar participantes: " + ex.getMessage());
        }
        return lista;
    }

    // UPDATE
    public boolean actualizar(Participante p) {
        final String sql = "UPDATE participante SET nombre_completo=?, tipo=?, institucion=? WHERE correo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, p.getNombreCompleto());
            ps.setString(2, p.getTipo());
            ps.setString(3, p.getInstitucion());
            ps.setString(4, p.getCorreo());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("Error actualizar participante: " + ex.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean eliminar(String correo) {
        final String sql = "DELETE FROM participante WHERE correo=?";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            // Si hay FKs (inscripcion, actividad como instructor), MySQL impedirá borrarlo.
            System.err.println("Error eliminar participante: " + ex.getMessage());
            return false;
        }
    }

    // Utilidad (verificar existencia)
    public boolean existePorCorreo(String correo) {
        final String sql = "SELECT 1 FROM participante WHERE correo=? LIMIT 1";
        try (Connection cn = obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            System.err.println("Error existePorCorreo: " + ex.getMessage());
            return false;
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

    private Participante mapear(ResultSet rs) throws SQLException {
        Participante p = new Participante();
        p.setCorreo(rs.getString("correo"));
        p.setNombreCompleto(rs.getString("nombre_completo"));
        p.setTipo(rs.getString("tipo"));
        p.setInstitucion(rs.getString("institucion"));
        return p;
    }
}
