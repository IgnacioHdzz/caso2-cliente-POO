package cliente.vista;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import comun.modelo.Especie;
import comun.protocolo.datos.DatosFinJuego;
import comun.protocolo.datos.DatosResumen;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;

// Muestra el resultado y explica cuando una manada fue eliminada por completo.
public class PantallaResumen extends PantallaBase {

    // El servidor crea diez animales por jugador al iniciar la partida.
    private static final int ANIMALES_INICIALES_JUGADOR = 10;

    private final JLabel etiquetaGanador;
    private final JTextArea areaResumen;
    private final JButton botonVolver;
    private DatosFinJuego ultimoFinNotificado;

    public PantallaResumen(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        etiquetaGanador = new JLabel("", SwingConstants.CENTER);
        etiquetaGanador.setFont(etiquetaGanador.getFont().deriveFont(Font.BOLD, 18f));
        areaResumen = new JTextArea(12, 40);
        areaResumen.setEditable(false);
        botonVolver = new JButton("Volver al inicio");
        setLayout(new BorderLayout(10, 10));
        add(etiquetaGanador, BorderLayout.NORTH);
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);
        add(botonVolver, BorderLayout.SOUTH);
        botonVolver.addActionListener(e -> controlador.volverAlInicio());
    }

    @Override
    public void actualizar(ModeloJuego modelo) {
        DatosFinJuego fin = modelo.getFin();
        if (fin == null) {
            etiquetaGanador.setText("Sin resumen");
            areaResumen.setText("");
            return;
        }

        DatosResumen resumen = fin.getResumen();
        String ganador = fin.getNicknameGanador();
        String mensaje = construirMensajeFinal(modelo);
        etiquetaGanador.setText(mensaje);

        // Solo la derrota por eliminacion total muestra un aviso emergente.
        if (mensaje.startsWith("Todos tus animales han muerto") && ultimoFinNotificado != fin) {
            ultimoFinNotificado = fin;
            if (!java.awt.GraphicsEnvironment.isHeadless()) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    this, mensaje, "Fin de la partida", JOptionPane.INFORMATION_MESSAGE));
            }
        }

        if (resumen == null) {
            areaResumen.setText("");
            return;
        }
        areaResumen.setText("Ganador: " + ganador + "\n"
                + "Duración: " + (resumen.getDuracionMs() / 1000) + " segundos\n"
                + "Bots eliminados: " + resumen.getBotsEliminadosPorJugador() + "\n"
                + "Animales eliminados: " + resumen.getAnimalesEliminadosPorEspecie() + "\n"
                + "Tazas recogidas: " + resumen.getTazasPorJugador());
    }

    // Texto accesible para pruebas unitarias sin abrir ventanas emergentes.
    static String construirMensajeFinal(ModeloJuego modelo) {
        DatosFinJuego fin = modelo.getFin();
        if (fin == null) return "Sin resumen";
        DatosResumen resumen = fin.getResumen();
        String ganador = fin.getNicknameGanador();
        String nickname = modelo.getNickname();
        boolean gane = nickname != null && nickname.equals(ganador);
        if (!gane && manadaEliminada(resumen, modelo.getEspeciePropia())) {
            return "Todos tus animales han muerto. ¡Has perdido la partida!";
        }
        if (gane && manadaEliminada(resumen, modelo.getEspecieRival())) {
            return "¡Has ganado! Todos los animales del rival han muerto.";
        }
        if (gane) return "¡Has ganado la partida!";
        return "Has perdido. Ganador: " + ganador;
    }

    // En el resumen las especies son unicas, una para cada manada.
    private static boolean manadaEliminada(DatosResumen resumen, Especie especie) {
        if (resumen == null || especie == null || resumen.getAnimalesEliminadosPorEspecie() == null) {
            return false;
        }
        return resumen.getAnimalesEliminadosPorEspecie().getOrDefault(especie, 0)
                >= ANIMALES_INICIALES_JUGADOR;
    }
}
