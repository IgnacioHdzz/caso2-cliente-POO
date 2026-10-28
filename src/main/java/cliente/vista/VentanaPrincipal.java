package cliente.vista;
import java.awt.CardLayout;
import javax.swing.*;
import cliente.constantes.Constantes;
import cliente.controlador.ControladorJuego;
import cliente.modelo.*;

// Controla qué pantalla se muestra según la fase actual del juego.
public class VentanaPrincipal extends JFrame implements IObservadorModelo {
    private final CardLayout tarjetas;
    private final JPanel contenedor;
    public VentanaPrincipal(ModeloJuego modelo, ControladorJuego controlador) {
        super(Constantes.TITULO_VENTANA);
        tarjetas = new CardLayout();
        contenedor = new JPanel(tarjetas);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Constantes.ANCHO_VENTANA, Constantes.ALTO_VENTANA);
        setLocationRelativeTo(null);
        agregarPantalla(FaseCliente.INICIO, new PantallaInicio(modelo, controlador), modelo);
        agregarPantalla(FaseCliente.ESPERANDO_RIVAL, new PantallaEsperandoRival(modelo, controlador), modelo);
        agregarPantalla(FaseCliente.SELECCION_ESPECIE, new PantallaSeleccionEspecie(modelo, controlador), modelo);
        agregarPantalla(FaseCliente.JUGANDO, new PantallaJuego(modelo, controlador), modelo);
        agregarPantalla(FaseCliente.RESUMEN, new PantallaResumen(modelo, controlador), modelo);
        add(contenedor);
        modelo.agregarObservador(this);
        actualizar(modelo);
    }
    private void agregarPantalla(FaseCliente fase, PantallaBase pantalla, ModeloJuego modelo) {
        contenedor.add(pantalla, fase.name());
        modelo.agregarObservador(pantalla);
    }
    @Override public void actualizar(ModeloJuego modelo) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> actualizar(modelo));
            return;
        }
        tarjetas.show(contenedor, modelo.getFase().name());
        String aviso = modelo.consumirAviso();
        if (aviso != null && !aviso.isBlank()) JOptionPane.showMessageDialog(this, aviso);
    }
}
