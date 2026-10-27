package cliente.procesadores;

import comun.protocolo.Mensaje;
import comun.protocolo.datos.DatosSeleccionEspecies;
import cliente.modelo.ModeloJuego;

// ANIMAL_SELECTION: especies disponibles y elecciones.
public class ProcesadorSeleccionEspecies implements IProcesadorMensaje {

    @Override
    public void procesar(Mensaje pMensaje, ModeloJuego pModelo) {
        DatosSeleccionEspecies datos = pMensaje.getDatos(DatosSeleccionEspecies.class);
        pModelo.actualizarSeleccion(datos);
    }
}
   
