package cliente.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import comun.constantes.ConstantesComunes;
import comun.modelo.Posicion;

class InterpoladorTest {

    private final Interpolador interpolador = new Interpolador();

    @Test
    void aMitadDelTickLaFraccionEsUnMedio() {
        long recepcion = 1000;
        long ahora = recepcion + ConstantesComunes.MS_POR_TICK / 2;
        assertEquals(0.5, interpolador.calcularFraccion(recepcion, ahora), 0.0001);
    }

    @Test
    void laFraccionNuncaPasaDeUno() {
        assertEquals(1.0, interpolador.calcularFraccion(1000, 5000), 0.0001);
    }

    @Test
    void aMitadDelCaminoLaPosicionEstaEnElMedio() {
        Posicion medio = interpolador.posicionEntre(new Posicion(100, 0), new Posicion(110, 20), 0.5);
        assertEquals(105, medio.getX(), 0.0001);
        assertEquals(10, medio.getY(), 0.0001);
    }

    @Test
    void sinPosicionAnteriorDevuelveLaActual() {
        Posicion actual = new Posicion(50, 50);
        assertSame(actual, interpolador.posicionEntre(null, actual, 0.3));
    }
}