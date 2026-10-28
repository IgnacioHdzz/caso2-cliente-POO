package cliente.controlador;

import comun.modelo.Posicion;
import cliente.modelo.ModeloJuego;

/**
 * Decide qué significa cada clic (lógica del cliente, sin ventana: se puede probar).
 * Izquierdo: seleccionar o quitar animales propios; en el vacío, quitar toda la selección.
 * Derecho: sobre un enemigo, atacar; en el vacío, mover los seleccionados.
 */
public class InterpreteClics {

    private final ModeloJuego modelo;
    private final ControladorJuego controlador;

    public InterpreteClics(ModeloJuego pModelo, ControladorJuego pControlador) {
        this.modelo = pModelo;
        this.controlador = pControlador;
    }

    public void clicIzquierdo(Posicion pPunto) {
        // TODO (Ignacio): buscar el animal bajo el punto (RADIO_CLIC).
        // Propio: alternar en la selección. Vacío: limpiar la selección. Ajeno: nada. Notificar.
    }

    public void clicDerecho(Posicion pPunto) {
        // TODO (Ignacio): si no hay selección, nada. Animal ajeno bajo el punto: atacar.
        // Vacío: mover los seleccionados. Propio: nada.
    }
}
