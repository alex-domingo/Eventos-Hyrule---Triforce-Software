/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.dao.EventoDAO;
import com.hyrule.eventos.hyrule.modelo.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 *
 * @author Ludvi
 */
public class JIFEventos extends JInternalFrame {

    private final JTextField txtCodigo = new JTextField(12);
    private final JTextField txtFecha = new JTextField(10); // yyyy-MM-dd
    private final JComboBox<String> cboTipo = new JComboBox<>(new String[]{"charla", "congreso", "taller", "debate"});
    private final JTextField txtTitulo = new JTextField(30);
    private final JTextField txtLugar = new JTextField(25);
    private final JSpinner spCupo = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JSpinner spTarifa = new JSpinner(new SpinnerNumberModel(0.00, 0.00, 1_000_000.00, 1.00));

    private final JTable tabla = new JTable();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Código", "Fecha", "Tipo", "Título", "Lugar", "Cupo", "Tarifa"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final EventoDAO dao = new EventoDAO(null); // Usamos DriverManager con DBConnection internamente

    public JIFEventos() {
        super("Gestión de eventos", true, true, true, true);
        setSize(900, 500);
        setLayout(new BorderLayout());

        // Formulario (arriba)
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Código:", txtCodigo);
        addRow(form, gc, y++, "Fecha (yyyy-MM-dd):", txtFecha);
        addRow(form, gc, y++, "Tipo:", cboTipo);
        addRow(form, gc, y++, "Título:", txtTitulo);
        addRow(form, gc, y++, "Lugar:", txtLugar);
        addRow(form, gc, y++, "Cupo:", spCupo);
        addRow(form, gc, y++, "Tarifa:", spTarifa);

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

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);

        // Tabla (abajo)
        tabla.setModel(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Eventos
        btnNuevo.addActionListener(e -> limpiar());
        btnRefrescar.addActionListener(e -> cargarTabla());
        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int i = tabla.getSelectedRow();
                txtCodigo.setText(modelo.getValueAt(i, 0).toString());
                txtFecha.setText(modelo.getValueAt(i, 1).toString());
                cboTipo.setSelectedItem(modelo.getValueAt(i, 2));
                txtTitulo.setText(modelo.getValueAt(i, 3).toString());
                txtLugar.setText(modelo.getValueAt(i, 4).toString());
                spCupo.setValue(Integer.parseInt(modelo.getValueAt(i, 5).toString()));
                spTarifa.setValue(Double.parseDouble(modelo.getValueAt(i, 6).toString()));
                txtCodigo.setEnabled(false);
            }
        });

        cargarTabla();
    }

    private void addRow(JPanel form, GridBagConstraints gc, int y, String label, JComponent comp) {
        gc.gridx = 0;
        gc.gridy = y;
        form.add(new JLabel(label), gc);
        gc.gridx = 1;
        form.add(comp, gc);
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtCodigo.setEnabled(true);
        txtFecha.setText("");
        cboTipo.setSelectedIndex(0);
        txtTitulo.setText("");
        txtLugar.setText("");
        spCupo.setValue(0);
        spTarifa.setValue(0.00);
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        dao.listar().forEach(e -> modelo.addRow(new Object[]{
            e.getCodigo(), e.getFechaEvento(), e.getTipo(), e.getTitulo(),
            e.getLugar(), e.getCupo(), String.format("%.2f", e.getTarifa())
        }));
    }

    private void guardar() {
        try {
            Evento e = leerFormulario(true);
            boolean ok = dao.crear(e);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Guardado");
                cargarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizar() {
        try {
            Evento e = leerFormulario(false);
            boolean ok = dao.actualizar(e);
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
        String codigo = txtCodigo.getText().trim();
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento en la tabla", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "¿Eliminar evento " + codigo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (r == JOptionPane.YES_OPTION) {
            boolean ok = dao.eliminar(codigo);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Eliminado");
                cargarTabla();
                limpiar();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar (¿tiene inscripciones/actividades?)", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Evento leerFormulario(boolean validarPK) {
        String codigo = txtCodigo.getText().trim();
        String fecha = txtFecha.getText().trim();
        String tipo = (String) cboTipo.getSelectedItem();
        String titulo = txtTitulo.getText().trim();
        String lugar = txtLugar.getText().trim();
        int cupo = ((Number) spCupo.getValue()).intValue();
        double tarifa = ((Number) spTarifa.getValue()).doubleValue();

        // Validaciones minimas
        if (validarPK && (codigo.isEmpty() || !codigo.matches("^EVT-\\d{8}$"))) {
            throw new IllegalArgumentException("Código inválido. Formato: EVT-00000001");
        }
        if (fecha.isEmpty()) {
            throw new IllegalArgumentException("Fecha requerida (yyyy-MM-dd).");
        }
        if (titulo.isEmpty()) {
            throw new IllegalArgumentException("Título requerido.");
        }
        if (lugar.isEmpty()) {
            throw new IllegalArgumentException("Lugar requerido.");
        }
        if (cupo < 0) {
            throw new IllegalArgumentException("Cupo no puede ser negativo.");
        }
        if (tarifa < 0) {
            throw new IllegalArgumentException("Tarifa no puede ser negativa.");
        }

        Evento e = new Evento();
        e.setCodigo(codigo);
        e.setFechaEvento(LocalDate.parse(fecha)); // yyyy-MM-dd
        e.setTipo(tipo);
        e.setTitulo(titulo);
        e.setLugar(lugar);
        e.setCupo(cupo);
        e.setTarifa(tarifa);
        return e;
    }
}
