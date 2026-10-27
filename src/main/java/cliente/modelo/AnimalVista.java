package cliente.modelo;

import comun.modelo.AnimalBase;
import comun.modelo.Posicion;

// Un animal en el cliente: los datos base más su posición anterior (para suavizar).
public class AnimalVista extends AnimalBase implements IDibujable {

    private final Posicion posicionAnterior;

    // pPosicionAnterior es null si el animal no estaba en el estado anterior. 
    public AnimalVista(AnimalBase pDatos, Posicion pPosicionAnterior) {
        super(pDatos);
        this.posicionAnterior = pPosicionAnterior;
    }

    @Override
    public void aceptar(IVisitanteElemento pVisitante) {
        pVisitante.visitar(this);
    }

    public Posicion getPosicionAnterior() {
        return posicionAnterior;
    }
}
