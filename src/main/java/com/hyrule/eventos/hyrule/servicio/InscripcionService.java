/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dao.InscripcionDAO;
import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.modelo.Inscripcion;

import java.sql.*;

/**
 *
 * @author Ludvi
 */
public class InscripcionService {

    private final InscripcionDAO dao = new InscripcionDAO();

    public boolean validarInscripcion(String correo, String codigoEvento) {
        // 1) Cargar inscripción
        Inscripcion ins = dao.obtener(correo, codigoEvento);
        if (ins == null) {
            System.err.println("Inscripcion no existe.");
            return false;
        }
        if (!"pendiente".equalsIgnoreCase(ins.getEstado())) {
            System.err.println("Inscripcion no esta en estado 'pendiente'.");
            return false;
        }

        // 2) Reglas de pago y cupo
        double tarifa = dao.obtenerTarifaEvento(codigoEvento);
        double totalPagado = dao.sumarPagosInscripcion(correo, codigoEvento);
        if (tarifa > 0 && totalPagado < tarifa) {
            System.err.printf("Pago insuficiente: Q%.2f de Q%.2f%n", totalPagado, tarifa);
            return false;
        }

        int cupo = dao.obtenerCupoEvento(codigoEvento);
        int validadas = dao.contarValidadasPorEvento(codigoEvento);
        if (validadas >= cupo) {
            System.err.println("Cupo del evento lleno.");
            return false;
        }

        // 3) Transaccion para actualizar estado
        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD)) {
            cn.setAutoCommit(false);
            try (PreparedStatement ps = cn.prepareStatement(
                    "UPDATE inscripcion SET estado='validada' WHERE correo=? AND codigo_evento=? AND estado='pendiente'")) {
                ps.setString(1, correo);
                ps.setString(2, codigoEvento);
                int updated = ps.executeUpdate();
                if (updated == 1) {
                    cn.commit();
                    return true;
                } else {
                    cn.rollback();
                    System.err.println("No se pudo validar (posible cambio concurrente).");
                    return false;
                }
            } catch (SQLException e) {
                cn.rollback();
                System.err.println("Error al validar: " + e.getMessage());
                return false;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexion en validacion: " + e.getMessage());
            return false;
        }
    }
}
