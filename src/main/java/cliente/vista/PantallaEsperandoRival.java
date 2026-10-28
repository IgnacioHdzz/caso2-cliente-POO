package cliente.vista;
import java.awt.*;
import javax.swing.*;
import cliente.constantes.Constantes;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;

// Muestra el código que debe introducir el segundo jugador.
public class PantallaEsperandoRival extends PantallaBase {
    private final JLabel etiquetaCodigo;
    private final JButton botonCancelar;
    public PantallaEsperandoRival(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        etiquetaCodigo = new JLabel("", SwingConstants.CENTER);
        botonCancelar = new JButton("Cancelar partida");
        setLayout(new GridBagLayout());
        JPanel contenido = new JPanel(new GridLayout(0, 1, 10, 10));
        contenido.add(new JLabel("Esperando a otro jugador...", SwingConstants.CENTER));
        contenido.add(etiquetaCodigo);
        contenido.add(botonCancelar);
        add(contenido);
        botonCancelar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, Constantes.TEXTO_CONFIRMAR_ABANDONO,
                    "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                controlador.abandonarPartida();
            }
        });
    }
    @Override public void actualizar(ModeloJuego modelo) {
        etiquetaCodigo.setText("Código: " + (modelo.getCodigo() == null ? "" : modelo.getCodigo()));
    }
}
