/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.servicio.PagoService;
import com.hyrule.eventos.hyrule.servicio.ResultadoPago;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Ludvi
 */
public class JIFPagos extends JInternalFrame {

    private final JTextField txtCorreo = new JTextField(25);
    private final JTextField txtEvento = new JTextField(12);
    private final JComboBox<String> cboMetodo = new JComboBox<>(new String[]{"efectivo", "transferencia", "tarjeta"});
    private final JSpinner spMonto = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 1.00));
    private final JTextArea txtLog = new JTextArea();

    private final PagoService service = new PagoService();

    public JIFPagos() {
        super("Registro de pagos", true, true, true, true);
        setSize(650, 430);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Correo del participante:", txtCorreo);
        addRow(form, gc, y++, "Código del evento:", txtEvento);
        addRow(form, gc, y++, "Método de pago:", cboMetodo);
        addRow(form, gc, y++, "Monto (Q):", spMonto);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPagar = new JButton("Registrar pago");
        acciones.add(btnPagar);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        btnPagar.addActionListener(e -> {
            String c = txtCorreo.getText().trim();
            String ev = txtEvento.getText().trim();
            String m = (String) cboMetodo.getSelectedItem();
            double monto = ((Number) spMonto.getValue()).doubleValue();

            if (c.isEmpty() || !c.contains("@")) {
                alerta("Correo inválido");
                return;
            }
            if (ev.isEmpty() || !ev.matches("^EVT-\\d{8}$")) {
                alerta("Código de evento inválido");
                return;
            }

            ResultadoPago res = service.registrarPagoExacto(c, ev, m, monto);
            if (res.exito) {
                JOptionPane.showMessageDialog(this, res.mensaje, "Pago", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, res.mensaje, "Pago rechazado", JOptionPane.WARNING_MESSAGE);
            }
            txtLog.append(res.toString() + "\n");
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
