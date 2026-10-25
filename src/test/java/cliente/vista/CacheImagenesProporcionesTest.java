package cliente.vista;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class CacheImagenesProporcionesTest {

    @Test
    void conservaProporcionesYCentraSvgRectangular() throws Exception {
        // Se prueba un SVG de 100 x 50 que debe caber en un lienzo de 80 x 80.
        Method metodo = CacheImagenes.class.getDeclaredMethod("rasterizar", String.class, int.class);
        metodo.setAccessible(true);
        BufferedImage imagen = (BufferedImage) metodo.invoke(
                CacheImagenes.getInstancia(), "prueba_rectangular", 80);

        assertNotNull(imagen);
        assertEquals(80, imagen.getWidth());
        assertEquals(80, imagen.getHeight());
        // La figura ocupa 80 x 40: 20 pixeles transparentes arriba y abajo.
        assertEquals(0, (imagen.getRGB(40, 5) >>> 24));
        assertTrue((imagen.getRGB(40, 40) >>> 24) > 0);
        assertEquals(0, (imagen.getRGB(40, 75) >>> 24));
    }
}
