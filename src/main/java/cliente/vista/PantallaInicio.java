package cliente.vista;
import java.awt.*;
import javax.swing.*;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;

// Entrada de nickname y código para iniciar o unirse a una partida.
public class PantallaInicio extends PantallaBase {
    private final JTextField campoNickname;
    private final JTextField campoCodigo;
    private final JButton botonCrear;
    private final JButton botonUnirse;
    public PantallaInicio(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        campoNickname = new JTextField(18);
        campoCodigo = new JTextField(8);
        botonCrear = new JButton("Crear partida");
        botonUnirse = new JButton("Unirse a partida");
        setLayout(new GridBagLayout());
        JPanel formulario = new JPanel(new GridLayout(0, 1, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        formulario.add(new JLabel("JUNGLE ATTACK", SwingConstants.CENTER));
        formulario.add(new JLabel("Nickname:")); formulario.add(campoNickname);
        formulario.add(botonCrear);
        formulario.add(new JLabel("Código de partida:")); formulario.add(campoCodigo);
        formulario.add(botonUnirse);
        add(formulario);
        botonCrear.addActionListener(e -> controlador.crearPartida(campoNickname.getText().trim()));
        botonUnirse.addActionListener(e -> controlador.unirsePartida(campoNickname.getText().trim(), campoCodigo.getText().trim()));
    }
    @Override public void actualizar(ModeloJuego modelo) { campoCodigo.setText(""); }
}
