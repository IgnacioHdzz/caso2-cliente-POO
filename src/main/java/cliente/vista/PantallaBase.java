package cliente.vista;

import javax.swing.JPanel;
import cliente.controlador.ControladorJuego;
import cliente.modelo.IObservadorModelo;
import cliente.modelo.ModeloJuego;

/** Lo común de las 5 pantallas: conocen el modelo y el controlador, y observan el modelo. */
public abstract class PantallaBase extends JPanel implements IObservadorModelo {

    protected final ModeloJuego modelo;
    protected final ControladorJuego controlador;

    protected PantallaBase(ModeloJuego pModelo, ControladorJuego pControlador) {
        this.modelo = pModelo;
        this.controlador = pControlador;
    }

    /** Cada pantalla refresca lo que le interesa. */
    @Override
    public abstract void actualizar(ModeloJuego pModelo);
}
