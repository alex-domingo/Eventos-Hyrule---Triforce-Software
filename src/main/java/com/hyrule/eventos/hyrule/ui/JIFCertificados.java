/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.ui;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import com.hyrule.eventos.hyrule.servicio.CertificadoService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ludvi
 */
public class JIFCertificados extends JInternalFrame {

    private final JTextField txtCorreo = new JTextField(28);
    private final JTextField txtEvento = new JTextField(12);
    private final JTextArea txtLog = new JTextArea();

    private final CertificadoService service = new CertificadoService();

    public JIFCertificados() {
        super("Emisión de certificados", true, true, true, true);
        setSize(720, 460);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 4, 4, 4);
        gc.anchor = GridBagConstraints.WEST;

        int y = 0;
        addRow(form, gc, y++, "Correo del participante:", txtCorreo);
        addRow(form, gc, y++, "Código del evento:", txtEvento);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnUno = new JButton("Emitir certificado (uno)");
        JButton btnTodos = new JButton("Emitir todos del evento");
        JButton btnAbrirCarpeta = new JButton("Abrir carpeta de certificados");
        acciones.add(btnUno);
        acciones.add(btnTodos);
        acciones.add(btnAbrirCarpeta);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(acciones, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        txtLog.setEditable(false);
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        // Acciones
        btnUno.addActionListener(e -> emitirUno());
        btnTodos.addActionListener(e -> emitirTodos());
        btnAbrirCarpeta.addActionListener(e -> abrirCarpeta());
    }

    private void emitirUno() {
        String c = txtCorreo.getText().trim();
        String ev = txtEvento.getText().trim();
        if (!c.contains("@")) {
            alerta("Correo inválido");
            return;
        }
        if (!ev.matches("^EVT-\\d{8}$")) {
            alerta("Código de evento inválido (EVT-00000001)");
            return;
        }
        try {
            File f = service.emitir(c, ev);
            log("OK -> " + f.getAbsolutePath());
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo emitir", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void emitirTodos() {
        String ev = txtEvento.getText().trim();
        if (!ev.matches("^EVT-\\d{8}$")) {
            alerta("Código de evento inválido (EVT-00000001)");
            return;
        }
        List<String> correos = listarElegibles(ev);
        if (correos.isEmpty()) {
            log("No hay participantes elegibles (validada + ≥1 asistencia) para " + ev);
            return;
        }
        int ok = 0, fail = 0;
        for (String c : correos) {
            try {
                File f = service.emitir(c, ev);
                log("OK -> " + f.getAbsolutePath());
                ok++;
            } catch (Exception ex) {
                log("Error (" + c + "): " + ex.getMessage());
                fail++;
            }
        }
        JOptionPane.showMessageDialog(this,
                "Proceso terminado. Éxitos: " + ok + ", Fallos: " + fail,
                "Emisión masiva", JOptionPane.INFORMATION_MESSAGE);
    }

    // Participantes con inscripcion VALIDADA y al menos 1 asistencia en cualquier actividad del evento
    private List<String> listarElegibles(String codigoEvento) {
        final String sql = """
            SELECT i.correo
            FROM inscripcion i
            WHERE i.codigo_evento = ?
              AND i.estado = 'validada'
              AND EXISTS (
                  SELECT 1
                  FROM asistencia a
                  JOIN actividad ac ON ac.codigo = a.codigo_actividad
                  WHERE a.correo = i.correo
                    AND ac.codigo_evento = i.codigo_evento
              )
        """;
        List<String> lista = new ArrayList<>();
        try (Connection cn = DriverManager.getConnection(DBConnection.URL, DBConnection.USER_NAME, DBConnection.PASSWORD); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoEvento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString(1));
                }
            }
        } catch (SQLException e) {
            log("Error listando elegibles: " + e.getMessage());
        }
        return lista;
    }

    private void abrirCarpeta() {
        try {
            // Usa la ruta configurada en Configuracion.RUTA_SALIDA
            String path = com.hyrule.eventos.hyrule.util.Configuracion.RUTA_SALIDA;
            Desktop.getDesktop().open(new java.io.File(path));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "No se pudo abrir la carpeta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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
