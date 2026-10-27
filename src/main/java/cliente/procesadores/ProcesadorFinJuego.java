package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosFinJuego;
import cliente.modelo.ModeloJuego;

// GAME_OVER, llega el ganador y el resumen.
public class ProcesadorFinJuego implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosFinJuego datos = pMensaje.getDatos(DatosFinJuego.class);
        pModelo.terminarJuego(datos);
    }
}