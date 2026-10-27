package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosPartidaCreada;
import cliente.modelo.ModeloJuego;

// MATCH_CREATED: se muestra el código y se espera al rival.
public class ProcesadorPartidaCreada implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosPartidaCreada datos = pMensaje.getDatos(DatosPartidaCreada.class);
        pModelo.registrarPartidaCreada(datos.getCodigo());
    }
}

