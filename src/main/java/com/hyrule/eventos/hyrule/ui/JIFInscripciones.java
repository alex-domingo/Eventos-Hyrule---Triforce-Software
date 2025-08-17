/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.dao.InscripcionDAO;
import com.hyrule.eventos.hyrule.modelo.Inscripcion;
import com.hyrule.eventos.hyrule.servicio.InscripcionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 *
 * @author Ludvi
 */
public class JIFInscripciones extends JInternalFrame {

    private final JTextField txtCorreo = new JTextField(25);
    private final JTextField txtEvento = new JTextField(12); // EVT-00000001
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"asistente", "conferencista", "tallerista", "otro"});
    private final JComboBox<String> cboEstado = new JComboBox<>(new String[]{"pendiente", "validada", "anulada"});

    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Correo", "Evento", "Tipo", "Estado", "Creado en"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final InscripcionDAO dao = new InscripcionDAO();
    private final InscripcionService service = new InscripcionService();

    public JIFInscripciones() {
        super("Gestión de inscripciones", true, true, true, true);
        setSize(950, 520);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Correo del participante:", txtCorreo);
        addRow(form, gc, y++, "Código del evento:", txtEvento);
        addRow(form, gc, y++, "Tipo inscripcion:", cboTipo);
        addRow(form, gc, y++, "Estado:", cboEstado);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnCrear = new JButton("Crear");
        JButton btnActualizar = new JButton("Actualizar estado");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListarEvento = new JButton("Listar por evento");
        JButton btnValidar = new JButton("Validar inscripcion");
        acciones.add(btnNuevo);
        acciones.add(btnCrear);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnListarEvento);
        acciones.add(btnValidar);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        tabla.setModel(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnNuevo.addActionListener(e -> limpiar());
        btnCrear.addActionListener(e -> crear());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnListarEvento.addActionListener(e -> cargarPorEvento());
        btnValidar.addActionListener(e -> validarInscripcion());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int i = tabla.getSelectedRow();
                txtCorreo.setText(modelo.getValueAt(i, 0).toString());
                txtEvento.setText(modelo.getValueAt(i, 1).toString());
                cboTipo.setSelectedItem(modelo.getValueAt(i, 2));
                cboEstado.setSelectedItem(modelo.getValueAt(i, 3));
                txtCorreo.setEnabled(false);
                txtEvento.setEnabled(false);
            }
        });

        cargarPorEvento();
    }

    private void addRow(JPanel form, GridBagConstraints gc, int y, String label, JComponent comp) {
        gc.gridx = 0;
        gc.gridy = y;
        form.add(new JLabel(label), gc);
        gc.gridx = 1;
        form.add(comp, gc);
    }

    private void limpiar() {
        txtCorreo.setEnabled(true);
        txtEvento.setEnabled(true);
        txtCorreo.setText("");
        txtEvento.setText("");
        cboTipo.setSelectedIndex(0);
        cboEstado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void crear() {
        try {
            Inscripcion i = leerFormulario(true);
            boolean ok = dao.crear(i);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Creada");
                cargarPorEvento();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear (¿duplicada o FK inválida?)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            Inscripcion i = leerFormulario(false);
            boolean ok = dao.actualizarEstado(i.getCorreo(), i.getCodigoEvento(), i.getEstado());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Actualizada");
                cargarPorEvento();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminar() {
        String c = txtCorreo.getText().trim();
        String ev = txtEvento.getText().trim();
        if (c.isEmpty() || ev.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una inscripción", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar " + c + " en " + ev + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            boolean ok = dao.eliminar(c, ev);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Eliminada");
                cargarPorEvento();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar (¿pagos/asistencias?)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarPorEvento() {
        String ev = txtEvento.getText().trim();
        modelo.setRowCount(0);
        if (ev.isEmpty()) {
            return;
        }
        dao.listarPorEvento(ev).forEach(i -> modelo.addRow(new Object[]{
            i.getCorreo(), i.getCodigoEvento(), i.getTipo(), i.getEstado(),
            i.getCreadoEn() != null ? i.getCreadoEn().toString() : ""
        }));
    }

    private void validarInscripcion() {
        String c = txtCorreo.getText().trim();
        String ev = txtEvento.getText().trim();
        if (c.isEmpty() || ev.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese correo y evento (o seleccione de la tabla)", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        boolean ok = service.validarInscripcion(c, ev);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Inscripción validada");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo validar (revise pagos/cupo/estado)", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
        cargarPorEvento();
    }

    private Inscripcion leerFormulario(boolean validarPK) {
        String c = txtCorreo.getText().trim();
        String ev = txtEvento.getText().trim();
        String t = (String) cboTipo.getSelectedItem();
        String es = (String) cboEstado.getSelectedItem();

        if (validarPK) {
            if (c.isEmpty() || !c.contains("@")) {
                throw new IllegalArgumentException("Correo inválido.");
            }
            if (ev.isEmpty() || !ev.matches("^EVT-\\d{8}$")) {
                throw new IllegalArgumentException("Código de evento inválido.");
            }
        }
        if (t == null || t.isBlank()) {
            throw new IllegalArgumentException("Tipo requerido.");
        }
        if (es == null || es.isBlank()) {
            throw new IllegalArgumentException("Estado requerido.");
        }

        return new Inscripcion(c, ev, t, es);
    }
}
