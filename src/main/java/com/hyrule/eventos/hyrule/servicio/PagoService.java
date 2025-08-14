/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dao.PagoDAO;
import com.hyrule.eventos.hyrule.modelo.Pago;

/**
 *
 * @author Ludvi
 */
public class PagoService {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final InscripcionService inscripcionService = new InscripcionService();

    public boolean registrarPagoYRevalidar(String correo, String codigoEvento, String metodo, double monto) {
        // Registramos pago
        boolean ok = pagoDAO.crear(new Pago(correo, codigoEvento, metodo, monto));
        if (!ok) {
            return false;
        }

        // Intentamos validar (si ya estaba validada, simplemente no cambia)
        inscripcionService.validarInscripcion(correo, codigoEvento);
        return true;
    }
}
