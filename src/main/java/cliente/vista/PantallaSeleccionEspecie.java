package cliente.vista;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import comun.modelo.Especie;
import cliente.constantes.Constantes;
import cliente.controlador.ControladorJuego;
import cliente.modelo.ModeloJuego;

// Un botón por especie con indicador de disponibilidad.
public class PantallaSeleccionEspecie extends PantallaBase {
    private final Map<Especie, JButton> botonesEspecie;
    private final JButton botonAbandonar;
    private final JLabel estadoSeleccion;
    public PantallaSeleccionEspecie(ModeloJuego modelo, ControladorJuego controlador) {
        super(modelo, controlador);
        botonesEspecie = new EnumMap<>(Especie.class);
        botonAbandonar = new JButton("Abandonar");
        estadoSeleccion = new JLabel("Elegí tu especie", SwingConstants.CENTER);
        setLayout(new BorderLayout(10, 10));
        add(estadoSeleccion, BorderLayout.NORTH);
        JPanel cuadricula = new JPanel(new GridLayout(0, 4, 10, 10));
        CacheImagenes.getInstancia().cargarTodas();
        for (Especie especie : Especie.values()) {
            JButton boton = new JButton(especie.getNombreVisible());
            BufferedImage imagen = CacheImagenes.getInstancia().obtener(especie.name().toLowerCase(Locale.ROOT));
            if (imagen != null) boton.setIcon(new ImageIcon(imagen));
            boton.addActionListener(e -> controlador.seleccionarEspecie(especie));
            botonesEspecie.put(especie, boton);
            cuadricula.add(boton);
        }
        add(cuadricula, BorderLayout.CENTER);
        add(botonAbandonar, BorderLayout.SOUTH);
        botonAbandonar.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, Constantes.TEXTO_CONFIRMAR_ABANDONO,
                    "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) controlador.abandonarPartida();
        });
    }
    @Override public void actualizar(ModeloJuego modelo) {
        for (Map.Entry<Especie, JButton> entrada : botonesEspecie.entrySet()) {
            entrada.getValue().setEnabled(modelo.getEspeciesDisponibles().contains(entrada.getKey()));
        }
        estadoSeleccion.setText("Tu elección: " + (modelo.getEspeciePropia() == null ? "pendiente" : modelo.getEspeciePropia().getNombreVisible())
                + " | Rival: " + (modelo.getEspecieRival() == null ? "pendiente" : modelo.getEspecieRival().getNombreVisible()));
    }
}
