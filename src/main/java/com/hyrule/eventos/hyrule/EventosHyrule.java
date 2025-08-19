/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.hyrule.eventos.hyrule;

import com.hyrule.eventos.hyrule.dbconnection.DBConnection;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ludvi
 */
public class EventosHyrule {
    public static void main(String[] args) {

        System.out.println("Hello World!");
        DBConnection connection = new DBConnection();
        connection.connect();

        // Llamamos a VentanaPrincipal para iniciar directamente la interfaz grafica
        SwingUtilities.invokeLater(() -> new com.hyrule.eventos.hyrule.ui.VentanaPrincipal().setVisible(true));
    }
}
