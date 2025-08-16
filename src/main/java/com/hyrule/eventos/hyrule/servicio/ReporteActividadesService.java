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
public class ReporteActividadesService {

    /*
    Lista de actividades de un evento con su conteo de asistencias. Filtros
    opcionales: tipo_actividad (charla,taller,debate,otra) y correo_instructor
     */
    public File generar(String codigoEvento, String tipoActividad, String correoInstructorLike) {
        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append("<table><thead><tr>")
                .append("<th>Código actividad</th>")
                .append("<th>Código evento</th>")
                .append("<th>Título actividad</th>")
                .append("<th>Nombre del encargado</th>")
                .append("<th>Hora inicio</th>")
                .append("<th>Cupo máximo</th>")
                .append("<th>Cantidad de participantes</th>")
                .append("</tr></thead><tbody>");

        String sql = """
            SELECT a.codigo,
                   a.codigo_evento,
                   a.titulo,
                   (SELECT p.nombre_completo FROM participante p WHERE p.correo = a.correo_instructor) AS nombre_encargado,
                   a.hora_inicio,
                   a.cupo,
                   (SELECT COUNT(*) FROM asistencia x WHERE x.codigo_actividad = a.codigo) AS total_asistencias
            FROM actividad a
            WHERE a.codigo_evento = ?
              AND (? IS NULL OR a.tipo = ?)
              AND (? IS NULL OR a.correo_instructor LIKE CONCAT('%', ?, '%'))
            ORDER BY a.codigo
        """;

        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoEvento);

            if (isBlank(tipoActividad)) {
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(2, tipoActividad);
                ps.setString(3, tipoActividad);
            }

            if (isBlank(correoInstructorLike)) {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(4, correoInstructorLike);
                ps.setString(5, correoInstructorLike);
            }

            try (ResultSet rs = ps.executeQuery()) {
                int filas = 0;
                while (rs.next()) {
                    cuerpo.append("<tr>")
                            .append(td(rs.getString("codigo")))
                            .append(td(rs.getString("codigo_evento")))
                            .append(td(rs.getString("titulo")))
                            .append(td(rs.getString("nombre_encargado")))
                            .append(td(rs.getTime("hora_inicio") != null ? rs.getTime("hora_inicio").toString() : ""))
                            .append(td(String.valueOf(rs.getInt("cupo"))))
                            .append(td(String.valueOf(rs.getInt("total_asistencias"))))
                            .append("</tr>");
                    filas++;
                }
                if (filas == 0) {
                    cuerpo.append("<tr><td colspan='7'>Sin resultados</td></tr>");
                }
            }
        } catch (SQLException e) {
            cuerpo.append("<tr><td colspan='7'>Error: ").append(e.getMessage()).append("</td></tr>");
        }

        cuerpo.append("</tbody></table>");
        String html = Html.plantilla("Reporte de Actividades — " + codigoEvento, cuerpo.toString());
        try {
            return ArchivoUtil.escribir(Configuracion.RUTA_SALIDA,
                    "reporte_actividades_" + codigoEvento + ".html", html);
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
