/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.servicio.ReporteEventosService;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author Ludvi
 */
public class JIFReportePagos extends JInternalFrame {

    private final JTextField txtEvento = new JTextField("EVT-00000001", 12);
    private final JTextArea txtLog = new JTextArea();

    public JIFReportePagos() {
        super("Reporte: Pagos por evento", true, true, true, true);
        setSize(550, 350);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Evento:"));
        top.add(txtEvento);
        JButton btn = new JButton("Generar");
        top.add(btn);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String ev = txtEvento.getText().trim();
            try {
                File f = new ReporteEventosService().generarPagosPorEvento(ev);
                txtLog.append("OK -> " + f.getAbsolutePath() + "\n");
            } catch (Exception ex) {
                txtLog.append("Error: " + ex.getMessage() + "\n");
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                try {
                    setMaximum(true);
                } catch (PropertyVetoException ignored) {
                }
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                try {
                    setMaximum(true);
                } catch (PropertyVetoException ignored) {
                }
            }
        });
    }
}
