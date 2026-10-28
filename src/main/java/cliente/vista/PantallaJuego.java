package cliente.vista;
import java.awt.BorderLayout;
import cliente.controlador.ControladorJuego;
import cliente.modelo.*;

// Distribuye el mapa y la barra de información.
public class PantallaJuego extends PantallaBase {
    private final PanelMapa panelMapa;
    private final PanelInformacion panelInformacion;
    public PantallaJuego(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        panelMapa = new PanelMapa(modelo, controlador);
        panelInformacion = new PanelInformacion(modelo, controlador);
        setLayout(new BorderLayout());
        add(panelMapa, BorderLayout.CENTER);
        add(panelInformacion, BorderLayout.SOUTH);
    }
    @Override public void actualizar(ModeloJuego modelo) {
        if (modelo.getFase() == FaseCliente.JUGANDO) panelMapa.iniciarAnimacion();
        else panelMapa.detenerAnimacion();
        panelInformacion.refrescar();
    }
}
