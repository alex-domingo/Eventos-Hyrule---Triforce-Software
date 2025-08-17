/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.modelo.Actividad;
import com.hyrule.eventos.hyrule.servicio.ActividadService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

/**
 *
 * @author Ludvi
 */
public class JIFActividades extends JInternalFrame {

    private final JTextField txtCodigo = new JTextField(12);
    private final JTextField txtEvento = new JTextField(12);
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"charla", "taller", "debate", "otra"});
    private final JTextField txtTitulo = new JTextField(30);
    private final JTextField txtInstructor = new JTextField(25);
    private final JTextField txtInicio = new JTextField(5); // HH:mm
    private final JTextField txtFin = new JTextField(5);    // HH:mm
    private final JSpinner spCupo = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JTextArea txtLog = new JTextArea();

    private final ActitudServiceFix aService = new ActitudServiceFix();

    // Con esto evitamos confusiones de nombre al copiar/pegar
    private static class ActitudServiceFix extends ActividadService {
    }

    public JIFActividades() {
        super("Gestión de actividades", true, true, true, true);
        setSize(800, 480);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Código actividad:", txtCodigo);
        addRow(form, gc, y++, "Código evento:", txtEvento);
        addRow(form, gc, y++, "Tipo:", cboTipo);
        addRow(form, gc, y++, "Título:", txtTitulo);
        addRow(form, gc, y++, "Correo instructor:", txtInstructor);
        addRow(form, gc, y++, "Hora inicio (HH:mm):", txtInicio);
        addRow(form, gc, y++, "Hora fin (HH:mm):", txtFin);
        addRow(form, gc, y++, "Cupo:", spCupo);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrear = new JButton("Crear actividad");
        acciones.add(btnCrear);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btnCrear.addActionListener(e -> {
            try {
                Actividad a = leer();
                boolean ok = aService.crearConValidacion(a);
                if (ok) {
                    log("Actividad creada.");
                } else {
                    log("No se pudo crear (valida instructor/horarios/FKs).");
                }
            } catch (Exception ex) {
                alerta(ex.getMessage());
            }
        });
    }

    private Actividad leer() {
        String cod = txtCodigo.getText().trim();
        String ev = txtEvento.getText().trim();
        String tipo = (String) cboTipo.getSelectedItem();
        String tit = txtTitulo.getText().trim();
        String inst = txtInstructor.getText().trim();
        String hi = txtInicio.getText().trim();
        String hf = txtFin.getText().trim();
        int cupo = ((Number) spCupo.getValue()).intValue();

        if (!cod.matches("^ACT-\\d{8}$")) {
            throw new IllegalArgumentException("Código actividad inválido (ACT-00000001).");
        }
        if (!ev.matches("^EVT-\\d{8}$")) {
            throw new IllegalArgumentException("Código evento inválido (EVT-00000001).");
        }
        if (tit.isEmpty()) {
            throw new IllegalArgumentException("Título requerido.");
        }
        if (!inst.contains("@")) {
            throw new IllegalArgumentException("Correo de instructor inválido.");
        }
        LocalTime t1 = LocalTime.parse(hi);
        LocalTime t2 = LocalTime.parse(hf);
        if (!t1.isBefore(t2)) {
            throw new IllegalArgumentException("Hora inicio debe ser menor a hora fin.");
        }
        if (cupo < 0) {
            throw new IllegalArgumentException("Cupo no puede ser negativo.");
        }

        return new Actividad(cod, ev, tipo, tit, inst, t1, t2, cupo);
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
