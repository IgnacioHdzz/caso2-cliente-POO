package cliente.vista;

import java.awt.Point;
import comun.modelo.Posicion;
import comun.modelo.Zona;

// Convierte posiciones del mapa a píxeles del panel y viceversa.
public class TransformadorCoordenadas {

    public Point aPantalla(Posicion pPosicion, Zona pZona, int pAnchoPanel, int pAltoPanel) {
        double escala = calcularEscala(pZona, pAnchoPanel, pAltoPanel);
        int x = (int) Math.round((pPosicion.getX() - pZona.getX()) * escala);
        int y = (int) Math.round((pPosicion.getY() - pZona.getY()) * escala);
        return new Point(x, y);
    }

    public Posicion aMundo(int pX, int pY, Zona pZona, int pAnchoPanel, int pAltoPanel) {
        double escala = calcularEscala(pZona, pAnchoPanel, pAltoPanel);
        double x = pZona.getX() + pX / escala;
        double y = pZona.getY() + pY / escala;
        return new Posicion(x, y);
    }

    public int escalar(double pTamanio, Zona pZona, int pAnchoPanel) {
        // El método del esqueleto solo recibe el ancho: se usa la escala horizontal.
        if (pZona.getAncho() <= 0 || pAnchoPanel <= 0) {
            throw new IllegalArgumentException("El ancho de la zona y del panel debe ser positivo");
        }
        return (int) Math.round(pTamanio * pAnchoPanel / pZona.getAncho());
    }

    // Usa la misma escala de posiciones para tamaños de animales y objetos.
    public int escalar(double pTamanio, Zona pZona, int pAnchoPanel, int pAltoPanel) {
        double escala = calcularEscala(pZona, pAnchoPanel, pAltoPanel);
        return (int) Math.round(pTamanio * escala);
    }

    private double calcularEscala(Zona pZona, int pAnchoPanel, int pAltoPanel) {
        if (pZona.getAncho() <= 0 || pZona.getAlto() <= 0
                || pAnchoPanel <= 0 || pAltoPanel <= 0) {
            throw new IllegalArgumentException("Las dimensiones de la zona y el panel deben ser positivas");
        }
        return Math.min(pAnchoPanel / pZona.getAncho(), pAltoPanel / pZona.getAlto());
    }
}
