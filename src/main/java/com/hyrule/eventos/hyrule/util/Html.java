/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.util;

/**
 *
 * @author Ludvi
 */
public class Html {

    public static String plantilla(String titulo, String cuerpo) {
        return """
            <!doctype html>
            <html lang="es"><head>
              <meta charset="utf-8">
              <meta name="viewport" content="width=device-width, initial-scale=1">
              <title>%s</title>
              <style>
                body{font-family:Arial,Helvetica,sans-serif;margin:20px}
                h1{font-size:20px;margin:0 0 10px}
                table{border-collapse:collapse;width:100%%}
                th,td{border:1px solid #ddd;padding:8px}
                th{background:#f4f4f4;text-align:left}
                .muted{color:#666;font-size:12px}
              </style>
            </head><body>
              <h1>%s</h1>
              %s
              <p class="muted">Generado por Eventos Hyrule</p>
            </body></html>
        """.formatted(titulo, titulo, cuerpo);
    }
}
