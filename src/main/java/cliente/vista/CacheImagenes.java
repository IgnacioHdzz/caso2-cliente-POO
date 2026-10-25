package cliente.vista;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.SVGLoader;

import cliente.constantes.Constantes;
import comun.modelo.Especie;

// Singleton que convierte los SVG una sola vez y reutiliza sus imagenes.
public class CacheImagenes {

    private static final CacheImagenes INSTANCIA = new CacheImagenes();

    private final Map<String, BufferedImage> imagenes;

    private CacheImagenes() {
        imagenes = new HashMap<>();
    }

    public static CacheImagenes getInstancia() {
        return INSTANCIA;
    }

    // Carga los recursos disponibles; los SVG que faltan se incorporaran despues.
    public void cargarTodas() {
        for (Especie especie : Especie.values()) {
            String nombre = especie.name().toLowerCase(Locale.ROOT);
            guardarSiExiste(nombre, Constantes.TAMANIO_ANIMAL);
        }
        guardarSiExiste(Constantes.IMAGEN_TAZA, Constantes.TAMANIO_TAZA);
        guardarSiExiste(Constantes.IMAGEN_BANDERA, Constantes.TAMANIO_BANDERA);
    }

    public BufferedImage obtener(String pNombre) {
        return imagenes.get(pNombre);
    }

    private void guardarSiExiste(String pNombre, int pTamanio) {
        BufferedImage imagen = rasterizar(pNombre, pTamanio);
        if (imagen != null) {
            imagenes.put(pNombre, imagen);
        }
    }

    private BufferedImage rasterizar(String pNombre, int pTamanio) {
        URL recurso = CacheImagenes.class.getResource(
                Constantes.CARPETA_IMAGENES + pNombre + Constantes.EXTENSION_IMAGEN);
        if (recurso == null) {
            return null;
        }
        SVGDocument documento = new SVGLoader().load(recurso);
        if (documento == null) {
            return null;
        }
        BufferedImage imagen = new BufferedImage(pTamanio, pTamanio, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graficos = imagen.createGraphics();
        try {
            graficos.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Ajusta el SVG al tamaño de la imagen sin depender de ViewBox.
            float ancho = documento.size().width;
            float alto = documento.size().height;
            if (ancho <= 0 || alto <= 0) {
                return null;
            }
            graficos.scale(pTamanio / (double) ancho, pTamanio / (double) alto);
            documento.render(null, graficos);
        } finally {
            graficos.dispose();
        }
        return imagen;
    }
}