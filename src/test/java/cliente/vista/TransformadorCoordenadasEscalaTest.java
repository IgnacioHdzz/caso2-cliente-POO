package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import comun.modelo.Posicion;
import comun.modelo.Zona;

class TransformadorCoordenadasEscalaTest {

    private final TransformadorCoordenadas transformador = new TransformadorCoordenadas();
    private final Zona zona = new Zona(3, 600, 400, 600, 400, new Posicion(900, 600));

    @Test
    void tamanoNoSeDeformaConPanelMuyAncho() {
        // La escala vertical limita el tamaño: 400 / 400 = 1.
        assertEquals(40, transformador.escalar(40, zona, 1200, 400));
    }

    @Test
    void tamanoSeAjustaConPanelProporcional() {
        assertEquals(60, transformador.escalar(40, zona, 900, 600));
    }

    @Test
    void tamanoUsaEscalaHorizontalCuandoEsMenor() {
        assertEquals(20, transformador.escalar(40, zona, 300, 800));
    }

    @Test
    void rechazaDimensionesInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> transformador.escalar(40, zona, 800, 0));
    }
}
