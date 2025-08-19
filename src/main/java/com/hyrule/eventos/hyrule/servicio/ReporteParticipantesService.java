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
public class ReporteParticipantesService {

    /*
    Generamos un HTML con los participantes de un evento. Filtros opcionales:
    tipo de participante (estudiante, profesional o invitado) e institucion (procedencia)
     */
    public File generar(String codigoEvento, String tipoParticipante, String institucionLike) {
        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append("<table><thead><tr>")
                .append("<th>Correo electrónico</th>")
                .append("<th>Tipo de participante</th>")
                .append("<th>Nombre completo</th>")
                .append("<th>Institución</th>")
                .append("<th>¿Inscripción válida?</th>")
                .append("</tr></thead><tbody>");

        String sql = """
            SELECT p.correo,
                   p.tipo            AS tipo_participante,
                   p.nombre_completo AS nombre,
                   p.institucion,
                   CASE WHEN i.estado = 'validada' THEN 'SI' ELSE 'NO' END AS inscripcion_valida
            FROM inscripcion i
            JOIN participante p ON p.correo = i.correo
            WHERE i.codigo_evento = ?
              AND (? IS NULL OR p.tipo = ?)
              AND (? IS NULL OR p.institucion LIKE CONCAT('%', ?, '%'))
            ORDER BY p.nombre_completo
        """;

        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoEvento);

            if (isBlank(tipoParticipante)) {
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(2, tipoParticipante);
                ps.setString(3, tipoParticipante);
            }

            if (isBlank(institucionLike)) {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(4, institucionLike);
                ps.setString(5, institucionLike);
            }

            try (ResultSet rs = ps.executeQuery()) {
                int filas = 0;
                while (rs.next()) {
                    cuerpo.append("<tr>")
                            .append(td(rs.getString("correo")))
                            .append(td(rs.getString("tipo_participante")))
                            .append(td(rs.getString("nombre")))
                            .append(td(rs.getString("institucion")))
                            .append(td(rs.getString("inscripcion_valida")))
                            .append("</tr>");
                    filas++;
                }
                if (filas == 0) {
                    cuerpo.append("<tr><td colspan='5'>Sin resultados</td></tr>");
                }
            }
        } catch (SQLException e) {
            cuerpo.append("<tr><td colspan='5'>Error: ").append(e.getMessage()).append("</td></tr>");
        }

        cuerpo.append("</tbody></table>");
        String html = Html.plantilla("Reporte de Participantes — " + codigoEvento, cuerpo.toString());
        try {
            return ArchivoUtil.escribir(Configuracion.RUTA_SALIDA,
                    "reporte_participantes_" + codigoEvento + ".html", html);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo escribir el archivo HTML: " + e.getMessage(), e);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static String td(String s) {
        return "<td>" + (s == null ? "" : s) + "</td>";
    }
}
