/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.servicio.AsistenciaService;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Ludvi
 */
public class JIFAsistencias extends JInternalFrame {

    private final JTextField txtCorreo = new JTextField(25);
    private final JTextField txtActividad = new JTextField(12);
    private final JTextArea txtLog = new JTextArea();

    private final AsistenciaService service = new AsistenciaService();

    public JIFAsistencias() {
        super("Registro de asistencias", true, true, true, true);
        setSize(600, 380);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Correo del participante:", txtCorreo);
        addRow(form, gc, y++, "Código de la actividad:", txtActividad);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnReg = new JButton("Registrar asistencia");
        acciones.add(btnReg);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btnReg.addActionListener(e -> {
            String c = txtCorreo.getText().trim();
            String act = txtActividad.getText().trim();
            if (c.isEmpty() || !c.contains("@")) {
                alerta("Correo inválido");
                return;
            }
            if (!act.matches("^ACT-\\d{8}$")) {
                alerta("Código de actividad inválido");
                return;
            }
            boolean ok = service.registrarAsistencia(c, act);
            if (ok) {
                log("Asistencia registrada.");
            } else {
                log("No se registró (valide estado/cupo/duplicado).");
            }
        });
    }

    private void addRow(JPanel p, GridBagConstraints gc, int y, String lab, JComponent comp) {
        gc.gridx = 0;
        gc.gridy = y;
        p.add(new JLabel(lab), gc);
        gc.gridx = 1;
        p.add(comp, gc);
    }

    private void alerta(String s) {
        JOptionPane.showMessageDialog(this, s, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    private void log(String s) {
        txtLog.append(s + "\n");
    }
}
