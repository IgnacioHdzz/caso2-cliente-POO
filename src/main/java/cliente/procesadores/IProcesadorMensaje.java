package cliente.procesadores;

import comun.protocolo.Mensaje;
import cliente.modelo.ModeloJuego;

// Sabe procesar un tipo de mensaje que llega del servidor: actualiza el modelo. 
public interface IProcesadorMensaje {

    void procesar(Mensaje pMensaje, ModeloJuego pModelo);
}
