package cliente.controlador;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import comun.modelo.Posicion;
import comun.modelo.Zona;
import cliente.modelo.FaseCliente;
import cliente.modelo.ModeloJuego;
import cliente.vista.TransformadorCoordenadas;

// Escucha el mouse sobre el mapa y le pasa los clics, ya en coordenadas del mundo, al intérprete. 
public class ControladorMouse extends MouseAdapter {

    private final ModeloJuego modelo;
    private final InterpreteClics interprete;
    private final TransformadorCoordenadas transformador;

    public ControladorMouse(ModeloJuego pModelo, InterpreteClics pInterprete, TransformadorCoordenadas pTransformador) {
        this.modelo = pModelo;
        this.interprete = pInterprete;
        this.transformador = pTransformador;
    }

    @Override
    public void mousePressed(MouseEvent pEvento) {
        if (modelo.getFase() != FaseCliente.JUGANDO || modelo.getMapa() == null) {
            return;
        }
        Zona zona = modelo.getMapa().getZona(modelo.getEstadoJuego().getIdZonaVisible());
        if (zona == null) {
            return;
        }
        Component panel = pEvento.getComponent();
        Posicion punto = transformador.aMundo(pEvento.getX(), pEvento.getY(), zona,
                panel.getWidth(), panel.getHeight());
        if (SwingUtilities.isLeftMouseButton(pEvento)) {
            interprete.clicIzquierdo(punto);
        } else if (SwingUtilities.isRightMouseButton(pEvento)) {
            interprete.clicDerecho(punto);
        }
    }
}