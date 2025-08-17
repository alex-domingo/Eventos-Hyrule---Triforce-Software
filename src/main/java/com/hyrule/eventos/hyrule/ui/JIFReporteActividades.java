/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.servicio.ReporteActividadesService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author Ludvi
 */
public class JIFReporteActividades extends JInternalFrame {

    private final JTextField txtEvento = new JTextField("EVT-00000001", 12);
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"", "charla", "taller", "debate", "otra"});
    private final JTextField txtInst = new JTextField(15);
    private final JTextArea txtLog = new JTextArea();

    public JIFReporteActividades() {
        super("Reporte: Actividades por evento", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Evento:"));
        top.add(txtEvento);
        top.add(new JLabel("Tipo:"));
        top.add(cboTipo);
        top.add(new JLabel("Correo encargado contiene:"));
        top.add(txtInst);
        JButton btn = new JButton("Generar");
        top.add(btn);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String ev = txtEvento.getText().trim();
            String t = (String) cboTipo.getSelectedItem();
            String enc = txtInst.getText().trim();
            try {
                File f = new ReporteActividadesService().generar(ev, t, enc);
                txtLog.append("OK -> " + f.getAbsolutePath() + "\n");
            } catch (Exception ex) {
                txtLog.append("Error: " + ex.getMessage() + "\n");
            }
        });
    }
}
