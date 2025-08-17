/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 *
 * @author Ludvi
 */
public class VentanaPrincipal extends JFrame {

    private final JDesktopPane desktop = new JDesktopPane();

    public VentanaPrincipal() {
        setTitle("Eventos Hyrule — Backend Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(desktop, BorderLayout.CENTER);

        setJMenuBar(crearMenu());
    }

    private JMenuBar crearMenu() {
        JMenuBar bar = new JMenuBar();

        JMenu mArchivo = new JMenu("Archivo");
        JMenuItem miSalir = new JMenuItem(new AbstractAction("Salir") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mArchivo.add(miSalir);

        JMenu mGestion = new JMenu("Gestión");
        JMenuItem miEventos = new JMenuItem(new AbstractAction("Eventos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFEventos());
            }
        });
        JMenuItem miParticipantes = new JMenuItem(new AbstractAction("Participantes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFParticipantes());
            }
        });
        JMenuItem miInscripciones = new JMenuItem(new AbstractAction("Inscripciones") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFInscripciones());
            }
        });
        JMenuItem miPagos = new JMenuItem(new AbstractAction("Pagos") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFPagos());
            }
        });
        JMenuItem miActividades = new JMenuItem(new AbstractAction("Actividades") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFActividades());
            }
        });
        JMenuItem miAsistencias = new JMenuItem(new AbstractAction("Asistencias") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFAsistencias());
            }
        });
        mGestion.add(miEventos);
        mGestion.add(miParticipantes);
        mGestion.add(miInscripciones);
        mGestion.add(miPagos);
        mGestion.add(miActividades);
        mGestion.add(miAsistencias);

        JMenu mReportes = new JMenu("Reportes");
        JMenuItem miRptParticipantes = new JMenuItem(new AbstractAction("Participantes por evento") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFReporteParticipantes());
            }
        });
        JMenuItem miRptActividades = new JMenuItem(new AbstractAction("Actividades por evento") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFReporteActividades());
            }
        });
        JMenuItem miRptPagos = new JMenuItem(new AbstractAction("Pagos por evento") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFReportePagos());
            }
        });
        JMenuItem miCertificados = new JMenuItem(new AbstractAction("Emitir certificados") {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirInternal(new JIFCertificados());
            }
        });
        mReportes.add(miRptParticipantes);
        mReportes.add(miRptActividades);
        mReportes.add(miRptPagos);
        mReportes.add(miCertificados);

        bar.add(mArchivo);
        bar.add(mGestion);
        bar.add(mReportes);
        return bar;
    }

    private void abrirInternal(JInternalFrame jif) {
        for (JInternalFrame f : desktop.getAllFrames()) {
            if (f.getClass().equals(jif.getClass())) {
                try {
                    f.setSelected(true);
                } catch (Exception ignored) {
                }
                f.toFront();
                return;
            }
        }
        desktop.add(jif);
        jif.setVisible(true);
    }
}
