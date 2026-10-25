package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.image.BufferedImage;
import org.junit.jupiter.api.Test;
import cliente.constantes.Constantes;

class CacheImagenesTest {

    @Test
    void devuelveSiempreLaMismaInstancia() {
        assertSame(CacheImagenes.getInstancia(), CacheImagenes.getInstancia());
    }

    @Test
    void nombreDesconocidoDevuelveNull() {
        assertNull(CacheImagenes.getInstancia().obtener("imagen_que_no_existe"));
    }

    @Test
    void cargaSvgDePruebaYTieneTamanioCorrecto() {
        CacheImagenes cache = CacheImagenes.getInstancia();
        cache.cargarTodas();
        BufferedImage imagen = cache.obtener("jaguar");
        assertNotNull(imagen);
        assertEquals(Constantes.TAMANIO_ANIMAL, imagen.getWidth());
        assertEquals(Constantes.TAMANIO_ANIMAL, imagen.getHeight());
    }

    @Test
    void reutilizaLaImagenCargada() {
        CacheImagenes cache = CacheImagenes.getInstancia();
        cache.cargarTodas();
        BufferedImage original = cache.obtener("jaguar");
        assertNotNull(original);
        assertSame(original, cache.obtener("jaguar"));
    }
}
