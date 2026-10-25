package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.awt.Point;
import org.junit.jupiter.api.Test;
import comun.modelo.Posicion;
import comun.modelo.Zona;

class TransformadorCoordenadasTest {

    private final TransformadorCoordenadas transformador = new TransformadorCoordenadas();
    private final Zona zona = new Zona(3, 600, 400, 600, 400, new Posicion(900, 600));

    @Test
    void convierteOrigenDeZonaAOrigenDePantalla() {
        assertEquals(new Point(0, 0), transformador.aPantalla(new Posicion(600, 400), zona, 900, 600));
    }

    @Test
    void convierteCentroDeZonaACentroDePantalla() {
        assertEquals(new Point(450, 300), transformador.aPantalla(new Posicion(900, 600), zona, 900, 600));
    }

    @Test
    void mantieneProporcionesCuandoPanelEsMasAncho() {
        assertEquals(new Point(600, 400), transformador.aPantalla(new Posicion(1200, 800), zona, 1200, 400));
    }

    @Test
    void convierteCoordenadasDelPanelAlMundo() {
        Posicion posicion = transformador.aMundo(450, 300, zona, 900, 600);
        assertEquals(900, posicion.getX(), 1e-9);
        assertEquals(600, posicion.getY(), 1e-9);
    }

    @Test
    void idaYVueltaConservaLaPosicionConToleranciaDePixel() {
        Posicion original = new Posicion(720, 550);
        Point pantalla = transformador.aPantalla(original, zona, 900, 600);
        Posicion restaurada = transformador.aMundo(pantalla.x, pantalla.y, zona, 900, 600);
        assertEquals(original.getX(), restaurada.getX(), 1);
        assertEquals(original.getY(), restaurada.getY(), 1);
    }

    @Test
    void escalaTamanioSegunAnchoDelPanel() {
        assertEquals(60, transformador.escalar(40, zona, 900));
    }

    @Test
    void rechazaPanelDeDimensionCero() {
        assertThrows(IllegalArgumentException.class,
                () -> transformador.aPantalla(new Posicion(700, 500), zona, 0, 600));
    }
}
