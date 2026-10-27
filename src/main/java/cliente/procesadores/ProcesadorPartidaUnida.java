package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosPartidaUnida;
import cliente.modelo.ModeloJuego;

// MATCH_JOINED: el jugador entró a la partida.
public class ProcesadorPartidaUnida implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosPartidaUnida datos = pMensaje.getDatos(DatosPartidaUnida.class);
        pModelo.registrarPartidaUnida(datos);
    }
}
