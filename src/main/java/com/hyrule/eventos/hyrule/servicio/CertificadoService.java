/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.util.ArchivoUtil;
import com.hyrule.eventos.hyrule.util.Configuracion;
import com.hyrule.eventos.hyrule.util.Html;

import java.io.File;
import java.sql.*;

/**
 *
 * @author Ludvi
 */
public class CertificadoService {

    public File emitir(String correo, String codigoEvento) {
        String nombre = null, tituloEvento = null;

        String sqlValidacion = """
            SELECT p.nombre_completo, e.titulo,
                   (SELECT COUNT(*) FROM asistencia a
                    JOIN actividad ac ON ac.codigo = a.codigo_actividad
                    WHERE a.correo = i.correo AND ac.codigo_evento = i.codigo_evento) AS asistencias
            FROM inscripcion i
            JOIN participante p ON p.correo = i.correo
            JOIN evento e ON e.codigo = i.codigo_evento
            WHERE i.correo=? AND i.codigo_evento=? AND i.estado='validada'
            LIMIT 1
        """;

        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sqlValidacion)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalStateException("Inscripcion no valida o no encontrada.");
                }
                int asistencias = rs.getInt("asistencias");
                if (asistencias < 1) {
                    throw new IllegalStateException("No hay asistencias registradas para emitir certificado.");
                }
                nombre = rs.getString("nombre_completo");
                tituloEvento = rs.getString("titulo");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error SQL: " + e.getMessage(), e);
        }

        String cuerpo = """
            <p>Se certifica que <strong>%s</strong> participó satisfactoriamente en el evento
            <strong>%s</strong>.</p>
        """.formatted(nombre, tituloEvento);

        String html = Html.plantilla("Certificado — " + nombre, cuerpo);

        try {
            File archivo = ArchivoUtil.escribir(Configuracion.RUTA_SALIDA,
                    "certificado_" + correo.replace("@", "_at_") + "_" + codigoEvento + ".html",
                    html);
            insertarRegistroCertificado(correo, codigoEvento, archivo.getAbsolutePath());
            return archivo;
        } catch (Exception e) {
            throw new RuntimeException("No se pudo escribir el certificado: " + e.getMessage(), e);
        }
    }

    private void insertarRegistroCertificado(String correo, String codigoEvento, String ruta) {
        String sql = "INSERT INTO certificado (correo, codigo_evento, ruta) VALUES (?,?,?) "
                + "ON DUPLICATE KEY UPDATE ruta=VALUES(ruta)";
        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, codigoEvento);
            ps.setString(3, ruta);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Aviso: no se pudo registrar el certificado: " + e.getMessage());
        }
    }
}
