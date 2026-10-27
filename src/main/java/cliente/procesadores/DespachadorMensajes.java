package cliente.procesadores;

import java.util.EnumMap;
import java.util.Map;
import comun.protocolo.Mensaje;
import comun.protocolo.TipoMensaje;
import cliente.modelo.ModeloJuego;

// Busca el procesador de cada tipo de mensaje del servidor en un mapa 
public class DespachadorMensajes {

    private final Map<TipoMensaje, IProcesadorMensaje> procesadores;

    public DespachadorMensajes() {
        this.procesadores = new EnumMap<>(TipoMensaje.class);
        procesadores.put(TipoMensaje.MATCH_CREATED, new ProcesadorPartidaCreada());
        procesadores.put(TipoMensaje.MATCH_JOINED, new ProcesadorPartidaUnida());
        procesadores.put(TipoMensaje.ANIMAL_SELECTION, new ProcesadorSeleccionEspecies());
        procesadores.put(TipoMensaje.OPPONENT_LEFT, new ProcesadorRivalAbandono());
        procesadores.put(TipoMensaje.GAME_STARTED, new ProcesadorInicioJuego());
        procesadores.put(TipoMensaje.STATE_UPDATE, new ProcesadorActualizacion());
        procesadores.put(TipoMensaje.GAME_OVER, new ProcesadorFinJuego());
        procesadores.put(TipoMensaje.ERROR, new ProcesadorError());
    }

    public void despachar(Mensaje pMensaje, ModeloJuego pModelo) {
        IProcesadorMensaje procesador = procesadores.get(pMensaje.getTipo());
        
        if (procesador == null) {
            return;
        }
        procesador.procesar(pMensaje, pModelo);
    }
}
