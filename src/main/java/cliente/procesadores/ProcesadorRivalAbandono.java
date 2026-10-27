package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosAviso;
import cliente.modelo.ModeloJuego;

// OPPONENT_LEFT: el rival se fue antes de empezar el juego.
public class ProcesadorRivalAbandono implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosAviso datos = pMensaje.getDatos(DatosAviso.class);
        pModelo.rivalAbandono(datos.getTexto());
    }
}
