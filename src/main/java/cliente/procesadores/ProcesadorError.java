package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosError;
import cliente.modelo.ModeloJuego;

// ERROR: el servidor rechazó una acción
public class ProcesadorError implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosError datos = pMensaje.getDatos(DatosError.class);
        pModelo.mostrarAviso(datos.getTexto());
    }
}