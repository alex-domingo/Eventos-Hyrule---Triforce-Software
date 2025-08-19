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
public class ReporteEventosService {

    /*
    Reporte general de eventos con: cupo, todas las
    inscripciones validadas, monto total recaudado. Filtros opcionales:
    tipo de evento (charla, congreso, taller o debate), fecha desde/hasta
    y rango de cupo
     */
    public File generarPagosPorEvento(String codigoEvento) {
        StringBuilder cuerpo = new StringBuilder();
        cuerpo.append("<table><thead><tr>")
                .append("<th>Correo del participante</th>")
                .append("<th>Nombre del participante</th>")
                .append("<th>Método de pago</th>")
                .append("<th>Monto pagado (Q)</th>")
                .append("</tr></thead><tbody>");

        String sql = """
            SELECT p.correo,
                   p.nombre_completo,
                   pg.metodo,
                   pg.monto
            FROM pago pg
            JOIN participante p ON p.correo = pg.correo
            WHERE pg.codigo_evento = ?
            ORDER BY pg.creado_en, p.nombre_completo
        """;

        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoEvento);

            try (ResultSet rs = ps.executeQuery()) {
                int filas = 0;
                double total = 0.0;
                while (rs.next()) {
                    double monto = rs.getDouble("monto");
                    total += monto;

                    cuerpo.append("<tr>")
                            .append(td(rs.getString("correo")))
                            .append(td(rs.getString("nombre_completo")))
                            .append(td(rs.getString("metodo")))
                            .append(td(String.format("%.2f", monto)))
                            .append("</tr>");
                    filas++;
                }
                if (filas == 0) {
                    cuerpo.append("<tr><td colspan='4'>Sin pagos registrados</td></tr>");
                } else {
                    // Fila de total
                    cuerpo.append("<tr>")
                            .append("<td colspan='3' style='text-align:right'><strong>Total:</strong></td>")
                            .append("<td><strong>").append(String.format("%.2f", total)).append("</strong></td>")
                            .append("</tr>");
                }
            }
        } catch (SQLException e) {
            cuerpo.append("<tr><td colspan='4'>Error: ").append(e.getMessage()).append("</td></tr>");
        }

        cuerpo.append("</tbody></table>");
        String html = Html.plantilla("Reporte de Pagos — " + codigoEvento, cuerpo.toString());
        try {
            return ArchivoUtil.escribir(Configuracion.RUTA_SALIDA,
                    "reporte_evento_pagos_" + codigoEvento + ".html", html);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo escribir el archivo HTML: " + e.getMessage(), e);
        }
    }

    private static String td(String s) {
        return "<td>" + (s == null ? "" : s) + "</td>";
    }
}
