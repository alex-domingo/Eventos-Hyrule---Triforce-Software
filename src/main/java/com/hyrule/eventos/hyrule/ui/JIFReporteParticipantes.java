/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.servicio.ReporteParticipantesService;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 *
 * @author Ludvi
 */
public class JIFReporteParticipantes extends JInternalFrame {

    private final JTextField txtEvento = new JTextField("EVT-00000001", 12);
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"", "estudiante", "profesional", "invitado"});
    private final JTextField txtInst = new JTextField(15);
    private final JTextArea txtLog = new JTextArea();

    public JIFReporteParticipantes() {
        super("Reporte: Participantes por evento", true, true, true, true);
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Evento:"));
        top.add(txtEvento);
        top.add(new JLabel("Tipo:"));
        top.add(cboTipo);
        top.add(new JLabel("Institución contiene:"));
        top.add(txtInst);
        JButton btn = new JButton("Generar");
        top.add(btn);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btn.addActionListener(e -> {
            String ev = txtEvento.getText().trim();
            String t = (String) cboTipo.getSelectedItem();
            String inst = txtInst.getText().trim();
            try {
                File f = new ReporteParticipantesService().generar(ev, t, inst);
                txtLog.append("OK -> " + f.getAbsolutePath() + "\n");
            } catch (Exception ex) {
                txtLog.append("Error: " + ex.getMessage() + "\n");
            }
        });
    }
}
