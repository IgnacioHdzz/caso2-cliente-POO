package cliente.vista;
import java.awt.*;
import javax.swing.*;
import cliente.constantes.Constantes;
import cliente.controlador.ControladorJuego;
import cliente.modelo.*;

// Panel inferior con estadísticas y acciones del jugador.
public class PanelInformacion extends JPanel {
    private final ModeloJuego modelo;
    private final ControladorJuego controlador;
    private final JLabel etiquetaEstadisticas;
    private final JPanel panelZonas;
    private final JButton botonSeleccionarTodos;
    private final JButton botonAbandonar;
    public PanelInformacion(ModeloJuego modelo, ControladorJuego controlador) {
        this.modelo = modelo;
        this.controlador = controlador;
        etiquetaEstadisticas = new JLabel();
        panelZonas = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        botonSeleccionarTodos = new JButton("Seleccionar todos");
        botonAbandonar = new JButton("Abandonar");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, Constantes.ALTO_PANEL_INFORMACION));
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(botonSeleccionarTodos);
        botones.add(botonAbandonar);
        add(etiquetaEstadisticas, BorderLayout.NORTH);
        add(panelZonas, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        botonSeleccionarTodos.addActionListener(e -> controlador.seleccionarTodos());
        botonAbandonar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, Constantes.TEXTO_CONFIRMAR_ABANDONO,
                    "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) controlador.abandonarPartida();
        });
    }
    public void refrescar() {
        EstadoJuegoCliente estado = modelo.getEstadoJuego();
        etiquetaEstadisticas.setText("Daño: " + estado.getCapacidadDanio()
                + "   Tazas: " + estado.getTazasConsumidas()
                + "   Bots eliminados: " + estado.getBotsEliminados());
        panelZonas.removeAll();
        for (Integer id : estado.getZonasConAnimales()) {
            JButton boton = new JButton("Zona " + id);
            boton.addActionListener(e -> controlador.verZona(id));
            panelZonas.add(boton);
        }
        panelZonas.revalidate(); panelZonas.repaint();
    }
}
