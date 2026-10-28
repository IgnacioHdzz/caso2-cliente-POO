package cliente.principal;

import javax.swing.SwingUtilities;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;
import cliente.vista.CacheImagenes;
import cliente.vista.VentanaPrincipal;

// Punto de entrada del cliente. Cada jugador abre uno.
public class MainCliente {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CacheImagenes.getInstancia().cargarTodas();
            ModeloJuego modelo = new ModeloJuego();
            ControladorJuego controlador = new ControladorJuego(modelo);
            VentanaPrincipal ventana = new VentanaPrincipal(modelo, controlador);
            ventana.setVisible(true);
        });
    }
}