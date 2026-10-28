package cliente.controlador;

import comun.modelo.Posicion;
import cliente.constantes.Constantes;
import cliente.modelo.AnimalVista;
import cliente.modelo.ModeloJuego;
import cliente.modelo.Seleccion;

/**
 * Decide qué significa cada clic.
 * Izquierdo: seleccionar o quitar animales propios. Si se hace clic en el vacío, quita toda la selección.
 * Derecho: sobre un enemigo, ataca y en el vacío mueve los seleccionados.
 */
public class InterpreteClics {

    private final ModeloJuego modelo;
    private final ControladorJuego controlador;

    public InterpreteClics(ModeloJuego pModelo, ControladorJuego pControlador) {
        this.modelo = pModelo;
        this.controlador = pControlador;
    }

    public void clicIzquierdo(Posicion pPunto) {
        AnimalVista animal = modelo.getEstadoJuego().buscarAnimalEn(pPunto, Constantes.RADIO_CLIC);
        Seleccion seleccion = modelo.getSeleccion();
        if (animal == null) {
            seleccion.limpiar();
            modelo.notificarObservadores();
            return;
        }
        if (modelo.esPropio(animal)) {
            seleccion.alternar(animal.getId());
            modelo.notificarObservadores();
        }
    }

    public void clicDerecho(Posicion pPunto) {
        if (modelo.getSeleccion().estaVacia()) {
            return;
        }
        AnimalVista animal = modelo.getEstadoJuego().buscarAnimalEn(pPunto, Constantes.RADIO_CLIC);
        if (animal == null) {
            controlador.moverSeleccionados(pPunto);
            return;
        }
        if (!modelo.esPropio(animal)) {
            controlador.atacar(animal.getId());
        }
    }
}