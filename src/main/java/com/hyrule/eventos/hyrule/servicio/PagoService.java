/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hyrule.eventos.hyrule.servicio;

import com.hyrule.eventos.hyrule.dao.InscripcionDAO;
import com.hyrule.eventos.hyrule.dao.PagoDAO;
import com.hyrule.eventos.hyrule.modelo.Pago;

/**
 *
 * @author Ludvi
 */
public class PagoService {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final InscripcionDAO insDAO = new InscripcionDAO();
    private final InscripcionService insService = new InscripcionService();

    /*
    Registramos un pago solo si existe inscripcion en estado "pendiente",
    si hay cupo disponible para validar y el monto es exactamente lo que falta
    para cubrir la tarifa, si todo va bien, registra el pago y valida la
    inscripcion
     */
    public ResultadoPago registrarPagoExacto(String correo, String codigoEvento, String metodo, double monto) {
        var ins = insDAO.obtener(correo, codigoEvento);
        if (ins == null) {
            return new ResultadoPago(false, "No existe una inscripción para ese participante en el evento.");
        }
        if (!"pendiente".equalsIgnoreCase(ins.getEstado())) {
            return new ResultadoPago(false, "La inscripción no está en estado 'pendiente'.");
        }

        // Verificamos cupo disponible antes de cobrar/validar
        int cupo = insDAO.obtenerCupoEvento(codigoEvento);
        int validadas = insDAO.contarValidadasPorEvento(codigoEvento);
        if (validadas >= cupo) {
            return new ResultadoPago(false, "Cupo del evento lleno. No se recibe el pago.");
        }

        double tarifa = insDAO.obtenerTarifaEvento(codigoEvento);

        // Evento gratuito (validamos cuando desde un inicio asignamos el valor de cero a un evento)
        if (tarifa <= 0.0) {
            boolean ok = insService.validarInscripcion(correo, codigoEvento);
            if (ok) {
                return new ResultadoPago(true, "Evento gratuito: inscripción validada sin pago.");
            }
            return new ResultadoPago(false, "No se pudo validar (revise estado/cupo).");
        }

        // Evento de pago (para validar la inscripcion obviamente debe pagar el monto exacto)
        double yaPagado = insDAO.sumarPagosInscripcion(correo, codigoEvento);
        double restante = Math.round((tarifa - yaPagado) * 100.0) / 100.0;

        if (restante <= 0.0) {
            return new ResultadoPago(false, "La inscripción ya está totalmente cubierta. No se requiere pago.");
        }

        // Notificamos exactamente el monto que se debe pagar, ni mas ni menos
        if (Math.abs(monto - restante) >= 0.01) {
            return new ResultadoPago(false, "Monto incorrecto. Debe pagar exactamente Q" + String.format("%.2f", restante) + ".");
        }

        // De no existir evento, participante o inscripcion, no registramos el pago
        boolean okPago = pagoDAO.crear(new Pago(correo, codigoEvento, metodo, monto));
        if (!okPago) {
            return new ResultadoPago(false, "No se pudo registrar el pago.");
        }

        // Si la transaccion es exitosa entonces validamos inscripcion (actualizamos estado de inscripcion) 
        boolean validada = insService.validarInscripcion(correo, codigoEvento);
        if (validada) {
            return new ResultadoPago(true, "Pago recibido. ¡Inscripción validada!");
        }

        // Manejamos validacion en caso de que no se actualice el estado de inscripcion o el cupo este completamente lleno
        return new ResultadoPago(true, "Pago recibido, pero no se pudo validar en este momento (cupo o estado). Intente de nuevo.");
    }

    private static String formato2(double v) {
        return String.format("%.2f", v);
    }

    private static double redondear2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private static boolean esIgual2(double a, double b) {
        return Math.abs(a - b) < 0.01;
    }
}
