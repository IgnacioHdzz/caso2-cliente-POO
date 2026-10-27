package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosInicioJuego;
import cliente.modelo.ModeloJuego;

// GAME_STARTED: empieza el juego, llegan el mapa, la manada propia y la zona inicial.
public class ProcesadorInicioJuego implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosInicioJuego datos = pMensaje.getDatos(DatosInicioJuego.class);
        pModelo.iniciarJuego(datos);
    }
}

