package cliente.vista;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;
import comun.modelo.*;
import cliente.constantes.Constantes;
import cliente.modelo.*;

// Visitor: dibuja animales, tazas y banderas sin mezclar dibujo con lógica.
public class RenderizadorElementos implements IVisitanteElemento {
    private final ModeloJuego modelo;
    private final TransformadorCoordenadas transformador;
    private final Interpolador interpolador;
    private Graphics2D g;
    private Zona zona;
    private int anchoPanel;
    private int altoPanel;
    private double fraccion;
    public RenderizadorElementos(ModeloJuego modelo, TransformadorCoordenadas transformador) {
        this.modelo = modelo;
        this.transformador = transformador;
        interpolador = new Interpolador();
        CacheImagenes.getInstancia().cargarTodas();
    }
    public void prepararCuadro(Graphics2D g, Zona zona, int ancho, int alto, long ahoraMs) {
        this.g = g;
        this.zona = zona;
        this.anchoPanel = ancho;
        this.altoPanel = alto;
        fraccion = interpolador.calcularFraccion(modelo.getEstadoJuego().getMomentoRecepcionMs(), ahoraMs);
    }
    public void dibujarZona() {
        if (g == null || zona == null) return;
        g.setColor(Constantes.COLOR_FONDO_ZONA);
        g.fillRect(0, 0, anchoPanel, altoPanel);
        g.setColor(Constantes.COLOR_CAMINO);
        g.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Camino camino : modelo.getMapa().getCaminosDe(zona.getId())) {
            Point inicio = transformador.aPantalla(camino.getInicio(), zona, anchoPanel, altoPanel);
            Point fin = transformador.aPantalla(camino.getFin(), zona, anchoPanel, altoPanel);
            g.drawLine(inicio.x, inicio.y, fin.x, fin.y);
        }
    }
    private void imagen(String nombre, Posicion posicion, int tamanio) {
        if (g == null || zona == null || posicion == null) return;
        Point punto = transformador.aPantalla(posicion, zona, anchoPanel, altoPanel);
        int lado = Math.max(1, transformador.escalar(tamanio, zona, anchoPanel, altoPanel));
        BufferedImage svg = CacheImagenes.getInstancia().obtener(nombre);
        if (svg != null) g.drawImage(svg, punto.x - lado / 2, punto.y - lado / 2, lado, lado, null);
        else { g.setColor(Color.WHITE); g.fillOval(punto.x - lado / 2, punto.y - lado / 2, lado, lado); }
    }
    @Override public void visitar(AnimalVista animal) {
        if (animal.getIdZona() != zona.getId()) return;
        Posicion posicion = interpolador.posicionEntre(animal.getPosicionAnterior(), animal.getPosicion(), fraccion);
        imagen(animal.getEspecie().name().toLowerCase(Locale.ROOT), posicion, Constantes.TAMANIO_ANIMAL);
        Point punto = transformador.aPantalla(posicion, zona, anchoPanel, altoPanel);
        int lado = Math.max(1, transformador.escalar(Constantes.TAMANIO_ANIMAL, zona, anchoPanel, altoPanel));
        if (!animal.esBot()) {
            g.setColor(modelo.esPropio(animal) ? Constantes.COLOR_PROPIO : Constantes.COLOR_RIVAL);
            g.setStroke(new BasicStroke(2));
            g.drawOval(punto.x - lado / 2, punto.y - lado / 2, lado, lado);
        }
        if (modelo.getSeleccion().contiene(animal.getId())) {
            g.setColor(Constantes.COLOR_SELECCION);
            g.drawOval(punto.x - lado / 2 - 3, punto.y - lado / 2 - 3, lado + 6, lado + 6);
        }
        int energiaMaxima = animal.esBot() ? Constantes.ENERGIA_MAXIMA_BOT : Constantes.ENERGIA_MAXIMA_JUGADOR;
        int barra = Math.max(1, lado);
        int y = punto.y - lado / 2 - 10;
        g.setColor(Constantes.COLOR_ENERGIA_VACIA);
        g.fillRect(punto.x - barra / 2, y, barra, Constantes.ALTO_BARRA_ENERGIA);
        g.setColor(Constantes.COLOR_ENERGIA);
        g.fillRect(punto.x - barra / 2, y, Math.max(0, Math.min(barra, barra * animal.getEnergia() / energiaMaxima)), Constantes.ALTO_BARRA_ENERGIA);
    }
    @Override public void visitar(TazaVista taza) {
        if (taza.getIdZona() == zona.getId()) imagen(Constantes.IMAGEN_TAZA, taza.getPosicion(), Constantes.TAMANIO_TAZA);
    }
    @Override public void visitar(BanderaVista bandera) {
        if (bandera.getIdZona() != zona.getId()) return;
        imagen(Constantes.IMAGEN_BANDERA, bandera.getPosicion(), Constantes.TAMANIO_BANDERA);
        Point punto = transformador.aPantalla(bandera.getPosicion(), zona, anchoPanel, altoPanel);
        g.setColor(bandera.getIdManadaDuena() == modelo.getIdManadaPropia() ? Constantes.COLOR_PROPIO : Constantes.COLOR_RIVAL);
        g.fillOval(punto.x - 5, punto.y - 5, 10, 10);
    }
}
