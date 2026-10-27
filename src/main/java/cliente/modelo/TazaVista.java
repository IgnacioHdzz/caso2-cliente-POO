package cliente.modelo;

import comun.modelo.TazaBase;

public class TazaVista extends TazaBase implements IDibujable {

    public TazaVista(TazaBase pDatos) {
        super(pDatos);
    }

    @Override
    public void aceptar(IVisitanteElemento pVisitante) {
        pVisitante.visitar(this);
    }
}
