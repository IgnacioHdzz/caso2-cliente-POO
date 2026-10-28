package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.Test;
import comun.modelo.Especie;
import comun.protocolo.datos.DatosFinJuego;
import comun.protocolo.datos.DatosInicioJuego;
import comun.protocolo.datos.DatosResumen;
import cliente.modelo.ModeloJuego;

class PantallaResumenDerrotaTest {

    @Test
    void explicaQuePerdioCuandoLosDiezAnimalesEstanMuertos() throws Exception {
        // No se abre ventana; en un entorno sin pantalla solo probamos el texto.
        ModeloJuego modelo = new ModeloJuego();
        modelo.setNickname("Julian");
        modelo.iniciarJuego(new DatosInicioJuego(null, 1, 0, Especie.JAGUAR));
        DatosResumen resumen = new DatosResumen(6000, Map.of("Julian", 0),
                Map.of(Especie.JAGUAR, 10), Map.of("Julian", 0));

        modelo.terminarJuego(new DatosFinJuego("Nacho", resumen));
        String texto = PantallaResumen.construirMensajeFinal(modelo);
        assertTrue(texto.contains("Todos tus animales han muerto"));
        assertTrue(texto.contains("Has perdido la partida"));
    }
}
