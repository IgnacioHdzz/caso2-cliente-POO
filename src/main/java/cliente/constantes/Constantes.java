package cliente.constantes;

import java.awt.Color;

// Valores fijos que solo usa el cliente: ventana, dibujo, imágenes y textos.
public final class Constantes {

    private Constantes() {
    }

    // Ventana
    public static final String TITULO_VENTANA = "Jungle Attack";
    public static final int ANCHO_VENTANA = 1000;
    public static final int ALTO_VENTANA = 720;
    public static final int ALTO_PANEL_INFORMACION = 90;

    // Animación
    public static final int FPS = 30;
    public static final int MS_POR_CUADRO = 1000 / FPS;

    // Dibujo (en unidades del mapa)
    public static final int TAMANIO_ANIMAL = 40;
    public static final int TAMANIO_TAZA = 24;
    public static final int TAMANIO_BANDERA = 48;
    public static final int ALTO_BARRA_ENERGIA = 5;
    public static final double RADIO_CLIC = 25;

    // Colores
    public static final Color COLOR_FONDO_ZONA = new Color(46, 94, 52);
    public static final Color COLOR_CAMINO = new Color(150, 120, 80);
    public static final Color COLOR_SELECCION = new Color(255, 230, 0);
    public static final Color COLOR_ENERGIA = new Color(60, 200, 60);
    public static final Color COLOR_ENERGIA_VACIA = new Color(120, 30, 30);
    public static final Color COLOR_PROPIO = new Color(70, 140, 255);
    public static final Color COLOR_RIVAL = new Color(230, 60, 60);

    // Energías máximas, solo para dibujar la barra (las reglas son del servidor)
    public static final int ENERGIA_MAXIMA_JUGADOR = 20;
    public static final int ENERGIA_MAXIMA_BOT = 10;

    // Imágenes SVG (en src/main/resources/imagenes)
    public static final String CARPETA_IMAGENES = "/imagenes/";
    public static final String EXTENSION_IMAGEN = ".svg";
    public static final String IMAGEN_TAZA = "taza";
    public static final String IMAGEN_BANDERA = "bandera";

    // Textos
    public static final String TEXTO_CONFIRMAR_ABANDONO = "¿Seguro que querés abandonar la partida?";
    public static final String TEXTO_SIN_CONEXION = "No se pudo conectar con el servidor.";
    public static final String TEXTO_CONEXION_PERDIDA = "Se perdió la conexión con el servidor.";
    public static final String TEXTO_NICKNAME_INVALIDO = "Escribí un nickname.";
    public static final String TEXTO_CODIGO_INVALIDO = "El código debe tener 4 letras o números.";
    public static final int LARGO_MAXIMO_NICKNAME = 15;
}
