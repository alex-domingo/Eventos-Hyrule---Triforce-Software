/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.servicio.ReporteParticipantesService;
import com.hyrule.eventos.hyrule.servicio.CertificadoService;
import com.hyrule.eventos.hyrule.servicio.ReporteActividadesService;
import com.hyrule.eventos.hyrule.servicio.ReporteEventosService;

/**
 *
 * @author Ludvi
 */
public class EventosHyrule {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        DBConnection connection = new DBConnection();
        connection.connect();

        CertificadoService cs = new CertificadoService();
        System.out.println("Certificado: " + cs.emitir("malon@hyrule.org", "EVT-00000001").getAbsolutePath());

        var rp = new ReporteParticipantesService();
        System.out.println(rp.generar("EVT-00000001", "", "").getAbsolutePath());

        var ra = new ReporteActividadesService();
        System.out.println(ra.generar("EVT-00000001", "", "").getAbsolutePath());

        var re = new ReporteEventosService();
        System.out.println(re.generarPagosPorEvento("EVT-00000001").getAbsolutePath());

    }
}
