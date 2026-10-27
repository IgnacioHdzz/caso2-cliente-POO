package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosEstado;
import cliente.modelo.ModeloJuego;

// STATE_UPDATE: la foto actual de lo que el jugador puede ver.
public class ProcesadorActualizacion implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosEstado datos = pMensaje.getDatos(DatosEstado.class);
        pModelo.actualizarEstado(datos);
    }
}