package cliente.vista;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private BufferedImage fondoCache;
    private int zonaCache = -1;
    private int anchoCache = -1;
    private int altoCache = -1;
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
        if (g == null || zona == null || anchoPanel <= 0 || altoPanel <= 0) return;
        // El escenario se genera una sola vez por zona y tamano de ventana.
        // Los animales se dibujan despues, por encima del terreno.
        if (fondoCache == null || zonaCache != zona.getId()
                || anchoCache != anchoPanel || altoCache != altoPanel) {
            fondoCache = new BufferedImage(anchoPanel, altoPanel, BufferedImage.TYPE_INT_RGB);
            Graphics2D terreno = fondoCache.createGraphics();
            try {
                terreno.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                crearTerreno(terreno);
            } finally {
                terreno.dispose();
            }
            zonaCache = zona.getId();
            anchoCache = anchoPanel;
            altoCache = altoPanel;
        }
        g.drawImage(fondoCache, 0, 0, null);
    }

    private void crearTerreno(Graphics2D terreno) {
        Random azar = new Random(812347L + zona.getId() * 104729L);
        int w = anchoPanel;
        int h = altoPanel;

        // Verdes con profundidad: el suelo no es un rectangulo de un solo color.
        terreno.setPaint(new GradientPaint(0, 0, new Color(48, 106, 65),
                w, h, new Color(28, 73, 47)));
        terreno.fillRect(0, 0, w, h);

        // Claros de vegetacion y zonas de sombra.
        for (int i = 0; i < 90; i++) {
            int x = azar.nextInt(w);
            int y = azar.nextInt(h);
            int rx = 25 + azar.nextInt(100);
            int ry = 18 + azar.nextInt(64);
            terreno.setColor(i % 3 == 0 ? new Color(15, 48, 27, 32)
                    : new Color(126, 168, 77, 18));
            terreno.fillOval(x - rx / 2, y - ry / 2, rx, ry);
        }

        // Pequeñas hojas y briznas para dar textura al pasto.
        for (int i = 0; i < Math.max(400, w * h / 500); i++) {
            int x = azar.nextInt(w);
            int y = azar.nextInt(h);
            if (i % 5 == 0) {
                terreno.setColor(new Color(147, 181, 88, 90));
            } else {
                terreno.setColor(new Color(19, 65, 38, 95));
            }
            terreno.drawLine(x, y, x + (i % 3) - 1, y - (i % 4));
        }

        List<Camino> caminos = modelo.getMapa().getCaminosDe(zona.getId());
        int anchoCamino = Math.max(13, Math.min(29, Math.min(w, h) / 22));
        dibujarCaminos(terreno, caminos, anchoCamino);

        // Crear puntos decorativos fijos por zona, respetando los caminos reales.
        List<Line2D.Double> tramos = new ArrayList<>();
        for (Camino camino : caminos) {
            Point a = transformador.aPantalla(camino.getInicio(), zona, anchoPanel, altoPanel);
            Point b = transformador.aPantalla(camino.getFin(), zona, anchoPanel, altoPanel);
            tramos.add(new Line2D.Double(a.x, a.y, b.x, b.y));
        }
        DecoracionesCostaRica decoraciones =
                new DecoracionesCostaRica(zona.getId(), w, h, tramos);

        // Arboles, arbustos y piedras son puramente decorativos.
        // Nunca tapan los caminos ni alteran las coordenadas de los clics.
        int arboles = Math.max(24, w * h / 13500);
        for (int i = 0; i < arboles; i++) {
            int x = 23 + azar.nextInt(Math.max(1, w - 46));
            int y = 23 + azar.nextInt(Math.max(1, h - 46));
            int radio = 10 + azar.nextInt(12);
            if (libreDeCaminos(x, y, caminos, radio + anchoCamino)
                    && decoraciones.espacioLibre(x, y, radio)) {
                dibujarArbol(terreno, x, y, radio, azar);
            }
        }

        int arbustos = Math.max(28, w * h / 11000);
        for (int i = 0; i < arbustos; i++) {
            int x = 12 + azar.nextInt(Math.max(1, w - 24));
            int y = 12 + azar.nextInt(Math.max(1, h - 24));
            if (libreDeCaminos(x, y, caminos, anchoCamino + 9)
                    && decoraciones.espacioLibre(x, y, 10)) {
                dibujarArbusto(terreno, x, y, 5 + azar.nextInt(5));
            }
        }

        for (int i = 0; i < Math.max(20, w * h / 18000); i++) {
            int x = 10 + azar.nextInt(Math.max(1, w - 20));
            int y = 10 + azar.nextInt(Math.max(1, h - 20));
            if (libreDeCaminos(x, y, caminos, anchoCamino / 2 + 5)
                    && decoraciones.espacioLibre(x, y, 8)) {
                int r = 2 + azar.nextInt(4);
                terreno.setColor(new Color(19, 50, 32, 95));
                terreno.fillOval(x - r + 2, y - r + 2, r * 2, r + 3);
                terreno.setColor(new Color(131, 149, 118));
                terreno.fillOval(x - r, y - r, r * 2, r + 3);
                terreno.setColor(new Color(178, 187, 145, 115));
                terreno.drawArc(x - r, y - r, r * 2, r * 2, 45, 100);
            }
        }

        // Carretas tipicas, estadio, cafetales y cataratas sobre la selva.
        // Todo permanece debajo de los animales, tazas y banderas.
        decoraciones.dibujar(terreno);

        // Un marco discreto delimita visualmente la zona sin crear obstaculos.
        terreno.setColor(new Color(10, 35, 22, 120));
        terreno.setStroke(new BasicStroke(5));
        terreno.drawRect(2, 2, w - 5, h - 5);
        terreno.setStroke(new BasicStroke(1));
    }

    private void dibujarCaminos(Graphics2D terreno, List<Camino> caminos, int anchoCamino) {
        // Varias capas generan borde de hierba, tierra y sendero transitable.
        for (Camino camino : caminos) {
            Point inicio = transformador.aPantalla(camino.getInicio(), zona, anchoPanel, altoPanel);
            Point fin = transformador.aPantalla(camino.getFin(), zona, anchoPanel, altoPanel);
            terreno.setStroke(new BasicStroke(anchoCamino + 15, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            terreno.setColor(new Color(24, 63, 34, 140));
            terreno.drawLine(inicio.x, inicio.y, fin.x, fin.y);
            terreno.setStroke(new BasicStroke(anchoCamino + 8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            terreno.setColor(new Color(100, 80, 47));
            terreno.drawLine(inicio.x, inicio.y, fin.x, fin.y);
            terreno.setStroke(new BasicStroke(anchoCamino, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            terreno.setColor(new Color(165, 128, 77));
            terreno.drawLine(inicio.x, inicio.y, fin.x, fin.y);
            terreno.setStroke(new BasicStroke(Math.max(2, anchoCamino / 4), BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
            terreno.setColor(new Color(202, 164, 100, 90));
            terreno.drawLine(inicio.x, inicio.y, fin.x, fin.y);
        }
        terreno.setStroke(new BasicStroke(1));
    }

    private boolean libreDeCaminos(int x, int y, List<Camino> caminos, double distanciaMinima) {
        for (Camino camino : caminos) {
            Point a = transformador.aPantalla(camino.getInicio(), zona, anchoPanel, altoPanel);
            Point b = transformador.aPantalla(camino.getFin(), zona, anchoPanel, altoPanel);
            double dx = b.x - a.x;
            double dy = b.y - a.y;
            double divisor = dx * dx + dy * dy;
            double t = divisor == 0 ? 0 : ((x - a.x) * dx + (y - a.y) * dy) / divisor;
            t = Math.max(0, Math.min(1, t));
            double cercanoX = a.x + t * dx;
            double cercanoY = a.y + t * dy;
            if (Math.hypot(x - cercanoX, y - cercanoY) < distanciaMinima) return false;
        }
        return true;
    }

    private void dibujarArbol(Graphics2D terreno, int x, int y, int r, Random azar) {
        // Sombra, tronco, copa y brillo: arbol en vista superior.
        terreno.setColor(new Color(8, 39, 23, 115));
        terreno.fillOval(x - r + 5, y - r + 7, r * 2 + 2, r * 2 - 1);
        terreno.setColor(new Color(85, 63, 34));
        terreno.fillOval(x - 3, y - 1, 7, 10);
        terreno.setColor(new Color(17, 72, 36));
        terreno.fillOval(x - r, y - r, r * 2, r * 2);
        terreno.setColor(new Color(27, 104 + azar.nextInt(30), 48));
        terreno.fillOval(x - r + 2, y - r + 1, r + 6, r + 10);
        terreno.setColor(new Color(78, 153, 68, 175));
        terreno.fillOval(x - r / 2, y - r / 2 - 2, r, r / 2 + 5);
        terreno.setColor(new Color(123, 179, 94, 105));
        terreno.fillOval(x - r / 2, y - r / 2 - 3, 5, 5);
    }

    private void dibujarArbusto(Graphics2D terreno, int x, int y, int r) {
        terreno.setColor(new Color(14, 53, 28, 110));
        terreno.fillOval(x - r + 2, y - r + 3, r * 2 + 3, r * 2);
        terreno.setColor(new Color(32, 113, 47));
        terreno.fillOval(x - r, y - r, r * 2, r * 2);
        terreno.setColor(new Color(70, 158, 69));
        terreno.fillOval(x - r / 2, y - r + 1, r + 1, r);
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
