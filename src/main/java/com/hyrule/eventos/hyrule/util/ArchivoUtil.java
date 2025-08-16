/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Ludvi
 */
public class ArchivoUtil {

    public static File escribir(String rutaCarpeta, String nombreArchivo, String contenido) throws IOException {
        File dir = new File(rutaCarpeta);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File f = new File(dir, nombreArchivo);
        try (FileWriter fw = new FileWriter(f, false)) {
            fw.write(contenido);
        }
        return f;
    }
}
