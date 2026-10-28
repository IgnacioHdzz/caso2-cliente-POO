package cliente.vista;
import java.awt.*;
import javax.swing.*;
import comun.protocolo.datos.*;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;

// Muestra las estadísticas enviadas por el servidor al terminar.
public class PantallaResumen extends PantallaBase {
    private final JLabel etiquetaGanador;
    private final JTextArea areaResumen;
    private final JButton botonVolver;
    public PantallaResumen(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        etiquetaGanador = new JLabel("", SwingConstants.CENTER);
        areaResumen = new JTextArea(12, 40);
        areaResumen.setEditable(false);
        botonVolver = new JButton("Volver al inicio");
        setLayout(new BorderLayout(10, 10));
        add(etiquetaGanador, BorderLayout.NORTH);
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);
        add(botonVolver, BorderLayout.SOUTH);
        botonVolver.addActionListener(e -> controlador.volverAlInicio());
    }
    @Override public void actualizar(ModeloJuego modelo) {
        DatosFinJuego fin = modelo.getFin();
        if (fin == null) { etiquetaGanador.setText("Sin resumen"); areaResumen.setText(""); return; }
        etiquetaGanador.setText("Ganador: " + fin.getNicknameGanador());
        DatosResumen r = fin.getResumen();
        if (r == null) { areaResumen.setText(""); return; }
        areaResumen.setText("Duración: " + (r.getDuracionMs() / 1000) + " segundos\n"
                + "Bots eliminados: " + r.getBotsEliminadosPorJugador() + "\n"
                + "Animales eliminados: " + r.getAnimalesEliminadosPorEspecie() + "\n"
                + "Tazas recogidas: " + r.getTazasPorJugador());
    }
}
