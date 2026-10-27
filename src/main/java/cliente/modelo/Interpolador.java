package cliente.modelo;

import comun.modelo.Posicion;
import comun.constantes.ConstantesComunes;

// Suaviza el movimiento, ya que el servidor envía 20 estados por segundo y la pantalla
// se dibuja 30 veces por segundo. Por lo que entre un estado y otro se dibuja un punto intermedio.

public class Interpolador {

    // Qué tanto avanzó el tiempo entre el último estado y el siguiente (de 0.0 a 1.0). 
    public double calcularFraccion(long pMomentoRecepcionMs, long pAhoraMs) {
        double fraccion = (double) (pAhoraMs - pMomentoRecepcionMs) / ConstantesComunes.MS_POR_TICK;
        if (fraccion > 1.0) {
            return 1.0;
        }
        return fraccion;
    }

    public Posicion posicionEntre(Posicion pAnterior, Posicion pActual, double pFraccion) {
        if (pAnterior == null) {
            return pActual;
        }
        double x = pAnterior.getX() + (pActual.getX() - pAnterior.getX()) * pFraccion;
        double y = pAnterior.getY() + (pActual.getY() - pAnterior.getY()) * pFraccion;
        return new Posicion(x, y);
    }
}
