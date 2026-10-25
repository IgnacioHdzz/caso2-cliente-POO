package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import cliente.constantes.Constantes;
import comun.modelo.Especie;
import java.awt.image.BufferedImage;
import java.util.Locale;

class CacheImagenesRecursosTest {

    @Test
    void cargaLasSieteEspecies() {
        CacheImagenes cache = CacheImagenes.getInstancia();
        cache.cargarTodas();
        for (Especie especie : Especie.values()) {
            String nombre = especie.name().toLowerCase(Locale.ROOT);
            BufferedImage imagen = cache.obtener(nombre);
            assertNotNull(imagen, "Falta o no se pudo cargar: " + nombre + ".svg");
            assertEquals(Constantes.TAMANIO_ANIMAL, imagen.getWidth());
            assertEquals(Constantes.TAMANIO_ANIMAL, imagen.getHeight());
        }
    }

    @Test
    void cargaLaTazaYLaBandera() {
        CacheImagenes cache = CacheImagenes.getInstancia();
        cache.cargarTodas();
        BufferedImage taza = cache.obtener(Constantes.IMAGEN_TAZA);
        BufferedImage bandera = cache.obtener(Constantes.IMAGEN_BANDERA);
        assertNotNull(taza, "Falta o no se pudo cargar taza.svg");
        assertNotNull(bandera, "Falta o no se pudo cargar bandera.svg");
        assertEquals(Constantes.TAMANIO_TAZA, taza.getWidth());
        assertEquals(Constantes.TAMANIO_BANDERA, bandera.getWidth());
    }
}
