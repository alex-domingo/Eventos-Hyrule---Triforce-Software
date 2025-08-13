/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ludvi
 */
public class DBConnection {

    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String SCHEMA = "eventos_hyrule";
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "7777";

    public static final String URL = "jdbc:mysql://"
            + IP + ":" + PUERTO + "/" + SCHEMA;

    public static final String URL_FATAL = "jdbc:mysql://"
            + IP + ":" + PUERTO + "/" + SCHEMA + "?allowMultiQueries=true";

    private Connection connection;

    public void connect() {
        System.out.println("URL de conexion: " + URL);
        try {
            Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Catalogo: " + connection.getSchema());
            System.out.println("Esquema: " + connection.getCatalog());

        } catch (SQLException e) {

            // Manejamos la exception
            System.out.println("Error al conectarse");
            e.printStackTrace();
        }
    }
}
