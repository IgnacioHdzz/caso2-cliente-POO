package cliente.vista;
import java.awt.*;
import javax.swing.*;
import comun.modelo.Zona;
import cliente.constantes.Constantes;
import cliente.controlador.*;
import cliente.modelo.*;

// Dibuja exclusivamente los elementos de la zona visible.
public class PanelMapa extends JPanel {
    private final ModeloJuego modelo;
    private final TransformadorCoordenadas transformador;
    private final RenderizadorElementos renderizador;
    private final Timer temporizador;
    public PanelMapa(ModeloJuego modelo, ControladorJuego controlador) {
        this.modelo = modelo;
        transformador = new TransformadorCoordenadas();
        renderizador = new RenderizadorElementos(modelo, transformador);
        temporizador = new Timer(Constantes.MS_POR_CUADRO, e -> repaint());
        InterpreteClics interprete = new InterpreteClics(modelo, controlador);
        addMouseListener(new ControladorMouse(modelo, interprete, transformador));
        setBackground(Constantes.COLOR_FONDO_ZONA);
    }
    public void iniciarAnimacion() { if (!temporizador.isRunning()) temporizador.start(); }
    public void detenerAnimacion() { temporizador.stop(); }
    @Override protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        if (modelo.getMapa() == null) return;
        Zona zona = modelo.getMapa().getZona(modelo.getEstadoJuego().getIdZonaVisible());
        if (zona == null) return;
        Graphics2D g = (Graphics2D) graficos.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            renderizador.prepararCuadro(g, zona, getWidth(), getHeight(), System.currentTimeMillis());
            renderizador.dibujarZona();
            for (IDibujable elemento : modelo.getEstadoJuego().getElementosDibujables()) {
                elemento.aceptar(renderizador);
            }
        } finally { g.dispose(); }
    }
}
