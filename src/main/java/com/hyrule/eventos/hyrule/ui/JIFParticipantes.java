/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.dao.ParticipanteDAO;
import com.hyrule.eventos.hyrule.modelo.Participante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyVetoException;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 *
 * @author Ludvi
 */
public class JIFParticipantes extends JInternalFrame {

    private final JTextField txtCorreo = new JTextField(25);
    private final JTextField txtNombre = new JTextField(25);
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"estudiante", "profesional", "invitado"});
    private final JTextField txtInst = new JTextField(25);
    private final JTextField txtBuscar = new JTextField(15);

    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Correo", "Nombre completo", "Tipo", "Institución"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final ParticipanteDAO dao = new ParticipanteDAO();

    public JIFParticipantes() {
        super("Gestión de participantes", true, true, true, true);
        setSize(900, 500);
        setLayout(new BorderLayout());

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Correo electrónico:", txtCorreo);
        addRow(form, gc, y++, "Nombre completo:", txtNombre);
        addRow(form, gc, y++, "Tipo:", cboTipo);
        addRow(form, gc, y++, "Institución:", txtInst);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        acciones.add(btnNuevo);
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnRefrescar);

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBuscar = new JButton("Buscar nombre");
        buscar.add(new JLabel("Nombre contiene:"));
        buscar.add(txtBuscar);
        buscar.add(btnBuscar);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.WEST);
        top.add(buscar, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // Tabla
        tabla.setModel(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Listeners
        btnNuevo.addActionListener(e -> limpiar());
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int i = tabla.getSelectedRow();
                txtCorreo.setText(modelo.getValueAt(i, 0).toString());
                txtNombre.setText(modelo.getValueAt(i, 1).toString());
                cboTipo.setSelectedItem(modelo.getValueAt(i, 2));
                txtInst.setText(modelo.getValueAt(i, 3).toString());
                txtCorreo.setEnabled(false);
            }
        });

        cargarTabla();

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

    private void addRow(JPanel form, GridBagConstraints gc, int y, String label, JComponent comp) {
        gc.gridx = 0;
        gc.gridy = y;
        form.add(new JLabel(label), gc);
        gc.gridx = 1;
        form.add(comp, gc);
    }

    private void limpiar() {
        txtCorreo.setEnabled(true);
        txtCorreo.setText("");
        txtNombre.setText("");
        txtInst.setText("");
        cboTipo.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        dao.listar().forEach(p -> modelo.addRow(new Object[]{
            p.getCorreo(), p.getNombreCompleto(), p.getTipo(), p.getInstitucion()
        }));
    }

    private void guardar() {
        try {
            Participante p = leerFormulario(true);
            boolean ok = dao.crear(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Guardado");
                cargarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar (¿correo duplicado?)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            Participante p = leerFormulario(false);
            boolean ok = dao.actualizar(p);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Actualizado");
                cargarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminar() {
        String correo = txtCorreo.getText().trim();
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un participante", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar " + correo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            boolean ok = dao.eliminar(correo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Eliminado");
                cargarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar (¿tiene inscripciones/actividades?)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscar() {
        String term = txtBuscar.getText().trim();
        modelo.setRowCount(0);
        dao.buscarPorNombreLike(term).forEach(p -> modelo.addRow(new Object[]{
            p.getCorreo(), p.getNombreCompleto(), p.getTipo(), p.getInstitucion()
        }));
    }

    private Participante leerFormulario(boolean validarPK) {
        String correo = txtCorreo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String tipo = (String) cboTipo.getSelectedItem();
        String inst = txtInst.getText().trim();

        if (validarPK && (correo.isEmpty() || !correo.contains("@"))) {
            throw new IllegalArgumentException("Correo electrónico inválido.");
        }
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("Nombre requerido.");
        }
        if (inst.isEmpty()) {
            throw new IllegalArgumentException("Institución requerida.");
        }

        Participante p = new Participante();
        p.setCorreo(correo);
        p.setNombreCompleto(nombre);
        p.setTipo(tipo);
        p.setInstitucion(inst);
        return p;
    }
}
